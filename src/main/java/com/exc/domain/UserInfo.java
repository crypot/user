package com.exc.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * UserDetails extends User
 */
@ApiModel(description = "UserDetails extends User")
@Entity
@Table(name = "user_info")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class UserInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    /**
     * inducate if 2fa enabled
     */
    @ApiModelProperty(value = "inducate if 2fa enabled")
    @Column(name = "is_2_fa")
    private Boolean is2Fa;

    /**
     * secret private key for 2fa
     */
    @ApiModelProperty(value = "secret private key for 2fa")
    @Column(name = "secret")
    private String secret;

    @Column(name = "street_address")
    private String streetAddress;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "city")
    private String city;

    @Column(name = "state_province")
    private String stateProvince;

    @Column(name = "country_code")
    private String countryCode;

    /**
     * User details
     */
    @Column(name = "user_id", unique = true)
    private Long userId;

    /**
     * User kyc
     */
    @ApiModelProperty(value = "User kyc")
    @OneToOne
    @JoinColumn(unique = true)
    private KYC kyc;

    @OneToMany(mappedBy = "userInfo")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Wallet> wallets = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isIs2Fa() {
        return is2Fa;
    }

    public UserInfo is2Fa(Boolean is2Fa) {
        this.is2Fa = is2Fa;
        return this;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public void setIs2Fa(Boolean is2Fa) {
        this.is2Fa = is2Fa;
    }

    public String getSecret() {
        return secret;
    }

    public UserInfo secret(String secret) {
        this.secret = secret;
        return this;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public String getStreetAddress() {
        return streetAddress;
    }

    public UserInfo streetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
        return this;
    }

    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public UserInfo postalCode(String postalCode) {
        this.postalCode = postalCode;
        return this;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public UserInfo city(String city) {
        this.city = city;
        return this;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStateProvince() {
        return stateProvince;
    }

    public UserInfo stateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
        return this;
    }

    public void setStateProvince(String stateProvince) {
        this.stateProvince = stateProvince;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public UserInfo countryCode(String countryCode) {
        this.countryCode = countryCode;
        return this;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }


    public KYC getKyc() {
        return kyc;
    }

    public UserInfo kyc(KYC kYC) {
        this.kyc = kYC;
        return this;
    }

    public void setKyc(KYC kYC) {
        this.kyc = kYC;
    }

    public Set<Wallet> getWallets() {
        return wallets;
    }

    public UserInfo wallets(Set<Wallet> wallets) {
        this.wallets = wallets;
        return this;
    }

    public UserInfo addWallet(Wallet wallet) {
        this.wallets.add(wallet);
        wallet.setUserInfo(this);
        return this;
    }

    public UserInfo removeWallet(Wallet wallet) {
        this.wallets.remove(wallet);
        wallet.setUserInfo(null);
        return this;
    }

    public void setWallets(Set<Wallet> wallets) {
        this.wallets = wallets;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UserInfo userInfo = (UserInfo) o;
        if (userInfo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), userInfo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "UserInfo{" +
            "id=" + getId() +
            ", is2Fa='" + isIs2Fa() + "'" +
            ", secret='" + getSecret() + "'" +
            ", streetAddress='" + getStreetAddress() + "'" +
            ", postalCode='" + getPostalCode() + "'" +
            ", city='" + getCity() + "'" +
            ", stateProvince='" + getStateProvince() + "'" +
            ", countryCode='" + getCountryCode() + "'" +
            "}";
    }
}
