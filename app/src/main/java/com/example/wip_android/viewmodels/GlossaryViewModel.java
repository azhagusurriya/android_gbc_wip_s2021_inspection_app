package com.example.wip_android.viewmodels;

import com.example.wip_android.models.GlossaryItem;
import com.example.wip_android.repositories.GlossaryRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class GlossaryViewModel {

    // Variables
    private static final GlossaryViewModel ourInstance = new GlossaryViewModel();
    private final GlossaryRepository glossaryRepository = new GlossaryRepository();

    public static GlossaryViewModel getInstance() {
        return ourInstance;
    }

    // Constructor
    private GlossaryViewModel() {
    }

    // Create new item
    public void createNewGlossaryItem(GlossaryItem glossaryItem) {
        this.glossaryRepository.createNewGlossaryItem(glossaryItem);
    }
}
