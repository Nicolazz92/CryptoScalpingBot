package com.velikokhatko.repositories;

import com.velikokhatko.model.CoinInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CoinInfoRepository extends JpaRepository<CoinInfo, Long> {
}
