package de.reikodd.ddweki;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class JSONCreate {
    List<String> strokes = new ArrayList<String>();
    List<String> stroke = new ArrayList<String>();

    public void addStroke(String value) {
        stroke.add(value);
    }

    public void endStroke() {
        strokes.add("[" + TextUtils.join(",", stroke) + "]");
        stroke.clear();
    }

    public String getJSON(String description) {
        return "{\"client\":\"" + Build.MODEL + "\","
                + "\"description\":\"" + description + "\"," +
                "\"strokes\":[" + TextUtils.join(",", strokes) + "]}";
    }

    public void clear() {
        strokes.clear();
        stroke.clear();
    }
}
