package com.juliusniiniranta.diary;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

public class DeleteEntryDialogFragment extends DialogFragment {

    DeleteEntryDialogListener listener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            listener = (DeleteEntryDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement " + DeleteEntryDialogListener.class.getName());
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.dialog_message_delete_entry)
                .setPositiveButton(R.string.action_delete, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onDeleteEntryPositiveClick(DeleteEntryDialogFragment.this);
                    }
                }).setNegativeButton(R.string.action_cancel, null).create();
        return builder.create();
    }

    public interface DeleteEntryDialogListener {
        void onDeleteEntryPositiveClick(DialogFragment fragment);
    }
}