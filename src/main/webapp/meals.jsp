<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="ru">
<head>
    <title>Meals</title>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>

    <table cellspacing="0" cellpadding="5" border="3" bgcolor="#eee">
        <caption><h2>List of meals</h2></caption>
        <tr bgcolor="#ccc">
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
        </tr>
        <c:forEach var="meal" items="${mealsTo}">
            <tr style="color:${meal.excess==true ? "#b00" : "#0b0"}">
                <td>${meal.dateTime.toString().replace("T", " ")}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
            </tr>
        </c:forEach>
    </table>

</body>
</html>