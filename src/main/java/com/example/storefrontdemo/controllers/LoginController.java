package com.example.storefrontdemo.controllers;

import com.example.storefrontdemo.domain.entities.Cart;
import com.example.storefrontdemo.domain.entities.Customer;
import com.example.storefrontdemo.domain.entities.Employee;
import com.example.storefrontdemo.domain.forms.ChangePasswordForm;
import com.example.storefrontdemo.domain.forms.LoginForm;
import com.example.storefrontdemo.services.CartService;
import com.example.storefrontdemo.services.CustomerService;
import com.example.storefrontdemo.services.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.http.HttpSession;

@Controller
//@SessionAttributes("loggedInUser")
public class LoginController {

    private EmployeeService employeeService;
    private CustomerService customerService;
    private CartService cartService;

    @Autowired
    public void setEmployeeService(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Autowired
    public void setCartService(CartService cartService) {
        this.cartService = cartService;
    }

//    ********************************************************************************** //
//    Employee login section
//    ********************************************************************************** //
    @GetMapping("/login")
    public String showLoginForm(
            Model model
    ) {
        model.addAttribute("genericHeader", "Employee Login");
        return "login";
    }

    @PostMapping("/login")
    public String verifyLogin(
            @ModelAttribute(name = "loginForm") LoginForm loginForm,
            HttpSession session,
            Model model
    ) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        Employee employee = employeeService.loginEmployee(username, password);
        if (employee != null && employee.getEnabled() == true) {
            session.setAttribute("loggedInUsername", employee.getUsername());
//  force password change
            if (username.equals(password)) {
                model.addAttribute("genericHeader", "Employee Change Password");
                return "redirect:/change_password";
            }
        }
        if (employee != null && employee.getEnabled() == true) {
            session.setAttribute("loggedInUser", employee.getFirstName() + " " + employee.getLastName());
            session.setAttribute("loggedInUserRole", employee.getRoleType());
            session.setAttribute("loggedInUsername", employee.getUsername());

            String userRole = new String(employee.getRoleType().toString());
            if (userRole.equals("ADMIN")) {
                return "redirect:/employee/list";
            }

            if (userRole.equals("MANAGER")) {
                return "redirect:/product/list";
            }

            if (userRole.equals("USER")) {
                return "redirect:/order/list";
            }
        }
        if(employee != null && employee.getEnabled() == false){
            model.addAttribute("loginDisabled" ,true);
        } else {
            model.addAttribute("invalidCredentials", true);
        }
        model.addAttribute("genericHeader", "Employee Login");
        return "login";
    }

     @GetMapping("/logout")
    public String logout(
            HttpSession session
    ) {
        session.removeAttribute("loggedInUser");
        session.removeAttribute("loggedInUserRole");
        session.removeAttribute("F");
        session.invalidate();
        return "redirect:/";
    }

    @GetMapping("/change_password")
    public String showChangePassword(
            Model model
    ) {
        model.addAttribute("genericHeader", "Employee Change Password");
        return "/change_password";
    }

    @PostMapping("/change_password")
    public String changePassword(
            @ModelAttribute(name = "changePasswordForm") ChangePasswordForm changePasswordForm,
            HttpSession session,
            Model model
    ) {
        String currentPassword = changePasswordForm.getCurrentPassword();
        String newPassword = changePasswordForm.getNewPassword();
        String confirmPassword = changePasswordForm.getConfirmPassword();

        String username = session.getAttribute("loggedInUsername").toString();
        Employee employee2 = employeeService.loginEmployee(username, currentPassword);
        if (employee2 == null) {
            model.addAttribute("currentPasswordError", true);
            model.addAttribute("genericHeader", "Employee Change Password");
            return "/change_password";
        }
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("newPasswordError", true);
            model.addAttribute("genericHeader", "Employee Change Password");
            return "/change_password";
        }
        employee2.setPassword(newPassword);
        employeeService.saveOrUpdate(employee2);
        model.addAttribute("genericHeader", "Employee Login");
        return "login";
    }

//    ********************************************************************************** //
//    Customer login section
//    ********************************************************************************** //
    @GetMapping("/login/customer")
    public String showLoginForm2() {
        return "login2";
    }

    @PostMapping("/login/customer")
    public String verifyLogin2(
            @ModelAttribute(name = "loginForm") LoginForm loginForm,
            HttpSession session,
            Model model
    ) {
        String username = loginForm.getUsername();
        String password = loginForm.getPassword();

        Customer customer = customerService.loginCustomer(username, password);
        if (customer != null) {
            Cart cart = cartService.findByCustomerId(customer.getId());
            session.setAttribute("loggedInCustomer", customer.getFirstName() + " " + customer.getLastName());
            session.setAttribute("loggedInCustomerUsername", customer.getUsername());
            session.setAttribute("loggedInCustomerId", customer.getId());
            session.setAttribute("loggedInCustomerCartId", cart.getId());

            Integer cartQty = cart.getQuantity();
            model.addAttribute("customerCartQty", "cartQty()");
            return "redirect:/storefront/listproducts/" + customer.getId();
        }
        model.addAttribute("invalidCredentials", true);
        return "login2";
    }

    @GetMapping("/logout/customer")
    public String logout2(
            HttpSession session
    ) {
        session.removeAttribute("loggedInCustomer");
        session.removeAttribute("loggedInCustomerUsername");
        session.removeAttribute("loggedInCustomerId");
        session.removeAttribute("loggedInCustomerCartId");
        return "redirect:/storefront/listproducts";
    }

    @GetMapping("/change_password/customer")
    public String showChangePassword2(
            HttpSession session,
            Model model
    ) {
        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        Cart cart =  cartService.findByCustomerId(customerId);
        Integer cartQty = cart.getQuantity();
        model.addAttribute("customerCartQty", cartQty);
        return "/change_password2";
    }

    @PostMapping("/change_password/customer")
    public String changePassword2(
            @ModelAttribute(name = "changePasswordForm") ChangePasswordForm changePasswordForm,
            HttpSession session,
            Model model
    ) {
        String currentPassword = changePasswordForm.getCurrentPassword();
        String newPassword = changePasswordForm.getNewPassword();
        String confirmPassword = changePasswordForm.getConfirmPassword();

        String username = session.getAttribute("loggedInCustomerUsername").toString();
        Customer customer = customerService.loginCustomer(username, currentPassword);
        if (customer == null) {
            model.addAttribute("currentPasswordError", true);
            return "/change_password2";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("newPasswordError", true);
            return "/change_password2";
        }
        customer.setPassword(newPassword);
        customerService.saveOrUpdate(customer);

        Integer customerId = (Integer) session.getAttribute("loggedInCustomerId");
        Cart cart =  cartService.findByCustomerId(customerId);
        Integer cartQty = cart.getQuantity();
        model.addAttribute("customerCartQty", cartQty);
        return "redirect:/storefront/listproducts/" + (customerId) ;
    }
}