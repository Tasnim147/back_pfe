package com.example.pfeback.Service;

import com.example.pfeback.Entities.Offer;
import com.example.pfeback.Entities.Product;
import com.example.pfeback.IService.IProductService;
import com.example.pfeback.Repositories.OfferRepository;
import com.example.pfeback.Repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing products.
 */
@Service
public class ProductService implements IProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OfferRepository offerRepository;

    /**
     * Retrieves all products.
     *
     * @return List of products.
     */
    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    /**
     * Creates a new product if the associated offer exists.
     *
     * @param product The product to create.
     * @throws IllegalArgumentException if the associated offer does not exist.
     */

    @Transactional
    @Override
    public Product createProduct(Product product) {
        // Validate if offer ID is null
        if (product.getOffer() == null || product.getOffer().getId() == null) {
            throw new IllegalArgumentException("Offer with id null does not exist");
        }

        // Fetch the offer
        Optional<Offer> offerOptional = offerRepository.findById(product.getOffer().getId());
        if (!offerOptional.isPresent()) {
            throw new IllegalArgumentException("Offer with id " + product.getOffer().getId() + " does not exist");
        }

        // Set the offer to the product and save
        product.setOffer(offerOptional.get());
        return productRepository.save(product);
    }




    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product.
     * @return The product, or null if not found.
     */
    @Override
    public Product getProductById(Long id) {
        return productRepository.findById(id).orElse(null);
    }

    /**
     * Retrieves products by category.
     *
     * @param category The category of the products.
     * @return List of products in the specified category.
     */
    @Override
    public List<Product> getProductsByCategory(String category) {
        return productRepository.findByCategory(category);
    }

    /**
     * Retrieves products by offer ID.
     *
     * @param offerId The ID of the offer.
     * @return List of products associated with the specified offer.
     */
    @Override
    public List<Product> getProductsByOfferId(Long offerId) {
        return productRepository.findByOfferId(offerId);
    }

    /**
     * Updates an existing product.
     *
     * @param id The ID of the product to update.
     * @param productDetails The new details for the product.
     * @return The updated product, or null if the product was not found.
     * @throws IllegalArgumentException if the product with the specified ID does not exist.
     */
    @Transactional
    @Override
    public Product updateProduct(Long id, Product productDetails) {
        Optional<Product> optionalProduct = productRepository.findById(id);
        if (optionalProduct.isPresent()) {
            Product product = optionalProduct.get();
            product.setName(productDetails.getName());
            product.setCategory(productDetails.getCategory());
            product.setDescription(productDetails.getDescription());
            product.setPrice(productDetails.getPrice());
            product.setImageUrl(productDetails.getImageUrl()); // Mise à jour de l'URL de l'image

            product.setOffer(productDetails.getOffer()); // Assurez-vous que cette ligne est correcte
            return productRepository.save(product);
        } else {
            throw new IllegalArgumentException("Product with id " + id + " does not exist");
        }
    }


    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @throws IllegalArgumentException if the product with the specified ID does not exist.
     */
    @Override
    @Transactional
    public void deleteProduct(Long id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Product with id " + id + " does not exist");
        }
    }
}
