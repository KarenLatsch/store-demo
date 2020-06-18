package com.example.storefrontdemo.services.Repository;

import com.example.storefrontdemo.domain.entities.Address;
import com.example.storefrontdemo.domain.entities.CreditCard;
import com.example.storefrontdemo.domain.forms.CreditCardForm;
import com.example.storefrontdemo.services.CreditCardService;
import com.example.storefrontdemo.services.security.EncryptionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Optional;

@Service
@Profile("jpadao")
public class CreditCardRepoServiceImpl implements CreditCardService {

    private EncryptionService encryptionService;
    private CreditCardRepoServiceRepository creditCardRepoServiceRepository;

    @Autowired
    public void setCreditCardRepoServiceRepository(CreditCardRepoServiceRepository creditCardRepoServiceRepository) {
        this.creditCardRepoServiceRepository = creditCardRepoServiceRepository;
    }

    @Autowired
    public void setEncryptionService(EncryptionService encryptionService) {
        this.encryptionService = encryptionService;
    }

    @Override
    public List<CreditCard> listAll() {
        return this.creditCardRepoServiceRepository.findAll();
    }

    @Override
    public List<CreditCard> findByCustomerId(Integer customerId) {
        return this.creditCardRepoServiceRepository.findByCustomerId(customerId);
    }

    @Override
    public CreditCard getById(Integer id) {
         Optional<CreditCard> optionalCreditCard = this.creditCardRepoServiceRepository.findById(id);
         return optionalCreditCard.get();
        }

    @Override
    public CreditCard findByCustomerIdAndCreditCardId(Integer customerId, Integer creditCardId) {
        return this.creditCardRepoServiceRepository.findByCustomerIdAndId(customerId, creditCardId);
    }

    @Override
    public String validateCreditCard(CreditCardForm creditCardForm) {
        String errorMessage = null;
        Calendar calendar = new GregorianCalendar();
        String year =  Integer.toString(calendar.get(Calendar.YEAR));
        String month =  Integer.toString(calendar.get(Calendar.MONTH)+1);
        String stringCurrentDate = year + month;
        int intCurrentDate = Integer.parseInt(stringCurrentDate);

        String expYear = creditCardForm.getDisplayExpYear();
        String expMonth = creditCardForm.getDisplayExpMonth();
        String stringExpireDate = expYear + expMonth;

        if(expYear != null && expMonth != null){
            int intExpireDate = Integer.parseInt(stringExpireDate);
            if(intExpireDate <= intCurrentDate) {
                errorMessage = "Credit card has expired, please enter a valid card.";
                return errorMessage;
            }
            if(creditCardForm.getDisplayExpMonth() != null){
                if(Integer.parseInt(creditCardForm.getDisplayExpMonth()) < 1 || Integer.parseInt(creditCardForm.getDisplayExpMonth()) > 12) {
                    errorMessage = "Expiration Month is invalid, please reenter.";
                    return errorMessage;
                }
            }
        }
        return errorMessage;
    }

    @Override
    public String setUpdatedFields(CreditCard creditCard, CreditCard passedInCreditCard) {
        String errorMessage = null;
        Calendar calendar = new GregorianCalendar();
        String year =  Integer.toString(calendar.get(Calendar.YEAR));
        String month =  Integer.toString(calendar.get(Calendar.MONTH)+1);
        int monthInt = Integer.parseInt(month);
        if(monthInt < 10) {
            month = "0" + month;
        }
        String stringCurrentDate = year + month;
        int intCurrentDate = Integer.parseInt(stringCurrentDate);
        String expYear = passedInCreditCard.getExpirationYear();
        String expMonth = passedInCreditCard.getExpirationMonth();
        String stringExpireDate = expYear + expMonth;

        if(expYear != null && expMonth != null) {
            int intExpireDate = Integer.parseInt(stringExpireDate);
            if (intExpireDate <= intCurrentDate) {
                errorMessage = "Credit card has expired, please enter a valid card.";
            }
            if(passedInCreditCard.getExpirationMonth() != null) {
                if (Integer.parseInt(passedInCreditCard.getExpirationMonth()) < 1 || Integer.parseInt(passedInCreditCard.getExpirationMonth()) > 12) {
                    errorMessage = "Expiration Month is invalid, please reenter.";
                }
            }
        }
        creditCard.setFirstName(passedInCreditCard.getFirstName());
        creditCard.setLastName(passedInCreditCard.getLastName());
        creditCard.getBillingAddress().setAddressLine1(passedInCreditCard.getBillingAddress().getAddressLine1());
        creditCard.getBillingAddress().setAddressLine2(passedInCreditCard.getBillingAddress().getAddressLine2());
        creditCard.getBillingAddress().setCity(passedInCreditCard.getBillingAddress().getCity());
        creditCard.getBillingAddress().setState(passedInCreditCard.getBillingAddress().getState());
        creditCard.getBillingAddress().setZipCode(passedInCreditCard.getBillingAddress().getZipCode());
        creditCard.setCreditCardType(passedInCreditCard.getCreditCardType());
        creditCard.setCardNumber(passedInCreditCard.getCardNumber());
        creditCard.setExpirationMonth(passedInCreditCard.getExpirationMonth());
        creditCard.setExpirationYear(passedInCreditCard.getExpirationYear());
        creditCard.setVerificationCode(passedInCreditCard.getVerificationCode());
        return errorMessage;
    }

