package Trabajo;

import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class SistemaInventario {
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
        int opcion = 0;
        do {
            try {
                System.out.println("\n=== Menú Principal ===");
                System.out.println("1. Registrar Ingrediente");
                System.out.println("2. Ingresar Envases");
                System.out.println("3. Registrar Producto");
                System.out.println("4. Generar Reporte");
                System.out.println("5. Agregar Stock de Ingrediente");
                System.out.println("6. Crear Orden de Producción");
                System.out.println("7. Reporte de Producciones");
                System.out.println("8. Buscar Ingrediente o Envase");
                System.out.println("9. Salir");
                System.out.print("Seleccione una opción: ");
                String opcionStr = scanner.nextLine();
                opcion = Integer.parseInt(opcionStr);

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
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número válido.");
            } catch (Exception e) {
                System.out.println("Ocurrió un error: " + e.getMessage());
            }
        } while (opcion != 9);
    }

    private void registrarIngrediente(Scanner scanner) {
        System.out.println("\n=== Registrar Ingrediente ===");
        try {
            System.out.print("Nombre del ingrediente: ");
            String nombre = scanner.nextLine();
            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();

            Ingrediente ingrediente = new Ingrediente(nombre, categoria);

            System.out.print("¿Cuántos lotes desea agregar? (Ingrese 0 para cancelar): ");
            int numLotes = Integer.parseInt(scanner.nextLine());
            if (numLotes <= 0) {
                System.out.println("Registro de ingrediente cancelado.");
                return;
            }

            for (int i = 0; i < numLotes; i++) {
                System.out.println("=== Lote " + (i + 1) + " ===");
                String codigoLote = generarCodigoLote();
                BigDecimal cantidad = leerCantidad(scanner, "Cantidad (ejemplo: 100.5): ");
                BigDecimal precioTotal = leerCantidad(scanner, "Precio total del lote (ejemplo: 500.00): ");
                Date fechaVencimiento = leerFecha(scanner, "Fecha de Vencimiento (dd/MM/yyyy): ");
                Lote lote = new Lote(codigoLote, cantidad, fechaVencimiento, precioTotal);
                ingrediente.agregarLote(lote);
            }

            inventario.agregarIngrediente(ingrediente);
            System.out.println("Ingrediente registrado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Regresando al menú principal.");
        }
    }

    private void registrarEnvase(Scanner scanner) {
        System.out.println("\n=== Registrar Envase ===");
        try {
            System.out.print("Nombre del envase: ");
            String nombre = scanner.nextLine();
            System.out.print("Tipo: ");
            String tipo = scanner.nextLine();

            Envase envase = new Envase(nombre, tipo);

            System.out.print("¿Cuántos lotes desea agregar? (Ingrese 0 para cancelar): ");
            int numLotes = Integer.parseInt(scanner.nextLine());
            if (numLotes <= 0) {
                System.out.println("Registro de envase cancelado.");
                return;
            }

            for (int i = 0; i < numLotes; i++) {
                System.out.println("=== Lote " + (i + 1) + " ===");
                String codigoLote = generarCodigoLote();
                BigDecimal cantidad = leerCantidad(scanner, "Cantidad (ejemplo: 1000): ");
                BigDecimal precioTotal = leerCantidad(scanner, "Precio total del lote (ejemplo: 2000.00): ");
                Lote lote = new Lote(codigoLote, cantidad, precioTotal);
                envase.agregarLote(lote);
            }

            inventario.agregarEnvase(envase);
            System.out.println("Envase registrado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Regresando al menú principal.");
        }
    }

    private void registrarProducto(Scanner scanner) {
        System.out.println("\n=== Registrar Producto ===");
        try {
            System.out.print("Nombre del producto: ");
            String nombre = scanner.nextLine();
            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();

            Receta receta = new Receta();

            System.out.print("¿Cuántos ingredientes requiere la receta? (Ingrese 0 para cancelar): ");
            int numIngredientes = Integer.parseInt(scanner.nextLine());
            if (numIngredientes <= 0) {
                System.out.println("Registro de producto cancelado.");
                return;
            }

            List<Ingrediente> listaIngredientes = inventario.getIngredientes();
            for (int i = 0; i < numIngredientes; i++) {
                while (true) {
                    System.out.println("=== Seleccione Ingrediente " + (i + 1) + " ===");
                    mostrarListaIngredientes();
                    System.out.print("Ingrese el número del ingrediente o 0 para cancelar: ");
                    int numIngrediente = Integer.parseInt(scanner.nextLine());
                    if (numIngrediente == 0) {
                        System.out.println("Registro de producto cancelado.");
                        return;
                    }
                    if (numIngrediente < 1 || numIngrediente > listaIngredientes.size()) {
                        System.out.println("Número inválido. Intente nuevamente.");
                        continue;
                    }
                    Ingrediente ingrediente = listaIngredientes.get(numIngrediente - 1);
                    BigDecimal cantidad = leerCantidad(scanner, "Cantidad requerida (ejemplo: 0.5): ");
                    receta.agregarIngrediente(ingrediente, cantidad);
                    break;
                }
            }

            System.out.print("¿Cuántos envases requiere la receta? (Ingrese 0 para cancelar): ");
            int numEnvases = Integer.parseInt(scanner.nextLine());
            if (numEnvases < 0) {
                System.out.println("Entrada inválida. Regresando al menú principal.");
                return;
            }
            List<Envase> listaEnvases = inventario.getEnvases();
            for (int i = 0; i < numEnvases; i++) {
                while (true) {
                    System.out.println("=== Seleccione Envase " + (i + 1) + " ===");
                    mostrarListaEnvases();
                    System.out.print("Ingrese el número del envase o 0 para cancelar: ");
                    int numEnvase = Integer.parseInt(scanner.nextLine());
                    if (numEnvase == 0) {
                        System.out.println("Registro de producto cancelado.");
                        return;
                    }
                    if (numEnvase < 1 || numEnvase > listaEnvases.size()) {
                        System.out.println("Número inválido. Intente nuevamente.");
                        continue;
                    }
                    Envase envase = listaEnvases.get(numEnvase - 1);
                    BigDecimal cantidad = leerCantidad(scanner, "Cantidad requerida (ejemplo: 1): ");
                    receta.agregarEnvase(envase, cantidad);
                    break;
                }
            }

            receta.ajustarReceta();

            Producto producto = new Producto(nombre, categoria, receta);

            inventario.agregarProducto(producto);
            System.out.println("Producto registrado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Regresando al menú principal.");
        } catch (Exception e) {
            System.out.println("Error al registrar producto: " + e.getMessage());
        }
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
        try {
            mostrarListaIngredientes();
            System.out.print("Ingrese el número del ingrediente o 0 para cancelar: ");
            int numIngrediente = Integer.parseInt(scanner.nextLine());
            if (numIngrediente == 0) {
                System.out.println("Operación cancelada.");
                return;
            }
            List<Ingrediente> listaIngredientes = inventario.getIngredientes();
            if (numIngrediente < 1 || numIngrediente > listaIngredientes.size()) {
                System.out.println("Número inválido.");
                return;
            }
            Ingrediente ingrediente = listaIngredientes.get(numIngrediente - 1);

            System.out.print("¿Cuántos lotes desea agregar? (Ingrese 0 para cancelar): ");
            int numLotes = Integer.parseInt(scanner.nextLine());
            if (numLotes <= 0) {
                System.out.println("Operación cancelada.");
                return;
            }

            for (int i = 0; i < numLotes; i++) {
                System.out.println("=== Lote " + (i + 1) + " ===");
                String codigoLote = generarCodigoLote();
                BigDecimal cantidad = leerCantidad(scanner, "Cantidad (ejemplo: 50): ");
                BigDecimal precioTotal = leerCantidad(scanner, "Precio total del lote (ejemplo: 100.00): ");
                Date fechaVencimiento = leerFecha(scanner, "Fecha de Vencimiento (dd/MM/yyyy): ");
                Lote lote = new Lote(codigoLote, cantidad, fechaVencimiento, precioTotal);
                ingrediente.agregarLote(lote);
            }

            System.out.println("Lotes agregados exitosamente al ingrediente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Regresando al menú principal.");
        }
    }

    private void pasarAProduccion(Scanner scanner) {
        System.out.println("\n=== Crear Orden de Producción ===");
        try {
            List<Producto> listaProductos = inventario.getProductos();
            if (listaProductos.isEmpty()) {
                System.out.println("No hay productos registrados.");
                return;
            }
            for (int i = 0; i < listaProductos.size(); i++) {
                System.out.println((i + 1) + ". " + listaProductos.get(i).getNombre());
            }
            System.out.print("Ingrese el número del producto a producir o 0 para cancelar: ");
            int numProducto = Integer.parseInt(scanner.nextLine());
            if (numProducto == 0) {
                System.out.println("Operación cancelada.");
                return;
            }
            if (numProducto < 1 || numProducto > listaProductos.size()) {
                System.out.println("Número inválido.");
                return;
            }
            Producto producto = listaProductos.get(numProducto - 1);

            System.out.print("¿Cuántos lotes desea producir? (Ingrese 0 para cancelar): ");
            int numLotes = Integer.parseInt(scanner.nextLine());
            if (numLotes <= 0) {
                System.out.println("Operación cancelada.");
                return;
            }

            for (int i = 0; i < numLotes; i++) {
                System.out.println("=== Lote de Producción " + (i + 1) + " ===");
                BigDecimal cantidadLote = leerCantidad(scanner, "Cantidad por lote (ejemplo: 100): ");
                int maxCantidadPosible = producto.getReceta().calcularMaximaProduccion();
                if (cantidadLote.compareTo(new BigDecimal(maxCantidadPosible)) > 0) {
                    System.out.println("No hay suficientes ingredientes o envases para producir esta cantidad.");
                    System.out.println("Cantidad máxima posible: " + maxCantidadPosible);
                    System.out.print("¿Desea producir la cantidad máxima posible? (S/N): ");
                    String respuesta = scanner.nextLine();
                    if (respuesta.equalsIgnoreCase("S")) {
                        cantidadLote = new BigDecimal(maxCantidadPosible);
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
                BigDecimal precioTotal = leerCantidad(scanner, "Precio total del lote (ejemplo: 1500.00): ");
                Date fechaVencimiento = leerFecha(scanner, "Fecha de Vencimiento (dd/MM/yyyy): ");
                Lote lote = new Lote(codigoLote, cantidadLote, fechaVencimiento, precioTotal);
                producto.agregarLote(lote);
                Date fechaProduccion = new Date();
                MovimientoInventario movimiento = new MovimientoInventario("Producción", fechaProduccion, producto.getNombre(), cantidadLote);
                movimientos.add(movimiento);
                System.out.println("Lote de producción agregado exitosamente.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Regresando al menú principal.");
        } catch (Exception e) {
            System.out.println("Error al crear orden de producción: " + e.getMessage());
        }
    }

    private String generarCodigoLote() {
        return UUID.randomUUID().toString();
    }

    private Date leerFecha(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String fechaStr = scanner.nextLine();
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            try {
                return sdf.parse(fechaStr);
            } catch (Exception e) {
                System.out.println("Fecha inválida. Formato correcto: dd/MM/yyyy. Intente nuevamente.");
            }
        }
    }

    private BigDecimal leerCantidad(Scanner scanner, String mensaje) {
        while (true) {
            System.out.print(mensaje);
            String input = scanner.nextLine();
            try {
                BigDecimal cantidad = new BigDecimal(input);
                if (cantidad.compareTo(BigDecimal.ZERO) <= 0) {
                    System.out.println("La cantidad debe ser mayor que cero. Intente nuevamente.");
                    continue;
                }
                return cantidad;
            } catch (NumberFormatException e) {
                System.out.println("Entrada inválida. Por favor, ingrese un número válido (ejemplo: 100.5).");
            }
        }
    }

    private void generarReportes(Scanner scanner) {
        System.out.println("\n=== Generar Reportes ===");
        System.out.println("1. Reporte de Productos");
        System.out.println("2. Reporte de Ingredientes");
        System.out.println("3. Reporte de Envases");
        System.out.println("4. Reporte General");
        System.out.print("Seleccione una opción o 0 para volver: ");
        try {
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
                case 0:
                    return;
                default:
                    System.out.println("Opción inválida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida.");
        }
    }

    private void reporteMovimientos() {
        System.out.println("\n=== Reporte de Producciones ===");
        if (movimientos.isEmpty()) {
            System.out.println("No hay movimientos registrados.");
            return;
        }
        for (MovimientoInventario movimiento : movimientos) {
            System.out.println(movimiento);
        }
    }

    private void buscarIngredienteOEnvase(Scanner scanner) {
        System.out.println("\n=== Buscar Ingrediente o Envase ===");
        System.out.print("Ingrese el nombre a buscar o '0' para cancelar: ");
        String nombreBusqueda = scanner.nextLine();
        if (nombreBusqueda.equals("0")) {
            System.out.println("Operación cancelada.");
            return;
        }

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
