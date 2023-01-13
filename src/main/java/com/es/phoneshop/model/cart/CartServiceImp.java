package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class CartServiceImp implements CartService {
    private static CartServiceImp instance;

    private static final String CART_SESSION_ATTRIBUTE = CartServiceImp.class.getName() + ".cart";

    private ProductDao productDao = ArrayListProductDao.getInstance();

    public static CartServiceImp getInstance() {
        if (instance == null) {
            instance = new CartServiceImp();
        }
        return instance;
    }

    @Override
    public Cart getCart(HttpServletRequest request) {
        synchronized (request.getSession()) {
            Cart cart = (Cart) request.getSession().getAttribute(CART_SESSION_ATTRIBUTE);
            if (cart == null) {
                request.getSession().setAttribute(CART_SESSION_ATTRIBUTE, cart = new Cart());
            }
            return cart;
        }
    }

    @Override
    public void add(Cart cart, Long productId, int quantity) throws OutOfStockException {
        List<CartItem> cartItems = cart.getCartItems().stream().filter(cartItem -> cartItem.getProduct() != null && cartItem.getProduct().getId().equals(productId)).collect(Collectors.toList());
        if (cartItems.size() == 1) {
            if (cartItems.get(0).getQuantity() + quantity > productDao.getProduct(productId).getStock()) {
                throw new OutOfStockException(productDao.getProduct(productId).getStock());
            }
            cartItems.get(0).setQuantity(quantity);
        } else {
            if (quantity > productDao.getProduct(productId).getStock()) {
                throw new OutOfStockException(productDao.getProduct(productId).getStock());
            }
            Product product = productDao.getProduct(productId);
            cart.getCartItems().add(new CartItem(product, quantity));
        }
    }
}
