package com.myyelp.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.myyelp.data.models.Business;
import com.myyelp.data.models.SearchResponse;
import com.myyelp.data.repos.MainRepo;

import kotlinx.coroutines.CoroutineScope;

public class MainViewModel extends ViewModel {
    public MainRepo mainRepo;
    public String searchTerm = "";

    public MainViewModel(MainRepo mainRepo){
        this.mainRepo = mainRepo;
    }

    public void searchBusiness(){
        mainRepo.search(searchTerm);
    }

    public void getFavourites(){
        mainRepo.getFavourites();
    }

    public void addFavourite(Business business){
        mainRepo.addFavourite(business);
    }
}

class ViewModelFactory implements ViewModelProvider.Factory {
    private MainRepo mainRepo;
    public ViewModelFactory(MainRepo mainRepo){
        this.mainRepo = mainRepo;
    }
    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> aClass) {
        if (aClass.isAssignableFrom(MainViewModel.class)){
            return (T) new MainViewModel(mainRepo);
        }
        throw new IllegalArgumentException("No such ViewModel Class");
    }
}