    @Override
    public String setNewFields(CreditCard passedInCreditCard) {
        String errorMessage = null;
        Calendar calendar = new GregorianCalendar();
        String year =  Integer.toString(calendar.get(Calendar.YEAR));
        String month =  Integer.toString(calendar.get(Calendar.MONTH)+1);
        int monthInt = Integer.parseInt(month);
        if(monthInt < 10) {
            month = "0" + month;
        }
        String stringCurrentDate = year + month;
        int intCurrentDate = Integer.parseInt(stringCurrentDate);
        String expYear = passedInCreditCard.getExpirationYear();
        String expMonth = passedInCreditCard.getExpirationMonth();



        String stringExpireDate = expYear + expMonth;

        if(expYear != null && expMonth != null) {
            int intExpireDate = Integer.parseInt(stringExpireDate);
            if (intExpireDate <= intCurrentDate) {
                errorMessage = "Credit card has expired, please enter a valid card.";
            }
            if(passedInCreditCard.getExpirationMonth() != null) {
                if (Integer.parseInt(passedInCreditCard.getExpirationMonth()) < 1 || Integer.parseInt(passedInCreditCard.getExpirationMonth()) > 12) {
                    errorMessage = "Expiration Month is invalid, please reenter.";
                }
            }
        }
        CreditCard creditCard = new CreditCard();
        creditCard.setFirstName(passedInCreditCard.getFirstName());
        creditCard.setLastName(passedInCreditCard.getLastName());
        creditCard.setBillingAddress(new Address());
        creditCard.getBillingAddress().setAddressLine1(passedInCreditCard.getBillingAddress().getAddressLine1());
        creditCard.getBillingAddress().setAddressLine2(passedInCreditCard.getBillingAddress().getAddressLine2());
        creditCard.getBillingAddress().setCity(passedInCreditCard.getBillingAddress().getCity());
        creditCard.getBillingAddress().setState(passedInCreditCard.getBillingAddress().getState());
        creditCard.getBillingAddress().setZipCode(passedInCreditCard.getBillingAddress().getZipCode());
        creditCard.setCreditCardType(passedInCreditCard.getCreditCardType());
        creditCard.setCardNumber(passedInCreditCard.getCardNumber());
        creditCard.setExpirationMonth(passedInCreditCard.getExpirationMonth());
        creditCard.setExpirationYear(passedInCreditCard.getExpirationYear());
        creditCard.setVerificationCode(passedInCreditCard.getVerificationCode());
        return errorMessage;
    }

    @Override
    public CreditCard saveOrUpdate(CreditCard domainObject) {

        if(domainObject.getCardNumber() != null){
            String x = "XXXXXXXXXXXX";
            String last4Digits = domainObject.getCardNumber().substring(11, 15);

            domainObject.setDisplayCardNumber(x + last4Digits);
            domainObject.setEncryptedCardNumber(encryptionService.encryptString(domainObject.getCardNumber()));
        }

        if(domainObject.getExpirationMonth() != null) {
            String x = "XX";
            domainObject.setDisplayExpMonth(x);
            domainObject.setEncryptedExpirationMonth(encryptionService.encryptString(domainObject.getExpirationMonth()));
        }
        if(domainObject.getExpirationYear() != null) {
            String x = "XXXX";
            domainObject.setDisplayExpYear(x);
            domainObject.setEncryptedExpirationYear(encryptionService.encryptString(domainObject.getExpirationYear()));
        }

        if(domainObject.getVerificationCode() != null) {
            String x = "XXX";
            domainObject.setDisplayVerificationCode(x);
            domainObject.setEncryptedVerificationCode(encryptionService.encryptString(domainObject.getVerificationCode()));
        }
        return this.creditCardRepoServiceRepository.save(domainObject);
    }

    @Override
    public void delete(Integer id) {
        creditCardRepoServiceRepository.deleteById(id);
    }
}