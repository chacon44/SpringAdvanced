package com.epam.esm.repository;

import com.epam.esm.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
  Page<Order> findAllByUserId(Long userId, Pageable pageable);
}
