package com.example.wip_android.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.models.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddProjectRepository {

    private final String TAG = this.getClass().getCanonicalName();
    private final String COLLECTION_NAME = "Client";
    private final FirebaseFirestore db;
    public ClientInfo newClientInfo = new ClientInfo();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public AddProjectRepository() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }


    public void createNewClient(ClientInfo clientInfo) {
        try {
            db.collection(COLLECTION_NAME).document(clientInfo.getClientName()).set(clientInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Log.d(TAG, "Document added with ID as client name");
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


//    public void createNewClient(ClientInfo clientInfo) {
//        try {
//            db.collection(COLLECTION_NAME).add(clientInfo)
//                    .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//                        @Override
//                        public void onSuccess(DocumentReference documentReference) {
//                            Log.d(TAG, "Document added with ID : " + documentReference.getId());
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Log.e(TAG, "Error adding document to the store " + e);
//                        }
//                    });
//
//        } catch (Exception ex) {
//            Log.e(TAG, ex.toString());
//            Log.e(TAG, ex.getLocalizedMessage());
//        }
//    }

}
