package com.juliusniiniranta.diary.persistence;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface DiaryEntryDao {
    @Query("SELECT * FROM diary_entry ORDER BY date DESC")
    LiveData<List<DiaryEntry>> getAll();

    @Query("SELECT * FROM diary_entry WHERE id = :id")
    LiveData<DiaryEntry> getSingle(long id);

    @Query("DELETE FROM diary_entry WHERE id = :id")
    void delete(long... id);

    @Insert
    void insert(DiaryEntry... entries);

    @Update
    void update(DiaryEntry... entries);

    @Delete
    void delete(DiaryEntry... entries);

    @Query("DELETE FROM diary_entry")
    void deleteAll();
}
