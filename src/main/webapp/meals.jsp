<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://topjava.javawebinar.ru/functions" %>
<%--<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>--%>
<html>
<head>
    <title>Meal list</title>
    <style>
        html, body {
            height: 100%;
        }

        html {
            display: table;
            margin: auto;
        }

        body {
            display: table-cell;
            vertical-align: top;
            padding-top: 50px;
            background-color: darkgray;
            font-family: Monospaced, monospace;
        }

        #mealsTable {
            border-bottom: 1px solid brown;
            border-collapse: collapse;
            background-color: beige;
        }

        #mealsTable th {
            color: wheat;
            text-align: left;
            background-color: brown;
            padding: 8px 16px 4px 16px;
            border-right: 1px solid beige;
        }

        #mealsTable td {
            padding: 8px 16px 4px 16px;
            border-right: 1px solid darkgray;
        }

        .even {
            background-color: lightyellow;
        }

        a[href*="create"] {
            display: inline-block;
            width: 100%;
            text-align: right;
        }

        #filterLink {
            display: inline-block;
            width: 100%;
            text-align: center;
        }

        #filter {
            display: none;
        }

        #filter table {
            width: 100%;
        }

        .normal {
            color: green;
        }

        .excess {
            color: red;
        }
    </style>
    <script type="text/javascript">
        function toggle_filter() {
            let e = document.getElementById("filter");
            let l = document.getElementById("filterLink");
            if (e.style.display === 'block') {
                e.style.display = 'none';
                l.innerText = "Apply Date/Time Filters";
            } else {
                e.style.display = 'block';
                l.innerText = ">>> Hide Filters <<<";
            }
        }
    </script>
</head>
<body>
<section>
    <h3><a href="index.html">Home</a></h3>
    <hr/>
    <h2>Meals</h2>
    <a href="meals?action=create">Add Meal</a>
    <br><br>
    <table id="mealsTable">
        <thead>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th></th>
        </tr>
        </thead>
        <c:forEach items="${meals}" var="meal" varStatus="status">
            <jsp:useBean id="meal" type="ru.javawebinar.topjava.to.MealTo"/>
            <tr class="${meal.excess ? 'excess' : 'normal'} ${status.index%2==0 ? "even" : ""}">
                <td>
                        <%--${meal.dateTime.toLocalDate()} ${meal.dateTime.toLocalTime()}--%>
                        <%--<%=TimeUtil.toString(meal.getDateTime())%>--%>
                        <%--${fn:replace(meal.dateTime, 'T', ' ')}--%>
                        ${fn:formatDateTime(meal.dateTime)}
                </td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
    <br>
    <a id="filterLink" href="#" onclick="toggle_filter()">Apply Date/Time Filters</a>
    <form id="filter" method="post" action="meals">
        <input type="hidden" name="action" value="filter">
        <table>
            <tr>
                <td><label for="startDate">date from:</label></td>
                <td><input id="startDate" name="startDate" type="date"/></td>
                <td style="width: 29px"></td>
                <td><label for="startTime">time from:</label></td>
                <td><input id="startTime" name="startTime" type="time"/></td>
                <td style="width: 29px"></td>
                <td>
                    <button type="submit">Apply</button>
                </td>
            </tr>
            <tr>
                <td><label for="endDate">date to:</label></td>
                <td><input id="endDate" name="endDate" type="date"/></td>
                <td></td>
                <td><label for="endTime">time to:</label></td>
                <td><input id="endTime" name="endTime" type="time"/></td>
                <td></td>
                <td>
                    <button type="reset">Reset</button>
                </td>
            </tr>
        </table>
    </form>
</section>
</body>
</html>