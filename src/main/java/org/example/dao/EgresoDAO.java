/** DAO para egresos individuales **/

package org.example.dao;

import org.example.model.Egreso;
import org.example.util.DBUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.math.BigDecimal;
import java.time.LocalDate;

public class EgresoDAO {

    public void addEgreso(Egreso egreso) throws SQLException {
        String sql = "INSERT INTO egresos (categoria, descripcion, monto, fecha) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, egreso.getCategoria());
            stmt.setString(2, egreso.getDescripcion());
            stmt.setBigDecimal(3, egreso.getMonto());
            stmt.setDate(4, Date.valueOf(egreso.getFecha()));
            stmt.executeUpdate();
        }
    }

    // Obtener egresos por categoría
    public List<Egreso> getEgresosByCategoria(String categoria) throws SQLException {
        List<Egreso> egresos = new ArrayList<>();
        String sql = "SELECT id, categoria, descripcion, monto, fecha FROM egresos WHERE categoria = ? ORDER BY fecha DESC, id DESC";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    egresos.add(new Egreso(
                            rs.getInt("id"),
                            rs.getString("categoria"),
                            rs.getString("descripcion"),
                            rs.getBigDecimal("monto"),
                            rs.getDate("fecha").toLocalDate()
                    ));
                }
            }
        }
        return egresos;
    }

    // ... (otros métodos como getEgresoById, updateEgreso, deleteEgreso)
// ... (métodos existentes: addEgreso, getEgresosByCategoria)

    // Método para obtener un egreso por ID (para edición)
    public Egreso getEgresoById(int id) throws SQLException {
        String sql = "SELECT id, categoria, descripcion, monto, fecha FROM egresos WHERE id = ?";
        Egreso egreso = null;
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    egreso = new Egreso(
                            rs.getInt("id"),
                            rs.getString("categoria"),
                            rs.getString("descripcion"),
                            rs.getBigDecimal("monto"),
                            rs.getDate("fecha").toLocalDate()
                    );
                }
            }
        }
        return egreso;
    }

    // Método para actualizar un egreso existente
    public void updateEgreso(Egreso egreso) throws SQLException {
        String sql = "UPDATE egresos SET categoria = ?, descripcion = ?, monto = ?, fecha = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, egreso.getCategoria());
            stmt.setString(2, egreso.getDescripcion());
            stmt.setBigDecimal(3, egreso.getMonto());
            stmt.setDate(4, Date.valueOf(egreso.getFecha()));
            stmt.setInt(5, egreso.getId());
            stmt.executeUpdate();
        }
    }

    // Método para eliminar un egreso por ID
    public void deleteEgreso(int id) throws SQLException {
        String sql = "DELETE FROM egresos WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }

    // Opcional: Obtener todos los egresos (sin filtrar por categoría, útil para un admin o resumen general)
    public List<Egreso> getAllEgresos() throws SQLException {
        List<Egreso> egresos = new ArrayList<>();
        String sql = "SELECT id, categoria, descripcion, monto, fecha FROM egresos ORDER BY fecha DESC, id DESC";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                egresos.add(new Egreso(
                        rs.getInt("id"),
                        rs.getString("categoria"),
                        rs.getString("descripcion"),
                        rs.getBigDecimal("monto"),
                        rs.getDate("fecha").toLocalDate()
                ));
            }
        }
        return egresos;
    }
}
