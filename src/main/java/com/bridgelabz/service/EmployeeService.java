package com.bridgelabz.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bridgelabz.dto.LoginDto;
import com.bridgelabz.dto.RegisterDto;
import com.bridgelabz.exception.UserException;
import com.bridgelabz.model.Model;
import com.bridgelabz.repository.IEmpRepository;
import com.bridgelabz.utility.JwtTokenUtil;



@Service
public class EmployeeService implements IEmpService {
	@Autowired
	IEmpRepository userRepo;

	@Autowired
	ModelMapper model;
	
	@Autowired
	JwtTokenUtil tokenUtil;
	

	@Override
	public RegisterDto register(RegisterDto employeeDto) {
		Optional<Model> empModel = userRepo.findByemployeeName(employeeDto.getEmployeeName());
		if (empModel.isPresent()) {
			throw new UserException("Username already exists!!");
		}
		Model registeredEmployee = model.map(employeeDto, Model.class);
		userRepo.save(registeredEmployee);

		System.out.println("Successfully registered");
		return employeeDto;
	}

//	@Override
//	public List<RegisterDto> getAllEmployee() {
//		return userRepo.findAll().stream().map(employee -> model.map(employee, RegisterDto.class))
//				.collect(Collectors.toList());
//	}

	@Override
	public RegisterDto getEmployeeByName(String name) {
		Optional<Model> findByName = userRepo.findByEmployeeName(name);
		if (findByName.isEmpty()) {
			throw new UserException("User does not exist");
		}
		RegisterDto employeeDto = model.map(findByName.get(), RegisterDto.class);
		return employeeDto;

	}

	@Override
	public Optional<Model> deleteEmployeeName(String employeeName) {
		Optional<Model> empModel = userRepo.findByEmployeeName(employeeName);
		if (empModel.isEmpty()) {
			throw new UserException("Employee doesn't exist!!!");
		}
		userRepo.deleteByEmployeeName(employeeName);
		return empModel;

	}
	
	@Override
	public String logoutByToken(String token) {
		LoginDto loginDTO = tokenUtil.decode(token);
		Optional<Model> checkUserDetails = userRepo.findByEmailAndPassword(loginDTO.getEmail(), loginDTO.getPassword());
		LoginDto logout = model.map(checkUserDetails, LoginDto.class);
		checkUserDetails.get().setLogin(false);
		userRepo.save(checkUserDetails.get());
		return "logout successful";
	}

	@Override
	public String loginByToken(LoginDto loginDto) {
		Optional<Model> user = userRepo.findByEmailAndPassword(loginDto.getEmail(), loginDto.getPassword());
		if (user.isEmpty()) {
			Optional<Model> userEmail = userRepo.findByEmail(loginDto.getEmail());
			Optional<Model> userPassword = userRepo.findByPassword(loginDto.getPassword());
			if (userEmail.isEmpty()) {
				throw new UserException("Email is incorrect");
			} else if (userPassword.isEmpty()) {
				 throw new UserException("Password is incorrect");
			}
		}
		String token = tokenUtil.generateToken(loginDto);

		user.get().setLogin(true);
		userRepo.save(user.get());
		System.out.println("Check the user is login or not " + user.get().isLogin());

		return token;
	}
	
	@Override
	public List<RegisterDto> getAllEmployee() {
	//	if(role.equals("Admin")) {
			return userRepo.findAll().stream().map(employee -> model.map(employee, RegisterDto.class))
					.collect(Collectors.toList());
	//	}
	//	else {
		//	throw new UserException("You are not Admin");
		//}
	}

	@Override
	public RegisterDto update(RegisterDto registerDto, String token) {
		LoginDto loginUser = tokenUtil.decode(token);
		Model empModel = model.map(registerDto, Model.class);

		if (userRepo.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword()).isPresent()
				&& userRepo.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword()).get().isLogin()) {
			empModel.setEmployeeId(userRepo.findByEmailAndPassword(loginUser.getEmail(), loginUser.getPassword()).get().getEmployeeId());
			empModel.setLogin(true);
			userRepo.save(empModel);
			System.out.println("Updated Successfully");

			return registerDto;
		} else {
			throw new UserException("Please Login!");
		}
	}
	
	@Override
	public RegisterDto getEmployee(int id) {
		Optional<Model> emp = userRepo.findById(id);
		RegisterDto empDto = model.map(emp.get(),RegisterDto.class);
		System.out.println("get employee : "+id+" "+empDto.getEmployeeName());
		return empDto;
	}
	
	@Override
	public RegisterDto deleteEmployee(int id) throws UserException {

		if(userRepo.findById(id).isPresent()) {

			Optional<Model> emp = userRepo.findById(id);
			RegisterDto empDto = model.map(emp.get(), RegisterDto.class);
			userRepo.deleteById(id);
			return empDto;

		} else {
			throw new UserException("Employee not found");
		}
	}
}
