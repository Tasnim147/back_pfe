package com.example.pfeback.IService;

import com.example.pfeback.Entities.Product;

import java.util.List;

/**
 * Interface for managing products.
 */
public interface IProductService {

    /**
     * Retrieves all products.
     *
     * @return List of products.
     */
    List<Product> getAllProducts();

    /**
     * Creates a new product.
     *
     * @param product The product to create.
     */
    public Product createProduct(Product product);
    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product.
     * @return The product, or null if not found.
     */
    Product getProductById(Long id);

    /**
     * Retrieves products by category.
     *
     * @param category The category of the products.
     * @return List of products in the specified category.
     */
    List<Product> getProductsByCategory(String category);

    /**
     * Retrieves products by offer ID.
     *
     * @param offerId The ID of the offer.
     * @return List of products associated with the specified offer.
     */
    List<Product> getProductsByOfferId(Long offerId);

    /**
     * Updates an existing product.
     *
     * @param id The ID of the product to update.
     * @param productDetails The new details for the product.
     * @return The updated product, or null if the product was not found.
     */
    Product updateProduct(Long id, Product productDetails);

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     */
    void deleteProduct(Long id);
}
