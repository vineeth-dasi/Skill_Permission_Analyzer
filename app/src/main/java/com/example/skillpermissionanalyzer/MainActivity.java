package com.example.skillpermissionanalyzer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;

public class MainActivity extends Activity {
    ListView itemList;
    String[] items = {"Amazon", "Moodle", "Instagram"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = findViewById(R.id.item_list);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
        itemList.setAdapter(adapter);

        itemList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Launch specific activities based on clicked item position
                switch (position) {
                    case 0:
                        startActivity(new Intent(MainActivity.this, AmazonActivity.class));
                        break;
                    case 1:
                        startActivity(new Intent(MainActivity.this, MoodleActivity.class));
                        break;
                    case 2:
                        startActivity(new Intent(MainActivity.this, InstagramActivity.class));
                        break;
                    // Add more cases if needed
                }
            }
        });


        final PackageManager pm = getPackageManager();
        String TAG="in main";
//get a list of installed apps.
        List<ApplicationInfo> packages = pm.getInstalledApplications(PackageManager.GET_META_DATA);

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                String appName = pm.getApplicationLabel(packageInfo).toString();
                String packageName = packageInfo.packageName;
                // Log the app name and package name
                Log.d(TAG, "App Name: " + appName + ", Package Name: " + packageName);
            }
        }
    }


}
