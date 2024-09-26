package com.consorcio.controller.messages;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */



import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 *
 * @author IS2
 */
public class Messages {
    
    public static void show(String texto, TypeMessages tipo){
    
        FacesMessage.Severity severity = FacesMessage.SEVERITY_ERROR;;
        
        switch(tipo){
        case NOTIFICACION:
            severity = FacesMessage.SEVERITY_INFO;
            break;
        case ALERTA:
            severity = FacesMessage.SEVERITY_WARN;
            break;
        case ERROR:
            severity = FacesMessage.SEVERITY_ERROR;
            break;
        case ERRORFATAL:    
            severity = FacesMessage.SEVERITY_FATAL;
            break;
        }
                    
        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(severity, "", texto));
        
    }
    
    public static void errorSystem(){
      FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_FATAL, "Error", "Error de Sistemas"));
    }
    
}
