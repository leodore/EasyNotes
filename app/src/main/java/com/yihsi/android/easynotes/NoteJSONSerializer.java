package com.yihsi.android.easynotes;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * Created by leodore on 2015/12/7.
 */
public class NoteJSONSerializer {
    private Context mContext;
    private String mFileName;

    public NoteJSONSerializer(Context c, String f) {
        mContext = c;
        mFileName = f;
    }

    public ArrayList<Note> loadNotes() throws IOException, JSONException {
        ArrayList<Note> notes = new ArrayList<>();
        BufferedReader reader = null;
        try {
            //Open and read the file into a StringBuilder
            InputStream in = mContext.openFileInput(mFileName);
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder jsonString = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                //Line breaks are omitted and irrelevant
                jsonString.append(line);
            }
            //Parse the JSON using JSONTokener
            JSONArray array = (JSONArray)new JSONTokener(jsonString.toString()).nextValue();
            //Build the array of notes from JSONObjects
            for (int i = 0; i < array.length(); i++) {
                notes.add(new Note(array.getJSONObject(i)));
            }
        } finally {
            if (reader != null)
                reader.close();
        }
        return notes;
    }

    public void saveNotes(ArrayList<Note> notes) throws IOException, JSONException {
        //Build an array in JSON
        JSONArray array = new JSONArray();
        for (Note note : notes) {
            array.put(note.toJSON());
        }

        //Write the file to disk
        Writer writer = null;
        try {
            OutputStream out = mContext.openFileOutput(mFileName, Context.MODE_PRIVATE);
            writer = new OutputStreamWriter(out);
            writer.write(array.toString());
        } finally {
            if (writer != null)
                writer.close();
        }
    }
}
