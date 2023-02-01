package com.es.phoneshop.model.product;

import com.es.phoneshop.model.generics.GenericDao;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ArrayListProductDao extends GenericDao<Product> implements ProductDao{

    private static ProductDao instance;

    public static synchronized ProductDao getInstance() {
        if (instance == null) {
            instance = new ArrayListProductDao();
        }
        return instance;
    }

    private ArrayListProductDao() {
        values = new ArrayList<>();
        maxId = 0;
    }

    @Override
    public List<Product> findProducts(String query, SortField sortField, SortOrder sortOrder) {
        synchronized (lock) {
            List<String> splitQuery = splitQuery(query);
            return values.stream().filter(product -> product.getPrice() != null)
                    .filter(product -> product.getStock() > 0)
                    .filter(product -> splitQuery.isEmpty() || splitQuery.stream().anyMatch(product.getDescription().toLowerCase()::contains))
                    .sorted(sortField == null ? generateComparatorForQuery(splitQuery) : generateComparatorForFieldAndOrder(sortField, sortOrder))
                    .collect(Collectors.toList());
        }
    }

    private Comparator<Product> generateComparatorForQuery(List<String> splitQuery) {
        Comparator<Product> comparator = Comparator
                .comparing(product -> splitQuery.size() - splitQuery.stream()
                        .filter(product.getDescription().toLowerCase()::contains)
                        .count());
        for (String query : splitQuery) {
            comparator = comparator.thenComparing(product -> !product.getDescription().toLowerCase().contains(query));
        }
        return comparator.thenComparing(product -> product.getDescription().length());
    }

    private List<String> splitQuery(String query) {
        synchronized (lock) {
            if (query == null) {
                return new ArrayList<>();
            }
            query.replaceAll("\\s+", " ").toLowerCase();
            return Stream.of(query.split(" "))
                    .map(String::new)
                    .collect(Collectors.toList());
        }
    }

    private Comparator<Product> generateComparatorForFieldAndOrder(SortField sortField, SortOrder sortOrder) {
        Comparator<Product> comparator = Comparator.comparing(product -> {
            if (sortField == SortField.description) {
                return (Comparable) product.getDescription();
            } else {
                return (Comparable) product.getPrice();
            }
        });
        if (sortOrder == SortOrder.desc) {
            comparator = comparator.reversed();
        }
        return comparator;
    }

}
