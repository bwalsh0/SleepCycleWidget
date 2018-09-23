package com.bryanwalsh.sleepcyclewidget;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.bryanwalsh.sleepcyclewidget.PublicBillingHelper;

public class MainActivity extends AppCompatActivity {
    private AdView mAdView;
    private TextView ad_text;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAdView = findViewById(R.id.adView);
        CardView settingsCard = findViewById(R.id.settings_card);
        CardView upgradeCard = findViewById(R.id.upgrade_card);
        CardView proCard = findViewById(R.id.pro_card);
        CardView adCard = findViewById(R.id.ad_card);
        TextView version = findViewById(R.id.versionInfo);

        adCard.setVisibility(View.GONE);

        if (!PublicBillingHelper.isPro()) {
            adCard.setVisibility(View.VISIBLE);
            MobileAds.initialize(this, getResources().getString(R.string.adID));

            AdView adView = new AdView(this);
            adView.setAdSize(AdSize.BANNER);
//        adView.setAdUnitId(getResources().getString(R.string.adID));
            adView.setAdUnitId(getResources().getString(R.string.test_adID));

            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            ad_text = findViewById(R.id.ad_textview);
        } else if (PublicBillingHelper.isPro()){
            upgradeCard.setVisibility(View.GONE);
            proCard.setVisibility(View.VISIBLE);
            version.setText("Thank you for supporting development :)");
        }
        Log.e("isPro()", "" + PublicBillingHelper.isPro());

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));

        settingsCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WidgetConfigurePreferences.class);
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, WidgetConfigurePreferences.GeneralPreferenceFragment.class.getName());
                startActivity(intent);
            }   //TODO: snackbar for returning to previous page (for saved settings)
        });

        upgradeCard.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PurchaseActivity.class);
                startActivity(intent);
            }   //TODO: cancel button or change view after purchase (& remove future ad)
        });

        //If there are issues with older versions, uncomment below
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
//        }

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                super.onAdLoaded();
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
//                mAdView.setVisibility(View.GONE);
                ad_text.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdOpened() {
                super.onAdOpened();
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
            }

            @Override
            public void onAdLeftApplication() {
                super.onAdLeftApplication();
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                super.onAdClosed();
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
            }
        });
    }
}
