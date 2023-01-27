package com.es.phoneshop.model.order;

import com.es.phoneshop.model.generics.GenericDao;

import java.util.ArrayList;
import java.util.NoSuchElementException;

public class ArrayListOrderDao extends GenericDao<Order> implements OrderDao{

    private static ArrayListOrderDao instance;

    public static synchronized ArrayListOrderDao getInstance() {
        if (instance == null) {
            instance = new ArrayListOrderDao();
        }
        return instance;
    }

    private ArrayListOrderDao() {
        values = new ArrayList<>();
        maxId = 0;
    }

    @Override
    public Order getOrderBySecureId(String id) {
        synchronized (lock) {
            return values.stream()
                    .filter(order -> id != null && id.equals(order.getSecureId()))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException(String.valueOf(id)));

        }
    }

}
