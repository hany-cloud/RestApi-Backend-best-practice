package net.hka.examples.restapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.hka.examples.restapi.domain.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}