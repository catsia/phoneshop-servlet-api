package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class ArrayListProductDao implements ProductDao {

    private static ArrayListProductDao instance;

    public static ArrayListProductDao getInstance(){
        if (instance == null){
            instance = new ArrayListProductDao();
        }
        return instance;
    }
    private List<Product> products;

    private long maxProductId;

    private final Object lock = new Object();

    private ArrayListProductDao() {
        products = new ArrayList<>();
        maxProductId = 0;
    }

    @Override
    public Product getProduct(Long id) throws NoSuchElementException {
        synchronized (lock) {
            return products.stream()
                    .filter(product -> id.equals(product.getId()))
                    .findAny().orElseThrow(NoSuchElementException::new);
        }
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        synchronized (lock) {
            Comparator<Product> comparator = Comparator.comparing(product -> {
                if (sortField == SortField.description){
                    return (Comparable) product.getDescription();
                } else {
                    return (Comparable) product.getPrice();
                }
            });
            if (sortOrder == SortOrder.desc) comparator = comparator.reversed();
            return products.stream()
                    .filter(product -> query == null || query.isEmpty() || product.getDescription().contains(query))
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .sorted(comparator)
                    .collect(Collectors.toList());
        }
    }

    @Override
    public void save(Product product) {
        synchronized (lock) {
            if (product.getId() != null) {
                delete(product.getId());
                products.add(product);
            } else {
                product.setId(maxProductId++);
                products.add(product);
            }
        }
    }

    @Override
    public void delete(Long id) {
        synchronized (lock) {
            products.removeIf(product -> id != null && id.equals(product.getId()));
        }
    }
}
