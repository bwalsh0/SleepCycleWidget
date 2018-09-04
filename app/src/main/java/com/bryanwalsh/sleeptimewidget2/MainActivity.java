package com.bryanwalsh.sleeptimewidget2;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.bryanwalsh.sleeptimewidget2.SleepWidgetLIGHTConfigureActivity.loadTitlePref;
import static com.bryanwalsh.sleeptimewidget2.SleepWidgetLIGHTConfigureActivity.saveTitlePref;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        Button settings = findViewById(R.id.settings_button);

        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SleepWidgetLIGHTConfigureActivity.class));
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_configure) {
            Intent intent = new Intent(this, WidgetConfigurePreferences.class);
            intent.putExtra(PreferenceActivity.EXTRA_SHOW_FRAGMENT, WidgetConfigurePreferences.GeneralPreferenceFragment.class.getName());
            intent.putExtra(PreferenceActivity.EXTRA_NO_HEADERS, true); //TODO: Try removing this flag to enable headers on tablets
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
//TODO: Replace Configure 3-dot menu with about page
//TODO: Add settings in mainActivity that are parallel to configureActivity
