package com.jatin.controller;

import com.jatin.model.Order;
import com.jatin.model.User;
import com.jatin.request.CreateOrderRequest;
import com.jatin.response.MessageResponse;
import com.jatin.response.PaymentResponse;
import com.jatin.service.OrderService;
import com.jatin.service.PaymentService;
import com.jatin.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class OrderController {
    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @GetMapping("/order/payment/link/{orderId}")
    public ResponseEntity<PaymentResponse> getPaymentLink(@PathVariable Long orderId,
                                                          @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.findOrderById(orderId);
        PaymentResponse paymentResponse = paymentService.createPaymentLink(orderId);
        System.out.println(paymentResponse.getPayment_url());
        return new ResponseEntity<>(paymentResponse, HttpStatus.OK);
    }

    @PutMapping("/order/payment/success/{orderId}")
    public ResponseEntity<MessageResponse> paymentSuccess(@PathVariable Long orderId,
                                                          @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        MessageResponse messageResponse = paymentService.paymentSuccess(orderId);
        return new ResponseEntity<>(messageResponse, HttpStatus.OK);
    }

    @DeleteMapping("/order/payment/failure/{orderId}")
    public ResponseEntity<MessageResponse> paymentFailure(@PathVariable Long orderId,
                                                          @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        orderService.cancelOrder(orderId);
        return new ResponseEntity<>(new MessageResponse("Success"), HttpStatus.OK);
    }

    @PostMapping("/order")
    public ResponseEntity<PaymentResponse> createOrder(@RequestBody CreateOrderRequest req,
                                                       @RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        Order order = orderService.createOrder(req, user);
        PaymentResponse paymentResponse = paymentService.createPaymentLink(order.getId());
        return new ResponseEntity<>(paymentResponse, HttpStatus.CREATED);
    }

    @GetMapping("/orders/user")
    public ResponseEntity<List<Order>> getUserOrders(@RequestHeader("Authorization") String jwt) throws Exception {
        User user = userService.findUserByJwtToken(jwt);
        List<Order> orders = orderService.getUsersOrder(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }
}
