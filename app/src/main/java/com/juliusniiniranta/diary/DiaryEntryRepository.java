package com.juliusniiniranta.diary;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

class DiaryEntryRepository {

    private DiaryEntryDao diaryEntryDao;
    private LiveData<List<DiaryEntry>> diaryEntries;

    DiaryEntryRepository(Application application) {
        AppDatabase db = AppDatabase.getDatabase(application);
        diaryEntryDao = db.diaryEntryDao();
        diaryEntries = diaryEntryDao.getAll();
    }

    LiveData<List<DiaryEntry>> getDiaryEntries() {
        return diaryEntries;
    }

    LiveData<DiaryEntry> getDiaryEntry(long id) {
        return diaryEntryDao.getSingle(id);
    }

    void insert(DiaryEntry... entries) {
        new insertAsyncTask(diaryEntryDao).execute(entries);
    }

    void delete(DiaryEntry... entries) {
        new deleteAsyncTask(diaryEntryDao).execute(entries);
    }

    void delete(long id) {
        new deleteByIdAsyncTask(diaryEntryDao).execute(id);
    }

    void update(DiaryEntry... entries) {
        new updateAsyncTask(diaryEntryDao).execute(entries);
    }

    void deleteAll() {
        new deleteAllAsyncTask(diaryEntryDao).execute();
    }

    private static class insertAsyncTask extends AsyncTask<DiaryEntry, Void, Void> {
        private DiaryEntryDao asyncTaskDao;

        insertAsyncTask(DiaryEntryDao dao) {
            this.asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final DiaryEntry... entries) {
            asyncTaskDao.insert(entries);
            return null;
        }
    }

    private static class deleteAsyncTask extends AsyncTask<DiaryEntry, Void, Void> {
        private DiaryEntryDao asyncTaskDao;

        deleteAsyncTask(DiaryEntryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(DiaryEntry... diaryEntries) {
            asyncTaskDao.delete(diaryEntries);
            return null;
        }
    }

    private static class updateAsyncTask extends AsyncTask<DiaryEntry, Void, Void> {
        private DiaryEntryDao asyncTaskDao;

        updateAsyncTask(DiaryEntryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(DiaryEntry... diaryEntries) {
            asyncTaskDao.update(diaryEntries);
            return null;
        }
    }

    private static class deleteAllAsyncTask extends AsyncTask<Void, Void, Void> {
        private DiaryEntryDao asyncTaskDao;

        deleteAllAsyncTask(DiaryEntryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            asyncTaskDao.deleteAll();
            return null;
        }
    }

    private static class deleteByIdAsyncTask extends AsyncTask<Long, Void, Void> {
        private DiaryEntryDao asyncTaskDao;

        deleteByIdAsyncTask(DiaryEntryDao dao) {
            asyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(Long... id) {
            asyncTaskDao.delete(id[0]);
            return null;
        }
    }
}
