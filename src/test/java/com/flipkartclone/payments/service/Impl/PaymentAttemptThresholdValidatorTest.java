package com.flipkartclone.payments.service.Impl;

import com.flipkartclone.payments.exception.PaymentValidationException;
import com.flipkartclone.payments.model.ValidationContext;
import com.flipkartclone.payments.pojo.CreatePaymentRequest;
import com.flipkartclone.payments.pojo.Payment;
import com.flipkartclone.payments.pojo.User;
import com.flipkartclone.payments.service.interfaces.MerchantPaymentRequestService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentAttemptThresholdValidatorTest {

    @Mock
    private MerchantPaymentRequestService merchantPaymentRequestService;

    @InjectMocks
    private PaymentAttemptThresholdValidator validator;

    @Test
    void shouldUseDatabaseDrivenConfigForThresholdValidation() {
        CreatePaymentRequest request = buildRequest("user-1");
        ValidationContext context = new ValidationContext(
                "PAYMENT_ATTEMPT_THRESHOLD_RULE",
                (short) 10,
                Map.of("durationInMins", "2", "maxPaymentThreshold", "5")
        );

        when(merchantPaymentRequestService.countRecentRequests("user-1", 2)).thenReturn(5);

        assertDoesNotThrow(() -> validator.validate(request, context));
        verify(merchantPaymentRequestService).countRecentRequests("user-1", 2);
    }

    @Test
    void shouldFailWhenThresholdIsExceeded() {
        CreatePaymentRequest request = buildRequest("user-2");
        ValidationContext context = new ValidationContext(
                "PAYMENT_ATTEMPT_THRESHOLD_RULE",
                (short) 10,
                Map.of("durationInMins", "2", "maxPaymentThreshold", "5")
        );

        when(merchantPaymentRequestService.countRecentRequests("user-2", 2)).thenReturn(6);

        assertThrows(PaymentValidationException.class, () -> validator.validate(request, context));
        verify(merchantPaymentRequestService).countRecentRequests("user-2", 2);
    }

    private CreatePaymentRequest buildRequest(String endUserId) {
        User user = new User();
        user.setEndUserID(endUserId);

        Payment payment = new Payment();
        payment.setMerchantTxnRef("txn-" + endUserId);

        CreatePaymentRequest request = new CreatePaymentRequest();
        request.setUser(user);
        request.setPayment(payment);
        return request;
    }
}
