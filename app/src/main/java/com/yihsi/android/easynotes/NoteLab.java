package com.yihsi.android.easynotes;

import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.UUID;

/**
 * Created by leodore on 2015/12/2.
 */
public class NoteLab {
    private static final String TAG = "NoteLab";
    private static final String FILENAME = "notes.json";

    private ArrayList<Note> mNotes;
    private NoteJSONSerializer mSerializer;

    private static NoteLab sNoteLab;
    private Context mAppContext;

    private NoteLab(Context appContext) {
        mAppContext = appContext;
        mNotes = new ArrayList<>();

        mSerializer = new NoteJSONSerializer(mAppContext, FILENAME);

        try {
            mNotes = mSerializer.loadNotes();
        } catch (Exception e) {
            mNotes = new ArrayList<>();
            //Log.e(TAG, "Error loading notes: ", e);
            Toast.makeText(mAppContext, R.string.error_load, Toast.LENGTH_SHORT).show();
        }

    }

    public static NoteLab getInstance(Context c) {
        if (sNoteLab == null) {
            sNoteLab = new NoteLab(c.getApplicationContext());
        }

        return sNoteLab;
    }

    public ArrayList<Note> getNotes() {
        return mNotes;
    }

    public Note getNote(UUID id) {
        for (Note n : mNotes) {
            if (n.getId().equals(id))
                return n;
        }
        return null;
    }

    public void addNote(Note n) {
        mNotes.add(n);
    }

    public void removeNote(Note n) {
        mNotes.remove(n);
    }

    public boolean saveNotes() {
        try {
            mSerializer.saveNotes(mNotes);
            //Log.d(TAG, "notes saved to file.");
            return true;
        } catch (Exception e) {
           // Log.e(TAG, "Error saving notes: ", e);
            Toast.makeText(mAppContext, R.string.error_save, Toast.LENGTH_SHORT).show();
            return false;
        }
    }
}
