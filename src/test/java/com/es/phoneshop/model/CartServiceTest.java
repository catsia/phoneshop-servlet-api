package com.es.phoneshop.model;

import com.es.phoneshop.model.cart.*;
import com.es.phoneshop.model.product.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.math.BigDecimal;
import java.util.Currency;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CartServiceTest {

    private CartService cartService;

    private Cart cart;
    private Product product;

    private ProductDao productDao;

    @Mock
    HttpServletRequest request;

    @Mock
    HttpSession session;

    @Before
    public void setup() {
        cartService = CartServiceImp.getInstance();
        productDao = ArrayListProductDao.getInstance();
        cart = new Cart();

        Currency currency = java.util.Currency.getInstance("USD");
        product = new Product(1L, "test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");
        productDao.save(product);

        cart.getCartItems().add(new CartItem(product, 1));
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute(anyString())).thenReturn(cart);
    }

    @Test
    public void testAddExistingProduct() throws OutOfStockException {
        cartService.add(request, 1L, 1);
        assertEquals(1, cart.getCartItems().size());
    }

    @Test(expected = OutOfStockException.class)
    public void testAddMoreThenInStock() throws OutOfStockException {
        cartService.add(request, 1L, 10);
    }

    @Test
    public void testAddNewProduct() throws OutOfStockException {
        Currency currency = java.util.Currency.getInstance("USD");
        product = new Product(2L, "test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");
        productDao.save(product);

        cartService.add(request, 2L, 1);
        assertEquals(2, cart.getCartItems().size());
    }

}
