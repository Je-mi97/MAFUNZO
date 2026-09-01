package com.example.mafunzo.service;

import com.example.mafunzo.dto.PaymentRequest;
import com.example.mafunzo.dto.PaymentResponse;
import com.example.mafunzo.model.Payment;
import com.example.mafunzo.repository.PaymentRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(
            PaymentRepository paymentRepository
    ) {

        this.paymentRepository =
                paymentRepository;
    }

    @Transactional
    public PaymentResponse initiatePayment(
            PaymentRequest request
    ) {

        validateRequest(request);

        /*
         * Vérifie si l'utilisateur
         * possède déjà cette formation.
         */
        boolean alreadyPaid =
                paymentRepository
                        .existsByUserIdAndCourseIdAndStatus(
                                request.getUserId(),
                                request.getCourseId(),
                                Payment.PaymentStatus.SUCCESS
                        );

        if (alreadyPaid) {

            throw new IllegalStateException(
                    "Cette formation a déjà été achetée."
            );
        }

        /*
         * Génération d'une référence unique.
         */
        String reference =
                generateReference();

        Payment payment =
                new Payment(
                        reference,
                        request.getUserId(),
                        request.getCourseId(),
                        request.getCourseName(),
                        request.getAmount(),
                        request.getCurrency(),
                        request.getPaymentMethod(),
                        request.getPhone()
                );

        Payment savedPayment =
                paymentRepository.save(
                        payment
                );

        return new PaymentResponse(
                savedPayment
        );
    }

    public PaymentResponse getPaymentByReference(
            String reference
    ) {

        Payment payment =
                paymentRepository
                        .findByReference(reference)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Paiement introuvable."
                                )
                        );

        return new PaymentResponse(
                payment
        );
    }

    public List<PaymentResponse> getUserPayments(
            Long userId
    ) {

        return paymentRepository
                .findByUserIdOrderByCreatedAtDesc(
                        userId
                )
                .stream()
                .map(
                        PaymentResponse::new
                )
                .collect(
                        Collectors.toList()
                );
    }

    @Transactional
    public PaymentResponse simulateSuccess(
            String reference
    ) {

        Payment payment =
                paymentRepository
                        .findByReference(reference)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Paiement introuvable."
                                )
                        );

        if (payment.getStatus()
                == Payment.PaymentStatus.SUCCESS) {

            return new PaymentResponse(
                    payment
            );
        }

        payment.setStatus(
                Payment.PaymentStatus.SUCCESS
        );

        Payment updated =
                paymentRepository.save(
                        payment
                );

        return new PaymentResponse(
                updated
        );
    }

    @Transactional
    public PaymentResponse simulateFailure(
            String reference
    ) {

        Payment payment =
                paymentRepository
                        .findByReference(reference)
                        .orElseThrow(
                                () -> new RuntimeException(
                                        "Paiement introuvable."
                                )
                        );

        payment.setStatus(
                Payment.PaymentStatus.FAILED
        );

        Payment updated =
                paymentRepository.save(
                        payment
                );

        return new PaymentResponse(
                updated
        );
    }

    private String generateReference() {

        return "MAF-"
                + UUID.randomUUID()
                .toString()
                .replace(
                        "-",
                        ""
                )
                .substring(
                        0,
                        12
                )
                .toUpperCase();
    }

    private void validateRequest(
            PaymentRequest request
    ) {

        if (request == null) {

            throw new IllegalArgumentException(
                    "La requête de paiement est obligatoire."
            );
        }

        if (request.getUserId() == null) {

            throw new IllegalArgumentException(
                    "L'utilisateur est obligatoire."
            );
        }

        if (isEmpty(request.getCourseId())) {

            throw new IllegalArgumentException(
                    "L'identifiant de la formation est obligatoire."
            );
        }

        if (isEmpty(request.getCourseName())) {

            throw new IllegalArgumentException(
                    "Le nom de la formation est obligatoire."
            );
        }

        if (request.getAmount() == null
                || request.getAmount() <= 0) {

            throw new IllegalArgumentException(
                    "Le montant doit être supérieur à zéro."
            );
        }

        if (isEmpty(request.getCurrency())) {

            throw new IllegalArgumentException(
                    "La devise est obligatoire."
            );
        }

        if (request.getPaymentMethod() == null) {

            throw new IllegalArgumentException(
                    "Le moyen de paiement est obligatoire."
            );
        }

        if (isEmpty(request.getPhone())) {

            throw new IllegalArgumentException(
                    "Le numéro de téléphone est obligatoire."
            );
        }
    }

    private boolean isEmpty(
            String value
    ) {

        return value == null
                || value.trim().isEmpty();
    }
}