import java.util.Scanner;

public class PruebasSupermercado {
    static int pasadas = 0;

    static void verificar(String nombre, boolean condicion) {
        if (!condicion) throw new RuntimeException("FALLA: " + nombre);
        pasadas++;
        System.out.println("OK: " + nombre);
    }

    public static void main(String[] args) {
        TablaHashInventario t = new TablaHashInventario(10);
        verificar("buscar en tabla vacía", t.buscar("102") == null);
        verificar("insertar nuevo", t.insertar("102", "Leche", 5200, 30));
        verificar("insertar con colisión", t.insertar("111", "Pan", 6500, 15));
        verificar("102 y 111 comparten casilla", t.buscar("102").siguiente == t.buscar("111"));
        verificar("111 se encuentra en la segunda comparación", t.getComparaciones() == 2);
        verificar("repetido no crea otro", !t.insertar("102", "Leche 2L", 9000, 5));
        Producto p = t.buscar("102");
        verificar("repetido actualiza", p.nombre.equals("Leche 2L") && p.precio == 9000 && p.stock == 35);
        verificar("eliminar el primero de la cadena", t.eliminar("102") && t.buscar("102") == null);
        verificar("el otro de la cadena sigue", t.buscar("111") != null);
        t.insertar("102", "Leche", 5200, 30);
        verificar("eliminar el último de la cadena", t.eliminar("102") && t.buscar("111").siguiente == null);
        verificar("eliminar inexistente", !t.eliminar("999"));

        ColaClientes c = new ColaClientes();
        verificar("desencolar vacía", c.desencolar() == null);
        c.encolar("Ana", "102", 1);
        c.encolar("Carlos", "111", 2);
        verificar("FIFO", c.desencolar().nombreCliente.equals("Ana") && c.desencolar().nombreCliente.equals("Carlos"));
        verificar("queda vacía", c.estaVacia());
        c.encolar("Luis", "111", 1);
        verificar("se puede usar tras vaciarse", c.desencolar().nombreCliente.equals("Luis"));

        verificar("entero: rechaza letras y negativos",
            SupermercadoApp.leerEntero(new Scanner("abc\n-5\n\n99999999999\n3\n"), "", 1) == 3);
        verificar("cantidad: rechaza cero", SupermercadoApp.leerEntero(new Scanner("0\n1\n"), "", 1) == 1);
        verificar("precio: rechaza negativos, letras y NaN",
            SupermercadoApp.leerPrecio(new Scanner("-100\nabc\nNaN\n0\n10,5\n"), "") == 10.5);
        verificar("precio con punto", SupermercadoApp.leerPrecio(new Scanner("10.5\n"), "") == 10.5);

        double[] r = ComparacionBusqueda.medir(1000);
        verificar("lineal revisa (n+1)/2 en promedio", r[0] == 500.5);
        verificar("hash revisa menos que lineal", r[1] < r[0]);
        System.out.println("\n" + pasadas + " pruebas correctas.");
    }
}
