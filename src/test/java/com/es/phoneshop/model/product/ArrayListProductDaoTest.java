package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Currency;
import java.util.List;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;

public class ArrayListProductDaoTest {
    private ProductDao productDao;
    Product product;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
        Currency currency = Currency.getInstance("USD");
        product = new Product("test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");
    }

    @Test
    public void testGetExistingProducts() {
        productDao.save(product);
        assertEquals("test", productDao.getProduct(product.getId()).getCode());
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetNonExistingProducts() {
        productDao.save(product);
        productDao.getProduct(product.getId() + 1);
    }

    @Test(expected = NoSuchElementException.class)
    public void testGetProductNullId() {
        productDao.getProduct(null);
    }

    @Test
    public void testFindProductsHasResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }

    @Test
    public void testFindProductsReturnsNotNullPrice() {
        List<Product> products = productDao.findProducts();
        assertTrue(products.stream().allMatch(product -> product.getPrice() != null && product.getStock() > 0));
    }

    @Test
    public void testSaveExistingProduct() {
        productDao.save(product);
        product.setCode("test-product");
        productDao.save(product);
        assertEquals("test-product", productDao.getProduct(product.getId()).getCode());
    }

    @Test
    public void testSaveNewProduct() {
        productDao.save(product);
        assertEquals(product.getCode(), productDao.getProduct(product.getId()).getCode());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSaveNullProduct() {
        productDao.save(null);
    }

    @Test(expected = NoSuchElementException.class)
    public void testDeleteExistingProduct() {
        productDao.save(product);
        productDao.delete(product.getId());
        productDao.getProduct(product.getId());
    }

    @Test
    public void testDeleteNonExistingProduct() {
        List<Product> products = productDao.findProducts();
        productDao.delete(Long.MAX_VALUE);
        productDao.delete(Long.MIN_VALUE);
        assertEquals(products, productDao.findProducts());
    }

    @Test
    public void testDeleteProductNullId() {
        List<Product> products = productDao.findProducts();
        productDao.delete(null);
        assertEquals(products, productDao.findProducts());
    }
}
