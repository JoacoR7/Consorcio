/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.business;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;

/**
 *
 * @author adrzanbar
 */
@Stateless
@LocalBean
public class InmuebleServiceBean {

    private @EJB InquilinoServiceBean inquilinoService;
    private @EJB PropietarioServiceBean propietarioService;
    //private @EJB DAOInmuebleImplBean dao;
}
