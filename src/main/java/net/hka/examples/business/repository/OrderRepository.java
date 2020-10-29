package net.hka.examples.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.hka.examples.business.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}