<%-- 
    Document   : viewCart
    Created on : Oct 8, 2020, 12:33:12 PM
    Author     : DELL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>View cart Page</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" crossorigin="anonymous">
    </head>
    <body>
        <c:set var="User" value="${sessionScope.USER}"/>
        <c:if test="${not empty User}">
            <nav class="navbar navbar-light bg-light">
                <a href="userHome" style="text-decoration: none;" class="navbar-brand">Hello ${User.name}</a>
                <a class="navbar-brand" href="logout" style="text-decoration: none;">Log out</a>
            </nav>
        </c:if>
        <c:if test="${empty User}">
            <nav class="navbar navbar-light bg-light">
                <a href="userHome" style="text-decoration: none;" class="navbar-brand">Hello Guest !</a>
                <a class="navbar-brand" href="letLogin" style="text-decoration: none;">Log in</a>
            </nav>
        </c:if>

        <br>
        <c:set var="cart" value="${sessionScope.CART}"/>
        <c:if test="${empty cart}">
            <h2>Your cart is empty ha ha ha</h2>
        </c:if>
        <c:if test="${not empty cart}">
            <c:set var="items" value="${cart.items}"/>
            <c:if test="${empty items}">
                <h2>Your cart is empty hi hi hi</h2>
            </c:if>
            <c:if test="${not empty items}">
                <c:set var="productList" value="${requestScope.productInCart}"/>
                <c:if test="${empty productList}">
                    Sorry man all of this product you chosen is not available now . Please choose again!
                </c:if>
                <c:if test="${not empty productList}">
                    <!--Delete Cake Confirmation Modal -->
                    <div class="modal" id="DeleteConfirmationModal">
                        <div class="modal-dialog">
                            <div class="modal-content">

                                <!-- Modal Header -->
                                <div class="modal-header">
                                    <h4 class="modal-title">Are you sure?</h4>
                                    <button type="button" class="close" data-dismiss="modal">&times;</button>
                                </div>
                                <form action="remove">
                                    <div class="modal-body">
                                        <input type="hidden" name="productID" value="" id="confirmProductID" />
                                    </div>
                                    <!-- Modal footer -->
                                    <div class="modal-footer">
                                        <button type="button" class="btn btn-danger" data-dismiss="modal">Close</button>
                                        <input type="submit" class="btn btn-primary" name="btnAction" value="Delete this cake"/>
                                    </div>
                                </form>
                            </div>
                        </div>
                    </div>
                    <div class="container">
                        <div class="row">
                            <div class="col-sm-12 col-md-10 col-md-offset-1">
                                <table class="table table-hover">
                                    <thead>
                                        <tr>
                                            <th>Cake</th>
                                            <th>Quantity</th>
                                            <th class="text-center">Price</th>
                                            <th class="text-center">Total</th>
                                            <th> </th>
                                            <th> </th>
                                        </tr>
                                    </thead>
                                    <tbody>
                                        <c:set var="total" value="0"/>
                                        <c:forEach var="product" items="${productList}" varStatus="counter">
                                            <tr>
                                        <form action="updateQuantity">
                                                <td class="col-sm-8 col-md-6">
                                                    <div class="media">
                                                        <a class="thumbnail pull-left" href="#"> <img class="media-object" src="${product.image}" style="width: 72px; height: 72px;"> </a>
                                                        <div class="media-body">
                                                            <h4 class="media-heading"><a href="#">${product.productName}</a></h4>
                                                        </div>
                                                    </div>
                                                </td>

                                                <td class="col-sm-1 col-md-1" style="text-align: center">
                                                    <input type="hidden" name="productID" value="${product.productID}" />
                                                    <input type="number"  min="1" class="form-control"  name="txtCakeQuantity" value="${items[product.productID]}"/>                                                 
                                                </td>
                                                <td class="col-sm-1 col-md-1 text-center"><strong>${product.price}</strong></td>
                                                <td class="col-sm-1 col-md-1 text-center"><strong>${product.price * items[product.productID]}</strong></td>
                                                        <c:set var="total" value="${total + product.price * items[product.productID]}"/>
                                                <td class="col-sm-1 col-md-1">
                                                    <button type="button" class="btn btn-danger" onclick="getInfoToConFirm(${product.productID})" data-toggle="modal" data-target="#DeleteConfirmationModal">
                                                        <span class="glyphicon glyphicon-remove"></span> Remove
                                                    </button>
                                                </td>
                                                <td class="col-sm-1 col-md-1">
                                                    <input type="submit" class="btn btn-success" value="Update"/>
                                                </td>
                                        </form>
                                            </tr>
                                        </c:forEach>
                                        <tr>
                                            <td>   </td>
                                            <td>   </td>
                                            <td>   </td>
                                            <td><h3>Total</h3></td>
                                            <td class="text-right"><h3><strong>${total}</strong></h3></td>
                                        </tr>
                                        <tr>
                                            <td>   </td>
                                            <td>   </td>
                                            <td>   </td>
                                            <td>
                                                <button type="button" class="btn btn-secondary">
                                                    <a href="userHome" style="text-decoration: none; color: wheat">
                                                        <span class="glyphicon glyphicon-shopping-cart"></span> Continue Shopping
                                                    </a>
                                                </button>
                                            </td>
                                            <td>
                                                <button type="button" class="btn btn-success">
                                                    <span class="glyphicon glyphicon-play"><a href="CheckOutNow" style="text-decoration: none; color: white">Checkout</a></span>
                                                </button>
                                            </td>
                                        </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </c:if>
            </c:if>
        </c:if>
        <script>
            function getInfoToConFirm(productID) {
                var confirmProductID = document.getElementById("confirmProductID");
                confirmProductID.value = productID;
            }
        </script>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"  crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"  crossorigin="anonymous"></script>
    </body>
</html>
