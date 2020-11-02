package net.hka.examples.restapi.business.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import net.hka.examples.restapi.business.domain.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
	@Query("select employee from Employee employee left join fetch employee.documents")
	List<Employee> findAllWithDocument();
}
