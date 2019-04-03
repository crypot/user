package com.exc.service;

import com.exc.domain.CurrencyName;
import com.exc.service.dto.WalletDTO;
import com.exc.service.remote.TxService;
import org.hibernate.Hibernate;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigInteger;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;


public class WalletServiceTest extends AbstractServiceTest {

    @Autowired
    WalletService walletService;
    @MockBean
    TxService txService;

    @Before
    public void setup() {
        doNothing().when(txService).genWallets(any(), anyList());
    }


    @Test
    public void shouldCreateAllWallets() {
        List<WalletDTO> walletDTOS = walletService.createWallets(userInfo.getId(), null);
        assertThat(walletDTOS.get(0).getId()).isNotNull();
        assertThat(walletDTOS.get(0).getUserInfoId()).isNotNull();
        assertThat(walletDTOS.get(0).getCurrencyId()).isNotNull();
    }

    @Test
    public void shouldCreateMissedWallet() {

        List<WalletDTO> walletDTOS = walletService.createWallets(userInfo.getId(), null);
        assertThat(walletDTOS.size()).isEqualTo(1);
        assertThat(walletDTOS.get(0).getLockBalance()).isNotNull();
    }

    @Test
    public void shouldCreateMissedWallet2() {

        List<WalletDTO> walletDTOS = walletService.createWallets(userInfo.getId(), CurrencyName.BTC);
        assertThat(walletDTOS.size()).isEqualTo(1);
    }

    @Test
    public void shouldCreateMissedWallet3() {
        List<WalletDTO> walletDTOS = walletService.createWallets(userInfo.getId(), CurrencyName.BTC);
        assertThat(walletDTOS.size()).isEqualTo(1);
    }


}
