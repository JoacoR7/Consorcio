/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import javax.ejb.Stateless;
import javax.ejb.LocalBean;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

/**
 *
 * @author adrzanbar
 */
@Stateless
@LocalBean
public class DAOImpl<T> implements DAO<T> {

    protected @PersistenceContext
    EntityManager em;
    public Class type;

    public DAOImpl() {
    }

    public DAOImpl(T entidad) {
        type = entidad.getClass();
    }

    @Override
    public T guardar(T entidad) {
        em.persist(entidad);
        return entidad;
    }

    @Override
    public void eliminar(T entidad) {
        em.remove(em.merge(entidad));
    }

    @Override
    public T buscarPorId(Object id) {
        return (T) em.find(type, id);
    }

    @Override
    public T actualizar(T t) {
        return em.merge(t);
    }

    @Override
    public Collection<T> listar() {
        CriteriaBuilder cb = this.em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(type);
        Root<T> root = criteriaQuery.from(type);

        criteriaQuery.select(root).where(cb.equal(root.get("eliminado"), false));

        TypedQuery<T> query = em.createQuery(criteriaQuery);
        return query.getResultList();
    }

    @Override
    public Collection<T> listarLike(Map<String, String> attributes) throws IllegalArgumentException {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<T> criteriaQuery = cb.createQuery(type);
        Root<T> root = criteriaQuery.from(type);

        List<Predicate> predicates = new ArrayList<>();

        predicates.add(cb.equal(root.get("eliminado"), false));

        for (Map.Entry<String, String> entry : attributes.entrySet()) {
            String attribute = entry.getKey();
            String value = entry.getValue();

            if (!isValidAttribute(attribute)) {
                throw new IllegalArgumentException("Attribute '" + attribute + "' is not valid for entity " + type.getSimpleName());
            }

            predicates.add(cb.like(root.<String>get(attribute), "%" + value + "%"));
        }

        criteriaQuery.select(root).where(cb.and(predicates.toArray(new Predicate[0])));

        return em.createQuery(criteriaQuery).getResultList();
    }

    private boolean isValidAttribute(String attribute) {
        try {
            type.getDeclaredField(attribute);
            return true;
        } catch (NoSuchFieldException e) {
            return false;
        }
    }

}
