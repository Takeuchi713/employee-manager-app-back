package teck.getarrays.employeemanager.domain.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import teck.getarrays.employeemanager.domain.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long>{

	void deleteEmployeeById(Long id);

	Optional<Employee> findEmployeeById(Long id);

//	nullになるんだ。
//	Employee findEmployeeById(Long id);


}
