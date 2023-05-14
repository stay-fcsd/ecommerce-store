package com.abranlezama.ecommercestore.dto.cart;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Builder;

@Builder
public record CartItemDTO(
        @NotBlank String name,
        @NotBlank Float price,
        @Positive Integer quantity
) { }