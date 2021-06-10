<%@ page contentType="text/html;charset=UTF-8" %>
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

        .addLink {
            text-align: right;
        }
    </style>
</head>
<body>
    <h3><a href="index.html">Home</a></h3>
    <hr>
    <h2>${action} meal</h2>

    <p class="addLink"><a href="meals">Back to meals</a></p>

    <form action="meals" method="post">
        <input name="action" type="hidden" value="${action}">
        <input name="id" type="hidden" value="${id}">
        <table>
            <tr>
                <td><label for="datetime">DateTime:</label></td>
                <td><input id="datetime" name="datetime" type="datetime-local" value="${datetime}"></td>
            </tr>
            <tr>
                <td><label for="description">Description:</label></td>
                <td><input id="description" name="description" type="text" value="${description}"></td>
            </tr>
            <tr>
                <td><label for="calories">Calories:</label></td>
                <td><input id="calories" name="calories" type="number" value="${calories}"></td>
            </tr>
            <tr>
                <td></td>
                <td>
                    <button type="submit" value="submit">${action.equals("Add") ? "Add new meal" : "Save changes"}</button>
                    <button type="reset">Reset</button>
                </td>
            </tr>
        </table>
    </form>
</body>
</html>
