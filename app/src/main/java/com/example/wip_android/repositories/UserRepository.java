package com.example.wip_android.repositories;

import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.wip_android.MainActivity;
import com.example.wip_android.activities.SignInActivity;
import com.example.wip_android.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class UserRepository {

    // Variables
    private final String TAG = this.getClass().getCanonicalName();
    private final String COLLECTION_NAME = "Users";
    private final FirebaseFirestore db;
    public User newUserInfo = new User();
    private FirebaseAuth mAuth;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    // Mutable Lists
    public MutableLiveData<String> signInStatus = new MutableLiveData<String>();
    public MutableLiveData<String> userExistStatus = new MutableLiveData<String>();
    public MutableLiveData<String> loggedInUserID = new MutableLiveData<String>();
    public MutableLiveData<String> userDeleteStatus = new MutableLiveData<String>();
    public MutableLiveData<String> userAuthEmailStatus = new MutableLiveData<String>();
    public MutableLiveData<String> loggedInUserDepartment = new MutableLiveData<String>();

    // Constructor
    public UserRepository() {
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

    }

    public void createAuthUser(User user, String password) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "Authentication Success");
                            addUser(user);
                            userAuthEmailStatus.postValue("NOT EXIST");
                        } else {
                            Log.d(TAG, "Authentication Failed : " + task.getException());
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (e instanceof FirebaseAuthWeakPasswordException) {
                            userAuthEmailStatus.postValue("WEAK PASSWORD");
                        } else if (e instanceof FirebaseAuthUserCollisionException) {
                            userAuthEmailStatus.postValue("EMAIL EXIST");
                        } else {
                            Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                        }
                    }
                });
        ;
    }

    public void addUser(User user) {
        try {
            // db.collection(COLLECTION_NAME).add(user).addOnSuccessListener(new
            // OnSuccessListener<DocumentReference>() {
            // @Override
            // public void onSuccess(DocumentReference documentReference) {
            // Log.d(TAG, "Document added with ID : " + documentReference.getId());
            // }
            // }).addOnFailureListener(new OnFailureListener() {
            // @Override
            // public void onFailure(@NonNull Exception e) {
            // Log.e(TAG, "Error adding document to the store " + e);
            // }
            // });

            db.collection(COLLECTION_NAME).document(user.getEmail()).set(user)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "Document added with ID as User EmailId");
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

    public void signInAuthUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    try {
                        db.collection(COLLECTION_NAME).whereEqualTo("email", email)
                                // .whereEqualTo("password",password)
                                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if (task.isSuccessful()) {
                                            if (task.getResult().getDocuments().size() != 0) {

                                                // get the id of the current user logged in
                                                loggedInUserID
                                                        .postValue(task.getResult().getDocuments().get(0).getId());
                                                Log.d(TAG, "Logged in user document ID: " + loggedInUserID);

                                            } else {
                                                signInStatus.postValue("FAILURE");
                                            }
                                        } else {
                                            Log.e(TAG, "Error fetching document" + task.getException());
                                            signInStatus.postValue("FAILURE");
                                        }
                                    }
                                });
                    } catch (Exception ex) {
                        Log.e(TAG, ex.toString());
                        Log.e(TAG, ex.getLocalizedMessage());
                        signInStatus.postValue("FAILURE");
                    }
                    Log.d(TAG, "Login Success");
                    signInStatus.postValue("SUCCESS");
                } else {
                    Log.d(TAG, "Login Failed");
                    signInStatus.postValue("FAILURE");
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    signInStatus.postValue("INVALID PASSWORD");
                } else if (e instanceof FirebaseAuthInvalidUserException) {
                    signInStatus.postValue("INCORRECT EMAIL");
                } else {
                    Log.d(TAG, "onFailure: " + e.getLocalizedMessage());
                }
            }
        });
    }

    public void getUser(String email, String password) {

        this.signInStatus.postValue("LOADING");

        try {
            db.collection(COLLECTION_NAME).whereEqualTo("email", email)
                    // .whereEqualTo("password",password)
                    .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().getDocuments().size() != 0) {
                                    if (task.getResult().getDocuments().get(0).toObject(User.class).getPassword()
                                            .equals(password)) {

                                        signInStatus.postValue("SUCCESS");

                                        // get the id of the current user logged in
                                        loggedInUserID.postValue(task.getResult().getDocuments().get(0).getId());
                                        Log.d(TAG, "Logged in user document ID: " + loggedInUserID);
                                    } else {
                                        signInStatus.postValue("FAILURE");
                                    }
                                } else {
                                    signInStatus.postValue("FAILURE");
                                }
                            } else {
                                Log.e(TAG, "Error fetching document" + task.getException());
                                signInStatus.postValue("FAILURE");
                            }
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
            signInStatus.postValue("FAILURE");
        }
    }

    public void checkUser(String email, String employeeID) {

        this.userExistStatus.postValue("LOADING");

        try {
            db.collection(COLLECTION_NAME).whereEqualTo("email", email).whereEqualTo("empID", employeeID).get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {

                                if (task.getResult().getDocuments().size() != 0) {
                                    if (task.getResult().getDocuments().get(0).toObject(User.class).getEmpID()
                                            .equals(employeeID)) {
                                        userExistStatus.postValue("EMPLOYEEID EXIST");
                                    }

                                }

                    else {
                                    userExistStatus.postValue("NOT EXIST");
                                }

                            } else {
                                Log.e(TAG, "Error fetching document" + task.getException());
                                userExistStatus.postValue("NOT EXIST");
                            }
                        }
                    });
        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
            userExistStatus.postValue("NOT EXIST");
        }
    }

    public void updateUser(User user) {
        try {
            db.collection(COLLECTION_NAME).document(loggedInUserID.getValue())
                    .update("firstName", user.getFirstName(), "lastName", user.getLastName(), "phone", user.getPhone())
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Document updated Successfully ");
                            } else {
                                Log.d(TAG, "Error Updating Document");
                            }

                        }
                    });

        } catch (Exception ex) {
            Log.e(TAG, ex.toString());
            Log.e(TAG, ex.getLocalizedMessage());
        }
    }

    public User getUpdateUserInfo(String userID) {

        db.collection(COLLECTION_NAME).document(userID).get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {

                            DocumentSnapshot documentSnapshot = task.getResult();
                            if (documentSnapshot != null) {

                                newUserInfo = documentSnapshot.toObject(User.class);

                            }

                        } else {

                        }
                    }
                });

        return newUserInfo;
    }

    public void deleteUser(String userID) {
        currentUser.delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Log.d(TAG, "User account deleted.");

                    db.collection(COLLECTION_NAME).document(userID).delete()
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.e(TAG, "Document deleted successfully");
                                    userDeleteStatus.postValue("DELETED");
                                    Log.d(TAG, "onSuccess Delete: " + userDeleteStatus.getValue());
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.e(TAG, "Failure deleting document");
                                }
                            });
                }
            }
        });
    }

}
