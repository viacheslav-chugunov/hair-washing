package io.hairwashing.activity

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.ads.AdListener
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import io.hairwashing.R
import io.hairwashing.db.ConfigDB
import io.hairwashing.extensions.startActivityWithoutBackStack

class SplitActivity : AppCompatActivity() {

    private lateinit var mInterstitialAd: InterstitialAd

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_split)
        initAd()
        rerouteToActivity()
    }

        private fun rerouteToActivity() {
            Handler().postDelayed({
                if (showPreSetupOnStart())
                    startActivityWithoutBackStack(PreSetupActivity::class.java)
                else
                    showAd()
                finish()
            }, 2000)
        }

            private fun showPreSetupOnStart() : Boolean {
                val db = ConfigDB(this)
                val needToShow = db.getArgBy(ConfigDB.VALUE_PRE_SETUP_ON_START).toBoolean()
                db.close()
                return needToShow
            }

    private fun initAd() {
        MobileAds.initialize(this)

        mInterstitialAd = InterstitialAd(this)
        mInterstitialAd.adUnitId = "ca-app-pub-6875098488739325/6051165230"
        mInterstitialAd.loadAd(AdRequest.Builder().build())
        mInterstitialAd.adListener = object: AdListener() {

            override fun onAdClosed() {
                startActivityWithoutBackStack(HomeActivity::class.java)
            }
        }
    }

    private fun showAd() {
        if (mInterstitialAd.isLoaded) {
            mInterstitialAd.show()
        } else {
            startActivityWithoutBackStack(HomeActivity::class.java)
        }
    }
}