<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>

<head>
    <meta charset="UTF-8">
    <title>Person hinzufügen</title>

    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.css">
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/toastr.js/latest/toastr.min.js"></script>

</head>
<body>
    <div class="container" style="margin-left: calc(40% - 120px)">
        <h1 class="p-3"> Person hinzufügen</h1>

        <form:form action="/persons" method="post" modelAttribute="person">

            <div class="row">
            	<div class="form-group col-md-12">
            		<label class="col-md-3" for="firstName">Vorname*</label>
            		<div class="col-md-6">
            		    <form:input type="text" path="firstName" id="firstName" class="form-control input-sm" required="required" />
            		</div>
            	</div>
            </div>
            
            <div class="row">
            	<div class="form-group col-md-12">
            		<label class="col-md-3" for="lastName">Nachname*</label>
            		<div class="col-md-6">
            			<form:input type="text" path="lastName" id="lastName" class="form-control input-sm" required="required" />
            		</div>
            	</div>
            </div>

            <div class="row">
            	<div class="form-group col-md-12">
            		<label class="col-md-3" for="email">E-Mail*</label>
            		<div class="col-md-6">
            			<form:input type="text" path="email" id="email" class="form-control input-sm" required="required" />
            		</div>
            	</div>
            </div>

            <div class="row">
            	<div class="form-group col-md-12">
            		<label class="col-md-3" for="date">Geburtstag*</label>
            		<div class="col-md-6">
            			<form:input type="date" path="birthday" id="birthday" class="form-control input-sm" required="required" />
            		</div>
            	</div>
            </div>

            <div class="row p-2">
            	<div class="col-md-6" style="display: flex; justify-content: space-between;">
                    <button type="button" class="btn btn-danger" onclick="window.location.href='/'">Abbrechen</button>
            		<button type="submit" value="Submit" class="btn btn-success">Person speichern</button>
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