/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.consorcio.persist.error;

import javax.ejb.ApplicationException;

/**
 *
 * @author IS2
 */
@ApplicationException(rollback=true)
public class ErrorDAOException extends Exception {

    /**
     * Creates a new instance of <code>PaisException</code> without detail message.
     */
    public ErrorDAOException() {
    }


    /**
     * Constructs an instance of <code>PaisException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public ErrorDAOException(String msg) {
        super(msg);
    }
}
