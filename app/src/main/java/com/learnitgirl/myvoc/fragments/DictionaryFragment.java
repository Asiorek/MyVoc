package com.learnitgirl.myvoc.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import com.learnitgirl.myvoc.R;
import com.learnitgirl.myvoc.activities.MainActivity;
import com.learnitgirl.myvoc.utils.AddNewWordEvent;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

/**
 * A simple {@link Fragment} subclass.
 */
public class DictionaryFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "1";
    private static final String TAG = DictionaryFragment.class.getSimpleName();

    private ListView listView;
    private SimpleCursorAdapter adapter;

    public DictionaryFragment() {
        // Required empty public constructor
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static DictionaryFragment newInstance(int sectionNumber) {
        DictionaryFragment fragment = new DictionaryFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
        adapter = MainActivity.dbHelper.getWordsAdapter(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dictionary, container, false);
        listView = (ListView) view.findViewById(R.id.dictionary_listview);

        listView.setAdapter(adapter);

        registerForContextMenu(listView);

        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.context_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
//            case R.id.edit_word:
//                Toast.makeText(getContext(), "Edit clicked ", Toast.LENGTH_SHORT).show();
//                //TODO: create dialog for edits
//                //edit_word(info.id);
//                return true;
            case R.id.delete_word:
                MainActivity.dbHelper.deleteWord(String.valueOf(info.id));
//                adapter.swapCursor(MainActivity.dbHelper.getWords());
//                adapter.notifyDataSetChanged();
                listView.setAdapter(MainActivity.dbHelper.getWordsAdapter(getContext()));
            default:
                return super.onContextItemSelected(item);
        }
    }

        @Subscribe
    public void onEvent(AddNewWordEvent event) {
//        Toast.makeText(DictionaryFragment.this.getContext(), event.getWord() + "has been delivered in Dictionary", Toast.LENGTH_SHORT).show();
//        adapter.changeCursor(newCursor);
//        adapter.notifyDataSetChanged();
        Context context = getContext() != null ? getContext() : (getActivity() != null ? getActivity() : null);
        if (context != null) {
            Log.i(TAG, event.getWord().toString() + " start processing event - update the view.");
            MainActivity.dbHelper.insertWord(event.getWord());
            listView.setAdapter(MainActivity.dbHelper.getWordsAdapter(context));
        } else {
            Log.i(TAG, "There is no context!");
        }
        Log.i(TAG, event.getWord() + " Saved and ListView updated");
    }
}
