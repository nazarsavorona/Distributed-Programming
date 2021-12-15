<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css" integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <form action="/film" method="post"  role="form" data-toggle="validator" >
        <c:if test ="${empty action}">
            <c:set var="action" value="update"/>
        </c:if>
        <input type="hidden" id="action" name="action" value="${action}">
        <input type="hidden" id="idFilm" name="idFilm" value="${film.id}">
        <input type="hidden" id="name" name="name" value="${film.name}"/>
        <input type="hidden" id="duration" name="name" value="${film.duration}"/>
        <input type="hidden" id="genre" name="name" value="${film.genre.name}"/>

        <h2>Film</h2>
        <div class="form-group col-xs-4">
            <label for="newName" class="control-label col-xs-4">Enter new name:</label>
            <input type="text" name="newName" id="newName" class="form-control" value="${film.name}" required="true"/>

            <label for="newDuration" class="control-label col-xs-4">Enter new duration:</label>
            <input type="text" name="newDuration" id="newDuration" class="form-control" value="${film.duration}" required="true"/>

            <label for="newGenre" class="control-label col-xs-4">Enter new genre:</label>
            <input type="text" name="newGenre" id="newGenre" class="form-control" value="${film.genre.name}" required="true"/>

            <br/>
            <button type="submit" class="btn btn-primary  btn-md">Accept</button>
        </div>
    </form>
</div>
</body>
</html>
