package com.consorcio.entity;

import com.consorcio.enums.SexoEnum;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2024-09-14T22:25:24")
@StaticMetamodel(Inquilino.class)
public class Inquilino_ extends Persona_ {

    public static volatile SingularAttribute<Inquilino, String> documento;
    public static volatile SingularAttribute<Inquilino, String> tipoDocumento;
    public static volatile SingularAttribute<Inquilino, SexoEnum> sexo;
    public static volatile SingularAttribute<Inquilino, String> fechaNacimiento;

}