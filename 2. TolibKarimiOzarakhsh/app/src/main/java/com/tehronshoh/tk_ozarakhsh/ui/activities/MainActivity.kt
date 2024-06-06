package com.tehronshoh.tk_ozarakhsh.ui.activities

import android.app.AlarmManager
import android.app.AlarmManager.*
import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.tehronshoh.tk_ozarakhsh.R
import com.tehronshoh.tk_ozarakhsh.databinding.ActivityMainBinding
import com.tehronshoh.tk_ozarakhsh.receivers.NotificationReceiver
import com.tehronshoh.tk_ozarakhsh.repository.SharedPreferencesRepository
import com.tehronshoh.tk_ozarakhsh.viewmodels.ActivityViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

/** Full Screen Ad Id for TESTING = "ca-app-pub-3940256099942544/1033173712" **/
private const val AD_UNIT_FULL_SCREEN_ID = "ca-app-pub-7394283010044563/5304923641"

class MainActivity : BaseActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseAnalytics: FirebaseAnalytics

    private val viewModel: ActivityViewModel by viewModels()

    private var interstitialAd: InterstitialAd? = null

    private var counterOfNavigating = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** Using SplashScreen(SS) API, showing SS 2000 milliSeconds!!!!! **/
        installSplashScreen().apply {
            setKeepOnScreenCondition {
                viewModel.isLoading.value
            }
        }

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navControllerControl()
        lifecycleScope.launch(Dispatchers.IO) {
            obtainFirebaseAnalyticsInstance()
        }

        setUpAd()
        settingNotification()

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.d("TAG_TEST", "Fetching FCM registration token failed")
                return@OnCompleteListener
            }

            // Get new FCM registration token
            val token = task.result
            println("token: $token")
        })
    }

    private fun settingNotification() {
        if(!SharedPreferencesRepository.getInstance().isFirstLaunch)
            return

        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        val date = Calendar.getInstance()
        if (date.get(Calendar.HOUR_OF_DAY) > 21) {
            date.set(Calendar.DAY_OF_MONTH, date.get(Calendar.DAY_OF_MONTH) + 1)
        }
        date.apply {
            set(Calendar.HOUR_OF_DAY, 21)
            set(Calendar.MINUTE, 30)
            set(Calendar.SECOND, 0)
        }

        alarmManager.setInexactRepeating(
            RTC_WAKEUP, date.timeInMillis, INTERVAL_DAY, PendingIntent.getBroadcast(
                this,
                0,
                Intent(this, NotificationReceiver::class.java),
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) PendingIntent.FLAG_IMMUTABLE
                else PendingIntent.FLAG_UPDATE_CURRENT
            )
        )
    }

    private fun navControllerControl() {
        val navController =
            (supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).navController

        binding.bottomNavigationView.setupWithNavController(navController)

        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.bottomNavigationView.visibility = when (destination.id) {
                R.id.gazalSherItemFragment -> {
                    if (++counterOfNavigating > 2) {
                        showInterstitial()
                        counterOfNavigating = 0
                    }
                    GONE
                }
                else -> VISIBLE
            }
        }


    }

    private fun obtainFirebaseAnalyticsInstance() {
        firebaseAnalytics = Firebase.analytics

        val bundle = Bundle()
        bundle.putString(FirebaseAnalytics.Param.COUPON, "SummerPromo")
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, "JPY")
        bundle.putString(FirebaseAnalytics.Param.VALUE, "10000")
        bundle.putString(FirebaseAnalytics.Param.SHIPPING, "500")
        bundle.putString(FirebaseAnalytics.Param.TRANSACTION_ID, "192803301")
        firebaseAnalytics.logEvent(FirebaseAnalytics.Event.PURCHASE, bundle)
    }

    private fun setUpAd() {
        MobileAds.initialize(this) {
            binding.adView.loadAd(AdRequest.Builder().build())
            loadFullScreenAd()
        }
    }

    private fun loadFullScreenAd() = InterstitialAd.load(this,
        AD_UNIT_FULL_SCREEN_ID,
        AdRequest.Builder().build(),
        object : InterstitialAdLoadCallback() {
            override fun onAdFailedToLoad(adError: LoadAdError) {
                Log.d(
                    "TAG_TEST",
                    "onAdFainledToLoad -> domain: ${adError.domain}, code: ${adError.code}, " + "message: ${adError.message}"
                )
                interstitialAd = null
            }

            override fun onAdLoaded(ad: InterstitialAd) {
                Log.d("TAG_TEST", "Ad was loaded.")
                interstitialAd = ad
            }
        })

    private fun showInterstitial() {
        if (interstitialAd != null) {
            interstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d("TAG_TEST", "Ad was dismissed.")
                    interstitialAd = null
                    loadFullScreenAd()
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d("TAG_TEST", "Ad failed to show.")
                    interstitialAd = null
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d("TAG_TEST", "Ad showed fullscreen content.")
                    // Called when ad is dismissed.
                }
            }
            interstitialAd?.show(this)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}