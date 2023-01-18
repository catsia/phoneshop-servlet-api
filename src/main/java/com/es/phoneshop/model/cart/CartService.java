package com.es.phoneshop.model.cart;

import javax.servlet.http.HttpServletRequest;

public interface CartService {
    Cart getCart(HttpServletRequest request);
    void add(HttpServletRequest request, Long productId, int quantity) throws OutOfStockException;
}
