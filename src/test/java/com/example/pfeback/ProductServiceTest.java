package com.example.pfeback;

import com.example.pfeback.Entities.Offer;
import com.example.pfeback.Entities.Product;
import com.example.pfeback.Repositories.OfferRepository;
import com.example.pfeback.Repositories.ProductRepository;
import com.example.pfeback.Service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private OfferRepository offerRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        List<Product> products = new ArrayList<>();
        products.add(new Product());
        when(productRepository.findAll()).thenReturn(products);

        List<Product> result = productService.getAllProducts();
        assertEquals(products, result);
    }

    @Test
    void testCreateProduct_Success() {
        // Set up a product with a valid offer ID
        Product product = new Product();
        Offer offer = new Offer();
        offer.setId(1L); // Set a valid ID
        product.setOffer(offer);

        when(offerRepository.findById(1L)).thenReturn(Optional.of(offer)); // Simulate finding the offer
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(product);
        assertEquals(product, result);
        verify(productRepository, times(1)).save(product);
    }
    @Test
    void testCreateProduct_Failure() {
        Product product = new Product();
        product.setOffer(new Offer());

        when(offerRepository.existsById(anyLong())).thenReturn(false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            productService.createProduct(product);
        });
        assertEquals("Offer with id null does not exist", thrown.getMessage());
    }

    @Test
    void testGetProductById_Success() {
        Product product = new Product();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(product));

        Product result = productService.getProductById(1L);
        assertEquals(product, result);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        Product result = productService.getProductById(1L);
        assertNull(result);
    }

    @Test
    void testUpdateProduct_Success() {
        Product existingProduct = new Product();
        Product productDetails = new Product();
        when(productRepository.findById(anyLong())).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenReturn(productDetails);

        Product result = productService.updateProduct(1L, productDetails);
        assertEquals(productDetails, result);
    }

    @Test
    void testUpdateProduct_NotFound() {
        Product productDetails = new Product();
        when(productRepository.findById(anyLong())).thenReturn(Optional.empty());

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            productService.updateProduct(1L, productDetails);
        });
        assertEquals("Product with id 1 does not exist", thrown.getMessage());
    }

    @Test
    void testDeleteProduct_Success() {
        when(productRepository.existsById(anyLong())).thenReturn(true);

        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepository.existsById(anyLong())).thenReturn(false);

        IllegalArgumentException thrown = assertThrows(IllegalArgumentException.class, () -> {
            productService.deleteProduct(1L);
        });
        assertEquals("Product with id 1 does not exist", thrown.getMessage());
    }
}
