<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
</head>
<body>
<div class="container">
    <form action="${pageContext.request.contextPath}/genre" method="post" role="form" data-toggle="validator">
        <c:if test="${empty action}">
            <c:set var="action" value="create"/>
        </c:if>
        <input type="hidden" id="action" name="action" value="${action}">
        <input type="hidden" id="genreId" name="genreId" value="${genre.id}">
        <h2>Genre</h2>
        <div class="form-group col-xs-4">
            <label for="name" class="control-label col-xs-4">Name:</label>
            <input type="text" name="name" id="name" class="form-control" value="${genre.name}" required="true"/>

            <br/>
            <button type="submit" class="btn btn-primary btn-md">Accept</button>
        </div>
    </form>
</div>
</body>
</html>