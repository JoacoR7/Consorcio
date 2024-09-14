/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Nacionalidad;
import java.util.Collection;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author joaqu
 */
@Stateless
@LocalBean
public class DAONacionalidadBean {
    private @PersistenceContext EntityManager em;
    
    public void guardarNacionalidad(Nacionalidad nacionalidad){
        em.persist(nacionalidad);
    }
    
    public void actualizarNacionalidad(Nacionalidad nacionalidad){
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(nacionalidad);
        em.flush();
    }
    
    public Nacionalidad buscarNacionalidadPorNombre (String nombre) {
        TypedQuery<Nacionalidad> query = em.createQuery("SELECT n FROM Nacionalidad n WHERE n.nombre = :nombre AND i.eliminado = FALSE", Nacionalidad.class);
        query.setParameter("nombre", nombre);
        return query.getSingleResult();
    }
    
    public Collection<Nacionalidad> listarNacionalidades() {
        TypedQuery<Nacionalidad> query = em.createQuery("SELECT n from Nacionalidad n WHERE n.eliminado = FALSE", Nacionalidad.class);
        return query.getResultList();
    }
}
