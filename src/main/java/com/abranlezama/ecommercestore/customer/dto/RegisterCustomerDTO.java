package com.abranlezama.ecommercestore.customer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

@Builder
public record RegisterCustomerDTO(
        @NotBlank String name,
        @Email String email,
        @Size (min = 8, max = 15) String password,
        @Size (min = 8, max = 15) String verifyPassword
) { }
