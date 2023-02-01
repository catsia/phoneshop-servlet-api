package com.es.phoneshop.web.pages;

import com.es.phoneshop.model.order.DefaultOrderService;
import com.es.phoneshop.model.order.Order;
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
import java.io.IOException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class OrderOverviewServletTest {
    @Mock
    private HttpServletRequest request;
    @Mock
    private HttpServletResponse response;
    @Mock
    private RequestDispatcher requestDispatcher;
    @Mock
    private ServletConfig config;

    private OrderOverviewPageServlet servlet = new OrderOverviewPageServlet();

    @Before
    public void setup() throws ServletException {
        servlet.init(config);
        when(request.getRequestDispatcher(anyString())).thenReturn(requestDispatcher);

        when(request.getPathInfo()).thenReturn("/1");
        Order order = new Order();
        order.setId(1L);
        DefaultOrderService.getInstance().saveOrder(order);
    }

    @Test
    public void testDoGet() throws ServletException, IOException {
        servlet.doGet(request, response);

        verify(request).setAttribute(eq("order"), any());
        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/orderOverview.jsp"));
        verify(requestDispatcher).forward(request, response);
    }

    @Test
    public void testDoGetNonExistingOrder() throws ServletException, IOException {
        when(request.getPathInfo()).thenReturn("/2");

        servlet.doGet(request, response);

        verify(request).getRequestDispatcher(eq("/WEB-INF/pages/noOrderError.jsp"));
        verify(requestDispatcher).forward(request, response);
    }
}