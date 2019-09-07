package com.anshultiwari.androidassignment.ViewModel;

import android.app.Application;

import com.anshultiwari.androidassignment.Repository.CelebRepository;
import com.anshultiwari.androidassignment.Model.Celebrity;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

public class CelebViewModel extends AndroidViewModel {
    private CelebRepository repository;
    private LiveData<List<Celebrity>> celebs;

    public CelebViewModel(@NonNull Application application) {
        super(application);
        repository = new CelebRepository(application);
        celebs = repository.getAllCelebs();
    }

    public LiveData<List<Celebrity>> getAllCelebs() {
        return celebs;
    }

    public void fetchCelebsAndStore() {
        repository.celebsApi();
    }

}
