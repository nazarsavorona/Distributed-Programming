<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css"
          integrity="sha384-Gn5384xqQ1aoWXA+058RXPxPg6fy4IWvTNh0E263XmFcJlSAwiGgFAW/dAiS6JXm" crossorigin="anonymous">
    <link href="//netdna.bootstrapcdn.com/bootstrap/3.0.0/css/bootstrap-glyphicons.css" rel="stylesheet">
</head>

<body>
<div class="container">
    <h2>Genres</h2>
    <!--Search Form -->
    <form class="mb-3" action="/genre" method="get" id="searchGenreForm" role="form">
        <input type="hidden" id="method" name="method" value="byName">
        <div class="form-group col-xs-5">
            <input type="text" name="genreName" id="genreName" class="form-control" required="true"
                   placeholder="Type the genre name"/>
        </div>
        <button type="submit" class="btn btn-info">
            <span class="glyphicon glyphicon-search"></span> Search
        </button>
    </form>

    <c:if test="${not empty message}">
        <div class="alert alert-success">
                ${message}
        </div>
    </c:if>
    <form action="${pageContext.request.contextPath}/genre" method="post" id="genreForm" role="form">
        <input type="hidden" id="genreId" name="genreId">
        <input type="hidden" id="action" name="action">
        <c:choose>
            <c:when test="${not empty genres}">
                <table class="table table-striped">
                    <thead>
                    <tr>
                        <td>#</td>
                        <td>Name</td>
                        <td></td>
                    </tr>
                    </thead>
                    <c:forEach var="genre" items="${genres}">
                        <c:set var="classSuccess" value=""/>
                        <c:if test="${genreId == genre.id}">
                            <c:set var="classSuccess" value="info"/>
                        </c:if>
                        <tr class="${classSuccess}">
                            <td>
                                <a href="${pageContext.request.contextPath}/genre?id=${genre.id}&method=byId&operation=update">${genre.id}</a>
                            </td>
                            <td>${genre.name}</td>
                            <td>
                                <a class="btn btn-lg btn-danger" style="color:white" href="#" id="delete"
                                   onclick="document.getElementById('action').value = 'delete';
                                           document.getElementById('genreId').value = '${genre.id}';
                                           document.getElementById('genreForm').submit();">
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
                    No genre found matching your search criteria
                </div>
            </c:otherwise>
        </c:choose>
    </form>
    <form action="create-genre.jsp">
        <br/>
        <button type="submit" class="btn btn-primary btn-md">New genre</button>
    </form>
    <form action="${pageContext.request.contextPath}/film">
        <button type="submit" class="btn btn-primary btn-md">To films list</button>
    </form>
</div>
</body>
</html>