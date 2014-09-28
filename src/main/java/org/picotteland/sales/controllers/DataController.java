package org.picotteland.sales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.picotteland.sales.dto.Categorie;
import org.picotteland.sales.dto.JsonDesc;
import org.picotteland.sales.dto.PhotoInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by gdepuille on 27/09/14.
 */
@Slf4j
@Secured("ROLE_USER")
@RestController
public class DataController {

    private static final String AUTRE_CATEGORIE = "Autre";
    private static Map<String, Categorie> res;

    @Autowired
    private ObjectMapper jsonMapper;

    @Autowired
    private File imagesDir;

    @PostConstruct
    public void init() {
        if (res == null) {
            // Construction de la liste des fichiers
            log.info("Construction de la listes des catégories pour tous les fichiers.");
            makeCategories();
        }
    }

    @RequestMapping(value = "/categorie", method = RequestMethod.GET)
    public Map<String, Categorie> listCategorie() {
        return res;
    }

    private void makeCategories() {
        res = new HashMap<>();

        Iterator<File> imageFiles = FileUtils.iterateFiles(imagesDir, new SuffixFileFilter("jpg", IOCase.INSENSITIVE), TrueFileFilter.TRUE);
        while (imageFiles.hasNext()) {
            File image = imageFiles.next();
            String imageName = image.getName().replace(".jpg", "");
            File jsonFile = FileUtils.getFile(imagesDir, image.getName() + ".json");

            String catName;
            JsonDesc desc;
            try {
                desc = jsonMapper.readValue(jsonFile, JsonDesc.class);
                catName = desc.getCategorie();
            } catch (IOException e) {
                log.warn("Impossible de lire le fichier : " + e.toString());
                log.info("Application du paramètre par défaut.");
                catName = AUTRE_CATEGORIE;
                desc = new JsonDesc();
                desc.setCategorie(AUTRE_CATEGORIE);
            }

            Categorie cat;
            if (res.containsKey(catName)) {
                cat = res.get(catName);
            } else {
                cat = new Categorie();
                res.put(catName, cat);
            }

            PhotoInfo photoInfo = new PhotoInfo();
            photoInfo.setDesc(desc);
            photoInfo.setUrl("/images/" + imageName);
            photoInfo.setRef(imageName);
            photoInfo.setThumbUrl("/images/" + imageName + "/thumbnail");

            cat.addPhoto(photoInfo);
        }
    }
}
