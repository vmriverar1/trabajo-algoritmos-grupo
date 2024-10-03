package Trabajo;

import java.util.*;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;

public class SistemaInventario {
    private final Inventario inventario; // Objeto que gestiona los ingredientes, envases y productos.
    private Usuario usuarioActual; // Usuario actualmente autenticado en el sistema.
    private static final HashMap<String, String> credenciales = new HashMap<>(); // Almacena las credenciales de los usuarios.
    private final List<MovimientoInventario> movimientos; // Lista de movimientos de inventario (producción, entrada de stock, etc.).

    // Constructor que inicializa el inventario y la lista de movimientos.
    public SistemaInventario() {
        this.inventario = new Inventario();
        this.movimientos = new ArrayList<>();
    }

    // Método principal que inicia el sistema.
    public static void main(String[] args) {
        SistemaInventario sistema = new SistemaInventario(); // Crea una instancia de SistemaInventario.
        sistema.iniciar(); // Llama al método para iniciar el sistema.
    }

    // Método que inicia el sistema cargando los datos de usuarios y mostrando el menú.
    public void iniciar() {
        String filePath = "src/Usuarios.txt"; // Ruta del archivo con las credenciales de los usuarios.
        LectorUsuarioContrasena lector = LectorUsuarioContrasena.getInstance(); // Obtiene la instancia del lector de credenciales.
        int resultado = lector.leerArchivoUsuarioYContrasena(filePath, credenciales); // Carga las credenciales desde el archivo.

        if (resultado == -1) { // Si hubo un error al cargar el archivo.
            System.out.println("No se pudo cargar el archivo de usuarios.");
            return;
        }

        cargarDatosIniciales(); // Carga los datos iniciales del inventario.

        autenticarUsuario(); // Llama al método para autenticar al usuario.
        mostrarMenu(); // Muestra el menú principal del sistema.
    }

    // Método que carga los datos iniciales del inventario desde un archivo.
    private void cargarDatosIniciales() {
        String dataFilePath = "src/data.txt"; // Ruta del archivo de datos.
        DataLoader dataLoader = new DataLoader(); // Crea una instancia del cargador de datos.
        dataLoader.cargarDatos(dataFilePath, inventario); // Carga los datos en el inventario.
    }

    // Método que autentica al usuario con sus credenciales.
    private void autenticarUsuario() {
        Scanner scanner = new Scanner(System.in);
        boolean autenticado = false;
        do {
            System.out.println("=== Sistema de Autenticación ===");
            System.out.print("Ingrese su nombre de usuario: ");
            String nombreUsuario = scanner.nextLine();
            System.out.print("Ingrese su contraseña: ");
            String contrasena = scanner.nextLine();

            // Verifica si el usuario y la contraseña son correctos.
            if (credenciales.containsKey(nombreUsuario) && credenciales.get(nombreUsuario).equals(contrasena)) {
                usuarioActual = new Usuario(nombreUsuario, contrasena); // Crea el objeto del usuario autenticado.
                autenticado = true;
                System.out.println("Autenticación exitosa. Bienvenido, " + nombreUsuario + "!");
            } else {
                System.out.println("Credenciales incorrectas. Intente nuevamente.");
            }
        } while (!autenticado); // Sigue pidiendo credenciales hasta que el usuario sea autenticado.
    }

    // Método que muestra el menú principal del sistema.
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
                System.out.println("6. Agregar Stock de Envase");
                System.out.println("7. Crear Orden de Producción");
                System.out.println("8. Reporte de Producciones");
                System.out.println("9. Buscar Ingrediente o Envase");
                System.out.println("0. Salir");
                System.out.print("Seleccione una opción: ");
                String opcionStr = scanner.nextLine();
                opcion = Integer.parseInt(opcionStr); // Convierte la opción seleccionada a entero.

