package com.es.phoneshop.web.pages;

import com.es.phoneshop.model.cart.Cart;
import com.es.phoneshop.model.cart.CartItem;
import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.Currency;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class CheckoutPageServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;

    @Mock
    private HttpSession session;

    private CheckoutPageServlet servlet = new CheckoutPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);
        when(request.getSession()).thenReturn(session);

        when(request.getParameter("firstName")).thenReturn("firstName");
        when(request.getParameter("lastName")).thenReturn("lastName");
        when(request.getParameter("phoneNumber")).thenReturn("+375293784784");
        when(request.getParameter("address")).thenReturn("address");
        when(request.getParameter("orderDate")).thenReturn("2023-11-11");
        when(request.getParameter("paymentMethods")).thenReturn("CASH");

        Currency currency = Currency.getInstance("USD");
        Product product = new Product(1L, "test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");
        ArrayListProductDao.getInstance().save(product);
        Cart cart = new Cart();
        cart.getCartItems().add(new CartItem(product, 1));

        when(session.getAttribute(anyString())).thenReturn(cart);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/checkout.jsp"));
        verify(requestDispatcher).forward(request, response);
        verify(request).setAttribute(eq("order"), any());
        verify(request).setAttribute(eq("errors"), any());
    }

    @Test
    public void testDoPost() throws ServletException, IOException {
        servlet.doPost(request, response);

        verify(response).sendRedirect(anyString());
    }

    @Test
    public void testDoPostWithoutAllAttributes() throws IOException, ServletException {
        when(request.getParameter("address")).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/checkout.jsp"));
        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostWithWrongNumber() throws IOException, ServletException {
        when(request.getParameter("phoneNumber")).thenReturn("29123");

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/checkout.jsp"));
        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }

    @Test
    public void testDoPostWithEmptyCart() throws IOException, ServletException {
        when(session.getAttribute(anyString())).thenReturn(null);

        servlet.doPost(request, response);

        verify(request).setAttribute(eq("errors"), any());
        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/checkout.jsp"));
        verify(requestDispatcher).forward(request, response);
        verify(response, never()).sendRedirect(anyString());
    }
}