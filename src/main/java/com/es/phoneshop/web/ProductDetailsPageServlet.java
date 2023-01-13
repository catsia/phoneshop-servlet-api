package com.es.phoneshop.web;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartService;
import com.es.phoneshop.model.cart.CartServiceImp;
import com.es.phoneshop.model.cart.OutOfStockException;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.RecentlyViewedProducts;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.NoSuchElementException;

public class ProductDetailsPageServlet extends HttpServlet {
    private ProductDao productDao;

    private CartService cartService;

    private RecentlyViewedProducts recentlyViewedProducts;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
        cartService = CartServiceImp.getInstance();
        recentlyViewedProducts = RecentlyViewedProducts.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Long productId = Long.valueOf(request.getPathInfo().substring(1));
        request.setAttribute("product", productDao.getProduct(productId));
        request.setAttribute("cart", cartService.getCart(request).toString());
        recentlyViewedProducts.addViewedProduct(recentlyViewedProducts.getRecentlyViewedProducts(request), productId);
        request.getRequestDispatcher("/WEB-INF/pages/productDetails.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int quantity;
        String message = "";

        try {
            quantity = NumberFormat
                    .getInstance(request.getLocale())
                    .parse(request.getParameter("quantity"))
                    .intValue();
        } catch (ParseException e) {
            request.setAttribute("error", "Not a number");
            doGet(request, response);
            return;
        }
        try {
            Cart cart = cartService.getCart(request);
            cartService.add(cart, Long.valueOf(request.getPathInfo().substring(1)), quantity);
            message = "Product added to the cart";
        } catch (OutOfStockException e) {
            request.setAttribute("error", "Out of stock, only " + e.getStock() + " left");
            doGet(request, response);
            return;
        } catch (NoSuchElementException exception) {
            response.setStatus(404);
        }

        response.sendRedirect(request.getContextPath() + "/products/" + request.getPathInfo().substring(1) + "?message=" + message);
    }
}
