<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <form action="/film" method="post" role="form" data-toggle="validator">
        <c:if test="${empty action}">
            <c:set var="action" value="add"/>
        </c:if>
        <input type="hidden" id="action" name="action" value="${action}">
        <input type="hidden" id="idFilm" name="idFilm" value="${film.id}">
        <h2>Film</h2>
        <div class="form-group col-xs-4">
            <label for="name" class="control-label col-xs-4">Name:</label>
            <input type="text" name="name" id="name" class="form-control" value="${film.name}" required="true"/>

            <label for="duration" class="control-label col-xs-4">Duration:</label>
            <input type="text" name="duration" id="duration" class="form-control" value="${film.duration}"
                   required="true"/>

            <label for="genre" class="control-label col-xs-4">Genre:</label>
            <input type="text" name="genre" id="genre" class="form-control" value="${film.genre.name}" required="true"/>

            <br/>
            <button type="submit" class="btn btn-primary  btn-md">Accept</button>
        </div>
    </form>
</div>
</body>
</html>
