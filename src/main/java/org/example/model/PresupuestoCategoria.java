/** Modelo para categorías de presupuesto
 *
 *
 * **/
package org.example.model;
import java.math.BigDecimal;

public class PresupuestoCategoria {
    private int id;
    private String nombre;
    private BigDecimal montoPresupuestado;

    // Constructor para crear
    public PresupuestoCategoria(String nombre, BigDecimal montoPresupuestado) {
        this.nombre = nombre;
        this.montoPresupuestado = montoPresupuestado;
    }

    // Constructor para leer de la DB
    public PresupuestoCategoria(int id, String nombre, BigDecimal montoPresupuestado) {
        this.id = id;
        this.nombre = nombre;
        this.montoPresupuestado = montoPresupuestado;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public BigDecimal getMontoPresupuestado() { return montoPresupuestado; }
    public void setMontoPresupuestado(BigDecimal montoPresupuestado) { this.montoPresupuestado = montoPresupuestado; }

}
