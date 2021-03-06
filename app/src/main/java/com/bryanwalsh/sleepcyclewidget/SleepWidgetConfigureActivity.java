package com.bryanwalsh.sleepcyclewidget;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

public class SleepWidgetConfigureActivity extends AppCompatActivity {

    private static final String PREFS_NAME = "com.bryanwalsh.sleeptimewidget2.SleepWidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;
    EditText mAppWidgetText;
    private AdView mAdView;

    //Create Widget
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            final Context context = SleepWidgetConfigureActivity.this;

            mAppWidgetText = findViewById(R.id.appwidget_text);
            String widgetText = mAppWidgetText.getText().toString();
            saveTitlePref(context, mAppWidgetId, widgetText);

            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    };

    //To Settings Activity
    View.OnClickListener sOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(getApplicationContext(), WidgetConfigurePreferences.class);
            intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, WidgetConfigurePreferences.GeneralPreferenceFragment.class.getName());
            intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, WidgetConfigurePreferences.GeneralPreferenceFragment.class.getName());
            startActivity(intent);
        }
    };

    //To Info Activity
    View.OnClickListener iOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            Intent intent = new Intent(SleepWidgetConfigureActivity.this, InformationActivity.class);
            startActivity(intent);
        }
    };

    public SleepWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    static void saveTitlePref(Context context, int appWidgetId, String text) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, text);
        prefs.apply();
    }

    //Overload SharedPreferences
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setResult(RESULT_CANCELED);

        setContentView(R.layout.sleep_widget_light_configure);
        mAppWidgetText = findViewById(R.id.appwidget_text);
        findViewById(R.id.add_button).setOnClickListener(mOnClickListener);
        findViewById(R.id.settings_button).setOnClickListener(sOnClickListener);
        findViewById(R.id.info_card2).setOnClickListener(iOnClickListener);
        CardView adCard = findViewById(R.id.ad_card2);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Window window = getWindow();
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.colorPrimary));

        //AdView
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
        }
        Log.e("ConfigActivity: isPro()", "" + PublicBillingHelper.isPro());


        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }

        mAppWidgetText.setText(loadTitlePref(SleepWidgetConfigureActivity.this, mAppWidgetId));
    }
}
