package com.exc.service.mapper;

import com.exc.domain.Wallet;
import com.exc.service.dto.GenWalletDTO;
import com.exc.service.dto.WalletDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * Mapper for the entity Wallet and its DTO WalletDTO.
 */
@Mapper(componentModel = "spring", uses = {UserInfoMapper.class})
public interface WalletMapper extends EntityMapper<WalletDTO, Wallet> {
    @Mapping(source = "userInfo.id", target = "userInfoId")
    WalletDTO toDto(Wallet wallet);

    @Mapping(source = "userInfoId", target = "userInfo")
    Wallet toEntity(WalletDTO walletDTO);

    WalletDTO toInternalDTO(GenWalletDTO genWalletDTO);

    List<WalletDTO> toInternalDTOs(List<GenWalletDTO> dtoList);
}
