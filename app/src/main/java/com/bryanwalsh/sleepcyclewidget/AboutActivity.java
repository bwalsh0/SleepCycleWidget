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
            urlIntent("https://github.com/bwalsh0");
        });

        linkedin.setOnClickListener(v -> {
            urlIntent("https://www.linkedin.com/in/walshbryanj");
        });

        reportbug.setOnClickListener(v -> {
            emailIntent("Sleep Cycle Widget [Bug Report]",
                    "Please include as much detail as possible about the issue.");
        });

        email.setOnClickListener(v -> {
            emailIntent("Sleep Cycle Widget [Misc.]",
                    "");
        });

        rate.setOnClickListener(v -> {
            Uri uri = Uri.parse("market://details?id=" + getPackageName());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                    Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                    Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            try {
                startActivity(intent);
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

    public void emailIntent(String subject, String body) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:bryanwalsh00@gmail.com"));
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT   , body);
        try {
            startActivity(Intent.createChooser(intent, "Send email using:"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AboutActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void urlIntent(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        startActivity(intent);
    }
}
