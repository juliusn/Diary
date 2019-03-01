package com.juliusniiniranta.diary.activities;

class Constants {
    static final String EXTRA_ENTRY_ID = "EXTRA_ENTRY_ID";
    static final String DELETE_ENTRY_DIALOG_FRAGMENT = "DELETE_ENTRY_DIALOG_FRAGMENT";
    static final String DELETE_ALL_DIALOG_FRAGMENT = "DELETE_ALL_DIALOG_FRAGMENT";
    static final int NEW_DIARY_ENTRY_REQUEST_CODE = 1;
    static final int EDIT_DIARY_ENTRY_REQUEST_CODE = 2;
    static class EntryOrder {
        static final int NEWEST_FIRST = 0;
        static final int OLDEST_FIRST = 1;
        static final int ALPHABETICALLY = 2;
    }
}
