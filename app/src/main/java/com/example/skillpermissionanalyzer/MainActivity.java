package com.example.skillpermissionanalyzer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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


 class CustomAdapter extends ArrayAdapter<String> {

    private Context mContext;
    private ArrayList<String> mItems;

    public CustomAdapter(Context context, ArrayList<String> items) {
        super(context, 0, items);
        mContext = context;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_layout, parent, false);
        }

        // Get the data item for this position
        String currentItem = mItems.get(position);

        // Find the TextView in the list_item_layout.xml layout
        TextView textViewItem = listItemView.findViewById(R.id.text_view_item);

        // Populate the data into the template view using the data object
        textViewItem.setText(currentItem);

        // Return the completed view to render on screen
        return listItemView;
    }
}

public class MainActivity extends Activity {
    ListView itemList;
//    String[] items = {"Amazon", "Moodle", "Instagram"};
    ArrayList<String> items=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        itemList = findViewById(R.id.item_list);

//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, items);
//        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.list_item_layout, items);
        CustomAdapter adapter = new CustomAdapter(this, items);



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

        items.add("Amazon");
        items.add("Moodle");
        items.add("Instagram");

        for (ApplicationInfo packageInfo : packages) {
            if ((packageInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {

                String appName = pm.getApplicationLabel(packageInfo).toString();
                String packageName = packageInfo.packageName;
                Log.d(TAG, "App Name: " + appName + ", Package Name: " + packageName);
                if(appName.contains(".") || appName.contains("$") ||appName.contains("%")||appName.contains("-") ||appName.contains("Amazon"))
                    continue;
                if(appName.split(" ").length>3)
                    continue;
                items.add(appName);
            }
        }

        itemList.setVerticalScrollBarEnabled(true);

        itemList.setAdapter(adapter);

    }


}
