package com.jatin.controller;

import com.jatin.model.Order;
import com.jatin.service.OrderService;
import com.jatin.service.RestaurantService;
import com.jatin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminOrderController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/orders/restaurant/{restaurantId}")
    public ResponseEntity<List<Order>> getRestaurantOrders(@PathVariable Long restaurantId,
                                                           @RequestParam(required = false) String orderStatus,
                                                           @RequestHeader("Authorization") String jwt) throws Exception {

        List<Order> orders = orderService.getRestaurantsOrder(restaurantId, orderStatus);
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @PutMapping("/orders/update/{orderId}/{orderStatus}")
    public ResponseEntity<Order> updateOrderStatus(@PathVariable Long orderId,
                                                           @PathVariable String orderStatus,
                                                           @RequestHeader("Authorization") String jwt) throws Exception {

        Order order = orderService.updateOrder(orderId, orderStatus);
        return new ResponseEntity<>(order, HttpStatus.OK);
    }
}
