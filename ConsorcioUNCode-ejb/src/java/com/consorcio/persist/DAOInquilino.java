/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Inquilino;
import java.util.Collection;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author joaqu
 */
@Stateless
@LocalBean
public class DAOInquilino {
    @PersistenceContext private EntityManager em;
    
    public void guardarInquilino(Inquilino inquilino){
        em.persist(inquilino);
    }
    
    public void actualizarInquilino(Inquilino inquilino) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(inquilino);
        em.flush();
    }
    
    public Inquilino buscarPorId(String id) throws NoResultException{
        return em.find(Inquilino.class, id);
    }
    
    public Inquilino buscarPorNombre(String nombre) {
        TypedQuery<Inquilino> query = em.createQuery("SELECT i FROM Inquilino i WHERE i.nombre = :nombre AND i.eliminado = FALSE", Inquilino.class);
        query.setParameter("nombre", nombre);
        return query.getSingleResult();
    }
    
    public Collection<Inquilino> listarInquilinos(){
        TypedQuery<Inquilino> query = em.createQuery("SELECT i from Inquilino i WHERE i.eliminado = FALSE", Inquilino.class);
        return query.getResultList();
    }
}
