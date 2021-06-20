package com.isajoh.app.utills;

import android.app.Activity;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.RequiresApi;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import com.isajoh.app.utills.NoInternet.AppLifeCycleManager;

/**
 * Created by apple on 11/21/17.
 */

public class Admob {
    public static final String TAG = Admob.class.getSimpleName();
    static Runnable loader = null;
    private static SettingsMain settingsMain;
    private static ScheduledFuture loaderHandler;
    private static boolean checkInterstitalLoad = false;


    public static void loadInterstitial(final Activity activity) {
        checkInterstitalLoad = false;
        settingsMain = new SettingsMain(activity);
        final AppLifeCycleManager appLifeCycleManager = AppLifeCycleManager.get(activity);
        try {
            loader = new Runnable() {
                @Override
                public void run() {
                    if (!checkInterstitalLoad && appLifeCycleManager.isForeground()) {
                        Log.d(TAG, "Loading Admob interstitial...");
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                final InterstitialAd interstitial = new InterstitialAd(activity);
                                interstitial.setAdUnitId(settingsMain.getInterstitialAdsId());
                                AdRequest adRequest = new AdRequest.Builder().build();
                                interstitial.loadAd(adRequest);
                                interstitial.setAdListener(new AdListener() {
                                    public void onAdLoaded() {

                                        if (interstitial != null && interstitial.isLoaded()) {
                                            adforest_ADsdisplayInterstitial(interstitial);
                                        }


                                    }

                                    @Override
                                    public void onAdFailedToLoad(int i) {
//                                        loadInterstitial(activity);
                                        Log.d(TAG, "Ad failed to loadvand error code is " + i);
                                    }

                                    @Override
                                    public void onAdClosed() {
                                        if (interstitial.isLoaded()) {
                                            checkInterstitalLoad = true;

                                        } else {

                                            adforest_cancelInterstitial();
                                            super.onAdClosed();
                                        }
                                    }
                                });
                            }
                        });
                    }
                }
            };

            ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
            loaderHandler = scheduler.scheduleWithFixedDelay(loader, 30, 30, TimeUnit.SECONDS);
            //settingsMain.getAdsInitialTime(),
            //                    settingsMain.getAdsDisplayTime(),
        } catch (Exception e) {
            Log.d("AdException===>", e.toString());
        }
    }

    public static void adforest_DisplaybannersForAdDetail(final Activity activity, final LinearLayout frameLayout, RelativeLayout layout, FloatingActionButton fab) {
        Log.d(TAG, "Loading Admob Banner...");

        settingsMain = new SettingsMain(activity);
        final AdView mAdView = new AdView(activity);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(settingsMain.getBannerAdsId());
        frameLayout.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

// mAdView.setVisibility(View.VISIBLE);
// frameLayout.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onAdFailedToLoad(int i) {
                Log.d(TAG, "Ad failed to loadvand error code is " + i);
                RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                layoutParams.bottomMargin = 0;
                layout.setLayoutParams(layoutParams);
                CoordinatorLayout.LayoutParams layoutParams2 = new CoordinatorLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                layoutParams2.bottomMargin = 0;
                layoutParams2.setMarginStart(0);
                layoutParams2.setMarginEnd(0);
                layoutParams2.leftMargin = 0;
                layoutParams2.rightMargin = 0;
                layoutParams2.gravity = Gravity.BOTTOM | Gravity.END;
                fab.setLayoutParams(layoutParams2);
            }


            @Override
            public void onAdLeftApplication() {
            }


            @Override
            public void onAdOpened() {
            }


            @Override
            public void onAdLoaded() {
                frameLayout.setVisibility(View.VISIBLE);
                settingsMain.setAdShowOrNot(false);
                Log.d(TAG, "Ad has has loaded to load");
            }
        });
    }
    public static void adforest_Displaybanners(final Activity activity, final LinearLayout frameLayout) {
        Log.d(TAG, "Loading Admob Banner...");

        settingsMain = new SettingsMain(activity);
        final AdView mAdView = new AdView(activity);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(settingsMain.getBannerAdsId());
        frameLayout.addView(mAdView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {

//                mAdView.setVisibility(View.VISIBLE);
//                frameLayout.setVisibility(View.INVISIBLE);
            }


            @Override
            public void onAdFailedToLoad(int i) {
                Log.d(TAG, "Ad failed to loadvand error code is " + i);
            }


            @Override
            public void onAdLeftApplication() {
            }


            @Override
            public void onAdOpened() {
            }


            @Override
            public void onAdLoaded() {
                frameLayout.setVisibility(View.VISIBLE);
                settingsMain.setAdShowOrNot(false);
                Log.d(TAG, "Ad has has loaded to load");
            }
        });
    }

    private static void adforest_ADsdisplayInterstitial(final InterstitialAd interstitialAd) {
        if (interstitialAd.isLoaded()) {
            interstitialAd.show();
            checkInterstitalLoad = true;
        }
    }

    public static void adforest_cancelInterstitial() {
        if (loaderHandler != null) {
            loaderHandler.cancel(true);
        }
    }
}


