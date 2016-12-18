package de.reikodd.ddweki;

import android.text.TextUtils;
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

    public String getJSON() {
        return "[" + TextUtils.join(",", strokes) + "]";
    }

    public void clear() {
        strokes.clear();
        stroke.clear();
    }
}
