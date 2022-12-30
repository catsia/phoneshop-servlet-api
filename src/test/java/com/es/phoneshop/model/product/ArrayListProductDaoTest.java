package com.es.phoneshop.model.product;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;


import java.math.BigDecimal;
import java.util.Currency;
import java.util.NoSuchElementException;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;

public class ArrayListProductDaoTest
{
    private ProductDao productDao;

    @Before
    public void setup() {
        productDao = new ArrayListProductDao();
    }
    @Test
    public void testFindProducts(){
        ProductDao productDaoMock = Mockito.spy(ProductDao.class);
        productDaoMock.findProducts();
        productDaoMock.findProducts();
        Mockito.verify(productDaoMock, times(2)).findProducts();
        Mockito.verifyNoMoreInteractions(productDaoMock);
    }
    @Test
    public void testFindProductsHasResults() {
        assertFalse(productDao.findProducts().isEmpty());
    }
    @Test
    public void testSaveExistingProduct(){
        Currency currency = Currency.getInstance("USD");
        Product product = new Product("test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");
        productDao.save(product);
        product.setCode("test-product");
        productDao.save(product);
        assertEquals("test-product", productDao.getProduct(product.getId()).getCode());
    }
    @Test
    public void testSaveProduct(){
        Currency currency = Currency.getInstance("USD");
        Product product = new Product("test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");
        productDao.save(product);
        assertEquals(product.getCode(), productDao.getProduct(product.getId()).getCode());
    }

    @Test
    public void testDeleteProduct(){
        Currency currency = Currency.getInstance("USD");
        Product product = new Product("test", "HTC EVO Shift 4G", new BigDecimal(320), currency, 3, "https://raw.githubusercontent.com/andrewosipenko/phoneshop-ext-images/master/manufacturer/HTC/HTC%20EVO%20Shift%204G.jpg");
        productDao.save(product);
        productDao.delete(product.getId());
        assertThrows(NoSuchElementException.class, () ->{
           productDao.getProduct(product.getId());
        });
    }
}
