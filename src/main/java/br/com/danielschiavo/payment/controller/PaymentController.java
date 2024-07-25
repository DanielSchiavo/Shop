package br.com.danielschiavo.payment.controller;

import br.com.danielschiavo.customer.dto.response.card.DetailCardResponse;
import br.com.danielschiavo.customer.service.card.CardService;
import br.com.danielschiavo.order.dto.response.DetailOrderResponse;
import br.com.danielschiavo.order.service.OrderService;
import br.com.danielschiavo.payment.dto.request.AddPaymentRequest;
import br.com.danielschiavo.payment.dto.response.ShowPaymentResponse;
import br.com.danielschiavo.payment.repository.PaymentRepository;
import br.com.danielschiavo.payment.service.PaymentService;
import br.com.danielschiavo.shared.Response;
import br.com.danielschiavo.shared.infra.security.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/payments")
public class PaymentController {

    @Autowired
    private PaymentService service;

    @Autowired
    private SecurityService securityService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CardService cardService;
    
    @PostMapping
    public ResponseEntity<?> executePayment(@RequestBody AddPaymentRequest request) {
        Long customerId = securityService.getCustomerId();
        DetailOrderResponse order = orderService.getOrderById(request.orderId());
        DetailCardResponse card = null;
        if (request.cardId() != null) {
            card = cardService.getCardByIdAndCustomerId(request.cardId(), customerId);
        }
        ShowPaymentResponse payment = service.executePayment(request, order, card);

        return ResponseEntity.ok().body(Response.success("Payment realized successfully!", payment));
    }
}
