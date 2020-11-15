<%-- 
    Document   : userHome
    Created on : Oct 4, 2020, 10:46:01 AM
    Author     : DELL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" crossorigin="anonymous">
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <title>Yellow Moon Shop</title>
    </head>
    <body>
        <c:set var="orderID" value="${requestScope.orderID}"/>
        <c:if test="${not empty orderID}">
            <script>
                console.log("${orderID}");
                alert("Your order is created successfully . Your order ID is : ${orderID} ");
            </script>
        </c:if>
        <c:set var="User" value="${sessionScope.USER}"/>
        <c:if test="${not empty User}">
            <nav class="navbar navbar-light bg-light">
                <a href="userHome" style="text-decoration: none;" class="navbar-brand">Hello ${User.name}</a>
                <a class="navbar-brand" href="viewCart" style="text-decoration: none;">View cart</a>
                <a class="navbar-brand" href="trackOrder" style="text-decoration: none;">Track Order</a>
                <a class="navbar-brand" href="logout" style="text-decoration: none;">Log out</a>
            </nav>
        </c:if>
        <c:if test="${empty User}">
            <nav class="navbar navbar-light bg-light">
                <a href="userHome" style="text-decoration: none;" class="navbar-brand">Hello Guest !</a>
                <a class="navbar-brand" href="viewCart" style="text-decoration: none;">View cart</a>
                <a class="navbar-brand"href="letLogin" style="text-decoration: none;">Log in</a>
            </nav>
        </c:if>
        <form class="form-inline my-2 my-lg-0" action="search" accept-charset="UTF-8">
            <input class="form-control mr-sm-2" type="text" name="txtSearch" placeholder="Your cake name..." value="${param.txtSearch}" />
            <input type="hidden" name="index" value="1"/>
            <c:set var="categoryList" value="${sessionScope.CATEGORY_LIST}"/>
            <c:if test="${not empty categoryList}">
                <select class="form-control mdb-select md-form colorful-select dropdown-primary" name="optionIndex" id="optionIndex">
                    <option value="">--Category--</option>
                    <c:forEach var="category" items="${categoryList}">
                        <option value="${category.categoryID}">${category.name}</option>
                    </c:forEach>
                    <script>
                        var optionIndex = document.getElementById("optionIndex");
                        <c:if test="${not empty param.optionIndex}">
                        optionIndex.value = ${param.optionIndex};
                        </c:if>

                    </script>
                </select>
            </c:if>
            <input class="form-control mr-sm-2 col-sm-2" type="text" name="txtMinPrice" placeholder="Minimum price" value="${param.txtMinPrice}" />
            <input class="form-control mr-sm-2 col-sm-2" type="text" name="txtMaxPrice" placeholder="Maximum price " value="${param.txtMaxPrice}" />
            <input class="btn btn-outline-success  my-2 my-sm-0" type="submit" name="btnAction" value="Search Cake Now!" />
        </form>
        <br>
        <br>
        <br>
        <c:if test="${not empty param.txtSearch or not empty param.optionIndex or not empty param.txtMinPrice or not empty param.txtMaxPrice}">
            <c:set var="searchList" value="${requestScope.searchList}"/>
            <c:if test="${not empty searchList}">
                <div class="container">
                    <div class="row"> 
                        <c:forEach var="product" items="${searchList}" varStatus="counter">
                            <div class="col-sm-12 col-md-6 col-xl-4">
                                <img src="${product.image}" alt="${product.productName}" class="img-thumbnail img-fluid" style="width: auto;height: 300px;" />
                                <div>
                                    <p>${product.productName}</p>
                                    <p>
                                        <strong>
                                            Category : ${product.category.name}
                                        </strong>
                                    </p>
                                    <p>
                                        <strong>
                                            Price : ${product.price} đ
                                        </strong>
                                    </p>
                                    <p>
                                        <strong>
                                            Quantity : ${product.quantity}
                                        </strong>
                                    </p>
                                    <p>
                                        <strong>
                                            Create date : ${product.createDate}
                                        </strong>
                                    </p>
                                    <p>
                                        <strong>
                                            Expiration date : ${product.exprirationDate}
                                        </strong>
                                    </p>
                                </div>
                                <input type="hidden" name="txtProductID" id="productID" value="${product.productID}" />
                                <button class="btn btn-primary" id="cart-btn" onclick="sendAddToCartRequest(${product.productID})">Add to cart</button>

                            </div>
                        </c:forEach>
                    </div>
                    <c:url var="previousPage" value="search">
                        <c:param name="txtSearch" value="${param.txtSearch}"/>
                        <c:param name="index" value="${param.index - 1}"/>
                        <c:param name="optionIndex" value="${param.optionIndex}"/>
                        <c:param name="txtMinPrice" value="${param.txtMinPrice}"/>
                        <c:param name="txtMaxPrice" value="${param.txtMaxPrice}"/>
                    </c:url>
                    <c:url var="nextPage" value="search">
                        <c:param name="txtSearch" value="${param.txtSearch}"/>
                        <c:param name="index" value="${param.index + 1}"/>
                        <c:param name="optionIndex" value="${param.optionIndex}"/>
                        <c:param name="txtMinPrice" value="${param.txtMinPrice}"/>
                        <c:param name="txtMaxPrice" value="${param.txtMaxPrice}"/>
                    </c:url>
                    <br>
                    <br>
                    <br>
                    <a href="${previousPage}" id="previousLink"><button class="btn btn-primary" id="previous">&laquo; Previous</button></a>
                    <a href="${nextPage}"><button class="btn btn-primary">Next &raquo;</button></a>
                    <script>
                        var previous = document.getElementById("previous");
                        var previousLink = document.getElementById("previousLink");
                        if (parseInt(${param.index}) <= 1) {
                            previous.setAttribute("disabled", true);
                            previousLink.href = "#";
                        }
                    </script>
                </div>
            </c:if>
            <c:if test="${empty searchList}">
                Sorry , We did not find more cake that you are looking for.
            </c:if>
        </c:if>
        <script>
            function sendAddToCartRequest(productID) {
                $.ajax({
                    url: "addToCart",
                    method: 'POST',
                    data: {txtProductID: productID},
                    success: function (data) {
                        console.log("Message:" + data);
                        alert("Message: " + data);
                    },
                    error: function (jqXHR, textStatus, errorThrown) {
                        alert("Some error occured");
                    }
                });
            }
        </script>     
        <script src="http://code.jquery.com/jquery-latest.min.js"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"  crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js" crossorigin="anonymous"></script>
    </body>
</html>
