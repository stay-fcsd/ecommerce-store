package com.abranlezama.ecommercestore.product.controller;

import com.abranlezama.ecommercestore.config.SecurityConfig;
import com.abranlezama.ecommercestore.product.service.ProductService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ProductController.class)
@Import(SecurityConfig.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService productService;

    @Nested
    @DisplayName("products retrieval")
    class ProductsRetrieval {
        @Test
        @DisplayName("return HTTP status OK(200) with products when params is valid")
        void returnHttpStatusOkWithProductsWhenValidParams() throws Exception {
            // Given
            int page = 0;
            int pageSize = 20;

            // When
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                            .param("page", String.valueOf(page))
                            .param("pageSize", String.valueOf(pageSize)))
                    .andExpect(status().isOk());

            // Then
            then(productService).should().retrieveProducts(page, pageSize);
        }

        @Test
        @DisplayName("return HTTP status OK(200) with products when params are missing")
        void returnHttpStatusOkWithProductWhenParamsAreMissing() throws Exception {
            // Given

            // When
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products"))
                    .andExpect(status().isOk());

            // Then
            then(productService).should().retrieveProducts(0, 20);
        }

        @Test
        @DisplayName("return HTTP status UnprocessableEntity(422) when params are invalid")
        void returnHttpStatusUnprocessableEntityWhenParamsAreInvalid() throws Exception {
            // Given
            int page = 0;
            int pageSize = 0;

            // When
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products")
                            .param("page", String.valueOf(page))
                            .param("pageSize", String.valueOf(pageSize)))
                    .andExpect(status().isUnprocessableEntity());

            // Then
            then(productService).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("product retrieval")
    class ProductRetrieval {

        @Test
        @DisplayName("return HTTP status OK(200) with product when valid path variable")
        void returnHttpStatusOKWithProductWhenValidInput() throws Exception {
            // Given
            long productId = 1L;

            // When
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{productId}", productId))
                    .andExpect(status().isOk());

            // Then
            then(productService).should().retrieveProduct(productId);
        }

        @Test
        @DisplayName("return HTTP status UnprocessableEntity(422) when invalid path variable")
        void returnHttpStatusUnprocessableEntityWhenInvalidPathVariable() throws Exception {
            // Given
            long productId = -1L;

            // When
            mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/products/{productId}", productId))
                    .andExpect(status().isUnprocessableEntity());

            // Then
            then(productService).shouldHaveNoInteractions();
        }
    }

}
