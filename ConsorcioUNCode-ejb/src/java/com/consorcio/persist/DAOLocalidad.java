/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import com.consorcio.entity.Localidad;
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
 * @author victo
 */
@Stateless
@LocalBean
public class DAOLocalidad {

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    @PersistenceContext
    private EntityManager em;
    
    public void guardarLocalidad(Localidad localidad) {
        em.persist(localidad);
    }

    public void actualizarLocalidad(Localidad localidad) {
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(localidad);
        em.flush();
    }

    public Localidad buscarLocalidadId(String id) throws NoResultException {
        return em.find(Localidad.class, id);
    }

    public Localidad buscarLocalidadPorNombre(String nombre) {
        try {
            TypedQuery<Localidad> query = em.createQuery("SELECT l FROM Localidad l WHERE l.nombre = :nombre AND l.eliminado = FALSE", Localidad.class);
            query.setParameter("nombre", nombre);
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    public Collection<Localidad> listarLocalidadActivo() {
        TypedQuery<Localidad> query = em.createQuery("SELECT l FROM Localidad l WHERE l.eliminado = FALSE", Localidad.class);
        return query.getResultList();
    }

    public Localidad buscarLocalidadPorDeptoYNombre(String idDpto, String nombre) {

        try {

            return (Localidad) em.createQuery("SELECT l "
                    + "  FROM Localidad l"
                    + " WHERE l.departamento.id = :idDpto"
                    + "   AND l.nombre = :nombre"
                    + "   AND l.eliminado = FALSE").
                    setParameter("idDpto", idDpto).
                    setParameter("nombre", nombre).
                    getSingleResult();

        } catch (NoResultException ex) {
            ex.getMessage();
            return null;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public Collection<Localidad> listarLocalidadPorDepartamento(String idDpto) {
        try {
            TypedQuery<Localidad> query;
            query = em.createQuery("SELECT l FROM Localidad l"
                    + " WHERE l.eliminado = FALSE AND l.departamento.id = :idDpto", Localidad.class);
            query.setParameter("idDpto", idDpto);
            return query.getResultList();
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
    
}
