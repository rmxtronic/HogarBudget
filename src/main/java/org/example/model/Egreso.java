/** Modelo de egreso
 *
 *
 *
 * **/
package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDate;

public class Egreso {
    private int id;
    private String categoria; // La clave foránea lógica a PresupuestoCategoria
    private String descripcion; // Ej: Nombre de la tienda
    private BigDecimal monto;
    private LocalDate fecha;

    // Constructores (ajusta según necesites)
    public Egreso(String categoria, String descripcion, BigDecimal monto) {
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = LocalDate.now();
    }

    public Egreso(int id, String categoria, String descripcion, BigDecimal monto, LocalDate fecha) {
        this.id = id;
        this.categoria = categoria;
        this.descripcion = descripcion;
        this.monto = monto;
        this.fecha = fecha;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getMonto() { return monto; }
    public void setMonto(BigDecimal monto) { this.monto = monto; }
    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }
}
