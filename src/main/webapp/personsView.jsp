<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Personen</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>

    <style>
        .table-wrapper {
            padding: 1%;
            display: flex;
            align-items: center;
            flex-direction: column;
        }
    </style>
</head>
<body>
    <div style="display: flex; padding: 1%">
        <h1>Startdaten initialisieren:</h1>
        <button class="btn btn-warning" style="margin-left: 20px" onclick="window.location.href = '/initialize'">
            <h2>Initialisieren</h2>
        </button>
    </div>

    <div class="table-wrapper">
        <table class="table table-striped">
            <tr>
                <th>Id</th>
                <th>Vorname</th>
                <th>Nachname</th>
                <th>E-Mail</th>
                <th>Geburtstag</th>
                <th>Alter</th>
                <th>Aktion</th>
            </tr>
            <c:forEach var="person" items="${ persons }">
                <tr>
                    <td>${ person.id }</td>
                    <td>${ person.firstName }</td>
                    <td>${ person.lastName }</td>
                    <td>${ person.email }</td>
                    <td>${ person.birthday }</td>
                    <td>${ person.age }</td>
                    <td>
                        <button class="btn btn-primary" onclick="window.location.href = 'person/${ person.id }'">View</button>
                    </td>
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
            Neue Person hinzufügen
        </button>
    </div>

    <script>
        window.onload = () => {
            let error = "${error}";

            if (error) {
                Command: toastr["error"](error)
            }

            toastr.options = {
                "closeButton": true,
                "debug": false,
                "newestOnTop": false,
                "progressBar": true,
                "positionClass": "toast-top-right",
                "preventDuplicates": false,
                "showDuration": "300",
                "hideDuration": "1000",
                "timeOut": "5000",
                "extendedTimeOut": "1000",
                "showEasing": "swing",
                "hideEasing": "linear",
                "showMethod": "fadeIn",
                "hideMethod": "fadeOut"
            }
        }
    </script>
</body>
</html>