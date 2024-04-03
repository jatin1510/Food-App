package com.jatin.service;

import com.jatin.model.Address;
import com.jatin.model.User;
import com.jatin.request.CreateAddressRequest;

import java.util.List;

public interface AddressService {
    public Address createAddress(CreateAddressRequest req, User user);

    public void deleteAddress(Long addressId, User user) throws Exception;

    public List<Address> getAllAddress(User user);
}
