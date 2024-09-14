
package com.consorcio.controller;

import com.consorcio.business.PaisServiceBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.SessionScoped;


@ManagedBean
@SessionScoped
public class ManagedPaisService {

    private @EJB PaisServiceBean paisService;
    
    @PostConstruct
    public void init() {
        paisService.crearPais("Argentina");
    }
    
}