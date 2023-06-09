package com.abranlezama.ecommercestore.customer.service.imp;

import com.abranlezama.ecommercestore.customer.model.Customer;
import com.abranlezama.ecommercestore.customer.util.CustomerUserDetails;
import com.abranlezama.ecommercestore.customer.repository.CustomerRepository;
import com.abranlezama.ecommercestore.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaCustomerDetailsService implements UserDetailsService {
    private final CustomerRepository customerRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Customer customer = customerRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.AUTHENTICATION_FAILED));

        return new CustomerUserDetails.SecurityCustomer(customer);
    }
}
