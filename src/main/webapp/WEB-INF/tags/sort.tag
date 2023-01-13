<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>

<a href = "?sort=${sort}&order=${order}&query=${param.query} ">
<c:if test="${sort eq param.sort and order eq param.order}">
    <img src="${ order eq 'asc' ? "images/asc-true.png" : "images/desc-true.png"}" width="15" height="15" >
</c:if>
<c:if test="${sort ne param.sort and order ne param.order}">
    <img src="${ order eq 'asc' ? "images/asc-false.png" : "images/desc-false.png"}" width="15" height="15" >
</c:if>
</a>
