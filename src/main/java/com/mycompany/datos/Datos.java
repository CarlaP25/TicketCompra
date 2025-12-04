/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.datos;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

/**
 * Clase principal que gestiona la interacción con el usuario,
 * coordina los cálculos de descuentos y presenta el ticket de compra.
 */
public class Datos {
    
    /**
     * Constructor de Datos
     */
     public Datos (){
     }
     
    /**
     * Punto de entrada del programa.
     * Gestiona la secuencia de obtención de datos, cálculo y presentación de resultados.
     * @param args Argumentos de la línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        Scanner lector = new Scanner(System.in);
        
        //Entradas del Usuario
        String nombreCliente = obtenerNombre(lector);
        LocalDate fechaNacimiento = obtenerFecha("nacimiento", lector);
        
        // La fecha actual se obtiene del sistema
        LocalDate fechaActual = LocalDate.now(); 
        
        List<Producto> listaProductos = obtenerListaProductos(lector);
        
        // Procesamiento
        CalculadoraDescuento calculadora = new CalculadoraDescuento(fechaNacimiento, fechaActual);
        
        double importeBruto = 0.0;
        for (Producto p : listaProductos) {
            importeBruto += p.getSubtotal();
        }
        
        // Cálculo de Descuentos
        double porcentajeDescuentoEdad = calculadora.calcularPorcentajeDescuentoEdad();
        double porcentajeDescuentoCumple = calculadora.calcularPorcentajeDescuentoCumpleanios();
        
        // Cálculo del descuento total aplicado (máximo 30%)
        double porcentajeDescuentoTotalAplicado = calculadora.aplicarDescuentos(porcentajeDescuentoEdad, porcentajeDescuentoCumple);
        
        double descuentoTotal = importeBruto * (porcentajeDescuentoTotalAplicado / 100.0);
        double importeFinal = importeBruto - descuentoTotal;
        
        // Salida del Programa (Ticket)
        imprimirTicket(
            nombreCliente,
            calculadora.calcularEdadAnios(),
            listaProductos,
            importeBruto,
            porcentajeDescuentoEdad,
            porcentajeDescuentoCumple,
            porcentajeDescuentoTotalAplicado,
            importeFinal,
            calculadora.getDiasHastaCumpleanios()
        );
        
        lector.close();
    }
    
    /**
     * Solicita al usuario su nombre completo.
     * @param lector El objeto Scanner para leer la entrada del usuario.
     * @return El nombre completo del cliente como String.
     */
    private static String obtenerNombre(Scanner lector) {
        System.out.println("--- Inicio del Programa de Descuentos ---");
        System.out.print("Ingrese su nombre completo: ");
        return lector.nextLine();
    }

    /**
     * Solicita y valida la fecha de nacimiento del cliente.
     * El formato esperado es DD/MM/AAAA.
     * @param tipo Indica el tipo de fecha que se está solicitando ("nacimiento" o "actual").
     * @param lector El objeto Scanner para leer la entrada del usuario.
     * @return Un objeto LocalDate con la fecha validada.
     */
    private static LocalDate obtenerFecha(String tipo, Scanner lector) {
        DateTimeFormatter formato = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate fecha = null;
        boolean entradaValida = false;
        
        while (!entradaValida) {
            System.out.print("Ingrese su fecha de " + tipo + " (formato DD/MM/AAAA): ");
            String entrada = lector.nextLine();
            try {
                fecha = LocalDate.parse(entrada, formato);
                entradaValida = true;
            } catch (DateTimeParseException e) {
                System.out.println("Error: Formato de fecha inválido. Por favor, use DD/MM/AAAA.");
            }
        }
        return fecha;
    }

