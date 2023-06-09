package com.abranlezama.ecommercestore.product.controller;

import com.abranlezama.ecommercestore.product.dto.ProductDTO;
import com.abranlezama.ecommercestore.product.service.ProductService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Public Products")
@Validated
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDTO> retrieveProducts(@PositiveOrZero @RequestParam(value = "page", defaultValue = "0") int page,
                                             @Positive @RequestParam(value = "pageSize", defaultValue = "20") int pageSize) {
        return productService.retrieveProducts(page, pageSize);
    }

    @GetMapping("/{productId}")
    public ProductDTO retrieveProduct(@Positive @PathVariable Long productId) {
        return productService.retrieveProduct(productId);
    }
}
