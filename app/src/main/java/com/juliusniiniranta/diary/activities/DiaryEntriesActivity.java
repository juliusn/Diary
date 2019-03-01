package com.juliusniiniranta.diary.activities;

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
import android.widget.Toast;

import com.juliusniiniranta.diary.R;
import com.juliusniiniranta.diary.activities.Constants.EntryOrder;
import com.juliusniiniranta.diary.fragments.DeleteAllDialogFragment;
import com.juliusniiniranta.diary.fragments.DeleteEntryDialogFragment;
import com.juliusniiniranta.diary.persistence.AppViewModel;
import com.juliusniiniranta.diary.persistence.DiaryEntry;

import java.util.List;

import static com.juliusniiniranta.diary.activities.Constants.DELETE_ALL_DIALOG_FRAGMENT;

public class DiaryEntriesActivity extends AppCompatActivity implements DeleteAllDialogFragment.DeleteAllDialogListener, DeleteEntryDialogFragment.DeleteEntryDialogListener {

    private AppViewModel viewModel;
    private DiaryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entries);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RecyclerView entriesView = findViewById(R.id.recyclerview_diary_entries);
        registerForContextMenu(entriesView);
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
                startActivityForResult(intent, Constants.NEW_DIARY_ENTRY_REQUEST_CODE);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        int order = preferences.getInt(getString(R.string.pref_entries_order), 0);
        switch (order) {
            case EntryOrder.NEWEST_FIRST:
                menu.findItem(R.id.action_newest_first).setChecked(true);
                break;
            case EntryOrder.OLDEST_FIRST:
                menu.findItem(R.id.action_oldest_first).setChecked(true);
                break;
            case EntryOrder.ALPHABETICALLY:
                menu.findItem(R.id.action_alphabetically).setChecked(true);
                break;
        }
        adapter.setOrder(order);
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
                saveOrderPreference(EntryOrder.NEWEST_FIRST);
                adapter.setOrder(EntryOrder.NEWEST_FIRST);
                item.setChecked(true);
                return true;
            case R.id.action_oldest_first:
                saveOrderPreference(EntryOrder.OLDEST_FIRST);
                adapter.setOrder(EntryOrder.OLDEST_FIRST);
                item.setChecked(true);
                return true;
            case R.id.action_alphabetically:
                saveOrderPreference(EntryOrder.ALPHABETICALLY);
                adapter.setOrder(EntryOrder.ALPHABETICALLY);
                item.setChecked(true);
                return true;
            case R.id.action_delete_all:
                showDeleteAllDialog(null);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void saveOrderPreference(int order) {
        SharedPreferences preferences = getPreferences(MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(getString(R.string.pref_entries_order), order);
        editor.apply();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.NEW_DIARY_ENTRY_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(),
                    R.string.info_entry_created,
                    Toast.LENGTH_SHORT).show();
        } else if (requestCode == Constants.EDIT_DIARY_ENTRY_REQUEST_CODE && resultCode == RESULT_OK) {
            Toast.makeText(getApplicationContext(),
                    R.string.info_entry_updated,
                    Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        return super.onContextItemSelected(item);
    }

    @Override
    public void onDeleteEntryPositiveClick(DialogFragment fragment) {
        long id = fragment.getArguments().getLong(Constants.EXTRA_ENTRY_ID);
        viewModel.delete(id);
        Toast.makeText(getApplicationContext(),
                R.string.info_entry_deleted,
                Toast.LENGTH_SHORT).show();
    }
}
