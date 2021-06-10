<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:useBean id="mealsTo" scope="request" type="java.util.List"/>
<html lang="ru">
<head>
    <title>Meals</title>
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

        table {
            border-bottom: 1px solid brown;
            border-collapse: collapse;
            background-color: beige;
        }

        th {
            color: wheat;
            text-align: left;
            background-color: brown;
            padding: 8px 16px 4px 16px;
            border-right: 1px solid beige;
        }

        td {
            padding: 8px 16px 4px 16px;
            border-right: 1px solid darkgray;
        }

        .excess {
            color: brown;
        }

        .noExcess {
            color: darkslategrey;
        }

        .even {
            background-color: lightyellow;
        }

        .addLink {
            text-align: right;
        }
    </style>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>Meals</h2>

    <p class="addLink"><a href="meals?action=add">Add a meal</a></p>

    <table>
        <tr>
            <th>Date</th>
            <th>Description</th>
            <th>Calories</th>
            <th></th>
            <th style="border-right: none"></th>
        </tr>
        <c:forEach var="meal" items="${mealsTo}" varStatus="iSt">
            <tr class="${meal.excess==true ? "excess" : "noExcess"} ${iSt.index%2==0 ? "even" : ""}">
                <td>${meal.dateTime.toString().replace("T", " ")}</td>
                <td>${meal.description}</td>
                <td>${meal.calories}</td>
                <td><a href="meals?action=update&id=${meal.id}">Update</a></td>
                <td style="border-right: none"><a href="meals?action=delete&id=${meal.id}">Delete</a></td>
            </tr>
        </c:forEach>
    </table>
</body>
</html>