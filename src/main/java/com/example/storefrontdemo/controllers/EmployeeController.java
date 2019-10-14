package com.example.storefrontdemo.controllers;

import com.example.storefrontdemo.domain.entities.Employee;
import com.example.storefrontdemo.domain.enums.RoleType;
import com.example.storefrontdemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/employee")
@Controller
@SessionAttributes({"loggedInUser", "loggedInUserRole"})
public class EmployeeController {

    private EmployeeService employeeService;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/list")
    public String listEmployees(
            Model model
    ){
        model.addAttribute("employees", employeeService.listAll());
        model.addAttribute("genericHeader", "Employee List");
        return "employee/list";
    }

    @GetMapping("/show/{id}")
    public String showEmployee(
            @PathVariable Integer id,
            Model model
    ){
        model.addAttribute("employee", employeeService.getById(id));
        model.addAttribute("genericHeader", "Employee Detail");
        return "employee/show";
    }

    @GetMapping("/edit/{id}")
    public String edit(
            @PathVariable Integer id,
            Model model
    ){
        model.addAttribute("employee", employeeService.getById(id));
//  pass enum roles to thymeleaf
        model.addAttribute("roleTypes", RoleType.values());
        model.addAttribute("genericHeader", "Employee Update");
        return "employee/employeeform";
    }

    @GetMapping("/new")
    public String newEmployee(
            Model model
    ){
        model.addAttribute("employee", new Employee());
//  pass enum roles to thymeleaf
        model.addAttribute("roleTypes", RoleType.values());
        model.addAttribute("genericHeader", "Employee Add");
        return "employee/employeeform";
    }

    @PostMapping("/edit")
    public String saveOrUpdate(
            Employee passedInEmployee,
            Model model
    ){
        Integer employeeId;
        String errorMessage;

        if(passedInEmployee.getId() != null){
            Employee employee = employeeService.getById(passedInEmployee.getId());
            errorMessage = employeeService.setUpdatedFields(employee, passedInEmployee);
        } else {
            errorMessage = employeeService.setUpdatedFields(passedInEmployee);
        }

        if(errorMessage != null) {
            model.addAttribute("employeeError", errorMessage);
            //  pass enum roles to thymeleaf
            model.addAttribute("roleTypes", RoleType.values());
            passedInEmployee.setRoleType(passedInEmployee.getRoleType());
            return "employee/employeeForm";
        }

        if(passedInEmployee.getId() != null){
            Employee employee = employeeService.getById(passedInEmployee.getId());
            employeeService.saveOrUpdate(employee);
            employeeId = passedInEmployee.getId();
        } else {
            Employee newEmployee = employeeService.saveOrUpdate(passedInEmployee);
            employeeId = newEmployee.getId();
     }
        model.addAttribute("genericHeader", "Employee Detail");
        return "redirect:/employee/show/" + employeeId;
    }

    @PostMapping("/delete/{id}")
    public String delete(
            @PathVariable Integer id,
            Model model
    ){
        employeeService.delete(id);
        model.addAttribute("genericHeader", "Employee List");
        return "redirect:/employee/list";
    }
}

