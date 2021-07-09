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

public class GlossaryDialog extends AppCompatDialogFragment {

    // Variables
    private GlossaryDialogInterface glossaryDialogInterface;
    private EditText edtGlossaryContent, edtGlossaryDescription;
    private GlossaryItem newGlossaryItem;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        // Inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_glossary, null);

        // UI Components
        edtGlossaryContent = view.findViewById(R.id.edtGlossaryContent);
        edtGlossaryDescription = view.findViewById(R.id.edtGlossaryDescription);

        // Dialog
        builder.setView(view).setTitle("Test Custom Dialog")
                // Negative button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                    // Positive button
                }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String glossaryContent = edtGlossaryContent.getText().toString();
                        String glossaryDescription = edtGlossaryDescription.getText().toString();
                        String glossarySection = (glossaryContent.charAt(0) + "").toUpperCase();
                        newGlossaryItem = new GlossaryItem("Test Category", glossaryContent, "Admin",
                                glossaryDescription, glossarySection);
                        glossaryDialogInterface.addNewGlossaryItem(newGlossaryItem);

                    }
                });

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull @NotNull Context context) {
        super.onAttach(context);
        glossaryDialogInterface = (GlossaryDialogInterface) context;
    }

    public interface GlossaryDialogInterface {
        void addNewGlossaryItem(GlossaryItem newGlossaryItem);
    }

}
