package com.es.phoneshop.model.order;

public interface OrderDao {
    Order getOrderBySecureId(String id);

    void save(Order order);

    Order getValue(Long id);

    void delete(Long id);
}
