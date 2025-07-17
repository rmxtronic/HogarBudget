/** Servlet para egresos individuales/específicos dentro de cada categoría.
 *
 *
 * **/

package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.EgresoDAO;
import org.example.dao.PresupuestoDAO; // Necesario para obtener las categorías
import org.example.model.Egreso;
import org.example.model.PresupuestoCategoria; // Necesario para obtener las categorías

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

@WebServlet("/egresos") // Mapea este Servlet a la URL /egresos
public class EgresoServlet extends HttpServlet {

    private EgresoDAO egresoDAO;
    private PresupuestoDAO presupuestoDAO; // Para obtener la lista de categorías

    @Override
    public void init() throws ServletException {
        super.init();
        egresoDAO = new EgresoDAO();
        presupuestoDAO = new PresupuestoDAO();
    }

    // Maneja las solicitudes GET (mostrar lista de egresos para una categoría, formulario de edición)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String selectedCategory = request.getParameter("categoria"); // Para filtrar egresos por categoría

        if (action != null) {
            switch (action) {
                case "edit":
                    showEditForm(request, response, selectedCategory);
                    break;
                case "delete":
                    deleteEgreso(request, response, selectedCategory);
                    break;
                default:
                    listEgresos(request, response, selectedCategory);
                    break;
            }
        } else {
            listEgresos(request, response, selectedCategory);
        }
    }

    // Maneja las solicitudes POST (añadir, actualizar)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        String selectedCategory = request.getParameter("categoria"); // Categoría seleccionada para el POST

        if (action == null) {
            action = "add";
        }

        switch (action) {
            case "add":
                addEgreso(request, response, selectedCategory);
                break;
            case "update":
                updateEgreso(request, response, selectedCategory);
                break;
            default:
                listEgresos(request, response, selectedCategory);
                break;
        }
    }

    private void listEgresos(HttpServletRequest request, HttpServletResponse response, String selectedCategory) throws ServletException, IOException {
        try {
            List<PresupuestoCategoria> categorias = presupuestoDAO.getAllCategorias();
            request.setAttribute("categoriasDisponibles", categorias); // Para el dropdown en la JSP

            // Si hay una categoría seleccionada, la guardamos para pre-seleccionar en el dropdown
            if (selectedCategory != null && !selectedCategory.trim().isEmpty()) {
                request.setAttribute("selectedCategory", selectedCategory);
                List<Egreso> egresos = egresoDAO.getEgresosByCategoria(selectedCategory);
                request.setAttribute("egresos", egresos);
            } else {
                // Si no hay categoría seleccionada, mostramos todos los egresos (o redirigimos a presupuestos)
                // Para este ejemplo, podemos mostrar todos, o dejar la tabla vacía y que elija categoría.
                // Decidí que se liste vacio si no hay categoria seleccionada.
                request.setAttribute("egresos", new java.util.ArrayList<Egreso>());
            }

            request.getRequestDispatcher("/egresos.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al listar egresos o categorías: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response, String selectedCategory) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            Egreso existingEgreso = egresoDAO.getEgresoById(id); // Asumiendo que EgresoDAO tiene getEgresoById
            if (existingEgreso != null) {
                request.setAttribute("egreso", existingEgreso);
                listEgresos(request, response, selectedCategory); // Reutiliza la misma JSP
            } else {
                request.setAttribute("errorMessage", "Egreso no encontrado.");
                listEgresos(request, response, selectedCategory);
            }
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al obtener egreso para edición: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void addEgreso(HttpServletRequest request, HttpServletResponse response, String selectedCategory) throws ServletException, IOException {
        String categoria = request.getParameter("categoria");
        String descripcion = request.getParameter("descripcion");
        String montoStr = request.getParameter("monto");

        if (categoria == null || categoria.trim().isEmpty() || descripcion == null || descripcion.trim().isEmpty() || montoStr == null || montoStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Categoría, descripción y monto son obligatorios.");
            listEgresos(request, response, selectedCategory);
            return;
        }

        try {
            BigDecimal monto = new BigDecimal(montoStr);
            Egreso newEgreso = new Egreso(categoria, descripcion, monto);
            egresoDAO.addEgreso(newEgreso);
            response.sendRedirect(request.getContextPath() + "/egresos?categoria=" + categoria + "&successMessage=Egreso%20añadido%20correctamente.");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Monto inválido. Debe ser un número.");
            listEgresos(request, response, selectedCategory);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al añadir egreso: " + e.getMessage());
            listEgresos(request, response, selectedCategory);
        }
    }

    private void updateEgreso(HttpServletRequest request, HttpServletResponse response, String selectedCategory) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String categoria = request.getParameter("categoria");
        String descripcion = request.getParameter("descripcion");
        String montoStr = request.getParameter("monto");
        String fechaStr = request.getParameter("fecha");

        if (categoria == null || categoria.trim().isEmpty() || descripcion == null || descripcion.trim().isEmpty() || montoStr == null || montoStr.trim().isEmpty() || fechaStr == null || fechaStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Todos los campos son obligatorios para actualizar.");
            showEditForm(request, response, selectedCategory);
            return;
        }

        try {
            BigDecimal monto = new BigDecimal(montoStr);
            LocalDate fecha = LocalDate.parse(fechaStr); // Asume formato YYYY-MM-DD

            Egreso egresoToUpdate = new Egreso(id, categoria, descripcion, monto, fecha);
            egresoDAO.updateEgreso(egresoToUpdate); // Asumiendo que EgresoDAO tiene updateEgreso
            response.sendRedirect(request.getContextPath() + "/egresos?categoria=" + categoria + "&successMessage=Egreso%20actualizado%20correctamente.");
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Monto inválido. Debe ser un número.");
            showEditForm(request, response, selectedCategory);
        } catch (DateTimeParseException e) {
            request.setAttribute("errorMessage", "Formato de fecha inválido. Use YYYY-MM-DD.");
            showEditForm(request, response, selectedCategory);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al actualizar egreso: " + e.getMessage());
            showEditForm(request, response, selectedCategory);
        }
    }

    private void deleteEgreso(HttpServletRequest request, HttpServletResponse response, String selectedCategory) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            egresoDAO.deleteEgreso(id); // Asumiendo que EgresoDAO tiene deleteEgreso
            response.sendRedirect(request.getContextPath() + "/egresos?categoria=" + selectedCategory + "&successMessage=Egreso%20eliminado%20correctamente.");
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al eliminar egreso: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
