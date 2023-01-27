package com.es.phoneshop.model.order;

public interface OrderDao {
    Order getOrderBySecureId(String id);

}
