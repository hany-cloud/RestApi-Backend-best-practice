package net.hka.examples.restapi.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.hka.examples.restapi.business.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}