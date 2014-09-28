package org.picotteland.sales.dto;

import lombok.Data;

/**
 * Created by gdepuille on 28/09/14.
 */
@Data
public class PhotoInfo {

    private String url;
    private String thumbUrl;
    private String ref;

    private JsonDesc desc;
}
