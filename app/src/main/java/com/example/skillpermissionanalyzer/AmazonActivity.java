package com.example.skillpermissionanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;

public class AmazonActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ArrayList<String> trackerList;
    private ArrayList<String> permissionList;
    private LinearLayout trackerListLinearLayout,permissionListLinearLayout;

    private  String imgpath="com.amazon.mShop.android.shopping logo";
    private String appname="Amazon";
    private  String imageurl;
    private String Appurl = "https://reports.exodus-privacy.eu.org/en/reports/com.amazon.mShop.android.shopping/latest/";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_amazon);

        // Initialize the ProgressDialog
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);

        // Fetch and display HTML data asynchronously
        new FetchHtmlData().execute();
    }

    private class FetchHtmlData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            // Show the loading spinner before starting the background task
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            HtmlParser htmlParser = new HtmlParser(); // Replace with your actual HTML parser class
            Document document = null;
            try {
                document = Jsoup.connect(Appurl).get();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            trackerList = htmlParser.fetchTrackers(document);
            permissionList = htmlParser.fetchPermissions(document);
            imageurl = htmlParser.getImageUrlFromWebPage(document,imgpath);

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Hide the loading spinner when the task is done

            // Find the TextView elements
            ImageView imageView = findViewById(R.id.imageView);
            TextView titlepageheader = findViewById(R.id.apptitle);
            trackerListLinearLayout = findViewById(R.id.trackerListLinearLayout);
            permissionListLinearLayout = findViewById(R.id.permissionListLinearLayout);


            titlepageheader.setText(appname);

            Picasso.get()
                    .load(imageurl)
                    .resize(150, 150) // Set the desired dimensions
                    .centerCrop() // Apply centerCrop transformation
                    .into(imageView);

            // Prepare text for tracker list
            StringBuilder trackerText = new StringBuilder();
            for (String trackerData : trackerList) {
                addTrackerTextView(trackerData);

            }

            // Prepare text for permission list
            StringBuilder permissionText = new StringBuilder();
            for (String permissionData : permissionList) {
                addPermissionTextView(permissionData);

            }

            TextView trackersTextView = findViewById(R.id.trackerscountsTextViewtoatalCounts);
            TextView permissionsTextView = findViewById(R.id.permissionsscountsTextViewtoatalCounts);

            trackersTextView.setText("Trackers Found: "+trackerList.size());
            permissionsTextView.setText("permissions Found: "+permissionList.size());

            progressDialog.dismiss();

            PackageManager packageManager = getPackageManager();
            String packageName = "com.amazon.mShop.android.shopping"; // Replace this with the package name of the app you want to check

            PermissionHelper permissionHelper = new PermissionHelper();
            permissionHelper.getPermissionsUsedByApp(packageManager, packageName);



        }


    }


    private void addPermissionTextView(String permissionData) {
        // Split the permission data
        String[] parts = permissionData.split(",");
        String namePart = parts[0].trim();
        String descriptionPart = parts[1].trim();
        boolean isDangerous = Boolean.parseBoolean(parts[2].split(":")[1].trim());

        descriptionPart=descriptionPart.replace("null" , " - ");

        // Create a SpannableStringBuilder to customize the appearance of the name and description
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(namePart + "\n" + descriptionPart);


        // Set different font sizes for name and description
        int nameSize = 13; // Adjust the font size (20) as needed for the name
        int descriptionSize = 10; // Adjust the font size (16) as needed for the description

        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(nameSize, true), 0, namePart.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(descriptionSize, true), namePart.length() + 1, spannableStringBuilder.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Customize the TextView's appearance
        TextView textView = new TextView(this);
        textView.setText(spannableStringBuilder);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        layoutParams.setMargins(0, 0, 0, 16); // You can adjust the margin (16) to control the spacing

        // Set layout parameters
        textView.setLayoutParams(layoutParams);

        textView.setPadding(8, 8, 8, 8); // Add padding
        textView.setBackground(ContextCompat.getDrawable(this, R.drawable.border_bg));

        // Set text color based on the "isDangerous" value
        if (isDangerous) {
            textView.setTextColor(Color.RED);
        } else {
        }

        // Add the formatted TextView to the permissionListLinearLayout
        permissionListLinearLayout.addView(textView);
    }


    private void addTrackerTextView(String trackerData) {
        TextView textView = new TextView(this);
        textView.setText(trackerData);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

        // Add top margin to create space between TextViews
        layoutParams.setMargins(0, 0, 0, 16); // You can adjust the margin (16) to control the spacing

        // Set the layout parameters
        textView.setLayoutParams(layoutParams);

        // Customize the TextView's appearance
//        textView.setTextColor(getResources().getColor(android.R.color.white)); // Set text color
        textView.setPadding(8, 8, 8, 8); // Add padding
        textView.setBackground(ContextCompat.getDrawable(this, R.drawable.border_bg));

        trackerListLinearLayout.addView(textView);
    }


}