package com.es.phoneshop.model.product;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
            List<String> splitQuery = splitQuery(query);
            return products.stream()
                    .filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> splitQuery.contains("") || containsQuery(splitQuery, product))
                    .sorted(generateComparator(sortField, sortOrder))
                    .collect(Collectors.toList());
        }
    }

    private boolean containsQuery(List<String> splitQuery, Product product){
        return new HashSet<>(splitQuery(product.getDescription())).containsAll(splitQuery);
    }

    private List<String> splitQuery(String query){
        return Stream.of(query.split(" "))
                .map (String::new)
                .collect(Collectors.toList());
    }

    private Comparator<Product> generateComparator(SortField sortField, SortOrder sortOrder){
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (sortField == SortField.description){
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if (sortOrder == SortOrder.desc) comparator = comparator.reversed();
        return comparator;
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
