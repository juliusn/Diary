package com.juliusniiniranta.diary;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

public class AppViewModel extends AndroidViewModel {

    private DiaryEntryRepository repository;
    private LiveData<List<DiaryEntry>> diaryEntries;

    public AppViewModel(@NonNull Application app) {
        super(app);
        repository = new DiaryEntryRepository(app);
        diaryEntries = repository.getDiaryEntries();
    }

    LiveData<List<DiaryEntry>> getDiaryEntries() {
        return diaryEntries;
    }

    LiveData<DiaryEntry> getDiaryEntry(long id) {
        return repository.getDiaryEntry(id);
    }

    void insert(DiaryEntry entry) {
        repository.insert(entry);
    }

    void delete(DiaryEntry entry) {
        repository.delete(entry);
    }

    void update(DiaryEntry entry) {
        repository.update(entry);
    }

    void deleteAll() {
        repository.deleteAll();
    }
}
