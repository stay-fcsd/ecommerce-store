package com.abranlezama.ecommercestore.cart.controller;

import com.abranlezama.ecommercestore.cart.dto.AddProductToCartDTO;
import com.abranlezama.ecommercestore.cart.dto.CartDTO;
import com.abranlezama.ecommercestore.cart.dto.UpdateCartItemDTO;
import com.abranlezama.ecommercestore.cart.service.CustomerCartService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers/cart")
@Tag(name = "Customer Cart")
@SecurityRequirement(name = "bearer-key")
@Validated
@RequiredArgsConstructor
public class CustomerCartController {

    private final CustomerCartService customerCartService;

    @GetMapping
    public CartDTO retrieveCustomerCart(Authentication authentication) {
        return this.customerCartService.retrieveCustomerCart(authentication.getName());
    }

    @PostMapping
    public void addProductToCustomerCart(Authentication authentication,
                                         @Valid @RequestBody AddProductToCartDTO addDto) {
        this.customerCartService.addProductToCart(addDto, authentication.getName());
    }

    @PatchMapping
    public void updateCustomerCartItem(Authentication authentication,
                                       @Valid @RequestBody UpdateCartItemDTO updateDto) {
        this.customerCartService.updateCartItem(updateDto, authentication.getName());
    }

    @DeleteMapping("/{productId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void removeItemFromCustomerCart(Authentication authentication,
                                           @Positive @PathVariable Long productId) {
        this.customerCartService.removeItemFromCustomerCart(productId, authentication.getName());
    }
}
