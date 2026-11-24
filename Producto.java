public class Producto {
    String codigo;
    String nombre;
    double precio;
    int stock;
    Producto siguiente; // Enlace para manejar colisiones en la Tabla Hash

    public Producto(String codigo, String nombre, double precio, int stock) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.precio = precio;
        this.stock = stock;
        this.siguiente = null;
    }

    @Override
    public String toString() {
        return "Código: " + codigo + " | " + nombre + " | Precio: $" + precio + " | Stock: " + stock;
    }
}
