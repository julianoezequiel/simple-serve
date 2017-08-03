package com.topdata.toppontoweb.entity;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.stereotype.Repository;

/**
 * @version 1.0.1 10/04/2016
 * @since 1.0.1 10/04/2016
 * @author juliano.ezequiel
 */
@Repository
@XmlRootElement
@JsonIgnoreProperties(ignoreUnknown = true)
public interface Entidade extends Serializable {

    static final long serialVersionUID = 1L;

}