                // Ejecuta la opción seleccionada.
                switch (opcion) {
                    case 1:
                        registrarIngrediente(scanner); // Registra un nuevo ingrediente.
                        break;
                    case 2:
                        registrarEnvase(scanner); // Registra un nuevo envase.
                        break;
                    case 3:
                        registrarProducto(scanner); // Registra un nuevo producto.
                        break;
                    case 4:
                        generarReportes(scanner); // Genera reportes del inventario.
                        break;
                    case 5:
                        agregarLoteIngrediente(scanner); // Agrega stock a un ingrediente.
                        break;
                    case 6:
                        agregarLoteEnvase(scanner); // Agrega stock a un envase.
                        break;
                    case 7:
                        pasarAProduccion(scanner); // Crea una orden de producción.
                        break;
                    case 8:
                        reporteMovimientos(); // Muestra el reporte de movimientos de producción.
                        break;
                    case 9:
                        buscarIngredienteOEnvase(scanner); // Busca un ingrediente o envase.
                        break;
                    case 0:
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
        } while (opcion != 0); // Se repite el menú hasta que se elige la opción de salir.
    }

    // Método para registrar un nuevo ingrediente.
    private void registrarIngrediente(Scanner scanner) {
        System.out.println("\n=== Registrar Ingrediente ===");
        try {
            System.out.print("Nombre del ingrediente: ");
            String nombre = scanner.nextLine();
            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();

            Ingrediente ingrediente = new Ingrediente(nombre, categoria); // Crea el nuevo ingrediente.

            System.out.print("¿Cuántos lotes desea agregar? (Ingrese 0 para cancelar): ");
            int numLotes = Integer.parseInt(scanner.nextLine());
            if (numLotes <= 0) {
                System.out.println("Registro de ingrediente cancelado.");
                return;
            }

            // Agrega la cantidad de lotes especificados al ingrediente.
            for (int i = 0; i < numLotes; i++) {
                System.out.println("=== Lote " + (i + 1) + " ===");
                String codigoLote = generarCodigoLote();
                BigDecimal cantidad = leerCantidad(scanner, "Cantidad (ejemplo: 100.5): ");
                BigDecimal precioTotal = leerCantidad(scanner, "Precio total del lote (ejemplo: 500.00): ");
                Date fechaVencimiento = leerFecha(scanner, "Fecha de Vencimiento (dd/MM/yyyy): ");
                Lote lote = new Lote(codigoLote, cantidad, fechaVencimiento, precioTotal);
                ingrediente.agregarLote(lote);
            }

            inventario.agregarIngrediente(ingrediente); // Agrega el ingrediente al inventario.
            System.out.println("Ingrediente registrado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Regresando al menú principal.");
        }
    }

    // Método para registrar un nuevo envase.
    private void registrarEnvase(Scanner scanner) {
        System.out.println("\n=== Registrar Envase ===");
        try {
            System.out.print("Nombre del envase: ");
            String nombre = scanner.nextLine();
            System.out.print("Tipo: ");
            String tipo = scanner.nextLine();

            Envase envase = new Envase(nombre, tipo); // Crea el nuevo envase.

            System.out.print("¿Cuántos lotes desea agregar? (Ingrese 0 para cancelar): ");
            int numLotes = Integer.parseInt(scanner.nextLine());
            if (numLotes <= 0) {
                System.out.println("Registro de envase cancelado.");
                return;
            }

            // Agrega la cantidad de lotes especificados al envase.
            for (int i = 0; i < numLotes; i++) {
                System.out.println("=== Lote " + (i + 1) + " ===");
                String codigoLote = generarCodigoLote();
                BigDecimal cantidad = leerCantidad(scanner, "Cantidad (ejemplo: 1000): ");
                BigDecimal precioTotal = leerCantidad(scanner, "Precio total del lote (ejemplo: 2000.00): ");
                Lote lote = new Lote(codigoLote, cantidad, precioTotal);
                envase.agregarLote(lote);
            }

            inventario.agregarEnvase(envase); // Agrega el envase al inventario.
            System.out.println("Envase registrado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Regresando al menú principal.");
        }
    }

    // Este método permite registrar un nuevo producto en el inventario, asignándole ingredientes y envases.
    private void registrarProducto(Scanner scanner) {
        System.out.println("\n=== Registrar Producto ===");
        try {
            // Solicitar al usuario el nombre y la categoría del producto.
            System.out.print("Nombre del producto: ");
            String nombre = scanner.nextLine();
            System.out.print("Categoría: ");
            String categoria = scanner.nextLine();

            // Crear una nueva receta para el producto.
            Receta receta = new Receta();

            // Preguntar cuántos ingredientes requiere la receta.
            System.out.print("¿Cuántos ingredientes requiere la receta? (Ingrese 0 para cancelar): ");
            int numIngredientes = Integer.parseInt(scanner.nextLine());
            if (numIngredientes <= 0) {
                System.out.println("Registro de producto cancelado.");
                return;
            }

            // Obtener la lista de ingredientes disponibles en el inventario.
            List<Ingrediente> listaIngredientes = inventario.getIngredientes();
            for (int i = 0; i < numIngredientes; i++) {
                while (true) {
                    // Mostrar la lista de ingredientes para que el usuario seleccione uno.
                    System.out.println("=== Seleccione Ingrediente " + (i + 1) + " ===");
                    mostrarListaIngredientes();
                    System.out.print("Ingrese el número del ingrediente o 0 para cancelar: ");
                    int numIngrediente = Integer.parseInt(scanner.nextLine());
                    if (numIngrediente == 0) {
                        System.out.println("Registro de producto cancelado.");
                        return;
                    }
                    // Validar la selección del ingrediente.
                    if (numIngrediente < 1 || numIngrediente > listaIngredientes.size()) {
                        System.out.println("Número inválido. Intente nuevamente.");
                        continue;
                    }
                    // Agregar el ingrediente seleccionado a la receta.
                    Ingrediente ingrediente = listaIngredientes.get(numIngrediente - 1);
                    BigDecimal cantidad = leerCantidad(scanner, "Cantidad requerida (ejemplo: 0.5): ");
                    receta.agregarIngrediente(ingrediente, cantidad);
                    break;
                }
            }

            // Preguntar cuántos envases requiere la receta.
            System.out.print("¿Cuántos envases requiere la receta? (Ingrese 0 para cancelar): ");
            int numEnvases = Integer.parseInt(scanner.nextLine());
            if (numEnvases < 0) {
                System.out.println("Entrada inválida. Regresando al menú principal.");
                return;
            }
            // Obtener la lista de envases disponibles en el inventario.
            List<Envase> listaEnvases = inventario.getEnvases();
            for (int i = 0; i < numEnvases; i++) {
                while (true) {
                    // Mostrar la lista de envases para que el usuario seleccione uno.
                    System.out.println("=== Seleccione Envase " + (i + 1) + " ===");
                    mostrarListaEnvases();
                    System.out.print("Ingrese el número del envase o 0 para cancelar: ");
                    int numEnvase = Integer.parseInt(scanner.nextLine());
                    if (numEnvase == 0) {
                        System.out.println("Registro de producto cancelado.");
                        return;
                    }
                    // Validar la selección del envase.
                    if (numEnvase < 1 || numEnvase > listaEnvases.size()) {
                        System.out.println("Número inválido. Intente nuevamente.");
                        continue;
                    }
                    // Agregar el envase seleccionado a la receta.
                    Envase envase = listaEnvases.get(numEnvase - 1);
                    BigDecimal cantidad = leerCantidad(scanner, "Cantidad requerida (ejemplo: 1): ");
                    receta.agregarEnvase(envase, cantidad);
                    break;
                }
            }

            // Ajustar la receta de acuerdo con la disponibilidad del inventario.
            receta.ajustarReceta();

            // Crear el producto con la receta completa.
            Producto producto = new Producto(nombre, categoria, receta);

            // Agregar el producto al inventario.
            inventario.agregarProducto(producto);
            System.out.println("Producto registrado exitosamente.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Regresando al menú principal.");
        } catch (Exception e) {
            System.out.println("Error al registrar producto: " + e.getMessage());
        }
    }

    // Mostrar lista de ingredientes disponibles en el inventario.
    private void mostrarListaIngredientes() {
        List<Ingrediente> listaIngredientes = inventario.getIngredientes();
        for (int i = 0; i < listaIngredientes.size(); i++) {
            System.out.println((i + 1) + ". " + listaIngredientes.get(i).getNombre());
        }
    }

    // Mostrar lista de envases disponibles en el inventario.
    private void mostrarListaEnvases() {
        List<Envase> listaEnvases = inventario.getEnvases();
        for (int i = 0; i < listaEnvases.size(); i++) {
            System.out.println((i + 1) + ". " + listaEnvases.get(i).getNombre());
        }
    }

    // Método para agregar lotes adicionales a un ingrediente existente.
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

            // Agregar cada lote al ingrediente.
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

    // Método para agregar lotes adicionales a un envase existente.
    private void agregarLoteEnvase(Scanner scanner) {
        System.out.println("\n=== Agregar Stock de Envase ===");
        try {
            mostrarListaEnvases();
            System.out.print("Ingrese el número del envase o 0 para cancelar: ");
            int numEnvase = Integer.parseInt(scanner.nextLine());
            if (numEnvase == 0) {
                System.out.println("Operación cancelada.");
                return;
            }
            List<Envase> listaEnvases = inventario.getEnvases();
            if (numEnvase < 1 || numEnvase > listaEnvases.size()) {
                System.out.println("Número inválido.");
                return;
            }
            Envase envase = listaEnvases.get(numEnvase - 1);

            System.out.print("¿Cuántos lotes desea agregar? (Ingrese 0 para cancelar): ");
            int numLotes = Integer.parseInt(scanner.nextLine());
            if (numLotes <= 0) {
                System.out.println("Operación cancelada.");
                return;
            }

            // Agregar cada lote al envase.
            for (int i = 0; i < numLotes; i++) {
                System.out.println("=== Lote " + (i + 1) + " ===");
                String codigoLote = generarCodigoLote();
                BigDecimal cantidad = leerCantidad(scanner, "Cantidad (ejemplo: 500): ");
                BigDecimal precioTotal = leerCantidad(scanner, "Precio total del lote (ejemplo: 800.00): ");
                Lote lote = new Lote(codigoLote, cantidad, precioTotal);
                envase.agregarLote(lote);
            }

            System.out.println("Lotes agregados exitosamente al envase.");
        } catch (NumberFormatException e) {
            System.out.println("Entrada inválida. Regresando al menú principal.");
        }
    }

    // Método para generar un código de lote único.
    private String generarCodigoLote() {
        return UUID.randomUUID().toString();
    }

    // Método para leer y validar una fecha ingresada por el usuario.
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

    // Método para leer y validar una cantidad ingresada por el usuario.
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

    // Método para pasar un producto a producción, verificando la disponibilidad de los ingredientes y envases.
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

            // Proceso de producción de cada lote.
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

    // Método para generar reportes del inventario.
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

    // Método para generar un reporte de todos los movimientos de producción.
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

    // Método para buscar ingredientes, envases o productos en el inventario.
    private void buscarIngredienteOEnvase(Scanner scanner) {
        System.out.println("\n=== Buscar Ingrediente, Envase o Producto ===");
        System.out.print("Ingrese el nombre a buscar o '0' para cancelar: ");
        String nombreBusqueda = scanner.nextLine();
        if (nombreBusqueda.equals("0")) {
            System.out.println("Operación cancelada.");
            return;
        }

        // Buscar en el inventario ingredientes, envases y productos que coincidan con el nombre ingresado.
        List<Ingrediente> ingredientesEncontrados = inventario.buscarIngredientes(nombreBusqueda);
        List<Envase> envasesEncontrados = inventario.buscarEnvases(nombreBusqueda);
        List<Producto> productosEncontrados = inventario.buscarProductos(nombreBusqueda);

        boolean encontrado = false;

        // Mostrar los ingredientes encontrados.
        if (!ingredientesEncontrados.isEmpty()) {
            System.out.println("Ingredientes encontrados:");
            for (int i = 0; i < ingredientesEncontrados.size(); i++) {
                System.out.println((i + 1) + ". " + ingredientesEncontrados.get(i).getNombre());
            }
            System.out.print("Seleccione un ingrediente por número o 0 para continuar: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion > 0 && opcion <= ingredientesEncontrados.size()) {
                System.out.println(ingredientesEncontrados.get(opcion - 1));
            }
            encontrado = true;
        }

        // Mostrar los envases encontrados.
        if (!envasesEncontrados.isEmpty()) {
            System.out.println("Envases encontrados:");
            for (int i = 0; i < envasesEncontrados.size(); i++) {
                System.out.println((i + 1) + ". " + envasesEncontrados.get(i).getNombre());
            }
            System.out.print("Seleccione un envase por número o 0 para continuar: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion > 0 && opcion <= envasesEncontrados.size()) {
                System.out.println(envasesEncontrados.get(opcion - 1));
            }
            encontrado = true;
        }

        // Mostrar los productos encontrados.
        if (!productosEncontrados.isEmpty()) {
            System.out.println("Productos encontrados:");
            for (int i = 0; i < productosEncontrados.size(); i++) {
                System.out.println((i + 1) + ". " + productosEncontrados.get(i).getNombre());
            }
            System.out.print("Seleccione un producto por número o 0 para continuar: ");
            int opcion = Integer.parseInt(scanner.nextLine());
            if (opcion > 0 && opcion <= productosEncontrados.size()) {
                System.out.println(productosEncontrados.get(opcion - 1));
            }
            encontrado = true;
        }

        if (!encontrado) {
            System.out.println("No se encontró ningún ingrediente, envase o producto con ese nombre.");
        }
    }
}