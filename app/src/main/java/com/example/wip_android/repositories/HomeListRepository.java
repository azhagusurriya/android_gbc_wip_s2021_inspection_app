package com.example.wip_android.repositories;

import androidx.annotation.NonNull;

import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.models.GlossaryItem;
import com.example.wip_android.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class HomeListRepository {

    private final String COLLECTION_NAME = "Client";
    private final FirebaseFirestore db;
    private FirebaseAuth mAuth;
    public List<ClientInfo> clientInfoList;

    public  HomeListRepository(){
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
    }

    public List<ClientInfo> getHomeList(String department){
        try {
            db.collection(COLLECTION_NAME)
                    .whereEqualTo("department", department)
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()){
                                if (task.getResult().getDocuments().size() != 0) {
                                    List<ClientInfo> clientInfoList = task.getResult().toObjects(ClientInfo.class);
                                    System.out.println("REPOSITORY" + clientInfoList);
                                } else {
                                    System.out.println("EMPTY");
                                }
                            } else {
                                System.out.println("FAILED");
                            }
                        }
                    });
        } catch (Exception ex) {
            System.out.println("EXCEPTION");
        }
        return clientInfoList;
    }

}
