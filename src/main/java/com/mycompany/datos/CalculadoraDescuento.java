/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.datos;

import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

/**
 * Clase que contiene la lógica para calcular la edad, los días hasta el cumpleaños
 * y determinar el porcentaje de descuento.
 */
public class CalculadoraDescuento {
    private final LocalDate fechaNacimiento;
    private final LocalDate fechaActual;
    private final int EDAD_MINIMA_ADULTO = 18;
    private final int EDAD_MAXIMA_ADULTO = 65;
    private final double DESCUENTO_MAXIMO = 30.0; // 30%

    /**
     * Constructor para la Calculadora de Descuentos.
     * @param fechaNacimiento La fecha de nacimiento del cliente.
     * @param fechaActual La fecha utilizada para calcular la edad y la proximidad del cumpleaños.
     */
    public CalculadoraDescuento(LocalDate fechaNacimiento, LocalDate fechaActual) {
        this.fechaNacimiento = fechaNacimiento;
        this.fechaActual = fechaActual;
    }

    /**
     * Calcula la edad del cliente en años cumplidos utilizando la API Period.
     * @return La edad del cliente en años enteros.
     */
    public int calcularEdadAnios() {
        return Period.between(fechaNacimiento, fechaActual).getYears();
    }

    /**
     * Calcula los días restantes hasta el próximo cumpleaños del cliente.
     * @return El número de días hasta el próximo cumpleaños (long).
     */
    public long calcularDiasHastaCumpleanios() {
        // Obtenemos la fecha del cumpleaños en el año actual
        LocalDate cumpleaniosHoy = fechaNacimiento.withYear(fechaActual.getYear());
        
        // Si el cumpleaños ya pasó este año, lo movemos al próximo año.
        if (cumpleaniosHoy.isBefore(fechaActual) || cumpleaniosHoy.isEqual(fechaActual)) {
            cumpleaniosHoy = cumpleaniosHoy.plusYears(1);
        }
        
        // Calculamos los días restantes
        return ChronoUnit.DAYS.between(fechaActual, cumpleaniosHoy);
    }
    
    /**
     * Get para obtener los días hasta el cumpleaños.
     * @return Los días restantes hasta el próximo cumpleaños.
     */
    public long getDiasHastaCumpleanios() {
        return calcularDiasHastaCumpleanios();
    }

    /**
     * Calcula el porcentaje de descuento aplicable basado en la edad del cliente.
     * Reglas:
     * Menores de 18: 10%
     * Mayores de 65: 15%
     * Entre 18 y 65: 0%
     * @return El porcentaje de descuento por edad (double).
     */
    public double calcularPorcentajeDescuentoEdad() {
        int edad = calcularEdadAnios();
        
        if (edad < EDAD_MINIMA_ADULTO) {
            return 10.0; 
        } else if (edad > EDAD_MAXIMA_ADULTO) {
            return 15.0; 
        } else {
            return 0.0; 
        }
    }

    /**
     * Calcula el porcentaje de descuento aplicable basado en la proximidad del cumpleaños.
     * Reglas:
     * Próximos 7 días: 20%
     * Entre 8 y 14 días: 10%
     * En cualquier otro caso: 0%
     * @return El porcentaje de descuento por proximidad de cumpleaños (double).
     */
    public double calcularPorcentajeDescuentoCumpleanios() {
        long dias = calcularDiasHastaCumpleanios();
        
        if (dias <= 7) {
            return 20.0; 
        } else if (dias <= 14) {
            return 10.0; 
        } else {
            return 0.0; 
        }
    }
    
    /**
     * Combina los descuentos por edad y cumpleaños, aplicando el límite máximo del 30%.
     * @param descEdad Porcentaje de descuento por edad.
     * @param descCumple Porcentaje de descuento por cumpleaños.
     * @return El porcentaje de descuento total final aplicado, limitado al 30%.
     */
    public double aplicarDescuentos(double descEdad, double descCumple) {
        // Los descuentos se acumulan sumando los porcentajes
        double descuentoAcumulado = descEdad + descCumple;
        
        // El descuento total no puede superar el 30%
        return Math.min(descuentoAcumulado, DESCUENTO_MAXIMO);
    }
}
