package com.juliusniiniranta.diary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.DateFormat;
import java.util.List;

import static com.juliusniiniranta.diary.Constants.EXTRA_ENTRY_ID;

public class DiaryAdapter extends RecyclerView.Adapter<DiaryAdapter.DiaryViewHolder> {

    private final LayoutInflater inflater;
    private List<DiaryEntry> entries;
    private Activity origin;

    DiaryAdapter(Activity activity) {
        this.origin = activity;
        inflater = LayoutInflater.from(activity);
    }

    @Override
    public DiaryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = inflater.inflate(R.layout.recyclerview_diary_item, parent, false);
        return new DiaryViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(DiaryViewHolder holder, int position) {
        if (entries != null) {
            final DiaryEntry entry = entries.get(position);
            final String date = DateFormat.getDateTimeInstance(DateFormat.SHORT, DateFormat.SHORT)
                    .format(entry.getDate());
            holder.entryDate.setText(date);
            holder.entryTitle.setText(entry.getTitle());
            holder.entryText.setText(entry.getDescription());
            holder.id = entry.getId();
        }
    }

    @Override
    public int getItemCount() {
        if (entries != null) {
            return entries.size();
        }
        return 0;
    }

    void setEntries(List<DiaryEntry> entries) {
        this.entries = entries;
        notifyDataSetChanged();
    }

    class DiaryViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {
        private final TextView entryDate;
        private final TextView entryTitle;
        private final TextView entryText;
        private long id;

        DiaryViewHolder(View itemView) {
            super(itemView);
            this.entryDate = itemView.findViewById(R.id.preview_entry_date);
            this.entryTitle = itemView.findViewById(R.id.preview_entry_title);
            this.entryText = itemView.findViewById(R.id.preview_entry_text);
            itemView.setOnCreateContextMenuListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            Context context = view.getContext();
            Intent intent = new Intent(context, DiaryEntryActivity.class);
            intent.putExtra(EXTRA_ENTRY_ID, id);
            context.startActivity(intent);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, final View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            MenuItem edit = contextMenu.add(0, view.getId(), 0, R.string.action_edit);
            edit.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    Intent intent = new Intent(origin, EditDiaryEntryActivity.class);
                    intent.putExtra(EXTRA_ENTRY_ID, id);
                    origin.startActivityForResult(intent, Constants.EDIT_DIARY_ENTRY_REQUEST_CODE);
                    return true;
                }
            });
        }
    }
}
