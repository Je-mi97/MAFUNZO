package com.example.mafunzo.controller;

import com.example.mafunzo.dto.PaymentRequest;
import com.example.mafunzo.dto.PaymentResponse;
import com.example.mafunzo.service.PaymentService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/payments")
@CrossOrigin
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(
            PaymentService paymentService
    ) {

        this.paymentService =
                paymentService;
    }

    /*
     * Créer un paiement.
     */
    @PostMapping("/initiate")
    public ResponseEntity<?> initiatePayment(
            @RequestBody PaymentRequest request
    ) {

        try {

            PaymentResponse response =
                    paymentService
                            .initiatePayment(
                                    request
                            );

            return ResponseEntity
                    .status(
                            HttpStatus.CREATED
                    )
                    .body(response);

        } catch (IllegalArgumentException e) {

            return ResponseEntity
                    .badRequest()
                    .body(
                            e.getMessage()
                    );

        } catch (IllegalStateException e) {

            return ResponseEntity
                    .status(
                            HttpStatus.CONFLICT
                    )
                    .body(
                            e.getMessage()
                    );
        }
    }

    /*
     * Consulter un paiement
     * grâce à sa référence.
     */
    @GetMapping("/{reference}")
    public ResponseEntity<?> getPayment(
            @PathVariable String reference
    ) {

        try {

            PaymentResponse response =
                    paymentService
                            .getPaymentByReference(
                                    reference
                            );

            return ResponseEntity.ok(
                    response
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(
                            HttpStatus.NOT_FOUND
                    )
                    .body(
                            e.getMessage()
                    );
        }
    }

    /*
     * Historique des paiements
     * d'un utilisateur.
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PaymentResponse>>
    getUserPayments(
            @PathVariable Long userId
    ) {

        return ResponseEntity.ok(
                paymentService
                        .getUserPayments(
                                userId
                        )
        );
    }

    /*
     * SIMULATION :
     * paiement réussi.
     *
     * Cette route sera conservée
     * temporairement pour les tests.
     */
    @PostMapping("/{reference}/simulate-success")
    public ResponseEntity<?> simulateSuccess(
            @PathVariable String reference
    ) {

        try {

            PaymentResponse response =
                    paymentService
                            .simulateSuccess(
                                    reference
                            );

            return ResponseEntity.ok(
                    response
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(
                            HttpStatus.NOT_FOUND
                    )
                    .body(
                            e.getMessage()
                    );
        }
    }

    /*
     * SIMULATION :
     * paiement échoué.
     */
    @PostMapping("/{reference}/simulate-failure")
    public ResponseEntity<?> simulateFailure(
            @PathVariable String reference
    ) {

        try {

            PaymentResponse response =
                    paymentService
                            .simulateFailure(
                                    reference
                            );

            return ResponseEntity.ok(
                    response
            );

        } catch (RuntimeException e) {

            return ResponseEntity
                    .status(
                            HttpStatus.NOT_FOUND
                    )
                    .body(
                            e.getMessage()
                    );
        }
    }
}