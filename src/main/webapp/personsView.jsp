<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Persons</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

    <style>
        .table-wrapper {
            height: 100vh;
            padding: 1%;
            display: flex;
            align-items: center;
            flex-direction: column;
        }
    </style>
</head>
    <body>
        <div style="display: flex; padding: 1%">
            <h1>Initialize Example Data:</h1>
            <button class="btn btn-warning" style="margin-left: 20px" onclick="window.location.href = '/initialize'">
                <h2>Initialize</h2>
            </button>
        </div>

        <div class="table-wrapper">
            <table class="table table-striped">
                <tr>
                    <th>Id</th>
                    <th>First Name</th>
                    <th>Last Name</th>
                    <th>E-Mail</th>
                    <th>Birthday</th>
                    <th>Age</th>
                </tr>
                <c:forEach var="person" items="${ persons }">
                    <tr>
                        <td>${person.id}</td>
                        <td>${person.firstName}</td>
                        <td>${person.lastName}</td>
                        <td>${person.email}</td>
                        <td>${person.birthday}</td>
                        <td>${person.age}</td>
                    </tr>
                </c:forEach>
            </table>

            <div style="display: flex;">
                <h3>Pagination:</h3>
                <nav aria-label="Page navigation example" style="margin-left: 20px">
                    <ul class="pagination">
                        <c:forEach var="page" items="${ pages }">
                            <li class="page-item"><a class="page-link" href="/?page=${ page }&pageSize=${ pageSize }">${ page + 1 }</a></li>
                        </c:forEach>
                    </ul>
                </nav>
            </div>

            <button type="button" class="btn btn-primary btn-block" onclick="window.location.href = '/addPerson'">
                Add new Person
            </button>
        </div>
    </body>
</html>