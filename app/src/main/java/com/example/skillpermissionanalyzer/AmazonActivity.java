package com.example.skillpermissionanalyzer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.SpannableStringBuilder;
import android.text.style.AbsoluteSizeSpan;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AmazonActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private ArrayList<String> trackerList;
    private ArrayList<String> permissionList;
    private LinearLayout trackerListLinearLayout,permissionListLinearLayout;

    private  String imgpath="com.amazon.mShop.android.shopping logo";
    private String appname="Amazon";
    private String apppackagename="com.moodle.moodlemobile";
    private HashMap<String , String> grantedlist;

    private  String imageurl;
    private String Appurl = "https://reports.exodus-privacy.eu.org/en/reports/com.amazon.mShop.android.shopping/latest/";
    private  int trackercount=0;
    private  int dangerouspermissioncount=0;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moodle);

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
            grantedlist= new HashMap<>();
            for(String str:new grantedpermissions(getApplicationContext()).getpermissions(apppackagename).split(","))
            {
                String[] spliter=str.split(":");
                grantedlist.put(spliter[0],spliter[1]);
            }
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
            imageurl = htmlParser.getImageUrlFromWebPage(document, imgpath);




            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // Hide the loading spinner when the task is done

            Log.d("someting",grantedlist.toString());


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

            trackersTextView.setText("Trackers Found : " + trackerList.size());
            permissionsTextView.setText("Permissions Found : " + permissionList.size());

            progressDialog.dismiss();
            String permissionsgranted =new grantedpermissions(getApplicationContext()).getpermissions(apppackagename);

            ImageView arrowImageView = findViewById(R.id.arrowImageView);
            ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) arrowImageView.getLayoutParams();

            int leftMarginInPixels = 0;


            leftMarginInPixels+=trackercount*105;
            leftMarginInPixels+=dangerouspermissioncount*65;

            if(leftMarginInPixels>800)
            {
                leftMarginInPixels=800;
            }

            layoutParams.leftMargin = leftMarginInPixels;
            arrowImageView.setLayoutParams(layoutParams);
            TextView textViewresult = findViewById(R.id.privacyscore);

            if(leftMarginInPixels<300)
            {
                textViewresult.append("Low");
            }
            else if(leftMarginInPixels>=300 && leftMarginInPixels<600)
            {
                textViewresult.append("Moderate");

            }
            else if(leftMarginInPixels>=600)
            {
                textViewresult.append("High");

            }


        }
    }


    private void addPermissionTextView(String permissionData) {
        // Split the permission data
        String[] parts = permissionData.split(",");
        String namePart = parts[0].trim();
        String descriptionPart = parts[1].trim();
        boolean isDangerous = Boolean.parseBoolean(parts[2].split(":")[1].trim());
        String fullandroidpermission=parts[3].split(":")[1].trim();

        descriptionPart=descriptionPart.replace("null" , " - ");
        String text1="can "+descriptionPart.split(":")[1].trim();
        String text2="Permission: "+namePart.split(":")[1].trim();

        // Create a SpannableStringBuilder to customize the appearance of the name and description
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder( text1.toLowerCase() + "\n" + text2);


        // Set different font sizes for name and description
        int nameSize = 14; // Adjust the font size (20) as needed for the name
        int descriptionSize = 10; // Adjust the font size (16) as needed for the description

        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(nameSize, true), 0, text1.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.setSpan(new AbsoluteSizeSpan(descriptionSize, true), text1.length() + 1, spannableStringBuilder.length(), SpannableStringBuilder.SPAN_EXCLUSIVE_EXCLUSIVE);

        // Customize the TextView's appearance
        TextView textView = new TextView(getApplicationContext());
        textView.setText(spannableStringBuilder);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        textView.setPadding(15, 15, 15, 15); // Set padding values (left, top, right, bottom)

        layoutParams.setMargins(0, 0, 0, 12); // You can adjust the margin (16) to control the spacing

        // Set layout parameters
        textView.setLayoutParams(layoutParams);


        // Set text color based on the "isDangerous" value
        if (isDangerous) {
            textView.setTextColor(Color.parseColor("#FF3333"));
            dangerouspermissioncount++;
        } else {
        }

        CardView cardView = new CardView(getApplicationContext());

        int paddingValue = 8; // Define in resources
        cardView.setUseCompatPadding(true); // Add padding for pre-Lollipop devices
        cardView.setContentPadding(paddingValue, paddingValue, paddingValue, paddingValue);

// Set margins for the CardView using LayoutParams
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

// Set margins for the CardView (left, top, right, bottom)
        int marginValue = 10; // Define in resources
        layoutParams1.setMargins(0, 0, 0, marginValue);
        cardView.setLayoutParams(layoutParams1);


        cardView.setCardBackgroundColor(Color.parseColor("#B6D0E2"));
        cardView.setRadius(20);
        if(grantedlist.containsKey(fullandroidpermission))
        {
            // Set the modified drawable as the background
            cardView.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.border_bg_grant));
            cardView.setCardBackgroundColor(Color.parseColor("#B6D0E2"));

        }

        cardView.addView(textView);


        // Add the formatted TextView to the permissionListLinearLayout
        permissionListLinearLayout.addView(cardView);
    }


    private void addTrackerTextView(String trackerData) {
        TextView textView = new TextView(getApplicationContext());
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
        textView.setTextSize(13); // Set text size
        textView.setPadding(8, 8, 8, 8); // Add padding


        CardView cardView = new CardView(getApplicationContext());

        int paddingValue = 10; // Define in resources
        cardView.setUseCompatPadding(true); // Add padding for pre-Lollipop devices
        cardView.setContentPadding(paddingValue, paddingValue, paddingValue, paddingValue);

// Set margins for the CardView using LayoutParams
        LinearLayout.LayoutParams layoutParams1 = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );

// Set margins for the CardView (left, top, right, bottom)
        int marginValue = 12; // Define in resources
        layoutParams1.setMargins(0, 0, 0, marginValue);
        cardView.setLayoutParams(layoutParams1);


        cardView.setCardBackgroundColor(Color.parseColor("#B6D0E2"));
        cardView.setRadius(20);

        cardView.addView(textView);
        trackercount++;


        trackerListLinearLayout.addView(cardView);
    }



}