package com.juliusniiniranta.diary.persistence;

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

    public LiveData<List<DiaryEntry>> getDiaryEntries() {
        return diaryEntries;
    }

    public LiveData<DiaryEntry> getDiaryEntry(long id) {
        return repository.getDiaryEntry(id);
    }

    public void insert(DiaryEntry entry) {
        repository.insert(entry);
    }

    public void delete(DiaryEntry entry) {
        repository.delete(entry);
    }

    public void delete(long id) {
        repository.delete(id);
    }

    public void update(DiaryEntry entry) {
        repository.update(entry);
    }

    public void deleteAll() {
        repository.deleteAll();
    }
}
