<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<head>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<tags:master pageTitle="Product List">
  <p>
    Welcome to Expert-Soft training!
  </p>
    <c:if test = "${not empty errors}">
      <div class = "error">
          Error in your input
      </div>
    </c:if>
  <form>
    <div>
    Description
    <input name = "query" value = "${param.query}">
    <select name="searchCriteria">
        <c:forEach var="searchCriteria" items="${searchCriteria}">
             <option>${searchCriteria}</option>
        </c:forEach>
    </select>
    </div>
    <div>
    Min price
    <input name = "minPrice">
    <c:if test="${not empty errors['minPrice']}">
        <div class = "error">
            ${errors['minPrice']}
            </div>
           </c:if>
    </div>
    <div>
    Max price
     <input name = "maxPrice">
       <c:if test="${not empty errors['maxPrice']}">
       <div class = "error">
        ${errors['maxPrice']}
        </div>
       </c:if>
     </div>
     <input name="isPressed" type="hidden" value="true">
    <button>Search</button>
  </form>
  <c:if test="${not empty products}">
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>Description</td>
        <td class="price">Price</td>
      </tr>
    </thead>
    <c:forEach var="product" items="${products}">
      <tr>
        <td>
          <img class="product-tile" src="${product.imageUrl}">
        </td>
        <td>
           <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
              ${product.description}
           </a>
        </td>
        <td class="price">
           <a href="#see/${product.id}">
            <fmt:formatNumber value="${product.price}" type="currency" currencySymbol="${product.currency.symbol}"/>
          </a>
      </tr>
    </c:forEach>
  </table>
  </c:if>


   <tags:footer />

</tags:master>