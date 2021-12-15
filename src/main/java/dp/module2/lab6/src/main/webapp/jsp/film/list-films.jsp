<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" rel="stylesheet">
</head>

<body>
<div class="container">
    <h2>Films</h2>
    <!--Search Form -->
    <form action="${pageContext.request.contextPath}/film" method="get" id="searchFilmForm" role="form">
        <input type="hidden" id="method" name="method" value="byName">
        <div class="form-group col-xs-5">
            <input type="text" name="filmName" id="filmName" class="form-control" required="true"
                   placeholder="Type the Name of the film"/>
        </div>
        <button type="submit" class="btn btn-info">
            <span class="glyphicon glyphicon-search"></span> Search
        </button>
        <br></br>
        <br></br>
    </form>

    <!--Films List-->
    <c:if test="${not empty message}">
        <div class="alert alert-success">
                ${message}
        </div>
    </c:if>
    <form action="${pageContext.request.contextPath}/film" method="post" id="filmForm" role="form">
        <input type="hidden" id="name" name="name">
        <input type="hidden" id="action" name="action">
        <c:choose>
            <c:when test="${not empty films}">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <td>#</td>
                        <td>Name</td>
                        <td>Duration</td>
                        <td>Genre</td>
                        <td></td>
                    </tr>
                    </thead>
                    <c:forEach var="film" items="${films}">
                        <c:set var="classSuccess" value=""/>
                        <c:if test="${name == film.name}">
                            <c:set var="classSuccess" value="info"/>
                        </c:if>
                        <tr class="${classSuccess}">
                            <td>
                                <a href="${pageContext.request.contextPath}/film?name=${film.name}&method=byName&operation=update">${film.id}</a>
                            </td>
                            <td>${film.name}</td>
                            <td>${film.duration}</td>
                            <td>${film.genre.name}</td>
                            <td>
                                <a class="btn btn-lg btn-danger" style="color:white" href="#" id="delete"
                                        onclick="document.getElementById('action').value = 'delete';
                                        document.getElementById('name').value = '${film.name}';
                                        document.getElementById('filmForm').submit();">
                                    <span class="glyphicon glyphicon-trash"></span>
                                </a>
                            </td>
                        </tr>
                    </c:forEach>
                </table>
            </c:when>
            <c:otherwise>
                <br>
                <div class="alert alert-info">
                    No films found matching your search criteria
                </div>
            </c:otherwise>
        </c:choose>
    </form>
    <form action="create-film.jsp">
        <button type="submit" class="btn btn-primary btn-md">New film</button>
    </form>
    <form action="${pageContext.request.contextPath}/genre">
        <button type="submit" class="btn btn-check btn-md">To genres list</button>
    </form>
</div>
</body>
</html>
