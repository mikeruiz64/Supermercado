import java.util.Scanner;

public class SupermercadoApp {
    // Repite la pregunta hasta recibir un entero mayor o igual al mínimo
    static int leerEntero(Scanner sc, String mensaje, int minimo) {
        while (true) {
            System.out.print(mensaje);
            try {
                int valor = Integer.parseInt(sc.nextLine().trim());
                if (valor >= minimo) return valor;
                System.out.println("El valor debe ser " + minimo + " o mayor.");
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un número entero válido.");
            }
        }
    }

    // Acepta coma o punto decimal; el precio debe ser mayor que cero
    static double leerPrecio(Scanner sc, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            try {
                double valor = Double.parseDouble(sc.nextLine().trim().replace(',', '.'));
                if (valor > 0 && Double.isFinite(valor)) return valor;
                System.out.println("El precio debe ser mayor que cero.");
            } catch (NumberFormatException e) {
                System.out.println("Debe escribir un precio válido, sin separador de miles.");
            }
        }
    }

    public static void main(String[] args) {
        TablaHashInventario inventario = new TablaHashInventario(10);
        ColaClientes filaCaja = new ColaClientes();
        Scanner sc = new Scanner(System.in);

        // Datos precargados para demostrar el funcionamiento inmediato en la sustentación
        inventario.insertar("101", "Arroz Diana 1kg", 4500, 50);
        inventario.insertar("102", "Leche Alquería 1L", 5200, 30);
        inventario.insertar("103", "Aceite Girasol 1L", 12800, 20);
        inventario.insertar("111", "Pan Tajado Bimbo", 6500, 15); // Colisión intencional con 102: ambos suman 147 y caen en el índice 7

        int opcion;
        do {
            System.out.println("\n=== SISTEMA DE GESTIÓN DE SUPERMERCADO (E193) ===");
            System.out.println("1. Registrar / Actualizar producto en inventario (Tabla Hash)");
            System.out.println("2. Buscar producto por código (Búsqueda O(1))");
            System.out.println("3. Eliminar producto del inventario");
            System.out.println("4. Mostrar todo el inventario");
            System.out.println("5. Agregar cliente a la fila de caja (Encolar)");
            System.out.println("6. Atender siguiente cliente en caja (Desencolar + Facturar)");
            System.out.println("7. Ver fila de clientes");
            System.out.println("8. Comparar búsqueda lineal y Tabla Hash");
            System.out.println("0. Salir");
            opcion = leerEntero(sc, "Seleccione una opción: ", 0);

            switch (opcion) {
                case 1:
                    System.out.print("Código de barras: ");
                    String cod = sc.nextLine();
                    System.out.print("Nombre del producto: ");
                    String nom = sc.nextLine();
                    double pre = leerPrecio(sc, "Precio: ");
                    int stk = leerEntero(sc, "Cantidad en stock: ", 0);
                    if (inventario.insertar(cod, nom, pre, stk)) {
                        System.out.println("Producto guardado correctamente.");
                    } else {
                        System.out.println("El código ya existía: se actualizó nombre y precio, y se sumó el stock.");
                    }
                    break;

                case 2:
                    System.out.print("Ingrese código a buscar: ");
                    String codBuscar = sc.nextLine();
                    long inicio = System.nanoTime();
                    Producto encontrado = inventario.buscar(codBuscar);
                    long fin = System.nanoTime();
                    if (encontrado != null) {
                        System.out.println("ENCONTRADO: " + encontrado);
                        System.out.println("Tiempo de búsqueda: " + (fin - inicio) + " nanosegundos.");
                    } else {
                        System.out.println("Producto no existe en el inventario.");
                    }
                    break;

                case 3:
                    System.out.print("Código del producto a eliminar: ");
                    String codEliminar = sc.nextLine();
                    if (inventario.eliminar(codEliminar)) {
                        System.out.println("Producto eliminado.");
                    } else {
                        System.out.println("No se encontró el producto.");
                    }
                    break;

                case 4:
                    inventario.mostrarInventario();
                    break;

                case 5:
                    System.out.print("Nombre del cliente: ");
                    String cliente = sc.nextLine();
                    System.out.print("Código del producto a comprar: ");
                    String codCompra = sc.nextLine();
                    int cant = leerEntero(sc, "Cantidad: ", 1);
                    filaCaja.encolar(cliente, codCompra, cant);
                    break;

                case 6:
                    NodoCliente atendido = filaCaja.desencolar();
                    if (atendido == null) {
                        System.out.println("No hay clientes en la fila.");
                    } else {
                        System.out.println("\nAtendiendo a: " + atendido.nombreCliente);
                        Producto prod = inventario.buscar(atendido.codigoProducto);
                        if (prod == null) {
                            System.out.println("Error: El producto " + atendido.codigoProducto + " no existe.");
                        } else if (prod.stock < atendido.cantidad) {
                            System.out.println("Stock insuficiente. Disponible: " + prod.stock);
                        } else {
                            prod.stock -= atendido.cantidad;
                            double total = prod.precio * atendido.cantidad;
                            System.out.println("FACTURA GENERADA -> Producto: " + prod.nombre + " x" + atendido.cantidad + " | Total a pagar: $" + total);
                            System.out.println("Nuevo stock restante: " + prod.stock);
                        }
                    }
                    break;

                case 7:
                    filaCaja.mostrarFila();
                    break;

                case 8:
                    ComparacionBusqueda.ejecutar();
                    break;

                case 0:
                    break;

                default:
                    System.out.println("Opción no válida.");
            }
        } while (opcion != 0);
        sc.close();
    }
}
