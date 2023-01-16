<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="viewedProducts" required="true" type="com.es.phoneshop.model.product.Product"%>



<html>


    <div >
            <div id = "viewedProducts">
                <div>
                <img class="product-tile" src="${viewedProducts.imageUrl}">
                </div>
                <a href="${pageContext.servletContext.contextPath}/products/${viewedProducts.id}">
                              ${viewedProducts.description}
                </a>
                <a href="#see/${viewedProducts.id}">

                    <fmt:formatNumber value="${viewedProducts.price}" type="currency" currencySymbol="${viewedProducts.currency.symbol}"/>
                </a>
            </div>
    </div>
</html>