// SistemaInventario.java
package Trabajo;

import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class SistemaInventario {
    // Atributos del sistema
    private final Inventario inventario;
    private Usuario usuarioActual;
    private static final HashMap<String, String> credenciales = new HashMap<>();
    private final List<MovimientoInventario> movimientos;

    public SistemaInventario() {
        this.inventario = new Inventario();
        this.movimientos = new ArrayList<>();
    }

    public static void main(String[] args) {
        SistemaInventario sistema = new SistemaInventario();
        sistema.iniciar();
    }

    public void iniciar() {
        String filePath = "src/Usuarios.txt";
        LectorUsuarioContrasena lector = LectorUsuarioContrasena.getInstance();
        int resultado = lector.leerArchivoUsuarioYContrasena(filePath, credenciales);

        if (resultado == -1) {
            System.out.println("No se pudo cargar el archivo de usuarios.");
            return;
        }

        cargarDatosIniciales(); // Carga datos de prueba desde data.txt

        autenticarUsuario();
        mostrarMenu();
    }

    private void cargarDatosIniciales() {
        String dataFilePath = "src/data.txt";
        DataLoader dataLoader = new DataLoader();
        dataLoader.cargarDatos(dataFilePath, inventario);
    }

    private void autenticarUsuario() {
        Scanner scanner = new Scanner(System.in);
        boolean autenticado = false;
        do {
            System.out.println("=== Sistema de Autenticación ===");
            System.out.print("Ingrese su nombre de usuario: ");
            String nombreUsuario = scanner.nextLine();
            System.out.print("Ingrese su contraseña: ");
            String contrasena = scanner.nextLine();

            if (credenciales.containsKey(nombreUsuario) && credenciales.get(nombreUsuario).equals(contrasena)) {
                usuarioActual = new Usuario(nombreUsuario, contrasena);
                autenticado = true;
                System.out.println("Autenticación exitosa. Bienvenido, " + nombreUsuario + "!");
            } else {
                System.out.println("Credenciales incorrectas. Intente nuevamente.");
            }
        } while (!autenticado);
    }

    private void mostrarMenu() {
        Scanner scanner = new Scanner(System.in);
        int opcion;
        do {
            System.out.println("\n=== Menú Principal ===");
            System.out.println("1. Registrar Ingrediente");
            System.out.println("2. Registrar Envase");
            System.out.println("3. Registrar Producto");
            System.out.println("4. Generar Reporte");
            System.out.println("5. Agregar Lote de Ingrediente");
            System.out.println("6. Pasar a Producción");
            System.out.println("7. Reporte de Movimientos");
            System.out.println("8. Buscar Ingrediente o Envase");
            System.out.println("9. Salir");
            System.out.print("Seleccione una opción: ");
            opcion = Integer.parseInt(scanner.nextLine());

            switch (opcion) {
                case 1:
                    registrarIngrediente(scanner);
                    break;
                case 2:
                    registrarEnvase(scanner);
                    break;
                case 3:
                    registrarProducto(scanner);
                    break;
                case 4:
                    generarReportes(scanner);
                    break;
                case 5:
                    agregarLoteIngrediente(scanner);
                    break;
                case 6:
                    pasarAProduccion(scanner);
                    break;
                case 7:
                    reporteMovimientos();
                    break;
                case 8:
                    buscarIngredienteOEnvase(scanner);
                    break;
                case 9:
                    System.out.println("Saliendo del sistema.");
                    break;
                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        } while (opcion != 9);
        scanner.close();
    }

    private void registrarIngrediente(Scanner scanner) {
        System.out.println("\n=== Registrar Ingrediente ===");
        System.out.print("Nombre del ingrediente: ");
        String nombre = scanner.nextLine();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();

        Ingrediente ingrediente = new Ingrediente(nombre, categoria);

        System.out.print("¿Cuántos lotes desea agregar? ");
        int numLotes = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numLotes; i++) {
            System.out.println("=== Lote " + (i + 1) + " ===");
            String codigoLote = generarCodigoLote();
            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            System.out.print("Precio total del lote: ");
            BigDecimal precioTotal = new BigDecimal(scanner.nextLine());
            System.out.print("Fecha de Vencimiento (dd/MM/yyyy): ");
            String fechaVencimientoStr = scanner.nextLine();
            Date fechaVencimiento = parseFecha(fechaVencimientoStr);
            Lote lote = new Lote(codigoLote, cantidad, fechaVencimiento, precioTotal);
            ingrediente.agregarLote(lote);
        }

        inventario.agregarIngrediente(ingrediente);
        System.out.println("Ingrediente registrado exitosamente.");
    }

    private void registrarEnvase(Scanner scanner) {
        System.out.println("\n=== Registrar Envase ===");
        System.out.print("Nombre del envase: ");
        String nombre = scanner.nextLine();
        System.out.print("Tipo: ");
        String tipo = scanner.nextLine();

        Envase envase = new Envase(nombre, tipo);

        System.out.print("¿Cuántos lotes desea agregar? ");
        int numLotes = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numLotes; i++) {
            System.out.println("=== Lote " + (i + 1) + " ===");
            String codigoLote = generarCodigoLote();
            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            System.out.print("Precio total del lote: ");
            BigDecimal precioTotal = new BigDecimal(scanner.nextLine());
            Lote lote = new Lote(codigoLote, cantidad, precioTotal);
            envase.agregarLote(lote);
        }

        inventario.agregarEnvase(envase);
        System.out.println("Envase registrado exitosamente.");
    }

    private void registrarProducto(Scanner scanner) {
        System.out.println("\n=== Registrar Producto ===");
        System.out.print("Nombre del producto: ");
        String nombre = scanner.nextLine();
        System.out.print("Categoría: ");
        String categoria = scanner.nextLine();

        Receta receta = new Receta();

        System.out.print("¿Cuántos ingredientes requiere la receta? ");
        int numIngredientes = Integer.parseInt(scanner.nextLine());

        List<Ingrediente> listaIngredientes = inventario.getIngredientes();
        for (int i = 0; i < numIngredientes; i++) {
            System.out.println("=== Seleccione Ingrediente " + (i + 1) + " ===");
            mostrarListaIngredientes();
            System.out.print("Ingrese el número del ingrediente: ");
            int numIngrediente = Integer.parseInt(scanner.nextLine());
            if (numIngrediente < 1 || numIngrediente > listaIngredientes.size()) {
                System.out.println("Número inválido. Intente nuevamente.");
                i--;
                continue;
            }
            Ingrediente ingrediente = listaIngredientes.get(numIngrediente - 1);
            System.out.print("Cantidad requerida: ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            receta.agregarIngrediente(ingrediente, cantidad);
        }

        System.out.print("¿Cuántos envases requiere la receta? ");
        int numEnvases = Integer.parseInt(scanner.nextLine());

        List<Envase> listaEnvases = inventario.getEnvases();
        for (int i = 0; i < numEnvases; i++) {
            System.out.println("=== Seleccione Envase " + (i + 1) + " ===");
            mostrarListaEnvases();
            System.out.print("Ingrese el número del envase: ");
            int numEnvase = Integer.parseInt(scanner.nextLine());
            if (numEnvase < 1 || numEnvase > listaEnvases.size()) {
                System.out.println("Número inválido. Intente nuevamente.");
                i--;
                continue;
            }
            Envase envase = listaEnvases.get(numEnvase - 1);
            System.out.print("Cantidad requerida: ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            receta.agregarEnvase(envase, cantidad);
        }

        receta.ajustarReceta();

        Producto producto = new Producto(nombre, categoria, receta);

        inventario.agregarProducto(producto);
        System.out.println("Producto registrado exitosamente.");
    }

    private void mostrarListaIngredientes() {
        List<Ingrediente> listaIngredientes = inventario.getIngredientes();
        for (int i = 0; i < listaIngredientes.size(); i++) {
            System.out.println((i + 1) + ". " + listaIngredientes.get(i).getNombre());
        }
    }

    private void mostrarListaEnvases() {
        List<Envase> listaEnvases = inventario.getEnvases();
        for (int i = 0; i < listaEnvases.size(); i++) {
            System.out.println((i + 1) + ". " + listaEnvases.get(i).getNombre());
        }
    }

    private void agregarLoteIngrediente(Scanner scanner) {
        System.out.println("\n=== Agregar Lote de Ingrediente ===");
        mostrarListaIngredientes();
        System.out.print("Ingrese el número del ingrediente: ");
        int numIngrediente = Integer.parseInt(scanner.nextLine());
        List<Ingrediente> listaIngredientes = inventario.getIngredientes();
        if (numIngrediente < 1 || numIngrediente > listaIngredientes.size()) {
            System.out.println("Número inválido.");
            return;
        }
        Ingrediente ingrediente = listaIngredientes.get(numIngrediente - 1);

        System.out.print("¿Cuántos lotes desea agregar? ");
        int numLotes = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numLotes; i++) {
            System.out.println("=== Lote " + (i + 1) + " ===");
            String codigoLote = generarCodigoLote();
            System.out.print("Cantidad: ");
            int cantidad = Integer.parseInt(scanner.nextLine());
            System.out.print("Precio total del lote: ");
            BigDecimal precioTotal = new BigDecimal(scanner.nextLine());
            System.out.print("Fecha de Vencimiento (dd/MM/yyyy): ");
            String fechaVencimientoStr = scanner.nextLine();
            Date fechaVencimiento = parseFecha(fechaVencimientoStr);
            Lote lote = new Lote(codigoLote, cantidad, fechaVencimiento, precioTotal);
            ingrediente.agregarLote(lote);
        }

        System.out.println("Lotes agregados exitosamente al ingrediente.");
    }

    private void pasarAProduccion(Scanner scanner) {
        System.out.println("\n=== Pasar a Producción ===");
        List<Producto> listaProductos = inventario.getProductos();
        if (listaProductos.isEmpty()) {
            System.out.println("No hay productos registrados.");
            return;
        }
        for (int i = 0; i < listaProductos.size(); i++) {
            System.out.println((i + 1) + ". " + listaProductos.get(i).getNombre());
        }
        System.out.print("Ingrese el número del producto a producir: ");
        int numProducto = Integer.parseInt(scanner.nextLine());
        if (numProducto < 1 || numProducto > listaProductos.size()) {
            System.out.println("Número inválido.");
            return;
        }
        Producto producto = listaProductos.get(numProducto - 1);

        System.out.print("¿Cuántos lotes desea producir? ");
        int numLotes = Integer.parseInt(scanner.nextLine());

        for (int i = 0; i < numLotes; i++) {
            System.out.println("=== Lote de Producción " + (i + 1) + " ===");
            System.out.print("Cantidad por lote: ");
            int cantidadLote = Integer.parseInt(scanner.nextLine());
            int maxCantidadPosible = producto.getReceta().calcularMaximaProduccion();
            if (cantidadLote > maxCantidadPosible) {
                System.out.println("No hay suficientes ingredientes o envases para producir esta cantidad.");
                System.out.println("Cantidad máxima posible: " + maxCantidadPosible);
                System.out.print("¿Desea producir la cantidad máxima posible? (S/N): ");
                String respuesta = scanner.nextLine();
                if (respuesta.equalsIgnoreCase("S")) {
                    cantidadLote = maxCantidadPosible;
                } else {
                    System.out.println("Producción cancelada para este lote.");
                    continue;
                }
            }
            try {
                producto.getReceta().consumirIngredientes(cantidadLote);
                producto.getReceta().consumirEnvases(cantidadLote);
            } catch (StockBajoException e) {
                System.out.println("Error en la producción: " + e.getMessage());
                continue;
            }
            String codigoLote = generarCodigoLote();
            System.out.print("Precio total del lote: ");
            BigDecimal precioTotal = new BigDecimal(scanner.nextLine());
            System.out.print("Fecha de Vencimiento (dd/MM/yyyy): ");
            String fechaVencimientoStr = scanner.nextLine();
            Date fechaVencimiento = parseFecha(fechaVencimientoStr);
            Lote lote = new Lote(codigoLote, cantidadLote, fechaVencimiento, precioTotal);
            producto.agregarLote(lote);
            Date fechaProduccion = new Date();
            MovimientoInventario movimiento = new MovimientoInventario("Producción", fechaProduccion, producto.getNombre(), cantidadLote);
            movimientos.add(movimiento);
            System.out.println("Lote de producción agregado exitosamente.");
        }
    }

    private String generarCodigoLote() {
        return UUID.randomUUID().toString();
    }

    private Date parseFecha(String fechaStr) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        try {
            return sdf.parse(fechaStr);
        } catch (Exception e) {
            System.out.println("Fecha inválida. Se usará la fecha actual.");
            return new Date();
        }
    }

    private void generarReportes(Scanner scanner) {
        System.out.println("\n=== Generar Reportes ===");
        System.out.println("1. Reporte de Productos");
        System.out.println("2. Reporte de Ingredientes");
        System.out.println("3. Reporte de Envases");
        System.out.println("4. Reporte General");
        System.out.print("Seleccione una opción: ");
        int opcion = Integer.parseInt(scanner.nextLine());

        switch (opcion) {
            case 1:
                inventario.generarReporteProductos();
                break;
            case 2:
                inventario.generarReporteIngredientes();
                break;
            case 3:
                inventario.generarReporteEnvases();
                break;
            case 4:
                inventario.generarReporteGeneral();
                break;
            default:
                System.out.println("Opción inválida.");
        }
    }

    private void reporteMovimientos() {
        System.out.println("\n=== Reporte de Movimientos ===");
        for (MovimientoInventario movimiento : movimientos) {
            System.out.println(movimiento);
        }
    }

    private void buscarIngredienteOEnvase(Scanner scanner) {
        System.out.println("\n=== Buscar Ingrediente o Envase ===");
        System.out.print("Ingrese el nombre a buscar: ");
        String nombreBusqueda = scanner.nextLine();

        Ingrediente ingrediente = inventario.buscarIngrediente(nombreBusqueda);
        if (ingrediente != null) {
            System.out.println("Ingrediente encontrado:");
            System.out.println(ingrediente);
            return;
        }

        Envase envase = inventario.buscarEnvase(nombreBusqueda);
        if (envase != null) {
            System.out.println("Envase encontrado:");
            System.out.println(envase);
            return;
        }

        System.out.println("No se encontró ningún ingrediente o envase con ese nombre.");
    }
}
