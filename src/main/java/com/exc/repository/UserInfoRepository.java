package com.exc.repository;

import com.exc.domain.UserInfo;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the UserInfo entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserInfoRepository extends JpaRepository<UserInfo, Long> {
    /**
     * will find userInfo by user Id.
     * @param login
     * @return
     */
    Optional<UserInfo> findOneByUserId(Long id);
}
