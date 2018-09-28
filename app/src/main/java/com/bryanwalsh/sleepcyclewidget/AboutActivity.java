package com.bryanwalsh.sleepcyclewidget;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class AboutActivity extends AppCompatActivity {

    private int debugVal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        Toolbar toolbar = findViewById(R.id.toolbar);
        TextView mTitle = toolbar.findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        LinearLayout rate = findViewById(R.id.review);
        LinearLayout github = findViewById(R.id.gith);
        LinearLayout debug = findViewById(R.id.debug);
        LinearLayout email = findViewById(R.id.email);
        LinearLayout linkedin = findViewById(R.id.linkedin);
        LinearLayout reportbug = findViewById(R.id.bugReport);

        github.setOnClickListener(v -> {
                Intent intent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://github.com/bwalsh0"));
                startActivity(intent);
        });

        linkedin.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW,
                    Uri.parse("https://www.linkedin.com/in/walshbryanj/"));
            startActivity(intent);
        });

        reportbug.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"bryanwalsh00@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Sleep Cycle Widget [Bug Report]");
            i.putExtra(Intent.EXTRA_TEXT   , "Please include as much detail as possible about the issue.");
            try {
                startActivity(Intent.createChooser(i, "Send email using:"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(AboutActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });

        email.setOnClickListener(v -> {
            Intent i = new Intent(Intent.ACTION_SEND);
            i.setType("text/plain");
            i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"bryanwalsh00@gmail.com"});
            i.putExtra(Intent.EXTRA_SUBJECT, "Sleep Cycle Widget");
            try {
                startActivity(Intent.createChooser(i, "Send email using:"));
            } catch (android.content.ActivityNotFoundException ex) {
                Toast.makeText(AboutActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
            }
        });

        rate.setOnClickListener(v -> {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
            goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(goToMarket);
            } catch (ActivityNotFoundException e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });

        debug.setOnClickListener(v -> {
            ++debugVal;
            if (debugVal == 8) {
                Intent intent = new Intent(this, PurchaseActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}
