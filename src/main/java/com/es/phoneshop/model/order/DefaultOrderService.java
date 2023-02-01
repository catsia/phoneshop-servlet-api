package com.es.phoneshop.model.order;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;

import java.math.BigDecimal;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;
import java.util.stream.Collectors;

public class DefaultOrderService implements OrderService {
    private static OrderService instance;

    private OrderDao orderDao;

    private final Object lock = new Object();

    public static synchronized OrderService getInstance() {
        if (instance == null) {
            instance = new DefaultOrderService();
        }
        return instance;
    }

    private DefaultOrderService() {
        orderDao = ArrayListOrderDao.getInstance();
    }

    @Override
    public Order getOrder(Cart cart) {
        synchronized (lock) {
            Order order = new Order();
            order.setCartItems(cart.getCartItems().stream()
                    .map(item -> {
                        CartItem cartItem;
                        try {
                            cartItem = (CartItem) item.clone();
                        } catch (CloneNotSupportedException e) {
                            throw new RuntimeException();
                        }
                        return cartItem;
                    }).collect(Collectors.toList()));
            order.setSubTotal(cart.getTotalCost());
            order.setDeliveryCost(calculateDeliveryCost(order.getSubTotal()));
            order.setTotalCost(order.getDeliveryCost().add(order.getSubTotal()));
            return order;
        }
    }

    @Override
    public List<PaymentMethod> getPaymentMethods() {
        return List.of(PaymentMethod.values());
    }

    @Override
    public void saveOrder(Order order) {
        synchronized (lock) {
            if (order == null) {
                return;
            }
            order.setSecureId(UUID.randomUUID().toString());
            orderDao.save(order);
        }
    }

    @Override
    public Order getOrderById(Long id) throws NoSuchElementException {
        return orderDao.getValue(id);
    }

    private BigDecimal calculateDeliveryCost(BigDecimal subtotal) {
        if (subtotal.compareTo(BigDecimal.valueOf(100)) > 0) {
            return BigDecimal.ZERO;
        } else {
            return BigDecimal.valueOf(15);
        }
    }
}
