package com.es.phoneshop.model;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.order.*;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(MockitoJUnitRunner.class)
public class DefaultOrderServiceTest {

    private OrderService orderService;

    private Cart cart;

    @Before
    public void setup() {
        cart = new Cart();
        orderService = DefaultOrderService.getInstance();
        Currency currency = Currency.getInstance("USD");
        Product product = new Product(2L, "test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");

        cart.getCartItems().add(new CartItem(product, 1));
    }

    @Test(expected = NoSuchElementException.class)
    public void testSaveNullOrder() {
        orderService.saveOrder(null);
        ArrayListOrderDao.getInstance().getValue(1L);
    }

    @Test
    public void testGetOrderWithCard() {
        Order order = orderService.getOrder(cart);
        assertEquals(cart.getCartItems().size(), order.getCartItems().size());
    }


    @Test
    public void testGetPaymentMethods() {
        assertEquals(List.of(PaymentMethod.values()), orderService.getPaymentMethods());
    }

    @Test
    public void testSaveNotNullOrder() {
        Order order = new Order();
        order.setId(2L);
        orderService.saveOrder(order);
        assertNotNull(ArrayListOrderDao.getInstance().getValue(2L));
    }

    @Test
    public void testGetOrderWithZeroDeliveryCost() {
        cart.setTotalCost(BigDecimal.valueOf(101));
        Order order = orderService.getOrder(cart);
        assertEquals(BigDecimal.ZERO, order.getDeliveryCost());
    }

    @Test
    public void testGetOrderById() {
        Order order = new Order();
        order.setId(2L);
        orderService.saveOrder(order);
        Order testOrder = orderService.getOrderById(2L);
        assertEquals(order, testOrder);
    }
}
