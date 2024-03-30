package com.jatin.service;

import com.jatin.model.Order;
import com.jatin.response.PaymentResponse;
import com.stripe.exception.StripeException;

public interface PaymentService {
    public PaymentResponse createPaymentLink(Order order) throws StripeException;
}
