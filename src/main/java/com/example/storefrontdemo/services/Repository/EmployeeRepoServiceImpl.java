package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.Address;
import com.example.storefrontdemo.domain.entities.Employee;
import com.example.storefrontdemo.services.EmployeeService;
import com.example.storefrontdemo.services.security.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("jpadao")
public class EmployeeRepoServiceImpl implements EmployeeService {

    private EncryptionService encryptionService;
    private EmployeeService employeeService;
    private EmployeeRepoServiceRepository employeeRepoServiceRepository;

    @Autowired
    public void setEmployeeRepoServiceRepository(EmployeeRepoServiceRepository employeeRepoServiceRepository) {
        this.employeeRepoServiceRepository = employeeRepoServiceRepository;
    }

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public List<Employee> listAll() {
        return this.employeeRepoServiceRepository.findAll();
    }

    @Override
    public Employee getById(Integer id) {
        Optional<Employee> optionalEmployee = this.employeeRepoServiceRepository.findById(id);
        return optionalEmployee.get();
    }

    @Override
    public Employee findByUsername(String username) {
        return this.employeeRepoServiceRepository.findByUsername(username);
    }

    @Override
    public String setUpdatedFields(Employee employee, Employee passedInEmployee) {
        String errorMessage = null;

        if (!passedInEmployee.getPassword().equals(passedInEmployee.getConfirmPassword())) {
            errorMessage = "Password must match confirm password.";
        }

        Employee getEmployee = employeeService.findByUsername(passedInEmployee.getUsername());
        if (getEmployee != null && passedInEmployee.getId() != getEmployee.getId()) {
            errorMessage = "Username already exist. Please choose another one.";
        }

        employee.setFirstName(passedInEmployee.getFirstName());
        employee.setLastName(passedInEmployee.getLastName());
        employee.setEmail(passedInEmployee.getEmail());
        employee.setPhoneNumber(passedInEmployee.getPhoneNumber());
        employee.setAddress(new Address());
        employee.getAddress().setAddressLine1(passedInEmployee.getAddress().getAddressLine1());
        employee.getAddress().setAddressLine2(passedInEmployee.getAddress().getAddressLine2());
        employee.getAddress().setCity(passedInEmployee.getAddress().getCity());
        employee.getAddress().setState(passedInEmployee.getAddress().getState());
        employee.getAddress().setZipCode(passedInEmployee.getAddress().getZipCode());
        employee.setUsername(passedInEmployee.getUsername());
        employee.setEnabled(passedInEmployee.getEnabled());
        String passedInPassword = passedInEmployee.getPassword();
        if (passedInPassword != "" && errorMessage == null) {
            employee.setPassword(passedInEmployee.getPassword());
        }
        return errorMessage;
    }

    @Override
    public String setUpdatedFields(Employee passedInEmployee) {
        String errorMessage = null;

        if (!passedInEmployee.getPassword().equals(passedInEmployee.getConfirmPassword())) {
            errorMessage = "Password must match confirm password.";
        }

        Employee getEmployee = employeeService.findByUsername(passedInEmployee.getUsername());
        if (getEmployee != null && passedInEmployee.getId() != getEmployee.getId()) {
            errorMessage = "Username already exist. Please choose another one.";
        }

        Employee employee = new Employee();
        employee.setFirstName(passedInEmployee.getFirstName());
        employee.setLastName(passedInEmployee.getLastName());
        employee.setEmail(passedInEmployee.getEmail());
        employee.setPhoneNumber(passedInEmployee.getPhoneNumber());
        employee.setAddress(new Address());
        employee.getAddress().setAddressLine1(passedInEmployee.getAddress().getAddressLine1());
        employee.getAddress().setAddressLine2(passedInEmployee.getAddress().getAddressLine2());
        employee.getAddress().setCity(passedInEmployee.getAddress().getCity());
        employee.getAddress().setState(passedInEmployee.getAddress().getState());
        employee.getAddress().setZipCode(passedInEmployee.getAddress().getZipCode());
        employee.setUsername(passedInEmployee.getUsername());
        employee.setEnabled(passedInEmployee.getEnabled());
        String passedInPassword = passedInEmployee.getPassword();
        if (passedInPassword != "" && errorMessage == null) {
            employee.setPassword(passedInEmployee.getPassword());
        }
        return errorMessage;
    }

    @Override
    public Employee saveOrUpdate(Employee domainObject) {

        if (domainObject.getPassword() != null) {
            domainObject.setEncryptedPassword(encryptionService.encryptString(domainObject.getPassword()));
        }
        return this.employeeRepoServiceRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        employeeRepoServiceRepository.deleteById(id);
    }

    public Employee loginEmployee(String username, String password) {
        Employee employee = this.employeeRepoServiceRepository.findByUsername(username);

        if (employee != null) {
            boolean correct = encryptionService.checkPassword(password, employee.getEncryptedPassword());
            if (correct) {
                return employee;
            }
            return null;
        }
        return null;
    }
}