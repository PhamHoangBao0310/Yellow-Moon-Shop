<%-- 
    Document   : createCake
    Created on : Oct 7, 2020, 10:06:20 AM
    Author     : DELL
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Create Cake</title>
        <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" crossorigin="anonymous">
    </head>
    <body>
        <c:set var="User" value="${sessionScope.USER}"/>
        <nav class="navbar navbar-light bg-light">
            <a href="adminHome" style="text-decoration: none;" class="navbar-brand">Hello admin ${User.name}</a>
            <a class="navbar-brand" href="logout" style="text-decoration: none;">Log out</a>
        </nav>
        <br>
        <br>
        <br>
        <div style="width: 90%; margin: 10% auto ;background: #f7f7f7; box-shadow: 0px 2px 2px rgba(0, 0, 0, 0.3);">
            <br>
            <c:set var="createError" value="${requestScope.createError}"/>
            <form action="createCake" method="POST" enctype="multipart/form-data">

                <div class="form-group row">
                    <label for="productName" class="col-2 col-form-label">Product Name</label>
                    <div class="col-10">
                        <input type="text"class="form-control" id="productName" name="txtProductName" value="${param.txtProductName}"/>
                    </div>
                    <c:if test="${not empty createError.nameError}">
                        <div class="col-10">
                            <font color="red">
                            ${createError.nameError}
                            </font>
                        </div>
                    </c:if>
                </div>

                <div class="form-group row">
                    <label for="price" class="col-2 col-form-label">Price</label>
                    <div class="col-10">
                        <input type="text" class="form-control" id="price" name="txtPrice" value="${param.txtPrice}"/>
                    </div>
                    <c:if test="${not empty createError.priceError}">
                        <div class="col-10">
                            <font color="red">
                            ${createError.priceError}
                            </font>
                        </div>
                    </c:if>
                </div>

                <div class="form-group row">
                    <label for="quantity" class="col-2 col-form-label">Quantity</label>
                    <div class="col-10">
                        <input type="number" class="form-control" id="quantity" name="txtQuantity" value="${param.txtQuantity}"/>
                    </div>  
                    <c:if test="${not empty createError.quantityError}">
                        <div class="col-10">
                            <font color="red">
                            ${createError.quantityError}
                            </font>
                        </div>
                    </c:if>
                </div>

                <div class="form-group row">
                    <label for="imageFile" class="col-2 col-form-label">Add image</label>
                    <input type="file" class="form-control-file" id="imageFile" accept="image/*" name="txtImage"
                           onchange ="loadImage(event)" style="width: 40%">   
                    <c:if test="${not empty createError.imageError}">
                        <div class="col-10">
                            <font color="red">
                            ${createError.imageError}
                            </font>
                        </div>
                    </c:if> 
                </div>

                <div class="form-group row">
                    <label for="image" class="col-2 col-form-label">Image</label>
                    <img id="dvPreview" src="${param.txtImage}" style="height: auto ; width: 40%"/>                    
                </div>

                <div class="form-group row">
                    <label for="CreationDate" class="col-2 col-form-label">Creation Date </label>
                    <div class="col-10">
                        <input type="date" class="form-control" id="CreationDate" name="txtCreationDate" value="${param.txtCreationDate}"/>
                    </div> 
                    <c:if test="${not empty createError.createDateError}">
                        <div class="col-10">
                            <font color="red">
                            ${createError.createDateError}
                            </font>
                        </div>
                    </c:if>                        
                </div>

                <div class="form-group row">
                    <label for="ExpirationDate" class="col-2 col-form-label">Expiration Date </label>
                    <div class="col-10">
                        <input type="date" class="form-control" id="ExpirationDate" name="txtExpirationDate" value="${param.txtExpirationDate}"/>
                    </div> 
                    <c:if test="${not empty createError.expirationDateError}">
                        <div class="col-10">
                            <font color="red">
                            ${createError.expirationDateError}
                            </font>
                        </div>
                    </c:if>    
                    <c:if test="${not empty createError.invalidDate}">
                        <div class="col-10">
                            <font color="red">
                            ${createError.invalidDate}
                            </font>
                        </div>
                    </c:if>  
                </div>
                <div class="form-group row">
                    <label for="Category" class="col-2 col-form-label">Category </label> 
                    <c:set var="categoryList" value="${sessionScope.CATEGORY_LIST}"/>
                    <c:if test="${not empty categoryList}">
                        <div class="col-10">
                            <select class="form-control mdb-select md-form colorful-select dropdown-primary" id="categoryID" name="optionIndex">
                                <c:forEach var="category" items="${categoryList}">
                                    <option value="${category.categoryID}">${category.name}</option>
                                </c:forEach>
                            </select>   
                        </div>
                    </c:if>
                </div>

                <script>
                    var categoryID = document.getElementById("categoryID");
                    categoryID.value = ${param.optionIndex};
                </script>

                <br>
                <br>
                <input type="submit" name="edit" value="Create new Cake" class="btn btn-primary" />
                <a class="btn btn-secondary" href="adminHome" style="text-decoration: none;" >Back to home page</a>
            </form>
        </div>
        <script>
            var loadImage = function (event) { // This function is used to load new image to preview
                var dvPreview = document.getElementById("dvPreview");
                dvPreview.src = URL.createObjectURL(event.target.files[0]);
            };
        </script>
        <script src="https://code.jquery.com/jquery-3.5.1.slim.min.js" crossorigin="anonymous"></script>
        <script src="https://cdn.jsdelivr.net/npm/popper.js@1.16.1/dist/umd/popper.min.js"  crossorigin="anonymous"></script>
        <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"  crossorigin="anonymous"></script>
    </body>
</html>
