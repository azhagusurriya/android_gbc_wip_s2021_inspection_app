package com.example.wip_android.fragments;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.wip_android.R;
import com.example.wip_android.models.DeficiencyCompletion;
import com.example.wip_android.models.DeficiencyInfo;
import com.example.wip_android.models.ProjectInfo;
import com.example.wip_android.models.States;
import com.example.wip_android.viewmodels.DeficiencyListViewModel;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class DeficiencyListFragment extends Fragment {

    // Variables
    private DeficiencyListViewModel mViewModel;
    String[] arrayDeficiency = {"1 Test", "2 Test", "3 Test"};
    ArrayAdapter<String> adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String COLLECTION_CLIENT = "Client";
    private String COLLECTION_PROJECT = "Project";
    private String COLLECTION_DEFICIENCY = "Deficiencies";
    private String clientName;
    ListView listView;
    MyCustomAdapter dataAdapter = null;

    // Fragment settings
    public static DeficiencyListFragment newInstance() {
        return new DeficiencyListFragment();
    }

    // Default function
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.deficiency_list_fragment, container, false);

        // Generate list View from ArrayList
        listView = (ListView) view.findViewById(R.id.listView);
        displayListView();

//        checkButtonClick();



        // Get variables
        clientName = this.getArguments().getString("name");

        //        getDeficiencyFromFirebase();


//        adapter = new ArrayAdapter<String>(
//                getActivity(),
//                android.R.layout.simple_list_item_multiple_choice,
//                arrayDeficiency);
//        listView.setAdapter(adapter);

        return view;
    }

    private void displayListView() {

        // Array list of countries
        ArrayList<States> stateList = new ArrayList<States>();

        States _states = new States("91", "India", false);
        stateList.add(_states);
        _states = new States("61", "Australia", true);
        stateList.add(_states);
        _states = new States("55", "Brazil", false);
        stateList.add(_states);
        _states = new States("86", "China", true);
        stateList.add(_states);
        _states = new States("49", "Germany", true);
        stateList.add(_states);
        _states = new States("36", "Hungary", false);
        stateList.add(_states);
        _states = new States("39", "Italy", false);
        stateList.add(_states);
        _states = new States("1", "US", false);
        stateList.add(_states);
        _states = new States("44", "UK", false);
        stateList.add(_states);

        // create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(getActivity(), R.layout.state_info, stateList);
//        ListView listView = (ListView) findViewById(R.id.listView);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                States state = (States) parent.getItemAtPosition(position);
//                Toast.makeText(getApplicationContext(),
//                        "Clicked on : " + state.getName(), Toast.LENGTH_LONG)
//                        .show();
            }
        });
    }

    private class MyCustomAdapter extends ArrayAdapter<States> {

        private ArrayList<States> stateList;

        public MyCustomAdapter(Context context, int textViewResourceId,

                               ArrayList<States> stateList) {
            super(context, textViewResourceId, stateList);
            this.stateList = new ArrayList<States>();
            this.stateList.addAll(stateList);
        }

        private class ViewHolder {
            TextView code;
            CheckBox name;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;

            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {

                LayoutInflater vi = (LayoutInflater)getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = vi.inflate(R.layout.state_info, null);

                holder = new ViewHolder();
                holder.code = (TextView) convertView.findViewById(R.id.code);
                holder.name = (CheckBox) convertView
                        .findViewById(R.id.checkBox1);

                convertView.setTag(holder);

                holder.name.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        CheckBox cb = (CheckBox) v;
                        States _state = (States) cb.getTag();

                        System.out.println(cb.getText());

//                        Toast.makeText(
//                                getApplicationContext(),
//                                "Checkbox: " + cb.getText() + " -> "
//                                        + cb.isChecked(), Toast.LENGTH_LONG)
//                                .show();

                        _state.setSelected(cb.isChecked());
                    }
                });

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            States state = stateList.get(position);

            holder.code.setText(" (" + state.getCode() + ")");
            holder.name.setText(state.getName());
            holder.name.setChecked(state.isSelected());

            holder.name.setTag(state);

            return convertView;
        }

    }



    // Upload information to Firebase Database
    public void getDeficiencyFromFirebase() {
        // Check and get current project information
        db.collection(COLLECTION_CLIENT).whereEqualTo("clientName", clientName).get()
                .addOnCompleteListener(secondTask -> {
                    if (secondTask.isSuccessful()) {
                        String clientId = secondTask.getResult().getDocuments().get(0).getId();
                        System.out.println("CLIENT ID: " + clientId);
                        db.collection(COLLECTION_CLIENT).document(clientId).collection(COLLECTION_PROJECT).get()
                                .addOnCompleteListener(thirdTask -> {
                                    if (thirdTask.isSuccessful()) {
                                        String projectId = thirdTask.getResult().getDocuments().get(0).getId();
                                        ProjectInfo projectInfo = thirdTask.getResult().getDocuments().get(0).toObject(ProjectInfo.class);

                                        db.collection(COLLECTION_CLIENT).document(clientId).collection(COLLECTION_PROJECT).document(projectId)
                                                .collection(COLLECTION_DEFICIENCY).get()
                                                .addOnCompleteListener(newTask -> {
                                                    if (newTask.isSuccessful()) {
                                                        for (int i = 0; i < newTask.getResult().getDocuments().size(); i++) {
                                                            String newId = newTask.getResult().getDocuments().get(i).getId();
                                                            System.out.println("NEW ID: " + newId);
                                                            System.out.println("------------------------");
//                                                            System.out.println(db.collection(COLLECTION_CLIENT)
//                                                                    .document(clientId)
//                                                                    .collection(COLLECTION_PROJECT)
//                                                                    .document(projectId)
//                                                                    .collection(COLLECTION_DEFICIENCY)
//                                                                    .document(newId));

                                                            System.out.println("COMMENT: "+newTask.
                                                                    getResult().
                                                                    getDocuments().
                                                                    get(i).toObject(DeficiencyInfo.class).getCommentBefore());
                                                        }

//                                                        ProjectInfo projectInfo = newTask.getResult().getDocuments().get(0).toObject(ProjectInfo.class);
//                                                        System.out.println("NEW ID: " + newTask.getResult().getDocuments().get(0).toObject(ProjectInfo.class));
                                                    }
                                                    });

//                                        System.out.println(db.collection(COLLECTION_CLIENT).document(clientId)
//                                                .collection(COLLECTION_PROJECT).document(projectId)
//                                                .collection(COLLECTION_DEFICIENCY).get());



                                    }
                                });
                    }

                });
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(DeficiencyListViewModel.class);
        // TODO: Use the ViewModel
    }

}