package com.exc.service;

import com.exc.domain.KYC;
import com.exc.domain.UserInfo;
import com.exc.domain.Wallet;
import com.exc.repository.KYCRepository;
import com.exc.repository.UserInfoRepository;
import com.exc.service.dto.UserProfileDTO;
import com.exc.service.mapper.KYCMapper;
import com.exc.service.mapper.UserInfoMapper;
import com.exc.service.mapper.WalletMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Service
@Transactional
public class UserProfileService {
    private final Logger log = LoggerFactory.getLogger(UserProfileService.class);

    private final UserInfoRepository userInfoRepository;

    private final UserInfoMapper userInfoMapper;

    private final KYCRepository kycRepository;

    private final KYCMapper kycMapper;


    private final WalletService walletService;

    private final WalletMapper walletMapper;


    public UserProfileService(UserInfoRepository userInfoRepository, UserInfoMapper userInfoMapper, KYCRepository kycRepository, KYCMapper kycMapper, WalletService walletService, WalletMapper walletMapper) {
        this.userInfoRepository = userInfoRepository;
        this.userInfoMapper = userInfoMapper;
        this.kycRepository = kycRepository;
        this.kycMapper = kycMapper;
        this.walletService = walletService;
        this.walletMapper = walletMapper;
    }

    public UserProfileDTO updateFromUser(Long userId, UserProfileDTO userProfileDTO) {
        log.debug("Request to update KYC : {}", userId);
        Optional<UserInfo> userInfo = userInfoRepository.findOneByUserId(userId);

        KYC newKyc = kycMapper.toEntity(userProfileDTO.getKyc());
        UserInfo newUserInfo = userInfoMapper.toEntity(userProfileDTO.getUserInfo());

        KYC currentKyc = userInfo.get().getKyc();
        UserInfo currentUserInfo = userInfo.get();

        currentKyc.setType(newKyc.getType());
        currentKyc.setFile(newKyc.getFile());

        currentUserInfo.setPostalCode(newUserInfo.getPostalCode());
        currentUserInfo.setCountryCode(newUserInfo.getCountryCode());
        currentUserInfo.setCity(newUserInfo.getCity());
        currentUserInfo.setStateProvince(newUserInfo.getStateProvince());
        currentUserInfo.setStreetAddress(newUserInfo.getStreetAddress());
        currentUserInfo.setCountryCode(newUserInfo.getCountryCode());
        kycRepository.save(currentKyc);
        currentUserInfo = userInfoRepository.save(currentUserInfo);

        UserProfileDTO res = new UserProfileDTO();
        res.setUserInfo(userInfoMapper.toDto(currentUserInfo));
        res.setKyc(kycMapper.toDto(currentKyc));
        return res;

    }

    /**
     * return or create new user profile with wallets
     *
     * @param userId
     * @return
     */
    public UserProfileDTO getProfile(Long userId) {
        log.debug("Request to get user profile : {}", userId);
        Optional<UserInfo> userInfo = userInfoRepository.findOneByUserId(userId);
        if (!userInfo.isPresent()) {

            UserInfo newUserInfo = new UserInfo();
            KYC kyc = new KYC();
            kyc.setIsValid(false);
            kyc = kycRepository.save(kyc);
            newUserInfo.setKyc(kyc);
            userInfo = Optional.ofNullable(userInfoRepository.save(newUserInfo));
            Set<Wallet> wallets = new HashSet<>(walletMapper.toEntity(walletService.createWallets(userInfo.get().getId(), null)));
            newUserInfo.setWallets(wallets);
            userInfo = Optional.ofNullable(userInfoRepository.save(newUserInfo));

        }
        UserProfileDTO res = new UserProfileDTO();
        res.setKyc(kycMapper.toDto(userInfo.get().getKyc()));
        res.setUserInfo(userInfoMapper.toDto(userInfo.get()));

        return res;
    }

}
