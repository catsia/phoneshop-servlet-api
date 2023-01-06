<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>

<a href = "?sort=${sort}&order=${order}&query=${param.query} ">
    <img src="${sort eq param.sort and order eq param.order ? "images/asc_true.png" : "images/asc_false.png"}" width="15" height="15" >
</a>
