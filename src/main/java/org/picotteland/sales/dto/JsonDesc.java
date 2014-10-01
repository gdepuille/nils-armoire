package org.picotteland.sales.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * Created by gdepuille on 27/09/14.
 */
@Data
public class JsonDesc {

    private String categorie;

    private String title;

    private Double prix;

    private String description;
}