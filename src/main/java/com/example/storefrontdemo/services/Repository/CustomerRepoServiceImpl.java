package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.Address;
import com.example.storefrontdemo.domain.entities.Customer;
import com.example.storefrontdemo.services.CustomerService;
import com.example.storefrontdemo.services.security.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Profile("jpadao")
public class CustomerRepoServiceImpl implements CustomerService {

    private EncryptionService encryptionService;
    private CustomerService customerService;
    private CustomerRepoServiceRepository customerRepoServiceRepository;

    @Autowired
    public void setCustomerRepoServiceRepository(CustomerRepoServiceRepository customerRepoServiceRepository) {
        this.customerRepoServiceRepository = customerRepoServiceRepository;
    }

    @Autowired
    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Autowired
    public void setCustomerService(CustomerService customerService) {
        this.customerService = customerService;
    }

    @Override
    public List<Customer> listAll() {
        return this.customerRepoServiceRepository.findAll();
    }

    @Override
    public Customer getById(Integer id) {
         Optional<Customer> optionalCustomer = this.customerRepoServiceRepository.findById(id);
         return optionalCustomer.get();
        }

    @Override
    public Customer findByUsername(String username) {
        return this.customerRepoServiceRepository.findByUsername(username);
    }

    @Override
    public String setUpdatedFields(Customer customer, Customer passedInCustomer) {
        String errorMessage = null;

        Customer getCustomer = customerService.findByUsername(passedInCustomer.getUsername());
        if(getCustomer != null && passedInCustomer.getId() != getCustomer.getId()){
            errorMessage = "Username already exist. Please choose another one.";
        }

        customer.setFirstName(passedInCustomer.getFirstName());
        customer.setLastName(passedInCustomer.getLastName());
        customer.setEmail(passedInCustomer.getEmail());
        customer.setPhoneNumber(passedInCustomer.getPhoneNumber());
        customer.getShippingAddress().setAddressLine1(passedInCustomer.getShippingAddress().getAddressLine1());
        customer.getShippingAddress().setAddressLine2(passedInCustomer.getShippingAddress().getAddressLine2());
        customer.getShippingAddress().setCity(passedInCustomer.getShippingAddress().getCity());
        customer.getShippingAddress().setState(passedInCustomer.getShippingAddress().getState());
        customer.getShippingAddress().setZipCode(passedInCustomer.getShippingAddress().getZipCode());
        customer.setUsername(passedInCustomer.getUsername());
        String passedInPassword = passedInCustomer.getPassword();
        return errorMessage;
    }

    @Override
    public String setNewFields(Customer passedInCustomer) {
        String errorMessage = null;

        if(!passedInCustomer.getPassword().equals(passedInCustomer.getConfirmPassword())){
            errorMessage = "Password must match confirm password.";
        }

        Customer getCustomer = customerService.findByUsername(passedInCustomer.getUsername());
        if(getCustomer != null && passedInCustomer.getId() != getCustomer.getId()){
            errorMessage = "Username already exist. Please choose another one.";
        }

        Customer customer = new Customer();
        customer.setFirstName(passedInCustomer.getFirstName());
        customer.setLastName(passedInCustomer.getLastName());
        customer.setEmail(passedInCustomer.getEmail());
        customer.setPhoneNumber(passedInCustomer.getPhoneNumber());
        customer.setShippingAddress(new Address());
        customer.getShippingAddress().setAddressLine1(passedInCustomer.getShippingAddress().getAddressLine1());
        customer.getShippingAddress().setAddressLine2(passedInCustomer.getShippingAddress().getAddressLine2());
        customer.getShippingAddress().setCity(passedInCustomer.getShippingAddress().getCity());
        customer.getShippingAddress().setState(passedInCustomer.getShippingAddress().getState());
        customer.getShippingAddress().setZipCode(passedInCustomer.getShippingAddress().getZipCode());
        customer.setUsername(passedInCustomer.getUsername());
        String passedInPassword = passedInCustomer.getPassword();
        if(passedInPassword != null || passedInPassword != ""){
            customer.setPassword(passedInCustomer.getPassword());
        }
        return errorMessage;
    }

    @Override
    public Customer saveOrUpdate(Customer domainObject) {

        if(domainObject.getPassword() != null && !domainObject.getPassword().isEmpty()){
            domainObject.setEncryptedPassword(encryptionService.encryptString(domainObject.getPassword()));
        }
        return this.customerRepoServiceRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        customerRepoServiceRepository.deleteById(id);

    }

    @Override
    public Customer loginCustomer(String username, String password) {

        Customer customer = this.customerRepoServiceRepository.findByUsername(username);
        if (customer != null) {
            boolean correct = encryptionService.checkPassword(password, customer.getEncryptedPassword());
            if(correct){
                return customer;
            }
            return null;
        }
        return null;
    }
}