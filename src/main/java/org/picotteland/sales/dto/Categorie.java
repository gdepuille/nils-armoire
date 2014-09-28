package org.picotteland.sales.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gdepuille on 27/09/14.
 */
@Data
public class Categorie {

    @Getter
    @Setter(AccessLevel.NONE)
    private List<PhotoInfo> photos = new ArrayList<>();

    public void addPhoto(PhotoInfo info) {
        photos.add(info);
    }
}
