package com.exc.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.Objects;
import com.exc.domain.enumeration.DocumentType;

/**
 * A DTO for the KYC entity.
 */
public class KYCDTO implements Serializable {

    private Long id;

    private String file;

    private DocumentType type;

    private Boolean isValid;

    private LocalDate validationDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public DocumentType getType() {
        return type;
    }

    public void setType(DocumentType type) {
        this.type = type;
    }

    public Boolean isIsValid() {
        return isValid;
    }

    public void setIsValid(Boolean isValid) {
        this.isValid = isValid;
    }

    public LocalDate getValidationDate() {
        return validationDate;
    }

    public void setValidationDate(LocalDate validationDate) {
        this.validationDate = validationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        KYCDTO kYCDTO = (KYCDTO) o;
        if (kYCDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), kYCDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "KYCDTO{" +
            "id=" + getId() +
            ", file='" + getFile() + "'" +
            ", type='" + getType() + "'" +
            ", isValid='" + isIsValid() + "'" +
            ", validationDate='" + getValidationDate() + "'" +
            "}";
    }
}
