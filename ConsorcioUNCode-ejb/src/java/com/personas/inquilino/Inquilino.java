/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.personas.inquilino;

import java.io.Serializable;
import com.personas.persona.Persona;
import javax.persistence.Entity;
import javax.persistence.Id;

/**
 *
 * @author joaqu
 */
@Entity
public class Inquilino extends Persona{
    private String documento;
    private String tipoDocumento;
    private SexoEnum sexo;
    private String fechaNacimiento;

    public Inquilino() {
    }

    public Inquilino(String documento, String tipoDocumento, SexoEnum sexo, String fechaNacimiento, String id, String nombre, String telefono, String correoElectronico, boolean eliminado) {
        super(id, nombre, telefono, correoElectronico, false);
        this.documento = documento;
        this.tipoDocumento = tipoDocumento;
        this.sexo = sexo;
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public SexoEnum getSexo() {
        return sexo;
    }

    public void setSexo(SexoEnum sexo) {
        this.sexo = sexo;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    @Override
    public String toString() {
        return "Inquilino{" + "documento=" + documento + ", tipoDocumento=" + tipoDocumento + ", sexo=" + sexo + ", fechaNacimiento=" + fechaNacimiento + '}';
    }
    
}
