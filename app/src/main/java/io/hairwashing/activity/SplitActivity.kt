package io.hairwashing.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import io.hairwashing.R
import io.hairwashing.db.ConfigDB
import io.hairwashing.extensions.startActivityWithoutBackStack

class SplitActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split)
        rerouteToActivity()
    }

        private fun rerouteToActivity() {
            Handler().postDelayed({
                if (showPreSetupOnStart())
                    startActivityWithoutBackStack(PreSetupActivity::class.java)
                else
                    startActivityWithoutBackStack(HomeActivity::class.java)
                finish()
            }, 1000)
        }

            private fun showPreSetupOnStart() : Boolean {
                val db = ConfigDB(this)
                val needToShow = db.getArgBy(ConfigDB.VALUE_PRE_SETUP_ON_START).toBoolean()
                db.close()
                return needToShow
            }
}