package com.airplane.service.dao;

import com.airplane.service.models.UserPersonalDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserPersonalDetailsDAO extends JpaRepository<UserPersonalDetail, Long> {
    Optional<UserPersonalDetail> findByUserID(Long userId);
}
