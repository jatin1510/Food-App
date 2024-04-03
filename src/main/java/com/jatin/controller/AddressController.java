package com.jatin.controller;

import com.jatin.model.Address;
import com.jatin.model.User;
import com.jatin.request.CreateAddressRequest;
import com.jatin.response.MessageResponse;
import com.jatin.service.AddressService;
import com.jatin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/address")
public class AddressController {
    @Autowired
    private UserService userService;

    @Autowired
    private AddressService addressService;

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody CreateAddressRequest request,
                                                 @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Address address = addressService.createAddress(request, user);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @DeleteMapping("/{addressId}")
    public ResponseEntity<MessageResponse> deleteAddress(@PathVariable Long addressId,
                                                         @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        addressService.deleteAddress(addressId, user);
        MessageResponse messageResponse = new MessageResponse();
        messageResponse.setMessage("Delete Successfully");
        return new ResponseEntity<>(messageResponse, HttpStatus.ACCEPTED);
    }

    @GetMapping
    public ResponseEntity<List<Address>> getAllAddress(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Address> addresses = addressService.getAllAddress(user);
        return new ResponseEntity<>(addresses, HttpStatus.OK);
    }
}
