package com.yihsi.android.easynotes;

import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ListFragment;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


/**
 * A placeholder fragment containing a simple view.
 */
public class NoteListFragment extends ListFragment {
    private ArrayList<Note> mNotes;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mNotes = NoteLab.getInstance(getActivity()).getNotes();

        NoteAdapter adapter = new NoteAdapter(mNotes);
        setListAdapter(adapter);

        setHasOptionsMenu(true);

        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_note_list, container, false);

        getActivity().getActionBar();

        ListView listView = (ListView)view.findViewById(android.R.id.list);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position,
                                                  long id, boolean checked) {

            }

            //ActionMode.Callback methods
            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.note_item_selected, menu);

                //If API level is not smaller than 21, change status bar color to #757575
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(
                            getActivity(), R.color.actionModePrimaryDark));
                }

                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.menu_item_delete_note:
                        NoteAdapter adapter = (NoteAdapter) getListAdapter();
                        NoteLab noteLab = NoteLab.getInstance(getActivity());
                        for (int i = adapter.getCount() - 1; i >= 0; i--) {
                            if (getListView().isItemChecked(i)) {
                                noteLab.removeNote(adapter.getItem(i));
                            }
                        }
                        mode.finish();
                        adapter.notifyDataSetChanged();
                        noteLab.saveNotes();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                //Change the color of status bar back
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(
                            getActivity(), R.color.colorPrimaryDark));
                }
            }
        });

        FloatingActionButton mAddNote = (FloatingActionButton) view.findViewById(R.id.add_note);
        mAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Note note = new Note();
                NoteLab.getInstance(getActivity()).addNote(note);
                Intent intent = new Intent(getActivity(), NoteActivity.class);
                intent.putExtra(NoteFragment.EXTRA_NOTE_ID, note.getId());
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        Note note = ((NoteAdapter)getListAdapter()).getItem(position);

        Intent intent = new Intent(getActivity(), NoteActivity.class);
        intent.putExtra(NoteFragment.EXTRA_NOTE_ID, note.getId());
        startActivity(intent);
    }

    //Inner class extends ArrayAdapter to customize list item
    private class NoteAdapter extends ArrayAdapter<Note> {
        public NoteAdapter(ArrayList<Note> notes) {
            //Argument 0 indicates using customized layout
            super(getActivity(), 0, notes);
        }

        @Override
        //Return a view generates from the customized layout
        public View getView(int position, View convertView, ViewGroup parent) {
            //If weren't given a view, inflate one
            if (convertView == null) {
                convertView = getActivity().getLayoutInflater()
                        .inflate(R.layout.list_item_note, null);
            }

            //Configure the view for this note
            Note note = getItem(position);

            TextView titleTextView = (TextView)convertView.findViewById(R.id.note_item_title);
            titleTextView.setText(note.getTitle());
            TextView textTextView = (TextView)convertView.findViewById
                    (R.id.note_item_text);
            if (note.getText() != null) {
                //If the note is filled with whitespace or
                if (note.getText().matches("\\s+|R+")) {
                    textTextView.setText(null);
                }
                else {
                    //If the note's length is smaller than 180, display all the characters
                    if (note.getText().length() < 180) {
                        textTextView.setText(note.getText());
                    }
                    //If the note's length is not smaller than 100, display the first 100 characters
                    //with "..." appended to the 180th character
                    else {
                        textTextView.setText(note.getText().substring(0, 179) + "...");
                    }
                }
            }
            else {
                //If note's title and text are both null, abandon the note
                if (note.getTitle() == null) {
                    NoteLab noteLab = NoteLab.getInstance(getActivity());
                    noteLab.removeNote(note);
                    ((NoteAdapter) getListAdapter()).notifyDataSetChanged();
                    noteLab.saveNotes();
                }
            }

            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        ((NoteAdapter)getListAdapter()).notifyDataSetChanged();
    }
}
