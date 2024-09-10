
package com.controller.pais;

import com.direccion.pais.PaisServiceBean;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;


@ManagedBean
@RequestScoped
public class ManagedPaisService {

    private @EJB PaisServiceBean paisService;
    
    @PostConstruct
    public void init() {
        paisService.crearPais("Argentina");
    }
    
}