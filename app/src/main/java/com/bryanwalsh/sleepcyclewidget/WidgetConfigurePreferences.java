package com.bryanwalsh.sleepcyclewidget;

import android.annotation.TargetApi;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;

import java.util.List;

public class WidgetConfigurePreferences extends AppCompatPreferenceActivity {

    static Boolean flag;

    private static Preference.OnPreferenceChangeListener sBindPreferenceSummaryToValueListener = new Preference.OnPreferenceChangeListener() {
        @Override
        public boolean onPreferenceChange(Preference preference, Object value) {
            String stringValue = value.toString();

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(stringValue);
                preference.setSummary(
                        index >= 0
                                ? listPreference.getEntries()[index]
                                : null );
                return true;
            }

            else if (preference instanceof EditTextPreference) {
                preference.setSummary(stringValue + " minutes");
                return true;
                }
                return true;
        }
    };

    private static boolean isXLargeTablet(Context context) {
        return (context.getResources().getConfiguration().screenLayout
                & Configuration.SCREENLAYOUT_SIZE_MASK) >= Configuration.SCREENLAYOUT_SIZE_XLARGE;
    }

    private static void bindPreferenceSummaryToValue(Preference preference) {
        preference.setOnPreferenceChangeListener(sBindPreferenceSummaryToValueListener);

        sBindPreferenceSummaryToValueListener.onPreferenceChange(preference,
                PreferenceManager
                        .getDefaultSharedPreferences(preference.getContext())
                        .getString(preference.getKey(), ""));
    }

    private static final String PREFS_NAME = "com.bryanwalsh.sleeptimewidget2.SleepWidgetProvider";
    private static final String PREF_PREFIX_KEY = "appwidget_";

    public WidgetConfigurePreferences() {
        super();
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupActionBar();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.registerOnSharedPreferenceChangeListener(listener);
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean onIsMultiPane() {
        return isXLargeTablet(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void onBuildHeaders(List<Header> target) {
        loadHeadersFromResource(R.xml.pref_headers, target);
    }

    /**
     * This method stops fragment injection in malicious applications.
     * Make sure to deny any unknown fragments here.
     */
    protected boolean isValidFragment(String fragmentName) {
        return PreferenceFragment.class.getName().equals(fragmentName)
                || GeneralPreferenceFragment.class.getName().equals(fragmentName)
//                || DataSyncPreferenceFragment.class.getName().equals(fragmentName)
                || NotificationPreferenceFragment.class.getName().equals(fragmentName);
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class GeneralPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_general);
            setHasOptionsMenu(true);

            Preference themeEnable = findPreference("theme1");
            themeEnable.setEnabled(false);
            themeEnable.setSummary("Upgrade to unlock themes");

            flag = PublicBillingHelper.isPro();
            Log.e("PrefScreen: IsPro()","" + flag);
                if (flag) {
                    getPreferenceScreen().findPreference("theme1").setEnabled(true);
                    getPreferenceScreen().findPreference("buyIap").setEnabled(false);
                    getPreferenceScreen().findPreference("buyIap").setTitle("Pro features enabled");
                    getPreferenceScreen().findPreference("buyIap").setSummary("Themes unlocked & no ads");
                }

            bindPreferenceSummaryToValue(findPreference("tts"));
            bindPreferenceSummaryToValue(findPreference("cycle_amt"));
            bindPreferenceSummaryToValue(findPreference("theme1"));

            CheckBoxPreference toast_flag = (CheckBoxPreference) findPreference("toast_flag");
            CheckBoxPreference curr_flag = (CheckBoxPreference)findPreference("curr_flag");

            //ABOUT ONCLICK
            Preference about = findPreference("aboutlink");
            about.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), AboutActivity.class);
                    startActivity(intent);
                    return true;
                }
            });

            //PURCHASE ONCLICK
            Preference buy = findPreference("buyIap");
            buy.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    Intent intent = new Intent(getActivity(), PurchaseActivity.class);
                    startActivity(intent);
                    return true;
                }
            });
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), WidgetConfigurePreferences.class));
                //Snackbar.make(getActivity().findViewById(android.R.id.content), Html.fromHtml("<font color=\"#FFAAB6FE\">Settings applied</font>"), Snackbar.LENGTH_SHORT).show();
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    /**
     * This fragment shows notification preferences only. It is used when the
     * activity is showing a two-pane settings UI.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public static class NotificationPreferenceFragment extends PreferenceFragment {
        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            addPreferencesFromResource(R.xml.pref_notification);
            setHasOptionsMenu(true);

            // Bind the summaries of EditText/List/Dialog/Ringtone preferences
            // to their values. When their values change, their summaries are
            // updated to reflect the new value, per the Android Design
            // guidelines.
            bindPreferenceSummaryToValue(findPreference("notifications_new_message_ringtone"));
        }

        @Override
        public boolean onOptionsItemSelected(MenuItem item) {
            int id = item.getItemId();
            if (id == android.R.id.home) {
                startActivity(new Intent(getActivity(), WidgetConfigurePreferences.class));
                return true;
            }
            return super.onOptionsItemSelected(item);
        }
    }

    SharedPreferences.OnSharedPreferenceChangeListener listener = new SharedPreferences.OnSharedPreferenceChangeListener() {
        public void onSharedPreferenceChanged(SharedPreferences prefs, String key) {
            Intent intent = new Intent();
            intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
            sendBroadcast(intent);
            if (key.matches("theme1") && PublicBillingHelper.isPro()) {
                Snackbar.make(getListView(), Html.fromHtml("<font color=\"#FFAAB6FE\">Theme applied</font>"), Snackbar.LENGTH_SHORT).show();
            }
            if (key.matches("tts") || key.matches("cycle_amt") || key.matches("toast_flag") || key.matches("curr_flag")) {
                Snackbar.make(getListView(), Html.fromHtml("<font color=\"#FFAAB6FE\">Setting applied</font>"), Snackbar.LENGTH_SHORT).show();
            }
        }
    };
}
