package io.hairwashing.structure.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.hairwashing.structure.dependences.Hair
import io.hairwashing.R
import io.hairwashing.structure.dependences.TimeRange
import io.hairwashing.db.ConfigDB
import io.hairwashing.structure.fragment.SetupFragment
import io.hairwashing.structure.fragment.WeeklyFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*
import java.time.LocalDate

class HomeActivity : AppCompatActivity() {
    private lateinit var setupFragment: SetupFragment
    private lateinit var weeklyFragment: WeeklyFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        initFragments()
        disableWashedButtonIfWashedToday()
    }

    private fun initFragments() {
        setupFragment = supportFragmentManager
            .findFragmentById(R.id.setup_fragment) as SetupFragment
        initSetupFragment()
        weeklyFragment = supportFragmentManager
            .findFragmentById(R.id.weekly_list_fragment) as WeeklyFragment
        updateWeeklyAdapter()
    }

    private fun initSetupFragment() {
        val activity = this
        setupFragment.run {
            hair = Hair.fromDB(activity)
            timeRange = TimeRange.fromDB(activity)
            listener = object : SetupFragment.Listener {
                override fun updateConfig() = updateConfigFor(hair, timeRange)
                override fun updateWeeklyAdapter() = activity.updateWeeklyAdapter()
            }
        }
    }

    private fun updateConfigFor(hair: Hair, timeRange: TimeRange) {
        val db = ConfigDB(this)
        db.updateBy(hair, timeRange)
        db.close()
    }

    private fun updateWeeklyAdapter() {
        weeklyFragment.updateAdapter(setupFragment.hair, setupFragment.timeRange)
    }

    fun onClickWashedButton(view: View) {
        val hair = setupFragment.hair.apply { lastWashing = LocalDate.now() }
        val timeRange = setupFragment.timeRange
        updateConfigFor(hair, timeRange)
        updateWeeklyAdapter()
        disableWashedButtonIfWashedToday()
    }

    private fun disableWashedButtonIfWashedToday() {
        val lastWashing = setupFragment.hair.lastWashing
        if (lastWashing == LocalDate.now())
            washedButton.isEnabled = false
    }
}