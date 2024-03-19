package com.jatin.request;

import com.jatin.model.Address;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    private Long restaurantId;
    private Address deliveryAddress;
}
