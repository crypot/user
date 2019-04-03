package com.exc.domain;

import io.swagger.annotations.ApiModel;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

import com.exc.domain.enumeration.DocumentType;

/**
 * user validation must be validated individually
 */
@ApiModel(description = "user validation must be validated individually")
@Entity
@Table(name = "kyc")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class KYC implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "jhi_file")
    private String file;

    @Enumerated(EnumType.STRING)
    @Column(name = "jhi_type")
    private DocumentType type;

    @Column(name = "is_valid")
    private Boolean isValid;

    @Column(name = "validation_date")
    private LocalDate validationDate;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public KYC file(String file) {
        this.file = file;
        return this;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public DocumentType getType() {
        return type;
    }

    public KYC type(DocumentType type) {
        this.type = type;
        return this;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public Boolean isIsValid() {
        return isValid;
    }

    public KYC isValid(Boolean isValid) {
        this.isValid = isValid;
        return this;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public LocalDate getValidationDate() {
        return validationDate;
    }

    public KYC validationDate(LocalDate validationDate) {
        this.validationDate = validationDate;
        return this;
    }

    public void setValidationDate(LocalDate validationDate) {
        this.validationDate = validationDate;
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
        KYC kYC = (KYC) o;
        if (kYC.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), kYC.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "KYC{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", type='" + getType() + "'" +
            ", isValid='" + isIsValid() + "'" +
            ", validationDate='" + getValidationDate() + "'" +
            "}";
    }
}
