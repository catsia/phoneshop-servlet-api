package com.es.phoneshop.model;

import com.es.phoneshop.model.product.Product;
import com.es.phoneshop.model.product.ProductDao;
import com.es.phoneshop.model.product.RecentlyViewedProducts;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class RecentlyViewedProductsTest {
    private RecentlyViewedProducts recentlyViewedProducts;

    private ProductDao productDao;

    private List<Product> products;

    private Product product;

    @Before
    public void setup() {
        recentlyViewedProducts = RecentlyViewedProducts.getInstance();
        products = new ArrayList<>(3);
        Currency currency = Currency.getInstance("USD");
        product = new Product(4L, "test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");
        products.add(product);
        productDao.save(product);
    }

    @Test
    public void testAddViewedProduct() {
        recentlyViewedProducts.addViewedProduct(products, 4L);
        assertEquals(1, products.size());
    }

    @Test
    public void testAddMoreThenThree() {
        Currency currency = Currency.getInstance("USD");
        products.add(new Product(2L, "test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.g"));
        products.add(new Product(3L, "test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.g"));

        recentlyViewedProducts.addViewedProduct(products, 4L);

        assertEquals(3, products.size());
    }


}
