package com.es.phoneshop.model.cart;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class HttpSessionCartService implements CartService {
    private static HttpSessionCartService instance;

    private static final String CART_SESSION_ATTRIBUTE = HttpSessionCartService.class.getName() + ".cart";

    private ProductDao productDao = ArrayListProductDao.getInstance();

    public static HttpSessionCartService getInstance() {
        if (instance == null) {
            instance = new HttpSessionCartService();
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
    public void add(HttpServletRequest request, Long productId, int quantity) throws OutOfStockException {
        synchronized (request.getSession()) {
            Cart cart = getCart(request);
            Product product = productDao.getProduct(productId);
            List<CartItem> cartItems = cart.getCartItems().stream()
                    .filter(cartItem -> cartItem.getProduct() != null && cartItem.getProduct().getId().equals(productId))
                    .collect(Collectors.toList());
            if (cartItems.size() == 1) {
                if (cartItems.get(0).getQuantity() + quantity > product.getStock()) {
                    throw new OutOfStockException(product.getStock());
                }
                cartItems.get(0).setQuantity(quantity);
            } else {
                if (quantity > product.getStock()) {
                    throw new OutOfStockException(product.getStock());
                }
                cart.getCartItems().add(new CartItem(product, quantity));
            }
        }
    }
}
