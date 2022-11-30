package com.bridgelabz.service;

import java.util.List;
import java.util.Optional;

import com.bridgelabz.dto.LoginDto;
import com.bridgelabz.dto.RegisterDto;
import com.bridgelabz.exception.UserException;
import com.bridgelabz.model.Model;


public interface IEmpService {

	RegisterDto register(RegisterDto employeeDto);

	//List<RegisterDto> getAllEmployee();

	RegisterDto getEmployeeByName(String name);


	Optional<Model> deleteEmployeeName(String employeeName);

	String logoutByToken(String token);

	RegisterDto update(RegisterDto registerDTO, String token);

	String loginByToken(LoginDto loginDTO);

	List<RegisterDto> getAllEmployee();

	RegisterDto getEmployee(int id);

	RegisterDto deleteEmployee(int id) throws UserException;


}
