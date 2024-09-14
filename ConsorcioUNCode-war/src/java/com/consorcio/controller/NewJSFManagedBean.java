/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.controller;

import com.consorcio.business.InquilinoServiceBean;
import com.consorcio.enums.SexoEnum;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 *
 * @author joaqu
 */
@ManagedBean
@RequestScoped
public class NewJSFManagedBean {
    private @EJB InquilinoServiceBean inquilinoService;
    /**
     * Creates a new instance of NewJSFManagedBean
     */
    @PostConstruct
    public void init(){
        inquilinoService.crearInquilino("a", "a", "a", "a", "a", SexoEnum.FEMENINO, "a");
    }
    
}
