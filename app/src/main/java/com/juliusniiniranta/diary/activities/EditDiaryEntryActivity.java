package com.juliusniiniranta.diary.activities;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.juliusniiniranta.diary.R;
import com.juliusniiniranta.diary.persistence.AppViewModel;
import com.juliusniiniranta.diary.persistence.DiaryEntry;

import java.text.DateFormat;
import java.util.Date;

import static java.text.DateFormat.SHORT;

public class EditDiaryEntryActivity extends AppCompatActivity {

    private final DateFormat dateFormat = DateFormat.getDateTimeInstance(SHORT, SHORT);
    private EditText editTitleView;
    private EditText editDescriptionView;
    private Button resetButton;
    private Bundle extras = new Bundle();
    private DiaryEntry entry;
    private Date date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_diary_entry);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancel(null);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        AppViewModel viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
        editTitleView = findViewById(R.id.edit_diaryentrytitle);
        editDescriptionView = findViewById(R.id.edit_diaryentrydescription);
        resetButton = findViewById(R.id.button_reset);
        long id = getIntent().getLongExtra(Constants.EXTRA_ENTRY_ID, -1);
        if (id > -1) {
            extras.putLong(Constants.EXTRA_ENTRY_ID, id);
            viewModel.getDiaryEntry(id).observe(this, new Observer<DiaryEntry>() {
                @Override
                public void onChanged(@Nullable DiaryEntry diaryEntry) {
                    entry = diaryEntry;
                    resetViews(null);
                }
            });
        } else {
            date = new Date();
            getSupportActionBar().setTitle(dateFormat.format(date));
        }
    }

    public void cancel(View view) {
        Intent replyIntent = new Intent();
        setResult(RESULT_CANCELED, replyIntent);
        finish();
    }

    public void resetViews(View view) {
        if (entry == null) {
            resetButton.setEnabled(false);
            resetButton.setVisibility(View.GONE);
            editTitleView.setText("");
            editDescriptionView.setText("");
            return;
        }
        resetButton.setEnabled(true);
        resetButton.setVisibility(View.VISIBLE);
        getSupportActionBar().setTitle(dateFormat.format(entry.getDate()));
        editTitleView.setText(entry.getTitle());
        editDescriptionView.setText(entry.getDescription());
    }

    public void save(View view) {
        if (TextUtils.isEmpty(editTitleView.getText()) ||
                TextUtils.isEmpty(editDescriptionView.getText())) {
            Toast.makeText(getApplicationContext(),
                    R.string.warning_not_saved,
                    Toast.LENGTH_LONG).show();
        } else {
            String title = editTitleView.getText().toString();
            String description = editDescriptionView.getText().toString();
            AppViewModel viewModel = ViewModelProviders.of(this).get(AppViewModel.class);
            if (entry == null) {
                entry = new DiaryEntry(title, description, date);
                viewModel.insert(entry);
            } else {
                entry.setTitle(formatTitle(title));
                entry.setDescription(formatDescription(description));
                viewModel.update(entry);
            }
            setResult(RESULT_OK, new Intent());
            finish();
        }
    }

    private String formatTitle(String title) {
        return  title.replace("\n", "").trim();
    }

    private String formatDescription(String description) {
        String[] paragraphs = description.split("\n");
        StringBuilder trimmed = new StringBuilder();
        for (String paragraph1 : paragraphs) {
            String paragraph = paragraph1.trim();
            if (paragraph.length() > 0) {
                trimmed.append(paragraph).append("\n\n");
            }
        }
        return trimmed.toString().trim();
    }
}
