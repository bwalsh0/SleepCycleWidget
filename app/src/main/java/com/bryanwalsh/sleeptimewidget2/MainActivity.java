package com.bryanwalsh.sleeptimewidget2;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        CollapsingToolbarLayout toolbar = findViewById(R.id.collapse);

        //Collapsing Toolbar Title Formatting
        final Typeface tf = Typeface.createFromAsset(getAssets(), "fonts/google_sans_regular.ttf");

        //Expanded and Collapsed modifiers
        toolbar.setCollapsedTitleTypeface(tf);
        toolbar.setExpandedTitleTypeface(tf);
        toolbar.setCollapsedTitleGravity(Gravity.CENTER);
        toolbar.setExpandedTitleGravity(Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));

        FloatingActionButton fab = findViewById(R.id.config_fab);
        fab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, WidgetConfigurePreferences.class);
                intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, WidgetConfigurePreferences.GeneralPreferenceFragment.class.getName());
                startActivity(intent);
            }   //TODO: snackbar for returning to previous page (for saved settings)
        });

        //If there are issues with older versions, uncomment below
//        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            getWindow().setNavigationBarColor(getResources().getColor(R.color.colorPrimary));
//        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_configure) {
//            Intent intent = new Intent(this, WidgetConfigurePreferences.class);
//            intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, WidgetConfigurePreferences.GeneralPreferenceFragment.class.getName());
//            startActivity(intent);
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
}
