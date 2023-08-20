<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Kommentar Hinzufügen</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
</head>
<body>
    <div style="display: flex; justify-content: space-between; margin-top: 50px">
        <div style="width: 50vw; padding-left: 50px">
            <h2 class="p-2"><b>Der Beitrag zu welchem der Kommentar hinzugefügt wird:</b></h2>
            <h3 class="p-2">${ parentPost.content }</h3>
            <hr>

            <h1 class="p-3">Kommentar hinzufügen</h1>

            <form:form action="/persons/${ person.id }/posts/${ parentPost.id }" method="post" modelAttribute="post">
                <div class="row">
                    <div class="form-group col-md-12">
                        <label class="col-md-6" for="email">E-Mail des Verfassers*</label>
                        <div class="col-md-9">
                            <form:input type="text" path="creator.email" id="email" class="form-control input-sm" value="${ person.email }" required="required" />
                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="form-group col-md-12">
                        <label class="col-md-3" for="content">Kommentar*</label>
                        <div class="col-md-6">
                            <form:textarea rows="4" path="content" id="content" class="form-control input-sm" required="required" />
                        </div>
                    </div>
                </div>

                <div class="row p-2">
                    <div class="col-md-6" style="display: flex; justify-content: space-between;">
                        <button type="button" class="btn btn-danger" onclick="window.location.href='/persons/${ person.id }'">Abbrechen</button>
                        <button type="submit" value="Submit" class="btn btn-success">Beitrag speichern</button>
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
                        <c:forEach var="page" begin="1" end="${ pagination.numPages }">
                            <c:set var="isCurrentPage" value="${ pagination.page == page - 1 }" />
                            <li class="page-item ${ isCurrentPage ? 'active' : '' }">
                                <a class="page-link" href="/persons/${ person.id }/posts/${ parentPost.id }?page=${ page - 1 }&pageSize=${ pagination.pageSize }">${ page }</a>
                            </li>
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