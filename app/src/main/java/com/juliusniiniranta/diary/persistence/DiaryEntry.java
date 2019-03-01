package com.juliusniiniranta.diary.persistence;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(tableName = "diary_entry")
public class DiaryEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo
    private long id;

    @NonNull
    @ColumnInfo
    private String title;

    @NonNull
    @ColumnInfo
    private String description;

    @NonNull
    @ColumnInfo
    private Date date;

    public DiaryEntry(@NonNull String title, @NonNull String description, @NonNull Date date) {
        this.title = title;
        this.description = description;
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public String getDescription() {
        return description;
    }

    public void setDescription(@NonNull String description) {
        this.description = description;
    }

    @NonNull
    public Date getDate() {
        return date;
    }

    public void setDate(@NonNull Date date) {
        this.date = date;
    }
}
