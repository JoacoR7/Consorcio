/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

/**
 *
 * @author joaqu
 */
@ManagedBean
@ViewScoped
public class ControllerDireccion {

    private String calle;
    private String numero;
    private String cp;
    private String localidad;
    private String provincia;

    public String search() {
        // Expresión regular para eliminar los prefijos "Avenida", "Avenida ", "Av." o "Av. "
        String regex = "^(Avenida |Avenida|Av\\. |Av\\.)";

        // Eliminar el prefijo de la dirección en calle si existe
        calle = calle.replaceFirst(regex, "");

        // Separar la dirección restante por palabras utilizando el espacio como delimitador
        String[] palabrasCalle = calle.split(" ");

        // Crear un StringBuilder para formar la URL
        StringBuilder url = new StringBuilder("https://www.google.com/maps/dir/");

        // Concatenar las palabras de la calle en la URL
        for (int i = 0; i < palabrasCalle.length; i++) {
            url.append(palabrasCalle[i]);
            if (i < palabrasCalle.length - 1) {
                url.append("+");
            }
        }

        // Concatenar el número, código postal (cp)
        url.append("+").append(numero).append("+").append(cp);

        // Concatenar localidad, reemplazando espacios por "+"
        String[] palabrasLocalidad = localidad.split(" ");
        for (int i = 0; i < palabrasLocalidad.length; i++) {
            url.append("+").append(palabrasLocalidad[i]);
        }

        // Concatenar provincia, reemplazando espacios por "+"
        String[] palabrasProvincia = provincia.split(" ");
        for (int i = 0; i < palabrasProvincia.length; i++) {
            url.append("+").append(palabrasProvincia[i]);
        }

        // Retornar la URL final
        return url.toString();
    }

    public String getCalle() {
        return calle;
    }

    public void setCalle(String calle) {
        this.calle = calle;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCp() {
        return cp;
    }

    public void setCp(String cp) {
        this.cp = cp;
    }

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

}
