package com.azubike.ellipsis.ordermcs.repository;

import com.azubike.ellipsis.ordermcs.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<PurchaseOrder, Integer> {
  List<PurchaseOrder> findByUserId(int userId);
}
