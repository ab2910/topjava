<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="ru">
<head>
    <title>Users</title>
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

        p {
            font-weight: bold;
            text-align: center;
        }
    </style>
</head>
<body>
<h3><a href="index.html">Home</a></h3>
<hr>
<h2>Users</h2>
<p>Hello, User #${userId}!</p>
</body>
</html>
