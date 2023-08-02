<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<head>
    <meta charset="ISO-8859-1">
    <title>Ehepartner hinzufügen</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>

</head>
<body>
    <div style="display: flex; justify-content: space-between; margin-top: 50px">
        <div style="width: 50vw; padding-left: 50px">
            <h1 class="p-3">Ehepartner hinzufügen</h1>

            <form:form action="/addMarriage/${ id }" method="post" modelAttribute="person">
                <div class="row">
                    <div class="form-group col-md-12">
                        <label class="col-md-6" for="email">E-Mail des Ehepartners*</label>
                        <div class="col-md-9">
                            <form:input type="text" path="email" id="email" class="form-control input-sm" required="required" />
                        </div>
                    </div>
                </div>

                <div class="row p-2">
                    <div class="col-md-9" style="display: flex; justify-content: space-between;">
                        <button type="button" class="btn btn-danger" onclick="window.location.href='/person/${ id }'">Abbrechen</button>
                        <button type="submit" value="Submit" class="btn btn-success">Ehepartner speichern</button>
                    </div>
                </div>
            </form:form>
        </div>

        <div>
            <table class="table table-striped">
                <tr>
                    <th>Vorname</th>
                    <th>Nachname</th>
                    <th>E-Mail</th>
                    <th>Aktion</th>
                </tr>
                <c:forEach var="person" items="${ persons }">
                    <tr>
                        <td>${ person.firstName }</td>
                        <td>${ person.lastName }</td>
                        <td>${ person.email }</td>
                        <td>
                            <button type="button" class="btn btn-primary" onclick="selectEmail('${ person.email }')">
                                Select
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>

            <div style="width: 50vw; display: flex; justify-content: center">
                <h3>Pagination:</h3>
                <nav aria-label="Page navigation example" style="margin-left: 20px">
                    <ul class="pagination">
                        <c:forEach var="page" items="${ pages }">
                            <li class="page-item"><a class="page-link" href="/addMarriage/${ id }?page=${ page }&pageSize=${ pageSize }">${ page + 1 }</a></li>
                        </c:forEach>
                    </ul>
                </nav>
            </div>
        </div>
    </div>

    <script>
        const selectEmail = (email) => {
            document.getElementById('email').value = email;
        }

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