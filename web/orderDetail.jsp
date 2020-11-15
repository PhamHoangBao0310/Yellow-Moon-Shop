<%-- 
    Document   : orderDetail
    Created on : Oct 11, 2020, 1:12:49 PM
    Author     : DELL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Order Tracking</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" crossorigin="anonymous">
    </head>
    <body>
        <c:set var="User" value="${sessionScope.USER}"/>
        
        <nav class="navbar navbar-light bg-light">
            <a href="userHome" style="text-decoration: none;" class="navbar-brand">Hello ${User.name}</a>
            <a class="navbar-brand" href="viewCart" style="text-decoration: none;">View cart</a>
            <a class="navbar-brand" href="trackOrder" style="text-decoration: none;">Track Order</a>
            <a class="navbar-brand" href="logout" style="text-decoration: none;">Log out</a>
        </nav>

        <br>
        <form class="form-inline my-2 my-lg-0" action="searchOrder" accept-charset="UTF-8">
            <input class="form-control mr-sm-5 col-5" type="text" name="txtOrderID" placeholder="Enter your order ID you want to search" value="${param.txtOrderID}" />
            <input class="btn btn-outline-success  my-2 my-sm-0" type="submit" name="btnAction" value="Search Now!" />
        </form>

        <c:if test="${not empty param.txtOrderID}">
            <c:set var="order" value="${requestScope.order}"/>
            <c:if test="${empty order}">
                No order with ID : ${param.txtOrderID}            
            </c:if>
            <c:if test="${not empty order}">
                <c:set var="detailList" value="${requestScope.orderDetails}"/>
                <br>
                <br>
                <div class="container">
                    <div class="row">
                        <div class="col-sm-12 col-md-10 col-md-offset-1">
                            <h3>Cake Invoice ID : ${param.txtOrderID}</h3>
                            <h4>On ${order.createDate}</h4>
                            <br>
                            <br>
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
                                    <c:forEach var="detail" items="${detailList}" varStatus="counter">
                                        <tr>
                                            <td>
                                                ${counter.count}
                                            </td>
                                            <td>
                                                ${detail.productName}
                                            </td>
                                            <td>
                                                ${detail.price}
                                            </td>
                                            <td>
                                                ${detail.quantity}
                                            </td>
                                            <td>
                                                ${detail.price * detail.quantity}
                                            </td>
                                        </tr>
                                    </c:forEach>
                                </tbody>
                            </table>
                            <p>----------------------------------------------------------------------------------</p>
                            <h3>Total : ${order.total}</h3>
                            <h2>User Information</h2>
                            <form>
                                <div class="form-group">
                                    <label for="txtUserName">Name</label>
                                    <input type="text" class="form-control" id="txtUserName" value="${order.customerName}" /> 
                                </div>
                                <div class="form-group">
                                    <label for="txtPhone">Phone</label>
                                    <input type="text" class="form-control" id="txtPhone" value="${order.phone}" />
                                </div>
                                <div class="form-group">
                                    <label for="txtAddress">Address</label>
                                    <input type="text" class="form-control" id="txtAddress" value="${order.customerAddress}" /> 
                                </div>
                                <div class="form-group">
                                    <label for="txtPayment">Payment Method</label>
                                    <c:set var="paymentMethod" value="${requestScope.paymentMethod}"/>
                                    <input type="text" class="form-control" id="txtPayment" value="${paymentMethod}" />
                                </div>
                                <div class="form-group">
                                    <label for="txtStatus">Status</label>
                                    <c:if test="${order.status}">
                                        <input type="text" class="form-control" id="txtStatus" value="Delivered" />
                                    </c:if>
                                    <c:if test="${not order.status}">
                                        <input type="text" class="form-control" id="txtStatus" value="On delivery" />
                                    </c:if>
                                </div>
                            </form>
                        </div> 
                    </div>
                </div>
            </c:if>
        </c:if>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"  crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"  crossorigin="anonymous"></script>
    </body>
</html>
