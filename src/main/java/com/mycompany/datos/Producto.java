/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.datos;

/**
 * Clase que representa un producto individual comprado, 
 * almacenando su nombre, precio unitario y cantidad.
 */
public class Producto {
    private String nombre;
    private double precioUnitario;
    private int cantidad;

    /**
     * Constructor para crear una instancia de Producto.
     * @param nombre El nombre del producto.
     * @param precioUnitario El costo por unidad del producto.
     * @param cantidad La cantidad comprada de este producto.
     */
    public Producto(String nombre, double precioUnitario, int cantidad) {
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    /**
     * Calcula y retorna el subtotal de este producto (precio * cantidad).
     * @return El subtotal del producto.
     */
    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

    // Getters para la impresión del ticket
    
    /**
     * Obtiene el nombre del producto.
     * @return El nombre del producto.
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Obtiene el precio unitario del producto.
     * @return El precio unitario.
     */
    public double getPrecioUnitario() {
        return precioUnitario;
    }

    /**
     * Obtiene la cantidad comprada.
     * @return La cantidad.
     */
    public int getCantidad() {
        return cantidad;
    }
}