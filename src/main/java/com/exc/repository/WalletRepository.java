package com.exc.repository;

import com.exc.domain.CurrencyName;
import com.exc.domain.UserInfo;
import com.exc.domain.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


/**
 * Spring Data  repository for the Wallet entity.
 */
@SuppressWarnings("unused")
@Repository
public interface WalletRepository extends JpaRepository<Wallet, Long> {
    Page<Wallet> findByUserInfo(UserInfo userInfo, Pageable pageable);

    Wallet findByUserInfoAndCurrencyName(UserInfo userInfo, CurrencyName currencyName);

    List<Wallet> findByUserInfo_Id(Long userInfoId);

    Optional<Wallet> findByUserInfo_IdAndCurrencyName(Long userInfoId, CurrencyName currencyName);


}
