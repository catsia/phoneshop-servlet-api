package com.es.phoneshop.model.product;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RecentlyViewedProducts {

    private static RecentlyViewedProducts instance;

    private static final String RECENTLY_VIEWED_PRODUCTS_ATTRIBUTE = RecentlyViewedProducts.class.getName();

    public static RecentlyViewedProducts getInstance() {
        if (instance == null) {
            instance = new RecentlyViewedProducts();
        }
        return instance;
    }

    public List<Product> getRecentlyViewedProducts(HttpServletRequest request) {
        synchronized (request.getSession()) {
            List<Product> recentlyViewed = (List<Product>) request.getSession().getAttribute(RECENTLY_VIEWED_PRODUCTS_ATTRIBUTE);
            if (recentlyViewed == null) {
                recentlyViewed = new ArrayList<>(3);
                request.getSession().setAttribute(RECENTLY_VIEWED_PRODUCTS_ATTRIBUTE, recentlyViewed);
            }
            return recentlyViewed;
        }
    }


    public void addViewedProduct(List<Product> recentlyViewedProducts, Long productId) {
        ProductDao productDao = ArrayListProductDao.getInstance();
        Optional<Product> product = recentlyViewedProducts.stream().filter(products -> products.getId().equals(productId)).findAny();
        if (product.isPresent()) {
            return;
        }
        if (recentlyViewedProducts.size() == 3) {
            recentlyViewedProducts.remove(0);
        }
        recentlyViewedProducts.add(productDao.getProduct(productId));
    }


}
