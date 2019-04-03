package com.exc.service.dto;

public class UserProfileDTO {
    private UserInfoDTO userInfo;
    private KYCDTO kyc;

    public UserInfoDTO getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfoDTO userInfo) {
        this.userInfo = userInfo;
    }

    public KYCDTO getKyc() {
        return kyc;
    }

    public void setKyc(KYCDTO kyc) {
        this.kyc = kyc;
    }
}
