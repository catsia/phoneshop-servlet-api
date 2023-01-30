package com.es.phoneshop.web.pages;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.HttpSessionCartService;
import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
import com.es.phoneshop.model.order.OrderService;
import com.es.phoneshop.model.order.PaymentMethod;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class CheckoutPageServlet extends HttpServlet {
    private CartService cartService;

    private OrderService orderService;

    private Order order;

    private Map<String, String> errors;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = HttpSessionCartService.getInstance();
        orderService = DefaultOrderService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cart cart = cartService.getCart(request);
        if (order == null) {
            order = orderService.getOrder(cart);
        }
        request.setAttribute("errors", errors);
        request.setAttribute("order", order);
        request.setAttribute("paymentMethods", orderService.getPaymentMethods());
        request.getRequestDispatcher("/WEB-INF/pages/checkout.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        errors = new HashMap<>();
        Cart cart = cartService.getCart(request);
        if (cart.getCartItems().isEmpty()) {
            errors.put("cart", "Your cart is empty");
        }
        order = orderService.getOrder(cart);
        setRequiredAttribute(request, "firstName", order::setFirstName, null);
        setRequiredAttribute(request, "lastName", order::setLastName, null);
        setRequiredAttribute(request, "phoneNumber", order::setPhoneNumber, "^\\+\\d+$");
        setRequiredAttribute(request, "address", order::setAddress, null);
        setOrderDate(request, order);
        order.setPaymentMethod(PaymentMethod.valueOf(request.getParameter("paymentMethods")));


        if (errors.isEmpty()) {
            orderService.saveOrder(order);
            cartService.deleteAll(request);
            response.sendRedirect(request.getContextPath() + "/order/overview/" + order.getSecureId());

        } else {
            doGet(request, response);
        }
    }

    private void setRequiredAttribute(HttpServletRequest request, String parameter, Consumer<String> consumer, String regex) {
        String value = request.getParameter(parameter);
        if (value == null || value.isEmpty()) {
            errors.put(parameter, "This field is required");
            return;
        }
        if (regex == null) {
            consumer.accept(value);
        } else if (!value.matches(regex)) {
            errors.put(parameter, "Error in your input");
        } else {
            consumer.accept(value);
        }
    }

    private void setOrderDate(HttpServletRequest request, Order order){
        String orderDate = request.getParameter("orderDate");
        if (orderDate == null || orderDate.isEmpty()) {
            errors.put("orderDate", "This field is required");
            return;
        }
        try {
            order.setOrderDate(new SimpleDateFormat("yyyy-MM-dd").parse(request.getParameter("orderDate")));
        } catch (ParseException e) {
            errors.put("orderDate", "Error in your input");
        }
    }
}
