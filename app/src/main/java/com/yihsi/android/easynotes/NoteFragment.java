package com.yihsi.android.easynotes;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.UUID;

/**
 * Created by leodore on 2015/12/1.
 */
public class NoteFragment extends Fragment {
    public static final String EXTRA_NOTE_ID =
            "com.yihsi.android.notes.note_id";
    private Note mNote;
    private TextView mDateField;

    public static NoteFragment newInstance(UUID noteId) {
        Bundle args = new Bundle();
        args.putSerializable(EXTRA_NOTE_ID, noteId);

        NoteFragment fragment = new NoteFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        UUID mNoteId = (UUID)getArguments().getSerializable(EXTRA_NOTE_ID);
        mNote = NoteLab.getInstance(getActivity()).getNote(mNoteId);

        setHasOptionsMenu(true);

        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Change the color of share icon to upNavAndShareIconColor
        Drawable shareIcon = ContextCompat.getDrawable(getActivity(),
                R.drawable.ic_share_black_24dp);
        shareIcon.setColorFilter(ContextCompat.getColor(getActivity(), R.color.upNavAndShareIconColor),
                PorterDuff.Mode.SRC_ATOP);
        //Change the color of up navigation arrow to upNavAndShareIconColor
        Drawable upNav = ContextCompat.getDrawable(getActivity(),
                R.drawable.ic_arrow_back_black_24dp);
        upNav.setColorFilter(ContextCompat.getColor(getActivity(), R.color.upNavAndShareIconColor),
                PorterDuff.Mode.SRC_ATOP);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setHomeAsUpIndicator(upNav);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note, parent, false);

        mDateField = (TextView)view.findViewById(R.id.note_date);
        mDateField.setText(DateFormat.format("E,MMM dd,yyyy", mNote.getDate()));

        EditText mTitleField = (EditText)view.findViewById(R.id.note_title);
        mTitleField.setText(mNote.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                mDateField.setText(DateFormat.format("E,MMM dd,yyyy", new Date()));
                mNote.setDate(new Date());
            }
        });

        EditText mTextField = (EditText)view.findViewById(R.id.note_text);
        mTextField.setText(mNote.getText());
        mTextField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mNote.setText(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                mDateField.setText(DateFormat.format("E,MMM dd,yyyy", new Date()));
                mNote.setDate(new Date());
            }
        });

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.note_edit, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_item_share) {
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(Intent.EXTRA_TEXT, getNote());
            intent = Intent.createChooser(intent, getString(R.string.share_note));
            startActivity(intent);
            return true;
        }
        else
            return super.onOptionsItemSelected(item);
    }

    private String getNote() {
        String noteDate = DateFormat.format("E,MMM dd,yyyy", mNote.getDate()).toString();

        String noteToShare = getString(R.string.note_to_share, mNote.getTitle(),
                mNote.getText(), noteDate);
        return noteToShare;
    }

    public void onPause() {
        super.onPause();
        NoteLab.getInstance(getActivity()).saveNotes();
    }
}
