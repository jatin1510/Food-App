package com.jatin.service;

import com.jatin.model.Address;
import com.jatin.model.User;
import com.jatin.repository.AddressRepository;
import com.jatin.request.CreateAddressRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressServiceImpl implements AddressService {
    @Autowired
    private AddressRepository addressRepository;

    @Override
    public Address createAddress(CreateAddressRequest req, User user) {
        Address address = new Address();
        address.setStreetAddress(req.getStreetAddress());
        address.setCity(req.getCity());
        address.setStateProvince(req.getStateProvince());
        address.setCountry(req.getCountry());
        address.setPostalCode(req.getPostalCode());
        address.setUser(user);
        return addressRepository.save(address);
    }

    @Override
    public void deleteAddress(Long addressId, User user) throws Exception {
        Optional<Address> address = addressRepository.findById(addressId);
        if (address.isEmpty()) {
            throw new Exception("Address not found with id " + addressId);
        }

        if (address.get().getUser() != user) {
            throw new Exception("You have not access");
        }

        addressRepository.delete(address.get());
    }

    @Override
    public List<Address> getAllAddress(User user) {
        return addressRepository.findByUserId(user.getId());
    }
}
