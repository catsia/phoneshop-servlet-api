package com.es.phoneshop.web.pages;

import com.es.phoneshop.model.product.ArrayListProductDao;
import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.SearchCriteria;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.*;


public class AdvancedSearchPageServlet extends HttpServlet {
    private ProductDao productDao;

    private Map<String, String> errors;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        productDao = ArrayListProductDao.getInstance();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        errors = new HashMap<>();
        request.setAttribute("searchCriteria", SearchCriteria.values());
        String query = request.getParameter("query");
        SearchCriteria searchCriteria = Optional.ofNullable(request.getParameter("searchCriteria")).map(SearchCriteria::valueOf).orElse(null);
        BigDecimal minPrice = parsePrice(request, "minPrice");
        BigDecimal maxPrice = parsePrice(request, "maxPrice");
        boolean isPressed = Boolean.parseBoolean(request.getParameter("isPressed"));

        if (!errors.isEmpty()) {
            request.setAttribute("errors", errors);
        }
        List<Product> products = isPressed ? productDao.findProductsWithPriceRange(query, searchCriteria, minPrice, maxPrice) : new ArrayList<>();
        request.setAttribute("products", products);
        request.getRequestDispatcher("/WEB-INF/pages/advancedSearch.jsp").forward(request, response);
    }

    private BigDecimal parsePrice(HttpServletRequest request, String param) {
        BigDecimal value = null;
        if (request.getParameter(param) == null || request.getParameter(param).equals("")) {
            return value;
        }
        try {
            value = BigDecimal.valueOf((Long) DecimalFormat.getInstance().parse(request.getParameter(param)));
        } catch (ParseException e) {
            errors.put(param, "Not a number");
        }
        return value;
    }
}
