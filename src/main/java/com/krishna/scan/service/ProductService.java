package com.krishna.scan.service;

import com.krishna.scan.dto.ProductDto;
import com.krishna.scan.entity.Product;
import com.krishna.scan.exception.ProductNotFoundException;
import com.krishna.scan.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public ProductDto addProduct(ProductDto productDto){
        Product product = Product.builder()
                .name(productDto.getName())
                    .barcode(productDto.getBarcode())
                        .price(productDto.getPrice())
                            .stock(productDto.getStock())
                                .build();
        productRepository.save(product);

        return productDto;
    }

    public List<Product> getAll(){
        return productRepository.findAll();
    }

    public List<ProductDto> getAllProduct(){
        List<Product> productList = productRepository.findAll();

        List<ProductDto> dtoList = productList.stream()
                .map(product -> new ProductDto(
                        product.getName(),
                        product.getBarcode(),
                        product.getPrice(),
                        product.getStock()
                ))
                .toList();

        return dtoList;
    }

    public Product getProduct(String barcode){
        return productRepository.findByBarcode(barcode)
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found"));
    }


    public ProductDto getByProductBarcode(String barcode){
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(()-> new ProductNotFoundException("Product Not Found For This Barcode:"+barcode));

        return ProductDto.builder()
                .name(product.getName())
                    .barcode(product.getBarcode())
                        .price(product.getPrice())
                            .stock(product.getStock())
                                .build();
    }


    public ProductDto updateProduct(ProductDto productDto){
        Product product = getProduct(productDto.getBarcode());

        product.setName(productDto.getName());
        product.setStock(productDto.getStock());
        product.setPrice(productDto.getPrice());

        productRepository.save(product);

        return productDto;
    }

    public void deleteProduct(String barcode){
        Product product = getProduct(barcode);

        productRepository.delete(product);
    }

}
