/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.entity;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.OneToOne;

/**
 *
 * @author joaqu
 */
@Entity
public class Propietario extends Persona implements Serializable{
    private boolean habitaConsorcio;
    @OneToOne (cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private Direccion direccion;
    
    public Propietario() {
    }

    public boolean isHabitaConsorcio() {
        return habitaConsorcio;
    }

    public void setHabitaConsorcio(boolean habitaConsorcio) {
        this.habitaConsorcio = habitaConsorcio;
    }

    public Direccion getDireccion() {
        return direccion;
    }

    public void setDireccion(Direccion direccion) {
        this.direccion = direccion;
    }
    
}
