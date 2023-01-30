package com.es.phoneshop.model;

import com.es.phoneshop.model.order.ArrayListOrderDao;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderDao;
import org.junit.Before;
import org.junit.Test;

import java.util.NoSuchElementException;

import static org.junit.Assert.assertEquals;

public class ArrayListOrderDaoTest {
    private OrderDao orderDao;
    private Order order;

    @Before
    public void setup() {
        orderDao = ArrayListOrderDao.getInstance();
        order = new Order();
        order.setId(1L);
    }

    @Test
    public void testGetOrder() {
        orderDao.save(order);
        Order tempOrder = orderDao.getValue(1L);
        assertEquals(order, tempOrder);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetNonExistingOrder() {
        orderDao.getValue(3L);
    }

    @Test
    public void testSaveNonNullOrder() {
        orderDao.save(order);
        assertEquals(order.getId(), orderDao.getValue(order.getId()).getId());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNullOrder() {
        orderDao.save(null);
    }

    @Test
    public void testSaveExistingOrder() {
        order.setFirstName("name");
        orderDao.save(order);
        Order testOrder = order;
        testOrder.setFirstName("test-name");
        orderDao.save(testOrder);
        assertEquals("test-name", orderDao.getValue(1L).getFirstName());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteOrder() {
        orderDao.save(order);
        orderDao.delete(1L);
        orderDao.getValue(1L);
    }
}
