package com.example.wip_android.viewmodels;

import com.example.wip_android.models.ClientInfo;
import com.example.wip_android.repositories.HomeListRepository;

import java.util.List;

public class HomeListViewModel {

    // Variables
    private static final HomeListViewModel ourInstance = new HomeListViewModel();
    private final HomeListRepository homeListRepository = new HomeListRepository();

    // Constructor
    private HomeListViewModel() {
    }

    // Methods
    public List<ClientInfo> getHomeList(String department) {
        List<ClientInfo> clientInfoList = this.homeListRepository.getHomeList(department);
        return clientInfoList;
    }

    // Getters
    public HomeListRepository getHomeListRepository() {
        return homeListRepository;
    }

    public static HomeListViewModel getInstance() {
        return ourInstance;
    }

}
