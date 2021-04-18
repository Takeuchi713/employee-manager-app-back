package teck.getarrays.employeemanager.domain.service;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import teck.getarrays.employeemanager.common.exception.UserNotFoundException;
import teck.getarrays.employeemanager.domain.model.Employee;
import teck.getarrays.employeemanager.domain.repository.EmployeeRepository;

@Service
@Transactional
public class EmployeeService {
	private final EmployeeRepository employeeRepository;

	@Autowired
	public EmployeeService(EmployeeRepository employRepository) {
		this.employeeRepository = employRepository;
	}

	public Employee addEmployee(Employee employee) {
		employee.setEmployeeCode(UUID.randomUUID().toString());
		return employeeRepository.save(employee);
	}

	public List<Employee> findAllEmployees() {
		return employeeRepository.findAll();
	}

	public Employee updateEmployee(Employee employee) {
		return employeeRepository.save(employee);
	}

	public Employee findEmployeeById(Long id) {
//			Employee employee = employeeRepository.findEmployeeById(id);
//			System.out.println(employee);
//			return employee;
		//上だとnullになるかよくわからないから、基本的にはoptionalを使う。
		return employeeRepository.findEmployeeById(id)
				.orElseThrow(() -> new UserNotFoundException("User by id " + id + "was not found"));
	}

	public void deleteEmployee(Long id) {
		employeeRepository.deleteEmployeeById(id);
	}

}
