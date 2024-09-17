package com.consorcio.entity;

import com.consorcio.entity.Pais;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-09-17T12:45:54")
@StaticMetamodel(Provincia.class)
public class Provincia_ { 

    public static volatile SingularAttribute<Provincia, String> id;
    public static volatile SingularAttribute<Provincia, String> nombre;
    public static volatile SingularAttribute<Provincia, Pais> pais;
    public static volatile SingularAttribute<Provincia, Boolean> eliminado;

}