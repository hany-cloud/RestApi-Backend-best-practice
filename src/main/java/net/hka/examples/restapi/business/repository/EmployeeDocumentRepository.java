package net.hka.examples.restapi.business.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import net.hka.examples.restapi.business.domain.EmployeeDocument;

public interface EmployeeDocumentRepository extends JpaRepository<EmployeeDocument, Long> {

}
