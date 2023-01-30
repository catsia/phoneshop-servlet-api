package com.es.phoneshop.model.product;

import java.util.List;

public interface ProductDao {
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    void save(Product product);

    Product getValue(Long id);

    void delete(Long id);
}
