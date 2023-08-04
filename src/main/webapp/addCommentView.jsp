<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Kommentar Hinzufügen</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
</head>
<body>
    <div class="container" style="margin-left: calc(40% - 120px)">
        <h2 class="p-2"><b>Der Beitrag zu dem der Kommentar hinzugefügt wird:</b></h2>
        <h3 class="p-2">${ parentPost.content }</h3>
        <hr>

        <h1 class="p-3">Kommentar hinzufügen</h1>

        <form:form action="/person/${ personId }/post/${ parentPost.id }" method="post" modelAttribute="post">

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
                    <button type="button" class="btn btn-danger" onclick="window.location.href='/person/${ personId }'">Abbrechen</button>
            		<button type="submit" value="Submit" class="btn btn-success">Beitrag speichern</button>
            	</div>
            </div>

        </form:form>
    </div>
</body>
</html>