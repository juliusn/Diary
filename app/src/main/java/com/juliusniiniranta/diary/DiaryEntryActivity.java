package com.juliusniiniranta.diary;

import android.app.DialogFragment;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;

import static com.juliusniiniranta.diary.Constants.DELETE_ENTRY_DIALOG_FRAGMENT;
import static com.juliusniiniranta.diary.Constants.EXTRA_ENTRY_DESCRIPTION;
import static com.juliusniiniranta.diary.Constants.EXTRA_ENTRY_ID;
import static com.juliusniiniranta.diary.Constants.EXTRA_ENTRY_TITLE;

public class DiaryEntryActivity extends AppCompatActivity implements DeleteEntryDialogFragment.DeleteEntryDialogListener {

    private static final int EDIT_DIARY_ENTRY_REQUEST_CODE = 1;
    private AppViewModel viewModel;
    private TextView entryTitle;
    private TextView entryText;
    private DiaryEntry entry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_diary_entry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        entryTitle = findViewById(R.id.entry_title);
        entryText = findViewById(R.id.entry_text);
        long id = getIntent().getExtras().getLong(EXTRA_ENTRY_ID);
        viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        viewModel.getDiaryEntry(id).observe(this, new Observer<DiaryEntry>() {
            @Override
            public void onChanged(@Nullable DiaryEntry diaryEntry) {
                entry = diaryEntry;
                if (entry == null) {
                    finish();
                    return;
                }
                String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT).format(entry.getDate());
                getSupportActionBar().setTitle(date);
                entryTitle.setText(entry.getTitle());
                entryText.setText(entry.getDescription());
            }
        });
    }

    public void startEditActivity(View view) {
        Intent intent = new Intent(DiaryEntryActivity.this, EditDiaryEntryActivity.class);
        intent.putExtra(EXTRA_ENTRY_ID, entry.getId());
        startActivityForResult(intent, EDIT_DIARY_ENTRY_REQUEST_CODE);
    }

    public void showDeleteDialog(View view) {
        DialogFragment fragment = new DeleteEntryDialogFragment();
        fragment.show(getFragmentManager(), DELETE_ENTRY_DIALOG_FRAGMENT);
    }

    public void onDeleteEntryPositiveClick(DialogFragment fragment) {
        viewModel.delete(entry);
        Toast.makeText(getApplicationContext(),
                R.string.info_entry_deleted,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_DIARY_ENTRY_REQUEST_CODE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            String title = extras.getString(EXTRA_ENTRY_TITLE);
            String description = extras.getString(EXTRA_ENTRY_DESCRIPTION);
            entry.setTitle(title);
            entry.setDescription(description);
            viewModel.update(entry);
            Toast.makeText(getApplicationContext(),
                    R.string.info_entry_updated,
                    Toast.LENGTH_SHORT).show();
        }
    }
}
