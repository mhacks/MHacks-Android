package com.mhacks.android.data.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.json.JSONArray;

import java.util.Date;

/**
 * Created by Omid Ghomeshi on 10/13/14.
 */
@ParseClassName("Event")
public class Event extends ParseObject implements Parcelable {

    public static final String CATEGORY_COL   = "category";
    public static final String DETAILS_COL    = "details";
    public static final String DURATION_COL   = "duration";
    public static final String HOST_COL       = "host";
    public static final String LOCATIONS_COL  = "locations";
    public static final String START_TIME_COL = "startTime";
    public static final String TITLE_COL      = "title";

    public EventType getCategory() {
        return (EventType) getParseObject(CATEGORY_COL);
    }

    public void setCategory(EventType category) {
        put(CATEGORY_COL, category);
    }

    public String getDetails() {
        return getString(DETAILS_COL);
    }

    public void setDetails(String details) {
        put(DETAILS_COL, details);
    }

    public int getDuration() {
        return getInt(DURATION_COL);
    }

    public void setDuration(int duration) {
        put(DURATION_COL, duration);
    }

    public Sponsor getHost() {
        return (Sponsor) getParseObject(HOST_COL);
    }

    public void setHost(Sponsor sponsor) {
        put(HOST_COL, sponsor);
    }

    public JSONArray getLocations() {
        return getJSONArray(LOCATIONS_COL);
    }

    public void setLocations(JSONArray locations) {
        put(LOCATIONS_COL, locations);
    }

    public Date getStartTime() {
        return getDate(START_TIME_COL);
    }

    public void setStartTime(Date date) {
        put(START_TIME_COL, date);
    }

    public String getTitle() {
        return getString(TITLE_COL);
    }

    public void setTitle(String title) {
        put(TITLE_COL, title);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
