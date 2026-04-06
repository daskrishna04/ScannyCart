package com.krishna.scan.controller;

import com.krishna.scan.entity.Cart;
import com.krishna.scan.entity.User;
import com.krishna.scan.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/get-cart")
    public ResponseEntity<Cart> getCart(@AuthenticationPrincipal User user){
        return new ResponseEntity<>(cartService.getCart(user.getId()), HttpStatus.FOUND);
    }

    @PutMapping("/update")
    public ResponseEntity<Cart> updateCart(@AuthenticationPrincipal User user,
                                           @RequestParam int quantity,
                                           @RequestParam int itemId){
        if(quantity<=0){
            throw new RuntimeException("Quantity can not be 0");
        }

        return new ResponseEntity<>(cartService.updateQuantity(user,itemId,quantity),HttpStatus.FOUND);
    }

    @DeleteMapping("/delete/{id}")
    public Cart delete(@AuthenticationPrincipal User user,
                       @PathVariable int id){
        return cartService.removeItem(user, id);
    }
}

