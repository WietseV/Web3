<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <meta charset="UTF-8">
    <title>Update Product</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
<div id="container">
    <header>
        <h1><span>Web shop</span></h1>
        <nav>
            <ul>
                <li><a href="Controller">Home</a></li>
                <li><a href="Controller?action=personoverview">Users</a></li>
                <li><a href="Controller?action=productoverview">Products</a></li>
                <li id="actual"><a href="Controller?action=addproduct">Add Product</a></li>
                <li><a href="Controller?action=signUp">Sign up</a></li>
            </ul>
        </nav>
        <h2>
            Update Product
        </h2>

    </header>
    <main>
        <c:if test="${! Errors.isEmpty()}">
            <div class="alert-danger">
                <c:forEach var="error" items="${Errors}">
                    <ul>
                        <li>${error}</li>
                    </ul>
                </c:forEach>
            </div>
        </c:if>

        <form method="post" action="Controller?action=updateproductform&id=${id}" novalidate="novalidate">
            <!-- novalidate in order to be able to run tests correctly -->
            <p><label for="name">Name</label><input type="text" id="name" name="name"
                                                    required value="${name}"></p>
            <p><label for="description">Description</label><input type="text" id="description" name="description"
                                                                  required value="${description}"></p>
            <p><label for="price">Price</label><input type="double" id="price" name="price"
                                                      required value="${price}"></p>
            <p><input type="submit" id="updateProduct" value="Update Product"></p>

        </form>
    </main>
    <footer>
        &copy; Webontwikkeling 3, UC Leuven-Limburg
    </footer>
</div>
</body>
</html>
