public class TablaHashInventario {
    private Producto[] tabla;
    private int capacidad;
    private int comparaciones; // Códigos revisados en la última búsqueda

    public TablaHashInventario(int capacidad) {
        this.capacidad = capacidad;
        this.tabla = new Producto[capacidad];
    }

    // Función Hash: convierte el código de barras en un índice del arreglo
    private int funcionHash(String codigo) {
        int suma = 0;
        for (int i = 0; i < codigo.length(); i++) {
            suma = suma * 31 + codigo.charAt(i); // El 31 hace que importe la posición
        }
        return Math.abs(suma % capacidad);
    }

    // Operación INSERTAR - Complejidad: O(1) promedio
    // Devuelve true si el producto es nuevo y false si el código ya existía
    public boolean insertar(String codigo, String nombre, double precio, int stock) {
        int indice = funcionHash(codigo);
        Producto nuevo = new Producto(codigo, nombre, precio, stock);

        if (tabla[indice] == null) {
            tabla[indice] = nuevo;
        } else {
            // Manejo de colisiones por encadenamiento (Lista Enlazada)
            Producto actual = tabla[indice];
            while (actual != null) {
                if (actual.codigo.equals(codigo)) {
                    actual.stock += stock; // Si ya existe, actualiza el stock
                    actual.precio = precio;
                    actual.nombre = nombre;
                    return false;
                }
                if (actual.siguiente == null) break;
                actual = actual.siguiente;
            }
            actual.siguiente = nuevo;
        }
        return true;
    }

    // Operación BUSCAR - Complejidad: O(1) promedio
    public Producto buscar(String codigo) {
        int indice = funcionHash(codigo);
        Producto actual = tabla[indice];
        comparaciones = 0;
        while (actual != null) {
            comparaciones++;
            if (actual.codigo.equals(codigo)) {
                return actual;
            }
            actual = actual.siguiente;
        }
        return null; // No encontrado
    }

    public int getComparaciones() {
        return comparaciones;
    }

    // Operación ELIMINAR - Complejidad: O(1) promedio
    public boolean eliminar(String codigo) {
        int indice = funcionHash(codigo);
        Producto actual = tabla[indice];
        Producto anterior = null;

        while (actual != null) {
            if (actual.codigo.equals(codigo)) {
                if (anterior == null) {
                    tabla[indice] = actual.siguiente;
                } else {
                    anterior.siguiente = actual.siguiente;
                }
                return true;
            }
            anterior = actual;
            actual = actual.siguiente;
        }
        return false;
    }

    // Operación RECORRER - Complejidad: O(n)
    public void mostrarInventario() {
        System.out.println("\n--- INVENTARIO DEL SUPERMERCADO ---");
        boolean vacio = true;
        for (int i = 0; i < capacidad; i++) {
            Producto actual = tabla[i];
            while (actual != null) {
                System.out.println("[Índice " + i + "] " + actual);
                actual = actual.siguiente;
                vacio = false;
            }
        }
        if (vacio) System.out.println("El inventario está vacío.");
    }
}
