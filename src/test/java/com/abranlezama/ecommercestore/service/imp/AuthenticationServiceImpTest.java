package com.abranlezama.ecommercestore.service.imp;

import com.abranlezama.ecommercestore.dto.authentication.AuthenticationRequestDTO;
import com.abranlezama.ecommercestore.dto.authentication.RegisterCustomerDTO;
import com.abranlezama.ecommercestore.dto.authentication.mapper.AuthenticationMapper;
import com.abranlezama.ecommercestore.exception.AuthException;
import com.abranlezama.ecommercestore.exception.EmailTakenException;
import com.abranlezama.ecommercestore.exception.ExceptionMessages;
import com.abranlezama.ecommercestore.exception.UnequalPasswordsException;
import com.abranlezama.ecommercestore.objectmother.AuthenticationRequestDTOMother;
import com.abranlezama.ecommercestore.objectmother.CustomerMother;
import com.abranlezama.ecommercestore.objectmother.RegisterCustomerDTOMother;
import com.abranlezama.ecommercestore.objectmother.UserMother;
import com.abranlezama.ecommercestore.repository.CustomerRepository;
import com.abranlezama.ecommercestore.repository.UserRepository;
import com.abranlezama.ecommercestore.service.TokenService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;



@ExtendWith(MockitoExtension.class)
class AuthenticationServiceImpTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private CustomerRepository customerRepository;
    @Mock
    private AuthenticationMapper authenticationMapper;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private TokenService tokenService;
    @InjectMocks
    private AuthenticationServiceImp cut;

    // Customer registration
    @Test
    void ShouldCreateRecordForUserAndCustomerFromRegisterCustomerDTO() {
        // Given
        RegisterCustomerDTO dto = RegisterCustomerDTOMother.complete().build();

        given(userRepository.existsByEmail(dto.email())).willReturn(false);
        given(authenticationMapper.mapToUser(dto)).willReturn(UserMother.complete().build());
        given(authenticationMapper.mapToCustomer(dto)).willReturn(CustomerMother.complete().build());

        // When
        cut.registerCustomer(dto);

        // Then
        then(passwordEncoder).should().encode(dto.password());
        then(customerRepository).should().save(any());
    }

    @Test
    void shouldThrowUnequalPasswordsExceptionWhenCustomerRegistersWithDifferentPasswords() {
        // Given
        RegisterCustomerDTO dto = RegisterCustomerDTOMother.complete()
                .verifyPassword("123456789")
                .build();

        assertThatThrownBy(() -> cut.registerCustomer(dto))
                .hasMessage(ExceptionMessages.DIFFERENT_PASSWORDS)
                .isInstanceOf(UnequalPasswordsException.class);

        // Then
        then(userRepository).shouldHaveNoInteractions();
    }

    @Test
    void shouldThrowEmailTakenExceptionWith409WhenCustomerRegistersWithAnExistingEmail() {
        // Given
        RegisterCustomerDTO dto = RegisterCustomerDTOMother.complete().build();

        given(userRepository.existsByEmail(dto.email())).willReturn(true);

        // When
        assertThatThrownBy(() -> cut.registerCustomer(dto))
                .hasMessage(ExceptionMessages.EMAIL_TAKEN)
                .isInstanceOf(EmailTakenException.class);

        // Then
        then(userRepository).shouldHaveNoMoreInteractions();
        then(customerRepository).shouldHaveNoInteractions();
    }

    // User authentication
    @Test
    void shouldAuthenticateUserWhenCredentialsAreCorrect() {
        // Given
        AuthenticationRequestDTO dto = AuthenticationRequestDTOMother.complete().build();

        // When
        cut.authenticateUser(dto);

        // Then
        then(tokenService).should().generateJwt(any());
    }

    @Test
    void shouldThrowAuthExceptionWhenEmailOrPasswordAreIncorrect() {
        // Given
        AuthenticationRequestDTO dto = AuthenticationRequestDTOMother.complete().build();

        given(authenticationManager.authenticate(any())).willThrow(new UsernameNotFoundException("User not found"));

        // When
        assertThatThrownBy(() -> cut.authenticateUser(dto))
                .hasMessage(ExceptionMessages.AUTHENTICATION_FAILED)
                .isInstanceOf(AuthException.class);
    }

}
