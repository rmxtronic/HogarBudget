<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gestión de Categorías de Presupuesto</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 900px; margin: auto; }
        h1, h2 { color: #333; }
        form { margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9; }
        form label { display: block; margin-bottom: 5px; font-weight: bold; }
        form input[type="text"], form input[type="number"] {
            width: calc(100% - 22px); padding: 10px; margin-bottom: 10px; border: 1px solid #ccc; border-radius: 4px;
        }
        form button {
            background-color: #007bff; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; margin-right: 10px;
        }
        form button:hover { background-color: #0056b3; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        table, th, td { border: 1px solid #ddd; }
        th, td { padding: 10px; text-align: left; }
        th { background-color: #f2f2f2; }
        .action-links a { margin-right: 10px; text-decoration: none; color: #007bff; }
        .action-links a.delete { color: #dc3545; }
        .action-links a:hover { text-decoration: underline; }
        .error-message { color: red; margin-bottom: 15px; }
        .success-message { color: green; margin-bottom: 15px; }
    </style>
</head>
<body>
    <div class="container">
        <h1>Gestión de Categorías de Presupuesto</h1>

        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
        </c:if>
        <c:if test="${not empty param.successMessage}">
            <p class="success-message">${param.successMessage}</p>
        </c:if>

        <h2><c:if test="${categoria != null}">Editar Categoría</c:if><c:if test="${categoria == null}">Añadir Nueva Categoría</c:if></h2>
        <form action="presupuestos" method="post">
            <c:if test="${categoria != null}">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${categoria.id}">
            </c:if>
            <c:if test="${categoria == null}">
                <input type="hidden" name="action" value="add">
            </c:if>

            <label for="nombre">Nombre de la Categoría:</label>
            <input type="text" id="nombre" name="nombre" value="${categoria.nombre}" required><br>

            <label for="montoPresupuestado">Monto Presupuestado:</label>
            <input type="number" id="montoPresupuestado" name="montoPresupuestado" step="0.01" value="${categoria.montoPresupuestado}" required><br>

            <button type="submit">
                <c:if test="${categoria != null}">Actualizar Categoría</c:if>
                <c:if test="${categoria == null}">Añadir Categoría</c:if>
            </button>
            <c:if test="${categoria != null}">
                <button type="button" onclick="window.location.href='presupuestos'">Cancelar Edición</button>
            </c:if>
        </form>

        <h2>Categorías Existentes</h2>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nombre</th>
                    <th>Monto Presupuestado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="catItem" items="${categorias}">
                    <tr>
                        <td>${catItem.id}</td>
                        <td>${catItem.nombre}</td>
                        <td><fmt:formatNumber value="${catItem.montoPresupuestado}" type="currency" currencySymbol="$" maxFractionDigits="2"/></td>
                        <td class="action-links">
                            <a href="presupuestos?action=edit&id=${catItem.id}">Editar</a>
                            <a href="presupuestos?action=delete&id=${catItem.id}" class="delete" onclick="return confirm('¿Estás seguro de que quieres eliminar la categoría ${catItem.nombre}? Esto NO eliminará sus egresos asociados.');">Eliminar</a>
                            <a href="egresos?categoria=${catItem.nombre}">Ver Egresos</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>
</body>
</html>