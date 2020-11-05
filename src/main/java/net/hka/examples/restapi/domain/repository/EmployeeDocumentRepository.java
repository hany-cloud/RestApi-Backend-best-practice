package net.hka.examples.restapi.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.hka.examples.restapi.domain.EmployeeDocument;

public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Long> {

}
