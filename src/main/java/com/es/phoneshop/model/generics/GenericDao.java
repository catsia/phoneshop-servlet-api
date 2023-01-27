package com.es.phoneshop.model.generics;

import java.util.List;
import java.util.NoSuchElementException;

public abstract class GenericDao <T extends Identifiable> {
    protected List<T> values;
    protected final Object lock = new Object();

    protected long maxId;


    public T getValue(Long id) throws NoSuchElementException {
        synchronized (lock) {
            return values.stream()
                    .filter(value -> id != null && id.equals(value.getId()))
                    .findAny()
                    .orElseThrow(() -> new NoSuchElementException(String.valueOf(id)));
        }
    }

    public void save(T value) {
        synchronized (lock) {
            if (value == null) {
                throw new IllegalArgumentException();
            }
            if (value.getId() != null) {
                delete(value.getId());
                values.add(value);
            } else {
                value.setId(maxId++);
                values.add(value);
            }
        }
    }

    public void delete(Long id) {
        synchronized (lock) {
            values.removeIf(product -> id != null && id.equals(product.getId()));
        }
    }

}
