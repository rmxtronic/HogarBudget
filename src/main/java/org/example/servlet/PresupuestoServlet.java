/** Servlet para las operaciones CRUD para tus PresupuestoCategorias.
 *
 * **/

package org.example.servlet;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.dao.PresupuestoDAO;
import org.example.model.PresupuestoCategoria;

import java.net.URLEncoder; // Asegúrate de importar esto
import java.nio.charset.StandardCharsets; // Y esto para StandardCharsets.UTF_8

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@WebServlet("/presupuestos") // Mapea este Servlet a la URL /presupuestos
public class PresupuestoServlet extends HttpServlet {

    private PresupuestoDAO presupuestoDAO;

    @Override
    public void init() throws ServletException {
        super.init();
        presupuestoDAO = new PresupuestoDAO();
    }

    // Maneja las solicitudes GET (mostrar lista, formulario de edición)
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // ¡AÑADE ESTA LÍNEA si lees parámetros GET con caracteres especiales!
        response.setContentType("text/html;charset=UTF-8");
        String action = request.getParameter("action");

        if (action != null) {
            switch (action) {
                case "edit":
                    showEditForm(request, response);
                    break;
                case "delete":
                    deleteCategoria(request, response);
                    break;
                default:
                    listCategorias(request, response);
                    break;
            }
        } else {
            listCategorias(request, response);
        }
    }

    // Maneja las solicitudes POST (añadir, actualizar)
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8"); // ¡AÑADE ESTA LÍNEA!
        String action = request.getParameter("action");

        if (action == null) {
            action = "add"; // Por defecto, si no hay acción, es añadir
        }

        switch (action) {
            case "add":
                addCategoria(request, response);
                break;
            case "update":
                updateCategoria(request, response);
                break;
            default:
                listCategorias(request, response); // Si la acción es desconocida, listar
                break;
        }
    }

    private void listCategorias(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            List<PresupuestoCategoria> categorias = presupuestoDAO.getAllCategorias();
            request.setAttribute("categorias", categorias);
            request.getRequestDispatcher("/presupuestos.jsp").forward(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al listar categorías de presupuesto: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            Optional<PresupuestoCategoria> existingCategoria = presupuestoDAO.getCategoriaById(id);
            if (existingCategoria.isPresent()) {
                request.setAttribute("categoria", existingCategoria.get());
                listCategorias(request, response); // Muestra la lista y el formulario pre-rellenado
            } else {
                request.setAttribute("errorMessage", "Categoría de presupuesto no encontrada.");
                listCategorias(request, response);
            }
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al obtener categoría para edición: " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }

    private void addCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombre = request.getParameter("nombre");
        String montoPresupuestadoStr = request.getParameter("montoPresupuestado");

        if (nombre == null || nombre.trim().isEmpty() || montoPresupuestadoStr == null || montoPresupuestadoStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Nombre y monto presupuestado son obligatorios.");
            listCategorias(request, response);
            return;
        }

        try {
            BigDecimal montoPresupuestado = new BigDecimal(montoPresupuestadoStr);
            PresupuestoCategoria newCategoria = new PresupuestoCategoria(nombre, montoPresupuestado);
            presupuestoDAO.addCategoria(newCategoria);
            //response.sendRedirect(request.getContextPath() + "/presupuestos?successMessage=Categoría%20añadida%20correctamente.");
            // --- CAMBIO AQUÍ ---
            String successMessage = "Categoría añadida correctamente.";
            String encodedSuccessMessage = URLEncoder.encode(successMessage, StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/presupuestos?successMessage=" + encodedSuccessMessage);
            // --- FIN DEL CAMBIO ---

        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Monto presupuestado inválido. Debe ser un número.");
            listCategorias(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al añadir categoría: " + e.getMessage());
            listCategorias(request, response);
        }
    }

    private void updateCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String nombre = request.getParameter("nombre");
        String montoPresupuestadoStr = request.getParameter("montoPresupuestado");

        if (nombre == null || nombre.trim().isEmpty() || montoPresupuestadoStr == null || montoPresupuestadoStr.trim().isEmpty()) {
            request.setAttribute("errorMessage", "Todos los campos son obligatorios para actualizar.");
            showEditForm(request, response); // Vuelve al formulario de edición con el error
            return;
        }

        try {
            BigDecimal montoPresupuestado = new BigDecimal(montoPresupuestadoStr);
            PresupuestoCategoria categoriaToUpdate = new PresupuestoCategoria(id, nombre, montoPresupuestado);
            presupuestoDAO.updateCategoria(categoriaToUpdate);
            //response.sendRedirect(request.getContextPath() + "/presupuestos?successMessage=Categoría%20actualizada%20correctamente.");
            // --- CAMBIO AQUÍ ---
            String successMessage = "Categoría actualizada correctamente.";
            String encodedSuccessMessage = URLEncoder.encode(successMessage, StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/presupuestos?successMessage=" + encodedSuccessMessage);
            // --- FIN DEL CAMBIO ---
        } catch (NumberFormatException e) {
            request.setAttribute("errorMessage", "Monto presupuestado inválido. Debe ser un número.");
            showEditForm(request, response);
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al actualizar categoría: " + e.getMessage());
            showEditForm(request, response);
        }
    }

    private void deleteCategoria(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        try {
            presupuestoDAO.deleteCategoria(id);
            //response.sendRedirect(request.getContextPath() + "/presupuestos?successMessage=Categoría%20eliminada%20correctamente.");
            // --- CAMBIO AQUÍ ---
            String successMessage = "Categoría eliminada correctamente.";
            String encodedSuccessMessage = URLEncoder.encode(successMessage, StandardCharsets.UTF_8);
            response.sendRedirect(request.getContextPath() + "/presupuestos?successMessage=" + encodedSuccessMessage);
            // --- FIN DEL CAMBIO ---
        } catch (SQLException e) {
            request.setAttribute("errorMessage", "Error al eliminar categoría. Asegúrate de que no haya egresos asociados a ella. " + e.getMessage());
            request.getRequestDispatcher("/error.jsp").forward(request, response);
        }
    }
}
