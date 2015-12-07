package com.yihsi.android.easynotes;

import android.support.v4.app.Fragment;

import java.util.UUID;

/**
 * Created by leodore on 2015/12/1.
 */
public class NoteActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        UUID mNoteId = (UUID)getIntent().getSerializableExtra(NoteFragment.EXTRA_NOTE_ID);
        return NoteFragment.newInstance(mNoteId);
    }
}
