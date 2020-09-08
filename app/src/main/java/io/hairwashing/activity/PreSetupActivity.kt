package io.hairwashing.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import io.hairwashing.fragment.PreSetupFragment
import io.hairwashing.R
import io.hairwashing.db.ConfigDB
import io.hairwashing.extensions.startActivityWithoutBackStack
import kotlinx.android.synthetic.main.toolbar.*

class PreSetupActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pre_setup)
        setSupportActionBar(toolbar.apply { setTitle(R.string.pre_setup_title) })
    }

    fun onApplyButtonCLick(view: View) {
        updateConfig()
        startActivityWithoutBackStack(HomeActivity::class.java)
    }

        private fun updateConfig() {
            val setup = supportFragmentManager.findFragmentById(R.id.pre_setup_fragment) as PreSetupFragment
            val db = ConfigDB(this)
            db.updateBy(setup.hair, setup.timeRange)
            db.updatePreSetupOnStart(false.toString())
            db.close()
        }
}