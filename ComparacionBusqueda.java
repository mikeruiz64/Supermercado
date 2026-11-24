public class ComparacionBusqueda {

    // ANTES: recorre el arreglo hasta encontrar el código - Complejidad: O(n)
    // Devuelve cuántos códigos tuvo que revisar
    static int busquedaLineal(Producto[] lista, String codigo) {
        for (int i = 0; i < lista.length; i++) {
            if (lista[i].codigo.equals(codigo)) {
                return i + 1;
            }
        }
        return lista.length;
    }

    // Busca una vez cada uno de los n productos con los dos métodos.
    // Devuelve promedios: {revisados lineal, revisados hash, ns lineal, ns hash}
    static double[] medir(int n) {
        Producto[] lista = new Producto[n];
        TablaHashInventario tabla = new TablaHashInventario(n);
        String[] buscados = new String[n];
        for (int i = 0; i < n; i++) {
            String codigo = "" + (100000 + i);
            lista[i] = new Producto(codigo, "Producto " + i, 1000, 10);
            tabla.insertar(codigo, "Producto " + i, 1000, 10);
            // Texto aparte, como el que llega del teclado o del lector
            buscados[i] = "" + (100000 + i);
        }

        long revisadosLineal = 0;
        long inicio = System.nanoTime();
        for (int i = 0; i < n; i++) {
            revisadosLineal += busquedaLineal(lista, buscados[i]);
        }
        long tiempoLineal = System.nanoTime() - inicio;

        long revisadosHash = 0;
        inicio = System.nanoTime();
        for (int i = 0; i < n; i++) {
            tabla.buscar(buscados[i]);
            revisadosHash += tabla.getComparaciones();
        }
        long tiempoHash = System.nanoTime() - inicio;

        return new double[] {
            (double) revisadosLineal / n, (double) revisadosHash / n,
            (double) tiempoLineal / n, (double) tiempoHash / n
        };
    }

    public static void ejecutar() {
        System.out.println("\n--- BÚSQUEDA LINEAL (ANTES) Y TABLA HASH (DESPUÉS) ---");
        System.out.println("Promedio por búsqueda. Los tiempos cambian en cada ejecución.");
        for (int i = 0; i < 10; i++) {
            medir(1000); // Calentamiento de Java: se descarta para que los tiempos sean justos
        }
        System.out.printf("%10s %16s %16s %14s %14s%n",
            "Productos", "Revisa lineal", "Revisa hash", "Lineal (ns)", "Hash (ns)");
        int[] tamanos = {100, 1000, 10000};
        for (int n : tamanos) {
            double[] r = medir(n);
            System.out.printf("%10d %16.1f %16.1f %14.0f %14.0f%n", n, r[0], r[1], r[2], r[3]);
        }
    }
}
