package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.List;

public interface ProductDao {
    List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder);

    List<Product> findProductsWithPriceRange(String query, SearchCriteria searchCriteria, BigDecimal minPrice, BigDecimal maxPrice);

    void save(Product product);

    Product getValue(Long id);

    void delete(Long id);
}
