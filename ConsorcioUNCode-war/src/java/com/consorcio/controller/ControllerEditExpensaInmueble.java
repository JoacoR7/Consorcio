/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.ExpensaInmuebleServiceBean;
import com.consorcio.business.ExpensaServiceBean;
import com.consorcio.business.InmuebleServiceBean;
import com.consorcio.controller.messages.Messages;
import com.consorcio.controller.messages.TypeMessages;
import com.consorcio.entity.Expensa;
import com.consorcio.entity.ExpensaInmueble;
import com.consorcio.entity.Inmueble;
import com.consorcio.enums.EstadoExpensaInmueble;
import com.consorcio.enums.Mes;
import com.consorcio.util.UtilFechaBean;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

/**
 *
 * @author victo
 */
@ManagedBean
@ViewScoped
public class ControllerEditExpensaInmueble {

    /**
     * Creates a new instance of ControllerEditExpensaInmueble
     */
    private @EJB
    ExpensaInmuebleServiceBean expensaInmuebleService;

    private String casoDeUso;
    private ExpensaInmueble expensaInmueble;
    private Date fechaVencimiento;
    private boolean disableButton;
    private EstadoExpensaInmueble estado;
    private double importe;
    private Mes mes;
    private long anio;
    private String idInmueble;
    private String idExpensa;
    private Collection<SelectItem> expensas;
    private Collection<SelectItem> inmuebles;
    private Collection<SelectItem> meses;
    private Collection<SelectItem> anios;
    private Collection<SelectItem> estados;
    private @EJB
    InmuebleServiceBean inmuebleService;
    private @EJB
    ExpensaServiceBean expensaService;

    @PostConstruct
    public void init() {
        try {
            casoDeUso = (String) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("ACCION");
            expensaInmueble = (ExpensaInmueble) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("EXPENSAINMUEBLE");

            if (casoDeUso.equals("ALTA")) {
                cargaComboInmueble();
                cargaComboExpensa();
                cargaComboAnio();
                cargaComboMes();
                cargaComboEstados();
                        
            } else if (casoDeUso.equals("MODIFICAR") || casoDeUso.equals("CONSULTAR")) {

                importe = expensaInmueble.getExpensa().getImporte();
                mes = expensaInmueble.getMes();
                anio = expensaInmueble.getAnio();
                idInmueble = expensaInmueble.getInmueble().getId();
                idExpensa = expensaInmueble.getExpensa().getId();
                estado = expensaInmueble.getEstado();
                fechaVencimiento = new Date(); // Para la fecha de vencimiento en caso de modificar
                
                cargaComboInmueble();
                cargaComboExpensa();
                cargaComboAnio();
                cargaComboMes();
                cargaComboEstados();

                if (casoDeUso.equals("CONSULTAR")) {
                    disableButton = true;
                }
            }
            

        } catch (Exception e) {
            e.getMessage();
        }
    }

