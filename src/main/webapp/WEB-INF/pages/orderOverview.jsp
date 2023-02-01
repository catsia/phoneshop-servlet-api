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
      <div class = "errors">
         ${errors['cart']}
      </div>
  </c:if>
<c:if test = "${not empty errors and empty errors['cart']}">
      <div class = "errors">
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

       <table class ="table">
          <tbody>
             <tags:orderRow name="firstName" label="First name" order="${order}"/>
             <tags:orderRow name="lastName" label="Last name" order="${order}" />
             <tags:orderRow name="phoneNumber" label="Phone number" order="${order}" />
             <tags:orderRow name="address" label="Address" order="${order}" />
              <tags:orderRow name="orderDate" label="Order date" order="${order}" />

             <tr>
                <td>Payment method</td>
                <td>



                         <option>${order.paymentMethod}</option>



                </td>
             </tr>
          </tbody>
       </table>

   <tags:footer />

</tags:master>