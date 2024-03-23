package com.fdmgroup.CreditCardProject.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.persistence.*;


@Entity
@Table(name = "`Credit Card Transaction`")
public class CreditCardTransaction {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "transactionId")
    private long transactionId;

    @ManyToOne
    @JoinColumn(name = "creditCardId")
    private CreditCard creditCard;

    @Column(name = "date")
    private LocalDateTime date;

    public LocalDateTime getDate() {
        return date;
    }

    public void setDate(LocalDateTime dateTime) {
        this.date = dateTime;
    }

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "storeInfo")
    private String storeInfo;

    @ManyToOne
    @JoinColumn(name = "merchantCategoryCodeId")
    private MerchantCategoryCode merchantCategoryCode;

    @Column(name = "originalCurrencyCode")
    private String originalCurrencyCode;

    @Column(name = "originalCurrencyAmount")
    private double originalCurrencyAmount;

    public CreditCardTransaction() {
        super();
    }

    public CreditCardTransaction(long transactionId, CreditCard creditCard, BigDecimal amount, String storeInfo, MerchantCategoryCode merchantCategoryCode, String originalCurrencyCode, double originalCurrencyAmount) {
        this.transactionId = transactionId;
        this.creditCard = creditCard;
        this.date = LocalDateTime.now();
        this.amount = amount;
        this.storeInfo = storeInfo;
        this.merchantCategoryCode = merchantCategoryCode;
        this.originalCurrencyCode = originalCurrencyCode;
        this.originalCurrencyAmount = originalCurrencyAmount;
    }


    public CreditCard getCreditCard() {
        return creditCard;
    }

    public void setCreditCard(CreditCard creditCard) {
        this.creditCard = creditCard;
    }

    public String getStoreInfo() {
        return storeInfo;
    }

    public void setStoreInfo(String storeInfo) {
        this.storeInfo = storeInfo;
    }

    public MerchantCategoryCode getMerchantCategoryCode() {
        return merchantCategoryCode;
    }

    public void setMerchantCategoryCode(MerchantCategoryCode merchantCategoryCode) {
        this.merchantCategoryCode = merchantCategoryCode;
    }

    public String getOriginalCurrencyCode() {
        return originalCurrencyCode;
    }

    public void setOriginalCurrencyCode(String originalCurrencyCode) {
        this.originalCurrencyCode = originalCurrencyCode;
    }

    public double getOriginalCurrencyAmount() {
        return originalCurrencyAmount;
    }

    public void setOriginalCurrencyAmount(double originalCurrencyAmount) {
        this.originalCurrencyAmount = originalCurrencyAmount;
    }

    public long getTransactionId() {
        return transactionId;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

}
