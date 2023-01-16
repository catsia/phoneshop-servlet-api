<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="viewedProducts" required="true" type="com.es.phoneshop.model.product.Product"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html>


    <div >
            <div id = "viewedProducts">
                <div>
                <img class="product-tile" src="${viewedProducts.imageUrl}">
                </div>
                <a href="${pageContext.servletContext.contextPath}/products/${viewedProducts.id}">
                              ${viewedProducts.description}
                </a>
                <div>
                    <fmt:formatNumber value="${viewedProducts.price}" type="currency" currencySymbol="${viewedProducts.currency.symbol}"/>
                </div>
            </div>
    </div>
</html>