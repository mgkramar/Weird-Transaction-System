package com.study.demo.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import com.study.demo.model.StateEnum;
import com.study.demo.validator.NullOrNotBlank;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@IdClass(TransactionId.class)
@Table(name = "TRANSACTION")
public class Transaction {

    @NotNull
    @Column(name = "AMOUNT")
    private long amount;

    @Column(name = "CURRENT_STATE")
    @Enumerated(EnumType.STRING)
    private StateEnum state;

    @Column(name = "INFORMED")
    private int informed;

    @Id
    @NotEmpty
    @Column(name = "TRANSACTION_REFERENCE")
    private String transactionReference;

    @NullOrNotBlank
    @Column(name = "PREVIOUS_TRANSACTION_REFERENCE")
    private String previousTransactionReference;

    @NotEmpty
    @Column(name = "RECEIVER_FIRST_NAME")
    private String receiverFirstName;

    @NotEmpty
    @Column(name = "RECEIVER_LAST_NAME")
    private String receiverLastName;

    @NotEmpty
    @Column(name = "RECEIVER_DOCUMENT_NUMBER")
    private String receiverDocumentNumber;

    @NotEmpty
    @Column(name = "RECEIVER_ACCOUNT_NUMBER")
    private String receiverAccountNumber;

    @Id
    @NotEmpty
    @Column(name = "RECEIVER_ENTITY_CODE")
    private String receiverEntityCode;

    @NotEmpty
    @Column(name = "SENDER_FIRST_NAME")
    private String senderFirstName;

    @NotEmpty
    @Column(name = "SENDER_LAST_NAME")
    private String senderLastName;

    @NotEmpty
    @Column(name = "SENDER_DOCUMENT_NUMBER")
    private String senderDocumentNumber;

    @NotEmpty
    @Column(name = "SENDER_ACCOUNT_NUMBER")
    private String senderAccountNumber;

    @NotEmpty
    @Column(name = "SENDER_ENTITY_CODE")
    private String senderEntityCode;

}