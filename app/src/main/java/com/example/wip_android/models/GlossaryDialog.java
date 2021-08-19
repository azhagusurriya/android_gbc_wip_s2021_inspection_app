package com.example.wip_android.models;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.wip_android.R;
import com.google.android.material.textfield.TextInputLayout;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class GlossaryDialog extends AppCompatDialogFragment implements View.OnClickListener {

    // Variables
    private GlossaryDialogInterface glossaryDialogInterface;
    private GlossaryItem newGlossaryItem;
    private TextInputLayout edtGlossaryContent, edtGlossaryDescription;
    private AutoCompleteTextView spnCategoryGlossary;

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

        this.spnCategoryGlossary = view.findViewById(R.id.spnCategoryGlossary);
        // this.spnCategoryGlossary.setOnItemSelectedListener(this);

        // Province list
        List<String> Category = new ArrayList<String>();
        Category.add("General Appearance");
        Category.add("Surface Condition");
        Category.add("Membrane Flashing");
        Category.add("Metal Counter Flashing");
        Category.add("Metal Cap Flashing");
        Category.add("Sleepers");
        Category.add("Drains");
        Category.add("Plastic Boxes");
        Category.add("Stack Jacks");
        Category.add("Tall Cones");
        Category.add("Others");

        // Dialog
        builder.setView(view).setTitle("Add New Term")
                // Negative button
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                    // Positive button
                }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String glossaryContent = edtGlossaryContent.getEditText().getText().toString();
                        String glossaryDescription = edtGlossaryDescription.getEditText().getText().toString();
                        // String glossaryCategory = spnCategoryGlossary.getText().toString();
                        String glossarySection = (glossaryContent.charAt(0) + "").toUpperCase();
                        newGlossaryItem = new GlossaryItem("Others", glossaryContent, "Admin", glossaryDescription,
                                glossarySection);
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

    @Override
    public void onClick(View v) {

    }

    public interface GlossaryDialogInterface {
        void addNewGlossaryItem(GlossaryItem newGlossaryItem);
    }

}
