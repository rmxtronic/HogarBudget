/** DAO para categorías de presupuesto **/

package org.example.dao;

import org.example.model.PresupuestoCategoria;
import org.example.util.DBUtil;

import java.sql.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PresupuestoDAO {

    public void addCategoria(PresupuestoCategoria categoria) throws SQLException {
        String sql = "INSERT INTO presupuestos_categorias (nombre, monto_presupuestado) VALUES (?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setBigDecimal(2, categoria.getMontoPresupuestado());
            stmt.executeUpdate();
        }
    }

    public Optional<PresupuestoCategoria> getCategoriaById(int id) throws SQLException {
        String sql = "SELECT id, nombre, monto_presupuestado FROM presupuestos_categorias WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new PresupuestoCategoria(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getBigDecimal("monto_presupuestado")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    public Optional<PresupuestoCategoria> getCategoriaByNombre(String nombre) throws SQLException {
        String sql = "SELECT id, nombre, monto_presupuestado FROM presupuestos_categorias WHERE nombre = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, nombre);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new PresupuestoCategoria(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getBigDecimal("monto_presupuestado")
                    ));
                }
            }
        }
        return Optional.empty();
    }

    public List<PresupuestoCategoria> getAllCategorias() throws SQLException {
        List<PresupuestoCategoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nombre, monto_presupuestado FROM presupuestos_categorias ORDER BY nombre";
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                categorias.add(new PresupuestoCategoria(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getBigDecimal("monto_presupuestado")
                ));
            }
        }
        return categorias;
    }

    public void updateCategoria(PresupuestoCategoria categoria) throws SQLException {
        String sql = "UPDATE presupuestos_categorias SET nombre = ?, monto_presupuestado = ? WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, categoria.getNombre());
            stmt.setBigDecimal(2, categoria.getMontoPresupuestado());
            stmt.setInt(3, categoria.getId());
            stmt.executeUpdate();
        }
    }

    public void deleteCategoria(int id) throws SQLException {
        String sql = "DELETE FROM presupuestos_categorias WHERE id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            stmt.executeUpdate();
        }
    }
}
