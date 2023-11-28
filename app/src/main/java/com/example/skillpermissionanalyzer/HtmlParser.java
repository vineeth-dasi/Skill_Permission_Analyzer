package com.example.skillpermissionanalyzer;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.util.Log;


import java.io.IOException;
import java.util.ArrayList;

public class HtmlParser {


    String getImageUrlFromWebPage(Document document,String imgpath) {
        try {
            Element imgElement = document.select("img[src*='/en/reports/'][alt='"+imgpath+"']").first();

            if (imgElement != null) {
                String imageUrl = imgElement.absUrl("src");
                return imageUrl;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    ArrayList<String> fetchTrackers(Document doc ) {

        ArrayList<String> trackerList = new ArrayList<>();

        // Extract the trackers
        Elements trackerElements = doc.select("p a.link.black");
        Elements badgeElements = doc.select("span.badge.badge-pill.badge-outline-primary");
        badgeElements.add(0,null);


        for (int i = 0; i < trackerElements.size(); i++) {
            Element trackerElement = trackerElements.get(i);
            Element badgeElement = (i < badgeElements.size()) ? badgeElements.get(i) : null;

            String trackerName = trackerElement.text();
            String trackerType = (badgeElement != null) ? badgeElement.text() : "empty";

            // Build a string representing the tracker data
            String data = "Tracker Name: " + trackerName + "\nTracker Type: " + trackerType;
            trackerList.add(data);

            // Log the tracker data to the console
            Log.d("TrackerData", data);
        }
        return trackerList;
    }

    ArrayList<String> fetchPermissions(Document doc) {
        ArrayList<String> permissionList = new ArrayList<>();


        // Extract the permission elements
        Elements permissionElements = doc.select("p.text-truncate");

        for (Element permissionElement : permissionElements) {
            Element permissionText = permissionElement.select("span[data-toggle='tooltip']").first();
            Element permissionDescription = permissionElement.select("small").first();

            String permissionName  = permissionText.text();
            String permissionDescriptionText = (permissionDescription != null) ? permissionDescription.text() : "null";
            boolean isDangerous = permissionElement.select("img[data-toggle='tooltip'][data-placement='top'][title='Protection level: dangerous|instant']").first() !=null;

            String permissionData = "Permission Name: " + permissionName + ",Permission Description: " + permissionDescriptionText + ",isDangerous : " + isDangerous;
            permissionList.add(permissionData);
            Log.d("permission", permissionData);


        }
        return permissionList;
    }

}