    public String aceptar() {
        try {
            if (casoDeUso.equals("ALTA")) {
                expensaInmuebleService.crearExpensaInmueble(idExpensa, idInmueble, mes, anio);
            } else if (casoDeUso.equals("MODIFICAR")) {
                expensaInmuebleService.modificarExpensaInmueble(expensaInmueble.getId(), idExpensa, idInmueble, mes, anio, estado, fechaVencimiento);
            }

            return "listarExpensaInmueble";

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public String cancelar() {
        return "listarExpensaInmueble";
    }

    public void cargaComboInmueble() {
        try {

            inmuebles = new ArrayList<>();
            inmuebles.add(new SelectItem(null, "Seleccione..."));
            for (Inmueble inmueble : inmuebleService.listarInmueble()) {
                inmuebles.add(new SelectItem(inmueble.getId(), inmueble.getPiso() + " - " + inmueble.getPuerta() + " | Propiestario: " + inmueble.getPropietario().getNombre() + " | Estado Inmueble: " + inmueble.getEstado().toString()));
            }

        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public void cargaComboExpensa() {
        try {

            expensas = new ArrayList<>();
            expensas.add(new SelectItem(null, "Seleccione..."));
            Expensa expensaActual = expensaService.expensaActual();
            expensas.add(new SelectItem(expensaActual.getId(), "Importe Actual $:" + expensaActual.getImporte()));

        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public void cargaComboEstados() {
        try {

            estados = new ArrayList<>();
            estados.add(new SelectItem(null, "Seleccione..."));
            estados.add(new SelectItem(EstadoExpensaInmueble.PENDIENTE, "PENDIENTE"));
            estados.add(new SelectItem(EstadoExpensaInmueble.PENDIENTE_VENCIDO, "PENDIENTE_VENCIDO"));
            estados.add(new SelectItem(EstadoExpensaInmueble.PAGADO, "PAGADO"));
            estados.add(new SelectItem(EstadoExpensaInmueble.PAGADO_VENCIDO, "PAGADO_VENCIDO"));
            estados.add(new SelectItem(EstadoExpensaInmueble.ANULADO, "ANULADO"));

        } catch (Exception e) {
            Messages.show(e.getMessage(), TypeMessages.ERROR);
        }
    }

    public void cargaComboMes() {

        meses = new ArrayList<>();
        meses.add(new SelectItem(null, "Seleccione..."));
        meses.add(new SelectItem(Mes.ENERO, "Enero"));
        meses.add(new SelectItem(Mes.FEBRERO, "Febrero"));
        meses.add(new SelectItem(Mes.MARZO, "Marzo"));
        meses.add(new SelectItem(Mes.ABRIL, "Abril"));
        meses.add(new SelectItem(Mes.MAYO, "Mayo"));
        meses.add(new SelectItem(Mes.JUNIO, "Junio"));
        meses.add(new SelectItem(Mes.JULIO, "Julio"));
        meses.add(new SelectItem(Mes.AGOSTO, "Agosto"));
        meses.add(new SelectItem(Mes.SEPTIEMBRE, "Septiembre"));
        meses.add(new SelectItem(Mes.OCTUBRE, "Octubre"));
        meses.add(new SelectItem(Mes.NOVIEMBRE, "Noviembre"));
        meses.add(new SelectItem(Mes.DICIEMBRE, "Diciembre"));
    }

    public void cargaComboAnio() {

        anios = new ArrayList<>();
        anios.add(new SelectItem(null, "Seleccione..."));
        int anioActual = UtilFechaBean.obtenerAnioDate(new Date());
        for (int i = anioActual; i < 2050; i++) {
            anios.add(new SelectItem(Long.valueOf(i)));
        }
    }

    public String getCasoDeUso() {
        return casoDeUso;
    }

    public void setCasoDeUso(String casoDeUso) {
        this.casoDeUso = casoDeUso;
    }

    public Collection<SelectItem> getEstados() {
        return estados;
    }

    public void setEstados(Collection<SelectItem> estados) {
        this.estados = estados;
    }

    public String getIdInmueble() {
        return idInmueble;
    }

    public void setIdInmueble(String idInmueble) {
        this.idInmueble = idInmueble;
    }

    public String getIdExpensa() {
        return idExpensa;
    }

    public void setIdExpensa(String idExpensa) {
        this.idExpensa = idExpensa;
    }

    public ExpensaInmueble getExpensaInmueble() {
        return expensaInmueble;
    }

    public void setExpensaInmueble(ExpensaInmueble expensaInmueble) {
        this.expensaInmueble = expensaInmueble;
    }

    public Date getFechaVencimiento() {
        return fechaVencimiento;
    }

    public void setFechaVencimiento(Date fechaVencimiento) {
        this.fechaVencimiento = fechaVencimiento;
    }

    public boolean isDisableButton() {
        return disableButton;
    }

    public void setDisableButton(boolean disableButton) {
        this.disableButton = disableButton;
    }

    public EstadoExpensaInmueble getEstado() {
        return estado;
    }

    public void setEstado(EstadoExpensaInmueble estado) {
        this.estado = estado;
    }

    public double getImporte() {
        return importe;
    }

    public void setImporte(double importe) {
        this.importe = importe;
    }

    public Mes getMes() {
        return mes;
    }

    public void setMes(Mes mes) {
        this.mes = mes;
    }

    public long getAnio() {
        return anio;
    }

    public void setAnio(long anio) {
        this.anio = anio;
    }

    public Collection<SelectItem> getExpensas() {
        return expensas;
    }

    public void setExpensas(Collection<SelectItem> expensas) {
        this.expensas = expensas;
    }

    public Collection<SelectItem> getInmuebles() {
        return inmuebles;
    }

    public void setInmuebles(Collection<SelectItem> inmuebles) {
        this.inmuebles = inmuebles;
    }

    public Collection<SelectItem> getMeses() {
        return meses;
    }

    public void setMeses(Collection<SelectItem> meses) {
        this.meses = meses;
    }

    public Collection<SelectItem> getAnios() {
        return anios;
    }

    public void setAnios(Collection<SelectItem> anios) {
        this.anios = anios;
    }

}
