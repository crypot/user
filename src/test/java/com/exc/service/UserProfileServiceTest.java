package com.exc.service;

import com.exc.domain.Wallet;
import com.exc.repository.WalletRepository;
import com.exc.service.dto.UserProfileDTO;
import com.exc.service.mapper.KYCMapper;
import com.exc.service.mapper.UserInfoMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class UserProfileServiceTest extends AbstractServiceTest {
    @Autowired
    WalletRepository walletRepository;
    @Autowired
    UserProfileService userProfileService;
    @Autowired
    UserInfoMapper userInfoMapper;
    @Autowired
    KYCMapper kycMapper;


    @Test
    public void shouldCreate() {
        List<Wallet> wallets = walletRepository.findByUserInfo_Id(userInfo.getId());
        //user profile should be created in abstract class
        assertThat(wallets.size()).isNotZero();
    }

    @Test
    public void shouldUpdate() {
        userInfo.setStreetAddress("rubna 4");
        userInfo.getKyc().setIsValid(true);
        UserProfileDTO userProfileDTO = new UserProfileDTO();
        userProfileDTO.setKyc(kycMapper.toDto(userInfo.getKyc()));
        userProfileDTO.setUserInfo(userInfoMapper.toDto(userInfo));

        UserProfileDTO res = userProfileService.updateFromUser(1l, userProfileDTO);
        userInfo = userInfoRepository.findOneByUserId(1l).get();
        assertThat(res.getUserInfo().getStreetAddress()).isEqualToIgnoringCase("rubna 4");
        //user can't active itself
        assertThat(res.getKyc().isIsValid()).isFalse();

        assertThat(userInfo.getStreetAddress()).isEqualToIgnoringCase("rubna 4");
        assertThat(userInfo.getKyc().isIsValid()).isFalse();
    }
}
