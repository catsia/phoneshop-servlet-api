<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>

<head>
  <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<tags:master pageTitle="Checkout">
  <p>
    Welcome to Expert-Soft training!
  </p>
<c:if test = "${not empty errors['cart']}">
      <div class = "error">
         ${errors['cart']}
      </div>
  </c:if>
<c:if test = "${not empty errors and empty errors['cart']}">
      <div class = "error">
         Check your input
      </div>
  </c:if>
  <table>
    <thead>
      <tr>
        <td>Image</td>
        <td>Description</td>
        <td class="price">Price</td>
        <td>Quantity</td>

      </tr>
    </thead>
    <c:forEach var="cartItem" items="${order.cartItems}">

      <tr>
        <td>
          <img class="product-tile" src="${cartItem.product.imageUrl}">
        </td>
        <td>
           <a href="${pageContext.servletContext.contextPath}/products/${cartItem.product.id}">
              ${cartItem.product.description}
           </a>
        </td>
        <td class="price">


            <fmt:formatNumber value="${cartItem.product.price}" type="currency" currencySymbol="${cartItem.product.currency.symbol}"/>

        </td>
        <td>


             <fmt:formatNumber value="${cartItem.quantity}"/>

        </td>

      </tr>
    </c:forEach>
    <tr>
    <td></td>
    <td></td>
    <td></td>
    <td>
        Subtotal:
        ${order.subTotal}
    </td>

    </tr>

    <tr>
        <td></td>
        <td></td>
        <td></td>

        <td>
                Delivery cost:
                ${order.deliveryCost}
            </td>

        </tr>

        <tr>
            <td></td>
            <td></td>
            <td></td>

            <td>
                    Total cost:
                    ${order.totalCost}
                </td>
            </tr>
  </table>

  <form method="post">
       <table class ="table">
          <tbody>
             <tags:orderRowError name="firstName" label="First name" order="${order}" errors="${errors}"/>
             <tags:orderRowError name="lastName" label="Last name" order="${order}" errors="${errors}"/>
             <tags:orderRowError name="phoneNumber" label="Phone number" order="${order}" errors="${errors}" placeholder="+375xxxxxxxxxxx"/>
             <tags:orderRowError name="address" label="Address" order="${order}" errors="${errors}"/>
             <tags:orderRowError name="orderDate" label="Delivery date" order="${order}" errors="${errors}" isDate="true"/>

                <td>Payment method<span style="color:red">*</span></td>
                <td>
                   <select name="paymentMethods">

                      <c:forEach var="paymentMethod" items="${paymentMethods}">
                         <option>${paymentMethod}</option>
                      </c:forEach>
                   </select>
                   <c:if test="${not empty errors['paymentMethod']}">
                      <div class="error">
                         ${errors['paymentMethod']}
                      </div>
                   </c:if>
                </td>
             </tr>
          </tbody>
       </table>
    <button type="submit">Submit</button>
    </form>




   <tags:footer />

</tags:master>