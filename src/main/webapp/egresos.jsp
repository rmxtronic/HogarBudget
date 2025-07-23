<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Egresos por Categoría</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 20px; background-color: #f4f4f4; }
        .container { background-color: #fff; padding: 20px; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1); max-width: 900px; margin: auto; }
        h1, h2 { color: #333; }
        .back-link { margin-bottom: 20px; display: inline-block; text-decoration: none; color: #007bff; }
        .back-link:hover { text-decoration: underline; }
        form { margin-bottom: 20px; padding: 15px; border: 1px solid #ddd; border-radius: 5px; background-color: #f9f9f9; }
        form label { display: block; margin-bottom: 5px; font-weight: bold; }
        form input[type="text"], form input[type="number"], form input[type="date"], form select {
            width: calc(100% - 22px); padding: 10px; margin-bottom: 10px; border: 1px solid #ccc; border-radius: 4px;
        }
        form button {
            background-color: #28a745; color: white; padding: 10px 15px; border: none; border-radius: 4px; cursor: pointer; font-size: 16px; margin-right: 10px;
        }
        form button:hover { background-color: #218838; }
        form button.cancel { background-color: #6c757d; }
        form button.cancel:hover { background-color: #5a6268; }
        table { width: 100%; border-collapse: collapse; margin-top: 20px; }
        table, th, td { border: 1px solid #ddd; }
        th, td { padding: 10px; text-align: left; }
        th { background-color: #f2f2f2; }
        .action-links a { margin-right: 10px; text-decoration: none; color: #007bff; }
        .action-links a.delete { color: #dc3545; }
        .action-links a:hover { text-decoration: underline; }
        .error-message { color: red; margin-bottom: 15px; }
        .success-message { color: green; margin-bottom: 15px; }
        .filter-section { margin-bottom: 20px; }
        .filter-section select { width: auto; margin-right: 10px; }
        .filter-section button { background-color: #6c757d; }
        .filter-section button:hover { background-color: #5a6268; }
    </style>
</head>
<body>
    <div class="container">
        <a href="presupuestos" class="back-link">&larr; Volver a Categorías de Presupuesto</a>
        <h1>Egresos para la Categoría: ${selectedCategory != null ? selectedCategory : 'Seleccione una'}</h1>

        <c:if test="${not empty errorMessage}">
            <p class="error-message">${errorMessage}</p>
        </c:if>
        <c:if test="${not empty param.successMessage}">
            <p class="success-message">${param.successMessage}</p>
        </c:if>

        <div class="filter-section">
            <form action="egresos" method="get">
                <label for="filterCategory">Filtrar por Categoría:</label>
                <select id="filterCategory" name="categoria" onchange="this.form.submit()">
                    <option value="">-- Seleccionar Categoría --</option>
                    <c:forEach var="cat" items="${categoriasDisponibles}">
                        <option value="${cat.nombre}" ${selectedCategory eq cat.nombre ? 'selected' : ''}>
                            ${cat.nombre}
                        </option>
                    </c:forEach>
                </select>
            </form>
        </div>


        <h2><c:if test="${egreso != null}">Editar Egreso</c:if><c:if test="${egreso == null}">Añadir Nuevo Egreso</c:if></h2>
        <form action="egresos" method="post">
            <c:if test="${egreso != null}">
                <input type="hidden" name="action" value="update">
                <input type="hidden" name="id" value="${egreso.id}">
            </c:if>
            <c:if test="${egreso == null}">
                <input type="hidden" name="action" value="add">
            </c:if>

            <label for="categoria">Categoría:</label>
            <select id="categoria" name="categoria" required>
                <option value="">-- Seleccione una Categoría --</option>
                <c:forEach var="cat" items="${categoriasDisponibles}">
                    <option value="${cat.nombre}"
                            <c:if test="${(egreso != null && egreso.categoria eq cat.nombre) || (egreso == null && selectedCategory eq cat.nombre)}">selected</c:if>>
                        ${cat.nombre}
                    </option>
                </c:forEach>
            </select><br>

            <label for="descripcion">Descripción (Tienda/Lugar):</label>
            <input type="text" id="descripcion" name="descripcion" value="${egreso.descripcion}" required><br>

            <label for="monto">Monto:</label>
            <input type="number" id="monto" name="monto" step="0.01" value="${egreso.monto}" required><br>

            <c:if test="${egreso != null}">
                <label for="fecha">Fecha:</label>
                <input type="date" id="fecha" name="fecha" value="${egreso.fecha}" required><br>
            </c:if>

            <button type="submit">
                <c:if test="${egreso != null}">Actualizar Egreso</c:if>
                <c:if test="${egreso == null}">Añadir Egreso</c:if>
            </button>
            <c:if test="${egreso != null}">
                <button type="button" class="cancel" onclick="window.location.href='egresos?categoria=${egreso.categoria}'">Cancelar Edición</button>
            </c:if>
        </form>

        <h2>Lista de Egresos</h2>
        <c:choose>
            <c:when test="${not empty egresos}">
                <table>
                    <thead>
                        <tr>
                            <th>ID</th>
                            <th>Categoría</th>
                            <th>Descripción</th>
                            <th>Monto</th>
                            <th>Fecha</th>
                            <th>Acciones</th>
                        </tr>
                    </thead>
                    <tbody>
                        <c:forEach var="egresoItem" items="${egresos}">
                            <tr>
                                <td>${egresoItem.id}</td>
                                <td>${egresoItem.categoria}</td>
                                <td>${egresoItem.descripcion}</td>
                                <td><fmt:formatNumber value="${egresoItem.monto}" type="currency" currencySymbol="$" maxFractionDigits="0"/></td>
                                <td><fmt:formatDate value="${egresoItem.fechaAsUtilDate}" pattern="yyyy-MM-dd"/></td>
                                <td class="action-links">
                                    <a href="egresos?action=edit&id=${egresoItem.id}&categoria=${egresoItem.categoria}">Editar</a>
                                    <a href="egresos?action=delete&id=${egresoItem.id}&categoria=${egresoItem.categoria}" class="delete" onclick="return confirm('¿Estás seguro de que quieres eliminar este egreso?');">Eliminar</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <p>No hay egresos registrados para esta categoría o no se ha seleccionado una categoría.</p>
            </c:otherwise>
        </c:choose>
    </div>
</body>
</html>

