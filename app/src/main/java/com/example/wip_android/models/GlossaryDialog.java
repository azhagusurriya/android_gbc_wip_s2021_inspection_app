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

//    GlossaryDialogInterface glossaryDialogInterface;
    EditText edtGlossaryContent, edtGlossaryDescription;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstance) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog_glossary, null);

        builder.setView(view)
                .setTitle("Test Custom Dialog")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        String textOne = edtGlossaryContent.getText().toString();
//                        String textTwo = edtGlossaryContent.getText().toString();
//                        glossaryDialogInterface.applyTexts(textOne, textTwo);

                    }
                });


        edtGlossaryContent = view.findViewById(R.id.edtGlossaryContent);
        edtGlossaryDescription = view.findViewById(R.id.edtGlossaryDescription);

        return builder.create();
    }

//    @Override
//    public void onAttach(@NonNull @NotNull Context context) {
//        super.onAttach(context);
//
//        glossaryDialogInterface = (GlossaryDialogInterface) context;
//    }
//
//    public interface GlossaryDialogInterface {
//        void applyTexts(String textOne, String textTwo);
//    }

}
