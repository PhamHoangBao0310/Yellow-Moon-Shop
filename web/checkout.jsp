<%-- 
    Document   : checkout
    Created on : Oct 11, 2020, 10:31:03 AM
    Author     : DELL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" crossorigin="anonymous">
        <title>Check Out</title>
    </head>
    <body>
        <c:set var="User" value="${sessionScope.USER}"/>
        <c:if test="${not empty User}">
            <nav class="navbar navbar-light bg-light">
                <a href="userHome" style="text-decoration: none;" class="navbar-brand">Hello ${User.name}</a>
                <a class="navbar-brand" href="viewCart" style="text-decoration: none;">View cart</a>
                <a class="navbar-brand" href="logout" style="text-decoration: none;">Log out</a>
            </nav>
        </c:if>
        <c:if test="${empty User}">
            <nav class="navbar navbar-light bg-light">
                <a href="userHome" style="text-decoration: none;" class="navbar-brand">Hello Guest !</a>
                <a class="navbar-brand" href="viewCart" style="text-decoration: none;">View cart</a>
                <a class="navbar-brand" href="letLogin" style="text-decoration: none;">Log in</a>
            </nav>
        </c:if>


        <c:set var="cart" value="${sessionScope.CART}"/>
        <c:if test="${empty cart}">
            <h2>Hey man you did not buy anything</h2>
        </c:if>
        <c:if test="${not empty cart}">
            <c:set var="items" value="${cart.items}"/> 
            <c:if test="${empty items}">
                <h2>Hey man you did not buy anything or some of product is deleted while you are shopping and choose it.</h2>
            </c:if>
            <c:if test="${not empty items}">
                <c:set var="productList" value="${requestScope.productForCheckOut}"/>
                <c:if test="${empty productList}">
                    Sorry man all of this product you chosen is not available now . Please choose again!
                </c:if>
                <c:if test="${not empty productList}">
                    <c:set var="errors" value="${requestScope.error}"/>
                    <c:set var="productIdError" value="${requestScope.productList_ID_Error}"/>
                    <div class="container">
                        <div class="row">
                            <div class="col-sm-12 col-md-10 col-md-offset-1">
                                <h1>Cake Invoice</h1>
                                <c:set var="total" value="${0}"/>
                                <c:if test="${not empty errors.nullObjectError}">
                                    <div class="alert alert-warning">
                                        <strong>Hey!</strong> ${errors.nullObjectError}
                                    </div>
                                </c:if>
                                <table border="1" class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>No.</th>
                                            <th>Cake Name</th>
                                            <th>Unit Price</th>
                                            <th>Quantity</th>
                                            <th>Line Total</th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:forEach var="item" items="${productList}" varStatus="counter">
                                            <tr>
                                                <td>
                                                    ${counter.count}
                                                </td>
                                                <td>
                                                    ${item.productName}
                                                    <c:forEach var="errodID" items="${productIdError}">
                                                        <c:if test="${errodID eq item.productID }">
                                                            <br>
                                                            <div class="alert alert-warning">
                                                                <strong>We are sorry!</strong> The quantity is out of stock , Please check your cart again!
                                                            </div>
                                                        </c:if>
                                                    </c:forEach>
                                                </td>
                                                <td>
                                                    ${item.price}
                                                </td>
                                                <td>
                                                    ${items[item.productID]}
                                                </td>
                                                <td>
                                                    ${item.price * items[item.productID]}
                                                    <c:set var="total" value="${total + item.price * items[item.productID]}"/>
                                                </td>
                                            </tr>
                                        </c:forEach>                           
                                    </tbody>
                                </table>
                                <p>----------------------------------------------------------------------------------</p>
                                <h3>Total : ${total}</h3>
                                <h4>Please fill in the form to create order</h4>
                                <form action="CheckOutNow" method="post"> 
                                    <div class="form-group">
                                        <label for="txtUserName">Name</label>
                                        <input type="text" class="form-control" id="txtUserName" name="txtUserName" value="${User.name}" />  
                                    </div>
                                    <c:if test="${not empty errors.nameError}">
                                        <div class="alert alert-warning">
                                            <strong>Hey!</strong> ${errors.nameError}
                                        </div>
                                    </c:if>
                                    <div class="form-group">
                                        <label for="txtPhone">Phone</label>
                                        <input type="text" class="form-control" id="txtPhone" name="txtPhone" value="${User.phoneNumber}" />  
                                    </div>
                                    <c:if test="${not empty errors.phoneError}">
                                        <div class="alert alert-warning">
                                            <strong>Hey!</strong> ${errors.phoneError}
                                        </div>
                                    </c:if>
                                    <div class="form-group">
                                        <label for="txtAddress">Address</label>
                                        <input type="text" class="form-control" id="txtAddress" name="txtAddress" value="${User.address}" />  
                                    </div>
                                    <c:if test="${not empty errors.addressError}">
                                        <div class="alert alert-warning">
                                            <strong>Hey!</strong> ${errors.addressError}
                                        </div>
                                    </c:if>
                                    <div class="form-group">
                                        <label for="optionIndex">Payment method</label>
                                        <c:set var="paymentList" value="${sessionScope.PAYMENT_LIST}"/>
                                        <c:if test="${not empty paymentList}">
                                            <select class="form-control mdb-select md-form colorful-select dropdown-primary" name="paymentIndex" id="paymentIndex">
                                                <c:forEach var="payment" items="${paymentList}">
                                                    <c:if test="${payment.name  ne 'Paypal'}">
                                                        <option value="${payment.paymentID}">${payment.name}</option>
                                                    </c:if>  
                                                </c:forEach>
                                                <script>
                                                    var optionIndex = document.getElementById("paymentIndex");
                                                    <c:if test="${not empty param.paymentIndex}">
                                                    optionIndex.value = ${param.paymentIndex};
                                                    </c:if>
                                                </script>
                                            </select>
                                        </c:if> 
                                    </div>
                                    <button type="button" class="btn btn-success">
                                        <span class="glyphicon glyphicon-play"><a href="authorize_payment" style="text-decoration: none; color: white">Check out with Paypal</a></span>
                                    </button>
                                    <input type="submit" value="Check out" class="btn btn-primary"/>
                                </form>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:if>
        </c:if>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"  crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"  crossorigin="anonymous"></script>
    </body>
</html>
