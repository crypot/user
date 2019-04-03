package com.exc.repository;

import com.exc.domain.KYC;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the KYC entity.
 */
@SuppressWarnings("unused")
@Repository
public interface KYCRepository extends JpaRepository<KYC, Long> {

}
