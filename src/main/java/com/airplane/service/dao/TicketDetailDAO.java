package com.airplane.service.dao;

import com.airplane.service.models.TicketDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TicketDetailDAO extends JpaRepository<TicketDetail, Long> {
    Page<TicketDetail> findAllByUserID(Long userId,Pageable pageable);
}
