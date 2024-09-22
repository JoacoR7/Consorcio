/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Departamento;
import java.util.Collection;
import java.util.List;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.FlushModeType;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 *
 * @author victo
 */
@Stateless
@LocalBean
public class DAODepartamento {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;

    public void guardarDepartamento(Departamento departamento) {
        try {
            em.persist(departamento);
        } catch (Exception e) {
            e.getMessage();
            e.printStackTrace();
            throw e;
        }
    }

    public void actualizarDepartamento(Departamento departamento) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(departamento);
        em.flush();
    }

    public Departamento buscarDepartamentoId(String id) throws NoResultException {
        return em.find(Departamento.class, id);
    }

    public Departamento buscarDepartamentoPorNombre(String nombre) {
        TypedQuery<Departamento> query = em.createQuery("SELECT d FROM Departamento d "
                + "WHERE d.nombre = :nombre AND d.eliminado = FALSE", Departamento.class);
        query.setParameter("nombre", nombre);
        List<Departamento> resultados = query.getResultList();

        if (resultados.isEmpty()) {
            return null;  // Devuelve null si no hay resultados
        } else {
            return resultados.get(0);  // Devuelve el primer resultado
        }
    }

    public Collection<Departamento> listarDepartamentoActivo() {
        try {
            TypedQuery<Departamento> query = em.createQuery("SELECT d FROM Departamento d "
                    + "WHERE d.eliminado = FALSE", Departamento.class);
            return query.getResultList(); 
        } catch (Exception e) {
            return null;
        }
    }
    
    public Departamento buscarDepartamentoPorProvinciaYNombre(String nombreDpto , String idProvincia) {
        try {
            TypedQuery<Departamento> query = em.createQuery("SELECT d FROM Departamento d "
            + "WHERE d.provincia.id := idProvincia AMD d.nombre := nombreDpto "
            + "AND d.elimnado = FALSE", Departamento.class);
            query.setParameter("nombreDpto", nombreDpto);
            query.setParameter("idProvincia", idProvincia);
            return query.getSingleResult();  
        } catch (Exception e) {
            return null;
        }
    }
    
}
