package com.example.wip_android.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wip_android.models.GlossaryItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class GlossaryRepository {

    // Variables
    private final String TAG = this.getClass().getCanonicalName();
    private final String COLLECTION_NAME = "Glossary";
    private final FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // Constructor
    public GlossaryRepository() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    // Methods
    public void createNewGlossaryItem(GlossaryItem glossaryItem) {
        try {
            db.collection(COLLECTION_NAME).add(glossaryItem)
                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                        @Override
                        public void onSuccess(DocumentReference documentReference) {
                            Log.d(TAG, "Document added with ID : " + documentReference.getId());
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e(TAG, "Error adding document to the store " + e);
                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

}
