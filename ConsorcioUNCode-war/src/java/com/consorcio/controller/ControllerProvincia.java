package com.consorcio.controller;

import com.consorcio.business.PaisServiceBean;
import com.consorcio.business.ProvinciaServiceBean;
import com.consorcio.entity.Pais;
import com.consorcio.entity.Provincia;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

@ManagedBean(name = "provinciaController")
@SessionScoped
public class ControllerProvincia implements Serializable {

    private @EJB ProvinciaServiceBean provinciaService;
    private @EJB PaisServiceBean paisService;
    private List<Provincia> provincias = new ArrayList<>();
    private String nombreProvincia;
    private String nombreProvinciaModificado;
    private boolean provinciaNoEncontrado = true;
    private boolean eliminado; // Atributo eliminado
    private String idPais;

    @PostConstruct
    public void init() {
        listarProvincias();
    }

    public String getNombreProvincia() {
        return nombreProvincia;
    }

    public void setNombreProvincia(String nombreProvincia) {
        this.nombreProvincia = nombreProvincia;
    }

    public String getNombreProvinciaModificado() {
        return nombreProvinciaModificado;
    }

    public void setNombreProvinciaModificado(String nombreProvinciaModificado) {
        this.nombreProvinciaModificado = nombreProvinciaModificado;
    }

    public boolean isEliminado() {
        return eliminado;
    }

    public void setEliminado(boolean eliminado) {
        this.eliminado = eliminado;
    }

    public boolean isProvinciaNoEncontrado() {
        return provinciaNoEncontrado;
    }

    public void setProvinciaNoEncontrado(boolean provinciaNoEncontrado) {
        this.provinciaNoEncontrado = provinciaNoEncontrado;
    }

    public Collection<Provincia> getProvincia() {
        return provincias;
    }

    public void guardarProvincia() {
        try {
            provinciaService.crearProvincia(nombreProvincia.toUpperCase() , idPais);
            listarProvincias();
        } catch (Exception e) {
            e.getMessage();
        }
    }

    public void listarProvincias() {
        try {
            provincias.clear();
            provincias.addAll(provinciaService.listarPais());

            // Ordenar alfabéticamente por el nombre de la provincia usando un Comparator
            Collections.sort(provincias, new Comparator<Provincia>() {
                @Override
                public int compare(Provincia p1, Provincia p2) {
                    return p1.getNombre().compareToIgnoreCase(p2.getNombre());
                }
            });

        } catch (Exception e) {
            throw e;
        }
    }

    public void buscarProvincia() {
        Provincia provincia = provinciaService.buscarProvinciaPorNombre(nombreProvincia.toUpperCase());
        if (provincia == null) {
            setNombreProvinciaModificado("No existe");
            setProvinciaNoEncontrado(true);
        } else {
            setNombreProvinciaModificado(nombreProvincia.toUpperCase());
            setProvinciaNoEncontrado(false);
        }
    }

    public void actualizarProvincia() {
        try {
            Provincia provincia = provinciaService.buscarProvinciaPorNombre(nombreProvincia.toUpperCase());

            if (provincia == null) {
                throw new IllegalArgumentException("La provincia no fue encontrada.");
            }
            
            provinciaService.modificarProvincia(provincia.getId(), nombreProvincia.toUpperCase());
        } catch (Exception e) {
            // Manejo de excepciones
        }
    }

    public void eliminarProvincia() {
        try {
            Provincia provincia = provinciaService.buscarProvinciaPorNombre(nombreProvincia.toUpperCase());
            if (provincia == null) {
                throw new IllegalArgumentException("La provincia no fue encontrada.");
            }
            provinciaService.eliminarProvincia(provincia.getId());

        } catch (Exception e) {
        }
    }

}