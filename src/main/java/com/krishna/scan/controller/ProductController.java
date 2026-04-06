package com.krishna.scan.controller;

import com.krishna.scan.dto.ProductDto;
import com.krishna.scan.entity.Product;
import com.krishna.scan.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PreAuthorize("hasAnyRole('ADMIN','CUSTOMER')")
    @PostMapping("/scan-product/{barcode}")
    public ResponseEntity<?> fetchProductsByBarcode(@PathVariable String barcode){
        ProductDto product = productService.getByProductBarcode(barcode);
        return new ResponseEntity<>(product, HttpStatus.FOUND);
    }


    @GetMapping("/all-products")
    public ResponseEntity<?> getAllProducts(){
        List<ProductDto> allProduct = productService.getAllProduct();

        return new ResponseEntity<>(allProduct,HttpStatus.FOUND);
    }

    @PutMapping("/update-product")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> update(@RequestBody ProductDto productDto){
        return ResponseEntity.ok(productService.updateProduct(productDto));
    }

    @DeleteMapping("/delete-product/{barcode}")
    @PreAuthorize("hasRole('ADMIN')")
    public void delete(@PathVariable String barcode){
        productService.deleteProduct(barcode);
    }
}
