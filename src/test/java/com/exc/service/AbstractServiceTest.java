package com.exc.service;

import com.exc.UserApp;
import com.exc.domain.UserInfo;
import com.exc.domain.Wallet;
import com.exc.repository.UserInfoRepository;
import com.exc.repository.WalletRepository;
import io.github.jhipster.config.JHipsterConstants;
import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApp.class)
@EnableTransactionManagement(proxyTargetClass = true, mode = AdviceMode.PROXY)
@ActiveProfiles({JHipsterConstants.SPRING_PROFILE_TEST})
public abstract class AbstractServiceTest {
   /* @MockBean
    protected NodeAdapterFactory nodeAdapterFactory;
    @Autowired
    protected UserRepository userRepository;*/
    @Autowired
    protected UserProfileService userProfileService;
    @Autowired
    protected UserInfoRepository userInfoRepository;
    @Autowired
    protected WalletRepository walletRepository;


    protected UserInfo userInfo;
    protected UserInfo userInfo2;
    protected Wallet wallet1;


    @Before
    public void setup() {
        userProfileService.getProfile(1l);
        userProfileService.getProfile(2l);
        userInfo = userInfoRepository.findOneByUserId(1l).get();
        userInfo2 = userInfoRepository.findOneByUserId(2l).get();
        Hibernate.initialize(userInfo);

        wallet1 = walletRepository.findByUserInfo_Id(userInfo.getId()).get(0);
    }

}
