<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
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
            height: 100vh;
        }

        .grid-item {
            display: flex;
            justify-content: center;
            align-items: center;
        }

        .green {
            background-color: green;
        }

        .blue {
            background-color: blue;
        }

        .yellow {
            background-color: yellow;
        }

        .orange {
            background-color: orange;
        }
    </style>
</head>
<body>
    <div class="grid-wrapper">
        <div class="grid-item">
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
                </ul>
                </div>
        </div>
        <div class="grid-item green">Add Post</div>
        <div class="grid-item blue" style="grid-row: span 2;">Posts</div>
        <div class="grid-item yellow">Family</div>
        <div class="grid-item orange">Friends</div>
    </div>
</body>
</html>