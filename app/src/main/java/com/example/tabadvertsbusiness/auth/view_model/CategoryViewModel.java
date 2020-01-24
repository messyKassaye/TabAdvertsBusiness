package com.example.tabadvertsbusiness.auth.view_model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import com.example.tabadvertsbusiness.auth.repository.CategoryRepository;
import com.example.tabadvertsbusiness.auth.response.CategoryResponse;


public class CategoryViewModel extends AndroidViewModel {
    private CategoryRepository categoryRepository;
    private final LiveData<CategoryResponse> responseLiveData;

    public CategoryViewModel(@NonNull Application application) {
        super(application);

        categoryRepository =new CategoryRepository();
        responseLiveData = categoryRepository.index();
    }

    public LiveData<CategoryResponse> index(){
        return  responseLiveData;
    }


}
