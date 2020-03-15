package com.example.tabadvertsbusiness.auth.repository;

import com.example.tabadvertsbusiness.auth.model.Address;
import com.example.tabadvertsbusiness.auth.response.SuccessResponse;
import com.example.tabadvertsbusiness.auth.retrofit.RetrofitRequest;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.AddressInterface;
import com.example.tabadvertsbusiness.auth.retrofit.interfaces.CategoryInterface;

import io.reactivex.Observable;

public class AddressRepository {
    private AddressInterface addressInterface;

    public AddressRepository() {
        addressInterface = RetrofitRequest.getApiInstance().create(AddressInterface.class);
    }

    public Observable<SuccessResponse> store(Address address){
        return addressInterface.store(address);
    }
}
