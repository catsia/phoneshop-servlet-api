package com.es.phoneshop.web.filters;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class DosFilterTest {
    private DosFilter dosFilter = new DosFilter();

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private FilterConfig filterConfig;

    @Before
    public void setup() throws ServletException, IOException {
        Mockito.doNothing().when(filterChain).doFilter(eq(request), eq(response));
        when(request.getRemoteAddr()).thenReturn("1");
        dosFilter.init(filterConfig);
    }

    @Test
    public void testDoFilter() throws ServletException, IOException {
        dosFilter.doFilter(request, response, filterChain);
        verify(filterChain).doFilter(eq(request), eq(response));
    }
}