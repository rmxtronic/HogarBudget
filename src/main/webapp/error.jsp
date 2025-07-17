<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Error</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; text-align: center; }
        .error-container { background-color: #fff; padding: 30px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 600px; margin: auto; }
        h1 { color: #dc3545; }
        p { color: #666; font-size: 1.1em; }
        .home-link { display: inline-block; margin-top: 20px; padding: 10px 20px; background-color: #007bff; color: white; text-decoration: none; border-radius: 5px; }
        .home-link:hover { background-color: #0056b3; }
    </style>
</head>
<body>
    <div class="error-container">
        <h1>¡Oops! Ha ocurrido un error.</h1>
        <p>Lo sentimos, algo salió mal.</p>
        <c:if test="${not empty errorMessage}">
            <p><strong>Detalle del error:</strong> ${errorMessage}</p>
        </c:if>
        <a href="${pageContext.request.contextPath}/egresos" class="home-link">Volver a la lista de Egresos</a>
    </div>
</body>
</html>