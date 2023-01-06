<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<%@ page isErrorPage="true" %>

<tags:master pageTitle="Product not found">
  <a href="#" id="main">
                          <div id="popup">
                            <h2>Price history</h2>
                            ${product.description}
                            <c:forEach var="price" items="${product.priceHistory}">
                               ${price.key} ${price.value}
                            </c:forEach>
                          </div>
                 </a>
  <footer>
    <p>© 2023 Expert-Soft Lab</p>
  </footer>
</tags:master>