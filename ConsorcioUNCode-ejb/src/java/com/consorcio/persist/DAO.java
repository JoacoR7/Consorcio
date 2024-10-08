/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.consorcio.persist;

import java.util.Collection;
import java.util.Map;

/**
 *
 * @author adrzanbar
 */
public interface DAO<T> {

    T guardar(T t);

    void eliminar(T t);

    T buscarPorId(T t);

    T actualizar(T t);

    Collection<T> listar();

    Collection<T> listar(Collection<Map<String, Object>> searchCriteria) throws DAOException;

    T buscarUnico(Collection<Map<String, Object>> searchCriteria) throws DAOException;
}
