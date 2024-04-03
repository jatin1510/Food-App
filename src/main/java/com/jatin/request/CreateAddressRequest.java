package com.jatin.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateAddressRequest {
    private String streetAddress;

    private String city;

    private String stateProvince;

    private String postalCode;

    private String country;
}
