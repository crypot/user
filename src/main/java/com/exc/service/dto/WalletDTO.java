package com.exc.service.dto;


import com.exc.domain.CurrencyName;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;

/**
 * A DTO for the Wallet entity.
 */
public class WalletDTO implements Serializable {

    private Long id;

    private String privateKey;

    private String publicKey;

    private BigInteger minAmount;

    private Long userInfoId;

    private Long currencyId;

    private CurrencyName currencyName;

    private BigInteger balance;

    private BigInteger lockBalance;

    public BigInteger getLockBalance() {
        return lockBalance;
    }

    public void setLockBalance(BigInteger lockBalance) {
        this.lockBalance = lockBalance;
    }


    public BigInteger getBalance() {
        return balance;
    }

    public void setBalance(BigInteger balance) {
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
    public BigInteger getMinAmount() {
        return minAmount;
    }

    public void setMinAmount(BigInteger minAmount) {
        this.minAmount = minAmount;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public Long getUserInfoId() {
        return userInfoId;
    }

    public void setUserInfoId(Long userInfoId) {
        this.userInfoId = userInfoId;
    }

    public Long getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(Long cryptoCurrencyId) {
        this.currencyId = cryptoCurrencyId;
    }

    public CurrencyName getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(CurrencyName cryptoCurrencyName) {
        this.currencyName = cryptoCurrencyName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        WalletDTO walletDTO = (WalletDTO) o;
        if(walletDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), walletDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "WalletDTO{" +
            "id=" + getId() +
            ", privateKey='" + getPrivateKey() + "'" +
            ", publicKey='" + getPublicKey() + "'" +
            "}";
    }
}
