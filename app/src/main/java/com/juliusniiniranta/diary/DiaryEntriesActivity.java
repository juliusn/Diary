package com.juliusniiniranta.diary;

import android.app.DialogFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Date;
import java.util.List;

import static com.juliusniiniranta.diary.Constants.DELETE_ALL_DIALOG_FRAGMENT;
import static com.juliusniiniranta.diary.Constants.EXTRA_ENTRY_DATE;
import static com.juliusniiniranta.diary.Constants.EXTRA_ENTRY_DESCRIPTION;
import static com.juliusniiniranta.diary.Constants.EXTRA_ENTRY_TITLE;

public class DiaryEntriesActivity extends AppCompatActivity implements DeleteAllDialogFragment.DeleteAllDialogListener {

    private static final int NEW_DIARY_ENTRY_REQUEST_CODE = 1;
    private AppViewModel viewModel;
    private DiaryAdapter adapter;
    private RecyclerView entriesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entries);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        entriesView = findViewById(R.id.recyclerview_diary_entries);
        adapter = new DiaryAdapter(this);
        entriesView.setAdapter(adapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        entriesView.setLayoutManager(layoutManager);
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        viewModel.getDiaryEntries().observe(this, new Observer<List<DiaryEntry>>() {
            @Override
            public void onChanged(@Nullable List<DiaryEntry> diaryEntries) {
                adapter.setEntries(diaryEntries);
            }
        });

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DiaryEntriesActivity.this, EditDiaryEntryActivity.class);
                startActivityForResult(intent, NEW_DIARY_ENTRY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        boolean newestFirst = preferences.getBoolean(getString(R.string.pref_entries_newest_first), true);
        if (newestFirst) {
            menu.findItem(R.id.action_newest_first).setChecked(true);
        } else {
            menu.findItem(R.id.action_oldest_first).setChecked(true);
        }
        updateLayout(newestFirst);
        viewModel.getDiaryEntries().observe(this, new Observer<List<DiaryEntry>>() {
            @Override
            public void onChanged(@Nullable List<DiaryEntry> diaryEntries) {
                menu.findItem(R.id.action_delete_all).setEnabled(!diaryEntries.isEmpty());
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_newest_first:
                if (item.isChecked()) return true;
                item.setChecked(true);
                saveDirectionPreference(true);
                updateLayout(true);
                return true;
            case R.id.action_oldest_first:
                if (item.isChecked()) return true;
                item.setChecked(true);
                saveDirectionPreference(false);
                updateLayout(false);
                return true;
            case R.id.action_delete_all:
                showDeleteAllDialog(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveDirectionPreference(boolean newestFirst) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(getString(R.string.pref_entries_newest_first), newestFirst);
        editor.apply();
    }

    private void updateLayout(boolean newestFirst) {
        LinearLayoutManager layoutManager = (LinearLayoutManager) entriesView.getLayoutManager();
        layoutManager.setReverseLayout(!newestFirst);
        layoutManager.setStackFromEnd(!newestFirst);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_DIARY_ENTRY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String title = extras.getString(EXTRA_ENTRY_TITLE);
            String description = extras.getString(EXTRA_ENTRY_DESCRIPTION);
            Date date = (Date) extras.getSerializable(EXTRA_ENTRY_DATE);
            DiaryEntry diaryEntry = new DiaryEntry(title, description);
            diaryEntry.setDate(date);
            viewModel.insert(diaryEntry);
        }
    }

    @Override
    public void onDeleteAllPositiveClick(DialogFragment fragment) {
        viewModel.deleteAll();
    }

    public void showDeleteAllDialog(View view) {
        DialogFragment fragment = new DeleteAllDialogFragment();
        fragment.show(getFragmentManager(), DELETE_ALL_DIALOG_FRAGMENT);
    }

}