    /**
     * Solicita al usuario la lista de productos comprados, incluyendo validación de precio y cantidad.
     * @param lector El objeto Scanner para leer la entrada del usuario.
     * @return Una lista de objetos Producto.
     */
    private static List<Producto> obtenerListaProductos(Scanner lector) {
        List<Producto> productos = new ArrayList<>();
        System.out.println("\n--- Registro de Productos ---");
        
        while (true) {
            System.out.print("Nombre del producto (o 'FIN' para terminar): ");
            String nombre = lector.nextLine().trim();
            if (nombre.equalsIgnoreCase("FIN") || nombre.isEmpty()) {
                break;
            }

            double precio = -1;
            while (precio < 0) {
                try {
                    System.out.print("Precio unitario de " + nombre + ": ");
                    precio = lector.nextDouble();
                    if (precio < 0) {
                        System.out.println("Error: El precio no puede ser negativo.");
                        precio = -1; // Reset para seguir en el bucle
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Entrada inválida. Ingrese un número para el precio.");
                    lector.nextLine(); // Limpiar buffer
                }
            }

            int cantidad = -1;
            while (cantidad <= 0) {
                try {
                    System.out.print("Cantidad comprada de " + nombre + ": ");
                    cantidad = lector.nextInt();
                    if (cantidad <= 0) {
                        System.out.println("Error: La cantidad debe ser positiva.");
                    }
                } catch (InputMismatchException e) {
                    System.out.println("Error: Entrada inválida. Ingrese un número entero para la cantidad.");
                    lector.nextLine(); // Limpiar buffer
                }
            }
            lector.nextLine(); // Limpiar buffer después de nextDouble/nextInt
            
            productos.add(new Producto(nombre, precio, cantidad));
            System.out.println("Producto añadido. Subtotal: " + String.format("%.2f", precio * cantidad) + " €");
        }
        
        if (productos.isEmpty()) {
            System.out.println("No se registraron productos.");
        }
        return productos;
    }

    /**
     * Muestra el resumen de la compra, incluyendo el detalle de los descuentos aplicados.
     * @param nombre Nombre del cliente.
     * @param edad Edad calculada del cliente.
     * @param lista Lista de productos comprados.
     * @param bruto Importe total de la compra antes de descuentos.
     * @param descEdad Porcentaje de descuento aplicado por edad.
     * @param descCumple Porcentaje de descuento aplicado por proximidad de cumpleaños.
     * @param descTotalPorcentaje Porcentaje de descuento total final aplicado (máximo 30%).
     * @param totalPagar Importe final a pagar después de aplicar el descuento total.
     * @param diasHastaCumple Días restantes para el próximo cumpleaños, usado para el mensaje personalizado.
     */
    private static void imprimirTicket(
        String nombre, int edad, List<Producto> lista, double bruto,
        double descEdad, double descCumple, double descTotalPorcentaje,
        double totalPagar, long diasHastaCumple
    ) {
        System.out.println("\n=============================================");
        System.out.println("          TICKET DE COMPRA Y DESCUENTOS      ");
        System.out.println("=============================================");
        System.out.println("Cliente: " + nombre);
        System.out.println("Edad calculada: " + edad + " años");
        System.out.println("---------------------------------------------");
        System.out.println("Productos Comprados:");
        
        for (Producto p : lista) {
            System.out.printf("  %s (%d uds. @ %.2f €) -> Subtotal: %.2f €\n", 
                p.getNombre(), p.getCantidad(), p.getPrecioUnitario(), p.getSubtotal());
        }
        
        System.out.println("---------------------------------------------");
        System.out.printf("Importe Bruto:                             %.2f €\n", bruto);
        System.out.printf("Desc. por Edad (%.0f%%):                    %.2f €\n", descEdad, bruto * (descEdad / 100.0));
        System.out.printf("Desc. por Cumpleaños (%.0f%%):             %.2f €\n", descCumple, bruto * (descCumple / 100.0));
        System.out.println("---------------------------------------------");
        System.out.printf("DESCUENTO TOTAL APLICADO (Máx 30%%): (%.0f%%)\n", descTotalPorcentaje);
        double descuentoAplicadoEuros = bruto * (descTotalPorcentaje / 100.0);
        System.out.printf("Total Descuento en €:                       -%.2f €\n", descuentoAplicadoEuros);
        System.out.println("---------------------------------------------");
        System.out.printf("TOTAL A PAGAR:                             %.2f €\n", totalPagar);
        System.out.println("=============================================");

        // Mensaje personalizado
        if (diasHastaCumple <= 14) {
            System.out.println("\n¡Mensaje Especial!");
            if (diasHastaCumple <= 7) {
                 System.out.println("¡Feliz cumplea\u00F1os adelantado! Disfruta de tu descuento especial del 20%.");
            } else {
                 System.out.println("¡Tu cumplea\u00F1os está cerca! Disfruta de tu descuento especial del 10%.");
            }
           
        }
    }
}