package com.jatin.service;

import com.jatin.model.Order;
import com.jatin.repository.OrderRepository;
import com.jatin.response.MessageResponse;
import com.jatin.response.PaymentResponse;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public PaymentResponse createPaymentLink(Long orderId) throws Exception {
        Order order = orderService.findOrderById(orderId);
        Stripe.apiKey = stripeSecretKey;
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment/success/" + order.getId())
                .setCancelUrl("http://localhost:3000/payment/failure/" + order.getId())
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("inr")
                                                .setUnitAmount((long) order.getTotalPrice() * 100 + 7000)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Hungrio Food")
                                                                .setDescription("Khayega India, Tabhi to badhega India")
                                                                .build()
                                                ).build()
                                ).build()
                )
                .build();

        Session session = Session.create(params);
        PaymentResponse paymentResponse = new PaymentResponse();
        paymentResponse.setPayment_url(session.getUrl());
        return paymentResponse;
    }

    @Override
    public MessageResponse paymentSuccess(Long orderId) throws Exception {
        Order order = orderService.findOrderById(orderId);
        order.setPaymentSuccess(true);
        orderRepository.save(order);
        return new MessageResponse("Payment Success");
    }
}
