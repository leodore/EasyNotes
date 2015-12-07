package com.yihsi.android.easynotes;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.UUID;

/**
 * Created by leodore on 2015/12/2.
 */
public class Note {
    private static final String JSON_ID = "id";
    private static final String JSON_TITLE = "title";
    private static final String JSON_TEXT ="content";
    private static final String JSON_DATE ="date";

    private UUID mId;
    private String mTitle;

    private String mText;
    private Date mDate;

    public Note() {
        mId = UUID.randomUUID();
        mDate = new Date();
    }

    public Note(JSONObject json) throws JSONException {
        mId = UUID.fromString(json.getString(JSON_ID));
        if (json.has(JSON_TITLE))
            mTitle = json.getString(JSON_TITLE);
        if (json.has(JSON_TEXT))
            mText = json.getString(JSON_TEXT);
        mDate = new Date(json.getLong(JSON_DATE));
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public String getText() {
        return mText;
    }

    public void setText(String text) {
        mText = text;
    }

    public Date getDate() {
        return mDate;
    }

    public JSONObject toJSON() throws JSONException {
        JSONObject json = new JSONObject();
        json.put(JSON_ID, mId.toString());
        json.put(JSON_TITLE, mTitle);
        json.put(JSON_TEXT, mText);
        json.put(JSON_DATE, mDate.getTime());
        return json;
    }

    @Override
    public String toString() {
        return mTitle;
    }
}
