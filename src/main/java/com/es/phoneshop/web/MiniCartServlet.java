package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.HttpSessionCartService;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class MiniCartServlet extends HttpServlet {
    private CartService cartService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        cartService = HttpSessionCartService.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        request.setAttribute("totalQuantity", cartService.getCart(request).getTotalQuantity());
        request.setAttribute("totalCost", cartService.getCart(request).getTotalCost() != null ? cartService.getCart(request).getTotalCost() : 0);
        request.getRequestDispatcher("/WEB-INF/pages/miniCart.jsp").include(request, response);
    }
}
