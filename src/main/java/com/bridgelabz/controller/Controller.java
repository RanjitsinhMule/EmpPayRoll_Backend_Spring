package com.bridgelabz.controller;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bridgelabz.ResponseEntity;
import com.bridgelabz.dto.LoginDto;
import com.bridgelabz.dto.RegisterDto;
import com.bridgelabz.exception.UserException;
import com.bridgelabz.model.Model;
import com.bridgelabz.service.IEmpService;

@RestController
@CrossOrigin("http://localhost:3000")
public class Controller {

	@Autowired
	IEmpService userservice;

	@PostMapping("/registerEmployee")
	public ResponseEntity registerUser(@RequestBody RegisterDto employeeDto) {
		RegisterDto register = userservice.register(employeeDto);
		return new ResponseEntity(register, "Registered successfully");
	}

	@GetMapping("/listAll")
	public List<RegisterDto> getAllEmployee(){
		return this.userservice.getAllEmployee();
	}

	@GetMapping("/searchEmployee")
	public ResponseEntity getUserByName(@RequestParam String name) {
		RegisterDto employee = userservice.getEmployeeByName(name);
		System.out.println("User fetched successfully");
		return new ResponseEntity(employee, "Employee details is fetched successfully");
	}

	@DeleteMapping("/deleteEmployee")
	@Transactional
	public ResponseEntity deleteEmployee(@RequestParam String employeeName) {
		Optional<Model> model = userservice.deleteEmployeeName(employeeName);
		return new ResponseEntity(model, "Deleted successfully");
	}
	
	@GetMapping("/logout")
	public ResponseEntity logoutByToken(@RequestHeader String token) {
		String logout = userservice.logoutByToken(token);
		System.out.println("Logout SuccessFully!!!");
		return new ResponseEntity(logout, "Logout successfully");
	}
	
	@PutMapping("/updateEmployeeByToken")
	public ResponseEntity updateUserByToken(@RequestBody RegisterDto registerDTO, @RequestHeader String token) {
		RegisterDto register = userservice.update(registerDTO, token);
		return new ResponseEntity(register, "User updated successfully");
	}
	
	@GetMapping("/login")
	public ResponseEntity loginByToken(@RequestBody LoginDto loginDTO) {
		String login = userservice.loginByToken(loginDTO);
		System.out.println("Login SuccessFully!!!");
		return new ResponseEntity(login, "Login successfully");
	}
	
	@GetMapping("/getsingleemployee/{id}")
	public ResponseEntity getEmployee(@PathVariable int id) {
		RegisterDto empDto = userservice.getEmployee(id);
		return new ResponseEntity(empDto,"Details of single Employee");
	}
	
	@DeleteMapping("/deleteemployee/{id}")
	public ResponseEntity deleteEmployee(@PathVariable int id) throws UserException {
		RegisterDto deletedEmployee = userservice.deleteEmployee(id);
		return new ResponseEntity(deletedEmployee,"one employee deleted");
	}
}