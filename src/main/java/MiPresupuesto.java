import java.util.HashMap;
import java.util.Map;

public class MiPresupuesto {
    private final Map<String, Integer> pasivos = new HashMap<>();

    /** Anade o actualiza un gasto */
    public void putPasivo(String nombre, int valor) {

        pasivos.put(nombre, valor);
    }

    /** Obtiene el valor de un gasto (o null si no existe) */
    public Integer getPasivo(String nombre) {

        return pasivos.get(nombre);
    }

    /** Quita el gasto por nombre */
    public void removePasivo(String nombre) {

        pasivos.remove(nombre);
    }

    /** Devuelve todos los gastos */
    public Map<String, Integer> getAllPasivos(){

        return new HashMap<>(pasivos);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Gastos:\n");
        pasivos.forEach((nom, val) ->
                sb.append(String.format(" - %s: %d%n", nom, val))
        );
        return sb.toString();
    }
}
