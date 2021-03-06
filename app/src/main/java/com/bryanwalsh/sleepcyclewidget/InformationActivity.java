package com.bryanwalsh.sleepcyclewidget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class InformationActivity extends AppCompatActivity {
    private AdView mAdView;
    protected TextView ad_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        CardView adCard = findViewById(R.id.ad_card);

        adCard.setVisibility(View.GONE);

        if (!PublicBillingHelper.isPro()) {
            adCard.setVisibility(View.VISIBLE);
            //ACC ID
            MobileAds.initialize(this, getResources().getString(R.string.adID));

            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
            //UNIT ID
            if (BuildConfig.DEBUG) {
                mAdView = findViewById(R.id.adView_test);
                mAdView.setVisibility(View.VISIBLE);
                adView.setAdUnitId(getResources().getString(R.string.adUnitID_test));
                Log.e("Debug", "is debug & " + BuildConfig.DEBUG);
            } else {
                mAdView = findViewById(R.id.adView);
                mAdView.setVisibility(View.VISIBLE);
                adView.setAdUnitId(getResources().getString(R.string.adUnitID));
                Log.e("Debug", "is not debug & " + BuildConfig.DEBUG);
            }

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            ad_text = findViewById(R.id.ad_textview);
        }
        Log.e("InfoActivity: isPro()", "" + PublicBillingHelper.isPro());

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mAdView.setAdListener(new AdListener() {
//            @Override
//            public void onAdLoaded() {
//                super.onAdLoaded();
//                // Code to be executed when an ad finishes loading.
//            }
//
//            @Override
//            public void onAdFailedToLoad(int errorCode) {
//                mAdView.setVisibility(View.GONE);
//                ad_text.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onAdOpened() {
//                super.onAdOpened();
//                // Code to be executed when an ad opens an overlay that
//                // covers the screen.
//            }
//
//            @Override
//            public void onAdLeftApplication() {
//                super.onAdLeftApplication();
//                // Code to be executed when the user has left the app.
//            }
//
//            @Override
//            public void onAdClosed() {
//                super.onAdClosed();
//                // Code to be executed when when the user is about to return
//                // to the app after tapping on an ad.
//            }
//        });


    }
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
