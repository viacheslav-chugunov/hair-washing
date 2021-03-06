package io.hairwashing.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import io.hairwashing.dependences.Hair
import io.hairwashing.R
import io.hairwashing.dependences.TimeRange
import io.hairwashing.db.ConfigDB
import io.hairwashing.db.ConfigDB.Companion.VALUE_SETUP_VISIBILITY
import io.hairwashing.fragment.SetupFragment
import io.hairwashing.fragment.WeeklyFragment
import kotlinx.android.synthetic.main.activity_home.*
import kotlinx.android.synthetic.main.toolbar.*

class HomeActivity : AppCompatActivity() {
    private lateinit var setupFragment: SetupFragment
    private lateinit var weeklyFragment: WeeklyFragment
    private lateinit var menu: Menu

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        setSupportActionBar(toolbar)
        initFragments()
        updateWashedButtonIfWashedToday()
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
                    listener = object : SetupFragment.Listener {
                        override fun updateConfig() = updateConfigFor(hair, timeRange)
                        override fun updateWeeklyAdapter() = activity.updateWeeklyAdapter()
                    }
                }
            }

    fun onClickWashedButton(view: View) {
        if (!setupFragment.isLastWashingToday())
            setupFragment.setLastWashingAsToday()
        else
            setupFragment.setLastWashingAsPrevious()
        updateConfigFor(setupFragment.hair, setupFragment.timeRange)
        updateWeeklyAdapter()
        updateWashedButtonIfWashedToday()
    }

    private fun updateConfigFor(hair: Hair, timeRange: TimeRange) {
        val db = ConfigDB(this)
        db.updateBy(hair, timeRange)
        db.close()
    }

    private fun updateWeeklyAdapter() {
        weeklyFragment.updateAdapter(setupFragment.hair, setupFragment.timeRange)
    }

    private fun updateWashedButtonIfWashedToday() {
        washed_button.text = when {
            setupFragment.isLastWashingToday() -> getString(R.string.cancel_todays_washing)
            else -> getString(R.string.hair_already_washed)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        this.menu = menu!!
        menuInflater.inflate(R.menu.home_menu, menu)
        if (!setupFragmentIsVisible()) {
            setupFragment.hideFragment()
            setOpenHideMenuItemTitle(R.string.open_setup)
        } else {
            setupFragment.showFragment()
            setOpenHideMenuItemTitle(R.string.hide_setup)
        }
        return super.onCreateOptionsMenu(menu)
    }

        private fun setupFragmentIsVisible() : Boolean {
            val db = ConfigDB(this)
            val isVisible = db.getArgBy(VALUE_SETUP_VISIBILITY).toBoolean()
            db.close()
            return isVisible
        }

    override fun onOptionsItemSelected(item: MenuItem) = when(item.itemId) {
        R.id.hide_open_setup -> {
            if (setupFragment.isVisible) {
                setupFragment.hideFragment()
                setOpenHideMenuItemTitle(R.string.open_setup)
                val setupVisibility = false
                updateConfigFor(setupVisibility)
            }
            else {
                setupFragment.showFragment()
                setOpenHideMenuItemTitle(R.string.hide_setup)
                val setupVisibility = true
                updateConfigFor(setupVisibility)
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

        private fun updateConfigFor(setupVisibility: Boolean) {
            val db = ConfigDB(this)
            db.updateSetupVisibility(setupVisibility.toString())
            db.close()
        }

    private fun setOpenHideMenuItemTitle(newTitleResId: Int) {
        menu.findItem(R.id.hide_open_setup)
            .setTitle(newTitleResId)
    }
}