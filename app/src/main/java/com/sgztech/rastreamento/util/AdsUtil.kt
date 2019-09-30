package com.sgztech.rastreamento.util

import android.content.Context
import android.util.Log
import com.google.android.gms.ads.*

object AdsUtil {

    fun init(context: Context) {
        MobileAds.initialize(context)
    }

    fun setupBannerAd(adView: AdView) {
        val adRequest = AdRequest.Builder().build()
        adView.loadAd(adRequest)
    }

    fun buildIntersticialAd(context: Context, idAd: String): InterstitialAd {
        val interstitialAd = InterstitialAd(context)
        interstitialAd.adUnitId = idAd
        loadIntersticialAd(interstitialAd)
        interstitialAd.adListener = object : AdListener() {

            override fun onAdClosed() {
                Log.d(TAG_ADS, "loaded new ads")
                loadIntersticialAd(interstitialAd)
            }
        }
        return interstitialAd
    }

    private fun loadIntersticialAd(interstitialAd: InterstitialAd){
        interstitialAd.loadAd(AdRequest.Builder().build())
    }

    fun showIntersticialAd(interstitialAd: InterstitialAd) {
        if (interstitialAd.isLoaded) {
            interstitialAd.show()
        } else {
            Log.d(TAG_ADS, "The interstitial wasn't loaded yet.")
        }
    }

    const val ID_INTERSTICIAL_AD = "ca-app-pub-9764822217711668/1366822331"
    const val TAG_ADS = "Ads"
}