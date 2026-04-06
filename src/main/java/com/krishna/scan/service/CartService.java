package com.krishna.scan.service;

import com.krishna.scan.dto.ProductDto;
import com.krishna.scan.entity.Cart;
import com.krishna.scan.entity.CartItem;
import com.krishna.scan.entity.Product;
import com.krishna.scan.entity.User;
import com.krishna.scan.exception.CartNotFoundException;
import com.krishna.scan.exception.ProductNotFoundException;
import com.krishna.scan.repository.CartRepository;
import com.krishna.scan.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final UserService userService;
    private final ProductRepository productRepository;

    public void addNewCart(Cart cart){
        cartRepository.save(cart);
    }

    public Cart getCart(int userId){
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new CartNotFoundException("cart not found"));
        return cart;
    }

    @Transactional
    public Cart addToCart(int userId, String barcode, int quantity){

        //Get Active Cart for the user
        Cart cart = cartRepository.findByUserIdAndStatus(userId, Cart.Status.ACTIVE);

        if(cart == null){
            cart = Cart.builder()
                    .user(userService.findById(userId))
                    .status(Cart.Status.ACTIVE)
                    .totalAmount(BigDecimal.ZERO)
                    .build();
            cartRepository.save(cart);
        }

        //find product
        Product product = productRepository.findByBarcode(barcode)
                .orElseThrow(()-> new ProductNotFoundException("product not found"));

        //check product is already exists or not
        Optional<CartItem> cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getBarcode().equals(barcode))
                .findFirst();

        if(cartItem.isPresent()){
            CartItem item = cartItem.get();
            item.setQuantity(item.getQuantity() + quantity);
        }else {
            CartItem item = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .price(product.getPrice())
                    .quantity(quantity)
                    .build();
        }

        BigDecimal total = cart.getItems().stream()
                .map(item -> item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);

        cart.setTotalAmount(total);

        return cartRepository.save(cart);
    }

    @Transactional
    private void recalculate(Cart cart){
        BigDecimal total = cart.getItems().stream()
                .map(item->item.getPrice()
                        .multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO,BigDecimal::add);
        cart.setTotalAmount(total);
    }

    @Transactional
    public Cart removeItem(User user, int itemId){
        Cart cart = cartRepository.findByUserId(user.getId())
                .orElseThrow(()->new CartNotFoundException("crt not found"));

        cart.getItems().removeIf(item->item.getId()==itemId);

        recalculate(cart);

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart updateQuantity(User user,int itemId,int quantity){
        Cart cart = getCart(user.getId());

        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getId() == itemId)
                .findFirst()
                .orElseThrow(()->new RuntimeException("Cart item not found"));

        cartItem.setQuantity(quantity);

        return cartRepository.save(cart);
    }


}
