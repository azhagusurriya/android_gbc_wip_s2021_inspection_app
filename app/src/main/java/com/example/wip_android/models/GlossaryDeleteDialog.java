package com.example.wip_android.models;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.wip_android.R;

import org.jetbrains.annotations.NotNull;

public class GlossaryDeleteDialog extends AppCompatDialogFragment {

    // Variables
    private GlossaryDeleteDialogInterface glossaryDeleteDialogInterface;
    private GlossaryItem glossaryItemToDelete;
    private GlossaryItem glossaryItemToDialog;

    // Default function
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_delete_glossary, null);

        // Get data from glossary
        Bundle bundle = getArguments();
        String category = bundle.getString("category", "");
        String content = bundle.getString("content", "");
        String department = bundle.getString("department", "");
        String description = bundle.getString("description", "");
        String section = bundle.getString("section", "");

        // Dialog
        builder.setView(view).setTitle("Are you sure you want to delete this item?")
                // Negative Button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                    // Positive Button
                }).setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Create an item and delete from Firebase
                        glossaryItemToDelete = new GlossaryItem(category, content, department, description, section);
                        glossaryDeleteDialogInterface.deleteGlossaryItem(glossaryItemToDelete);
                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        glossaryDeleteDialogInterface = (GlossaryDeleteDialogInterface) context;
    }

    public interface GlossaryDeleteDialogInterface {
        void deleteGlossaryItem(GlossaryItem glossaryItem);
    }

    public void setComplexVariable(GlossaryItem glossaryItemToDialog) {
        this.glossaryItemToDialog = glossaryItemToDialog;
    }
}
