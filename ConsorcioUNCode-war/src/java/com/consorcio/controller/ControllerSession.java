package com.consorcio.controller;

import java.io.Serializable;
import java.util.HashMap;
import javax.faces.application.NavigationHandler;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

@ManagedBean(name = "ControllerSession")
@SessionScoped
public class ControllerSession implements Serializable {

    private HashMap<String, Object> entidadSeleccionada = new HashMap<>();

    // Método para seleccionar una entidad (general para cualquier ABM)
    public void seleccionarEntidad(String tipoEntidad, Object entidad) {
        entidadSeleccionada.put(tipoEntidad, entidad);
    }

    // Método para obtener la entidad seleccionada por tipo (general para cualquier ABM)
    public Object getEntidadSeleccionada(String tipoEntidad) {
        return entidadSeleccionada.get(tipoEntidad);
    }

    // Método para redirigir a la vista de modificación (general)
    public String irAModificar(String tipoEntidad, Object entidad) {
        seleccionarEntidad(tipoEntidad, entidad);  // Guardamos la entidad seleccionada
        // Redirigir a otra página
        return "modificar"+tipoEntidad;  // Redirigir a la vista de modificación correspondiente
    }
}
