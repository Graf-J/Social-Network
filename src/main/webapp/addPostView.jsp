<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Beitrag Hinzufügen</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>
</head>
<body>
    <div class="container" style="margin-left: calc(40% - 120px)">
        <h1 class="p-3"> Beitrag hinzufügen</h1>

        <form:form action="/persons/${ personId }/posts" method="post" modelAttribute="post">

            <div class="row">
                <div class="form-group col-md-12">
                    <label class="col-md-3" for="content">Beitrag*</label>
                    <div class="col-md-6">
                        <form:textarea rows="4" path="content" id="content" class="form-control input-sm" required="required" />
                    </div>
                </div>
            </div>

            <div class="row p-2">
            	<div class="col-md-6" style="display: flex; justify-content: space-between;">
                    <button type="button" class="btn btn-danger" onclick="window.location.href='/persons/${ personId }'">Abbrechen</button>
            		<button type="submit" value="Submit" class="btn btn-success">Beitrag speichern</button>
            	</div>
            </div>

        </form:form>
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