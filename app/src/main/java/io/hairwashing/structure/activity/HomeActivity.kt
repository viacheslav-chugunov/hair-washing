package io.hairwashing.structure.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.hairwashing.structure.dependences.Hair
import io.hairwashing.R
import io.hairwashing.TimeRange
import io.hairwashing.db.ConfigDB
import io.hairwashing.structure.fragment.SetupFragment
import io.hairwashing.structure.fragment.WeeklyFragment
import io.hairwashing.tools.SetupFile
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
        initSetupFile()
        initFragments()
        adjustSetupFragment()
        updateWeeklyAdapter()
        disableWashedButtonIfWashedToday()
    }

    private fun initSetupFile() {
        if (!SetupFile.existsIn(fileList()))
            SetupFile.initFrom(openFileOutput(SetupFile.FileData.FILE_NAME, Context.MODE_PRIVATE))
    }

    private fun initFragments() {
        setupFragment = supportFragmentManager.findFragmentById(R.id.setup_fragment) as SetupFragment
        weeklyFragment = supportFragmentManager.findFragmentById(R.id.weekly_list_fragment) as WeeklyFragment
    }

    private fun adjustSetupFragment() {
        val hair = Hair.from(openFileInput(SetupFile.FileData.FILE_NAME))
        val timeRange = TimeRange.from(openFileInput(SetupFile.FileData.FILE_NAME))
        setupFragment.hair = hair
        setupFragment.timeRange = timeRange
        setupFragment.listener = object : SetupFragment.Listener {
            override fun updateSetupFile() {
                updateSetupFileFor(setupFragment.hair, setupFragment.timeRange)
            }

            override fun updateWeeklyAdapter() { this@HomeActivity.updateWeeklyAdapter() }
        }
    }

    private fun updateSetupFileFor(hair: Hair, timeRange: TimeRange) {
        SetupFile.updateValuesFromStream(openFileOutput(SetupFile.FileData.FILE_NAME, Context.MODE_PRIVATE),
            hair,timeRange)
    }

    private fun updateWeeklyAdapter() {
        weeklyFragment.updateAdapter(setupFragment.hair, setupFragment.timeRange)
    }

    fun onClickWashedButton(view: View) {
        val hair = setupFragment.hair.apply { lastWashing = LocalDate.now() }
        val timeRange = setupFragment.timeRange
        updateSetupFileFor(hair, timeRange)
        updateWeeklyAdapter()
        disableWashedButtonIfWashedToday()
    }

    private fun disableWashedButtonIfWashedToday() {
        val lastWashing = setupFragment.hair.lastWashing
        if (lastWashing == LocalDate.now())
            washedButton.isEnabled = false
    }
}