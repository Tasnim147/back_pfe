package com.example.pfeback.Controller;

import com.example.pfeback.Entities.Product;
import com.example.pfeback.Service.OfferService;
import com.example.pfeback.Service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Controller for managing products.
 */
@RestController
public class ProductController {
    private static final String UPLOAD_DIR = "uploads/";

    @Autowired
    private ProductService productService;

    @Autowired
    private OfferService offerService;

    /**
     * Retrieves all products.
     *
     * @return List of products.
     */
    @GetMapping
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    /**
     * Creates a new product.
     *
     * @param name        The product's name.
     * @param category    The product's category.
     * @param description The product's description.
     * @param price       The product's price.
     * @param offerId     The ID of the associated offer.
     * @param file        The image file (optional).
     * @return ResponseEntity with the created product or error status.
     */
    @PostMapping
    public ResponseEntity<Product> createProduct(
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("offer") Long offerId,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Product product = new Product();
            product.setName(name);
            product.setCategory(category);
            product.setDescription(description);
            product.setPrice(price);
            product.setOffer(offerService.getOfferById(offerId));

            if (file != null && !file.isEmpty()) {
                System.out.println("File received: " + file.getOriginalFilename());
                String imageUrl = saveFile(file);
                product.setImageUrl(imageUrl);
            } else {
                System.out.println("No file received");
            }

            Product createdProduct = productService.createProduct(product);
            return ResponseEntity.ok(createdProduct);
        } catch (IOException e) {
            e.printStackTrace(); // Log the exception for debugging
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Updates an existing product.
     *
     * @param id          The ID of the product to update.
     * @param name        The product's name.
     * @param category    The product's category.
     * @param description The product's description.
     * @param price       The product's price.
     * @param offerId     The ID of the associated offer.
     * @param file        The image file (optional).
     * @return ResponseEntity with the updated product or error status.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestParam("name") String name,
            @RequestParam("category") String category,
            @RequestParam("description") String description,
            @RequestParam("price") Double price,
            @RequestParam("offer") Long offerId,
            @RequestParam(value = "file", required = false) MultipartFile file) {
        try {
            Product product = productService.getProductById(id);
            if (product == null) {
                return ResponseEntity.notFound().build();
            }
            product.setName(name);
            product.setCategory(category);
            product.setDescription(description);
            product.setPrice(price);
            product.setOffer(offerService.getOfferById(offerId));

            if (file != null && !file.isEmpty()) {
                String imageUrl = saveFile(file);
                product.setImageUrl(imageUrl);
            }

            Product updatedProduct = productService.updateProduct(id, product);
            return ResponseEntity.ok(updatedProduct);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id The ID of the product.
     * @return The product, or 404 if not found.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        if (product != null) {
            return new ResponseEntity<>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Retrieves products by category.
     *
     * @param category The category of the products.
     * @return List of products in the specified category.
     */
    @GetMapping("/category/{category}")
    public List<Product> getProductsByCategory(@PathVariable String category) {
        return productService.getProductsByCategory(category);
    }

    /**
     * Retrieves products by offer ID.
     *
     * @param offerId The ID of the offer.
     * @return List of products associated with the specified offer.
     */
    @GetMapping("/offer/{offerId}")
    public List<Product> getProductsByOfferId(@PathVariable Long offerId) {
        return productService.getProductsByOfferId(offerId);
    }

    /**
     * Deletes a product by its ID.
     *
     * @param id The ID of the product to delete.
     * @return ResponseEntity with status.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        try {
            productService.deleteProduct(id);
            return new ResponseEntity<>("Product deleted successfully", HttpStatus.NO_CONTENT);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Endpoint for uploading an image.
     *
     * @param file The image file to upload.
     * @return ResponseEntity with image URL or error message.
     */
    @PostMapping("/uploadImage")
    public ResponseEntity<Map<String, String>> uploadImage(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "No file uploaded"));
        }

        try {
            String imageUrl = saveFile(file);
            Map<String, String> response = new HashMap<>();
            response.put("imageUrl", imageUrl);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Failed to store file"));
        }
    }

    /**
     * Helper method to save a file and return its URL.
     *
     * @param file The file to save.
     * @return The URL of the saved file.
     * @throws IOException If an I/O error occurs.
     */
    private String saveFile(MultipartFile file) throws IOException {
        Path path = Paths.get(UPLOAD_DIR + file.getOriginalFilename());
        Files.createDirectories(path.getParent());
        Files.write(path, file.getBytes());
        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/products/uploads/")
                .path(file.getOriginalFilename())
                .toUriString();
    }
}
