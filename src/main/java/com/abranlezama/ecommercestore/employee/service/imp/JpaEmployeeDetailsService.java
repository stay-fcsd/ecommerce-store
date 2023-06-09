package com.abranlezama.ecommercestore.employee.service.imp;

import com.abranlezama.ecommercestore.employee.model.Employee;
import com.abranlezama.ecommercestore.employee.util.EmployeeUserDetails;
import com.abranlezama.ecommercestore.employee.repository.EmployeeRepository;
import com.abranlezama.ecommercestore.exception.ExceptionMessages;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JpaEmployeeDetailsService implements UserDetailsService {
    private final EmployeeRepository employeeRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(ExceptionMessages.AUTHENTICATION_FAILED));

        return new EmployeeUserDetails.SecurityEmployee(employee);
    }
}
