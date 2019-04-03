package com.exc.service.mapper;

import com.exc.domain.UserInfo;
import com.exc.service.dto.UserInfoDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Mapper for the entity UserInfo and its DTO UserInfoDTO.
 */
@Mapper(componentModel = "spring", uses = {KYCMapper.class})
public interface UserInfoMapper extends EntityMapper<UserInfoDTO, UserInfo> {

  /*  @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")*/
    @Mapping(source = "kyc.id", target = "kycId")
    @Mapping(source = "kyc.type", target = "kycType")
    @Mapping(target = "secret",ignore = true)
    UserInfoDTO toDto(UserInfo userInfo);

    @Mapping(source = "kycId", target = "kyc")
    @Mapping(target = "wallets", ignore = true)
    //@Mapping(target = "orders", ignore = true)
    //@Mapping(target = "operations", ignore = true)
    UserInfo toEntity(UserInfoDTO userInfoDTO);

    default UserInfo fromId(Long id) {
        if (id == null) {
            return null;
        }
        UserInfo userInfo = new UserInfo();
        userInfo.setId(id);
        return userInfo;
    }
}
