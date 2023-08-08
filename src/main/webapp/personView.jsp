<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="ISO-8859-1">
    <title>Person</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>

    <style>
        .grid-wrapper {
            display: grid;
            grid-template-columns: repeat(3, 1fr);
            grid-template-rows: repeat(2, 1fr);
            height: calc(100vh - 57px);
        }

        .grid-item-flex {
            display: flex;
            justify-content: center;
            align-items: center;
        }
    </style>
</head>
<body>
    <nav class="navbar navbar-expand-lg navbar-light bg-light">
        <a class="navbar-brand" href="/">Personen-Liste-Ansicht</a>
    </nav>

    <div class="grid-wrapper">
        <div class="grid-item-flex">
            <div class="card" style="width: 90%">
                <div class="card-header">
                    <b>Person:${ person.id }</b>
                </div>
                <ul class="list-group list-group-flush">
                    <li class="list-group-item"><b>Vorname: </b>${ person.firstName }</li>
                    <li class="list-group-item"><b>Nachname: </b>${ person.lastName }</li>
                    <li class="list-group-item"><b>E-Mail: </b>${ person.email }</li>
                    <li class="list-group-item"><b>Geburtstag: </b>${ person.birthday }</li>
                    <li class="list-group-item"><b>Alter: </b>${ person.age}</li>
                    <li class="list-group-item"><b>Ehepartner: </b>
                        <c:if test="${ spouse.isPresent() }">
                            <button class="btn btn-primary" onclick="window.location.href = '/person/${ spouse.get().getId() }'">
                                ${ spouse.get().getFirstName() } ${ spouse.get().getLastName() }
                            </button>
                        </c:if>
                        <c:if test="${ spouse.isEmpty() }">
                            <button class="btn btn-warning" onclick="window.location.href='/addMarriage/${ person.getId() }'">
                                Ehepartner hinzufügen
                            </button>
                        </c:if>
                    </li>
                </ul>
                </div>
        </div>

        <div class="grid-item-flex green">
            <ul class="list-group">
                <li class="list-group-item">
                    <h4>Familienmitglieder: <span class="badge bg-info">${ familyMemberCount }</span></h4>
                </li>
                <li class="list-group-item">
                    <h4>Freunde: <span class="badge bg-info">${ friendCount }</span></h4>
                </li>
                <li class="list-group-item">
                    <h4>Beiträge: <span class="badge bg-info">${ postCount }</span></h4>
                </li>
                <li class="list-group-item">
                    <h4>Kommentare: <span class="badge bg-info">${ commentCount }</span></h4>
                </li>
            </ul>
        </div>

        <div class="w-100" style="grid-row: span 2;">
            <div style="display: flex; justify-content: space-between; margin-bottom: 20px; padding-left: 20px; padding-top: 20px; padding-right: 20px;">
                <h2>Beiträge</h2>
                <button class="btn btn-warning" onclick="window.location.href='/person/${ person.id }/post'">
                    Beitrag hinzufügen
                </button>
            </div>
            <div class="list-group" style="height: calc(100vh - 150px); padding: 3px; border: 3px solid rgb(200, 200, 200); overflow-y: scroll">
                <c:forEach var="post" items="${ posts }">
                    <a class="list-group-item list-group-item-action">
                        <div class="d-flex w-100 justify-content-between">
                            <h4 class="mb-1">${ post.content }</h4>
                            <small>${ post.timeAgo }</small>
                        </div>
                        <div style="display: flex; flex-direction: column; align-items: flex-end;">
                            <button class="btn btn-warning btn-sm" onclick="window.location.href='/person/${ person.id }/post/${ post.id }'">
                                Kommentieren
                            </button>
                        </div>
                        <hr>
                        <c:if test="${ post.childPosts != null }">
                            <h5 class="ml-3">Kommentare:</h5>
                            <c:set var="posts" value="${ post.childPosts }" scope="request" />
                            <c:import url="/templates/commentTemplate.jsp">
                                <c:param name="personId" value="${ person.id }" />
                            </c:import>
                        </c:if>
                    </a>
                </c:forEach>
            </div>
        </div>

        <div class="p-3" style="display: flex; flex-direction: column;">
            <div style="display: flex; justify-content: space-between; margin-bottom: 20px;">
                <h2>Familie</h2>
                <button class="btn btn-warning" onclick="window.location.href='/addFamilyMember/${ person.id }'">
                    Familienmitglied hinzufügen
                </button>
            </div>
            <table class="table table-striped">
                <tr>
                    <th>Vorname</th>
                    <th>Nachname</th>
                    <th>Email</th>
                    <th>Aktion</th>
                </tr>
                <c:forEach var="familyMember" items="${ familyMembers }">
                    <tr>
                        <td>${ familyMember.firstName }</td>
                        <td>${ familyMember.lastName }</td>
                        <td>${ familyMember.email }</td>
                        <td>
                            <button class="btn btn-primary" onclick="window.location.href = '/person/${ familyMember.id }'">
                                View
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <c:if test="${ familyMembers.size() != 0 }">
                <div style="display: flex; justify-content: center">
                    <h3>Pagination:</h3>
                    <nav aria-label="Page navigation example" style="margin-left: 20px">
                        <ul class="pagination">
                            <c:forEach var="page" begin="1" end="${ familyPagination.numPages }">
                                <li class="page-item"><a class="page-link" href="/person/${ person.id }?familyPage=${ page - 1 }&familyPageSize=${ familyPagination.pageSize }">${ page }</a></li>
                            </c:forEach>
                        </ul>
                    </nav>
                </div>
            </c:if>
        </div>

        <div class="p-3" style="display: flex; flex-direction: column;">
            <div style="display: flex; justify-content: space-between; margin-bottom: 20px;">
                <h2>Freunde</h2>
                <button class="btn btn-warning" onclick="window.location.href='/addFriend/${ person.id }'">
                    Freund hinzufügen
                </button>
            </div>
            <table class="table table-striped">
                <tr>
                    <th>Vorname</th>
                    <th>Nachname</th>
                    <th>Email</th>
                    <th>Aktion</th>
                </tr>
                <c:forEach var="friend" items="${ friends }">
                    <tr>
                        <td>${ friend.firstName }</td>
                        <td>${ friend.lastName }</td>
                        <td>${ friend.email }</td>
                        <td>
                            <button class="btn btn-primary" onclick="window.location.href='/person/${ friend.id }'">
                                View
                            </button>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <c:if test="${ friends.size() != 0 }">
                <div style="display: flex; justify-content: center">
                    <h3>Pagination:</h3>
                    <nav aria-label="Page navigation example" style="margin-left: 20px">
                        <ul class="pagination">
                            <c:forEach var="page" begin="1" end="${ friendPagination.numPages }">
                                <li class="page-item"><a class="page-link" href="/person/${ person.id }?friendPage=${ page - 1 }&friendPageSize=${ friendPagination.pageSize }">${ page }</a></li>
                            </c:forEach>
                        </ul>
                    </nav>
                </div>
            </c:if>
        </div>
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