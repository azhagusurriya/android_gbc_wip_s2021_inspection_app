package com.example.wip_android.repositories;

import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class AddProjectRepository {

    private  final String TAG = this.getClass().getCanonicalName();
    private  final String COLLECTION_NAME = "Client";
    private final FirebaseFirestore db;
    public ClientInfo newClientInfo = new ClientInfo();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    public  AddProjectRepository(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }


}
