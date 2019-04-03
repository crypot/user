package com.exc.service.mapper;

import com.exc.domain.*;
import com.exc.service.dto.KYCDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity KYC and its DTO KYCDTO.
 */
@Mapper(componentModel = "spring", uses = {})
public interface KYCMapper extends EntityMapper<KYCDTO, KYC> {



    default KYC fromId(Long id) {
        if (id == null) {
            return null;
        }
        KYC kYC = new KYC();
        kYC.setId(id);
        return kYC;
    }
}
