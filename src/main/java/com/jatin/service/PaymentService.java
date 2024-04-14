package com.jatin.service;

import com.jatin.model.Order;
import com.jatin.response.MessageResponse;
import com.jatin.response.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {
    public PaymentResponse createPaymentLink(Long orderId) throws Exception;

    public MessageResponse paymentSuccess(Long orderId) throws Exception;
}
