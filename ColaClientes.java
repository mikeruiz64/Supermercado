class NodoCliente {
    String nombreCliente;
    String codigoProducto;
    int cantidad;
    NodoCliente siguiente;

    public NodoCliente(String nombreCliente, String codigoProducto, int cantidad) {
        this.nombreCliente = nombreCliente;
        this.codigoProducto = codigoProducto;
        this.cantidad = cantidad;
        this.siguiente = null;
    }
}

public class ColaClientes {
    private NodoCliente frente;
    private NodoCliente fin;

    public ColaClientes() {
        this.frente = null;
        this.fin = null;
    }

    // Operación ENCOLAR (Enqueue) - Complejidad: O(1)
    public void encolar(String nombreCliente, String codigoProducto, int cantidad) {
        NodoCliente nuevo = new NodoCliente(nombreCliente, codigoProducto, cantidad);
        if (fin == null) {
            frente = fin = nuevo;
        } else {
            fin.siguiente = nuevo;
            fin = nuevo;
        }
        System.out.println("Cliente " + nombreCliente + " agregado a la fila de caja.");
    }

    // Operación DESENCOLAR (Dequeue) - Complejidad: O(1)
    public NodoCliente desencolar() {
        if (frente == null) {
            return null;
        }
        NodoCliente atendido = frente;
        frente = frente.siguiente;
        if (frente == null) {
            fin = null;
        }
        return atendido;
    }

    public boolean estaVacia() {
        return frente == null;
    }

    public void mostrarFila() {
        System.out.println("\n--- FILA DE CLIENTES EN CAJA ---");
        if (frente == null) {
            System.out.println("No hay clientes esperando.");
            return;
        }
        NodoCliente actual = frente;
        int turno = 1;
        while (actual != null) {
            System.out.println("Turno #" + turno + ": " + actual.nombreCliente + " (Quiere prod: " + actual.codigoProducto + ", Cant: " + actual.cantidad + ")");
            actual = actual.siguiente;
            turno++;
        }
    }
}
