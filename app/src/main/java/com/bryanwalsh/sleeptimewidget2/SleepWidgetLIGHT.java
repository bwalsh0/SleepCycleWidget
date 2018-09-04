package com.bryanwalsh.sleeptimewidget2;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import java.util.Calendar;
import java.util.Locale;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import static android.content.Context.MODE_PRIVATE;

//ToDo: Change app name to SleepCycleWidget

public class SleepWidgetLIGHT extends AppWidgetProvider {
    private static final String onClick1 = "GET_TIME";
    public Context timeContext;
    public Intent timeIntent;

    //SharedPrefs
    SharedPreferences tts;

    Locale aLocale = Locale.getDefault();
    Calendar calendar = Calendar.getInstance(aLocale);

    //java.util.Calendar
    int mHour = calendar.get(Calendar.HOUR);
    int mHour24 = calendar.get(Calendar.HOUR_OF_DAY);
    int mMin = calendar.get(Calendar.MINUTE);

    //Times to be converted into readable w/ ConvertTime() method
    String min;
    String hour;
    String time24;
    String time12 = "Error Retrieving Locale";

    String nextTime1, nextTime2, nextTime3, nextTime4, nextTime5;

    int time_offset; //User Preference modifiers
    int cycle_num;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.sleep_widget_light);
        int[] idArray = new int[]{appWidgetId};

        Intent intent = new Intent(context, SleepWidgetLIGHT.class);
        intent.setAction(onClick1);
        intent.putExtra("appWidgetId", appWidgetId);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, idArray);
        intent.putExtra(AppWidgetManager.ACTION_APPWIDGET_UPDATE, idArray);

        views.setOnClickPendingIntent(R.id.sleep, PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT));

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        timeIntent = intent;
        timeContext = context; //links context and intent updates to UpdateTime()

        if (onClick1.equals(intent.getAction())){
            Log.e("onRecieve-onClick", "This just got called");
            time_offset = Integer.parseInt(getDefaults("tts", context));
            ConvertTime(); //Moved into if statement to prevent double-run on init for efficiency
            UpdateTime(); //call helper
            Toast.makeText(context, "Time updated, sleep well!", Toast.LENGTH_SHORT).show();

            //ToDo: Add option for 12h or 24h time style (if (hour > 12) then {hour - 12})
        }
    }

    public void UpdateTime() { //Helper for grabbing time -only- when clicked
            Log.e("UpdateTime", "This got called");
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(timeContext);
            ComponentName AppWidget = new ComponentName(timeContext.getPackageName(), SleepWidgetLIGHT.class.getName());
            int[] appWidgetIds = appWidgetManager.getAppWidgetIds(AppWidget);

            RemoteViews remoteViews = new RemoteViews(AppWidget.getPackageName(), R.layout.sleep_widget_light);
            remoteViews.setTextViewText(R.id.currTime, time12);
            remoteViews.setViewVisibility(R.id.currTime, View.VISIBLE);
            remoteViews.setViewVisibility(R.id.default_ll, View.GONE);
            remoteViews.setViewVisibility(R.id.nextTimes_ll, View.VISIBLE);

            //nextTimes
            remoteViews.setTextViewText(R.id.nT1, nextTime1);
            remoteViews.setTextViewText(R.id.nT2, nextTime2);
            remoteViews.setTextViewText(R.id.nT3, nextTime3);
            remoteViews.setTextViewText(R.id.nT4, nextTime4);
            remoteViews.setTextViewText(R.id.nT5, nextTime5);

            appWidgetManager.updateAppWidget(appWidgetIds, remoteViews);

    }

    public void ConvertTime() {

        //minutes '9' to "09"
        if (mMin < 10) {
            min = "0" + Integer.toString(mMin);
        }
            else {
            min = Integer.toString(mMin);
        }

        hour = Integer.toString(mHour);

        time24 = mHour24 + ":" + min;
        time12 = mHour + ":" + min + GetMeridiem();

        NextTimes();
    }

    public String AppendMin(int integer) {
        if (integer < 10)
            return "0" + Integer.toString(integer);
                else {
            return Integer.toString(integer);
        }
    }

    public String GetMeridiem() { //am/pm for 12h format
        if (calendar.get(Calendar.AM_PM) != 0) {
            return " PM";
        }
        else {
            return " AM";
        }
    }

    public void NextTimes(){
        //helper mthd to calculate sleep cycles //OR CREATE NEW CALENDAR INSTANCE
        //cycles = 90m + user_tts (time til sleep)

        calendar.add(Calendar.MINUTE, 90 + time_offset);
        nextTime1 = ShortenString(Integer.toString(calendar.get(Calendar.HOUR)) + ":" +
                AppendMin(calendar.get(Calendar.MINUTE)) + GetMeridiem());                               //TODO: Find a way to incorporate ConvertTimes() for min < 10
        Log.e("NextTime1", nextTime1);

        calendar.add(Calendar.MINUTE, 90 + time_offset);
        nextTime2 = ShortenString(Integer.toString(calendar.get(Calendar.HOUR)) + ":" +
                AppendMin(calendar.get(Calendar.MINUTE)) + GetMeridiem());
        Log.e("NextTime2", nextTime2);

        calendar.add(Calendar.MINUTE, 90 + time_offset);
        nextTime3 = ShortenString(Integer.toString(calendar.get(Calendar.HOUR)) + ":" +
                AppendMin(calendar.get(Calendar.MINUTE)) + GetMeridiem());
        Log.e("NextTime3", nextTime3);

        calendar.add(Calendar.MINUTE, 90 + time_offset);
        nextTime4 = ShortenString(Integer.toString(calendar.get(Calendar.HOUR)) + ":" +
                AppendMin(calendar.get(Calendar.MINUTE)) + GetMeridiem());
        Log.e("NextTime4", nextTime4);
        //TODO: Fix time landing on 12:00 being presented as 0:00

        calendar.add(Calendar.MINUTE, 90 + time_offset);
        nextTime5 = ShortenString(Integer.toString(calendar.get(Calendar.HOUR)) + ":" +
                AppendMin(calendar.get(Calendar.MINUTE)) + GetMeridiem());
        Log.e("NextTime5", nextTime5);

        //ShortenString();

    //TODO: Include avg time taken to fall asleep (settings)
        //TODO: Append Cycle #'s to each time

    }

    public String ShortenString(String nextTime) {
        String temp = "";
        String[] strArray = nextTime.split("\\s+");
        StringBuffer result = new StringBuffer();

        for (String singleWord : strArray) {
            if ((temp + singleWord).length() > 5) {
                result.append(temp + "\n");
                temp = singleWord;
            } else {
                temp = temp + singleWord;
            }
        }

        if (temp.length() > 0) {
            result.append(temp);
        }
        return result.toString();
    }

    public static String getDefaults(String key, Context context) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        return preferences.getString(key, null);
    }
}

