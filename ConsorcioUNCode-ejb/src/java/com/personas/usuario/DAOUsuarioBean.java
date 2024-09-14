/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.personas.usuario;

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
public class DAOUsuarioBean {
    private @PersistenceContext EntityManager em;
    
    public void guardarUsuario(Usuario usuario){
        em.persist(usuario);
    }
    
    public void actualizarUsuario(Usuario usuario){
        em.setFlushMode(FlushModeType.COMMIT);
        em.merge(usuario);
        em.flush();
    }
    
    public Usuario buscarUsuario(String usuario){
        TypedQuery<Usuario> query = em.createQuery("SELECT u FROM Usuario u WHERE u.usuario = :usuario AND u.eliminado = FALSE", Usuario.class);
        query.setParameter("usuario", usuario);
        return query.getSingleResult();
    }
    
    public Collection<Usuario> listarUsuarios(){
        TypedQuery<Usuario> query = em.createQuery("SELECT u from Usuario u WHERE u.eliminado = FALSE", Usuario.class);
        return query.getResultList();
    }
}
