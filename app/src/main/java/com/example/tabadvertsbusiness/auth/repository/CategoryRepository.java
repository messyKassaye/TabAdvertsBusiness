package com.example.tabadvertsbusiness.auth.repository;

import androidx.lifecycle.MutableLiveData;

import com.example.tabadvertsbusiness.auth.response.CategoryResponse;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.CategoryInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CategoryRepository {
    private CategoryInterface categoryInterface;

    public CategoryRepository() {
        categoryInterface = RetrofitRequest.getApiInstance().create(CategoryInterface.class);
    }

    public MutableLiveData<CategoryResponse> index(){
        final MutableLiveData<CategoryResponse> data = new MutableLiveData<>();
        categoryInterface.get().enqueue(new Callback<CategoryResponse>() {
            @Override
            public void onResponse(Call<CategoryResponse> call, Response<CategoryResponse> response) {
                if(response.body()!=null){
                    data.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<CategoryResponse> call, Throwable t) {
                data.setValue(null);

            }
        });
        return data;
    }
}
