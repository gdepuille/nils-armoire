package org.picotteland.sales.controllers;

import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Created by gdepuille on 27/09/14.
 */
@Controller
@ResponseBody
@RequestMapping("/images")
public class ImagesController {

    @Autowired
    private File imagesDir;

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE,
            value = "/{fileName}")
    public ResponseEntity<byte[]> image(@PathVariable(value = "fileName") String fileName) throws IOException {
        File imageFile = FileUtils.getFile(imagesDir, fileName + ".jpg");
        return makeResponse(FileUtils.readFileToByteArray(imageFile));
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE,
            value = "/{fileName}/thumbnail")
    public ResponseEntity<byte[]> imageThumbnail(@PathVariable(value = "fileName") final String fileName,
                                                 @RequestParam(value = "size", required = false, defaultValue = "75") final Integer size) throws IOException {
        File imageFile = FileUtils.getFile(imagesDir, fileName + ".jpg");

        BufferedImage sourceImg = ImageIO.read(imageFile);
        BufferedImage scaleImg = Scalr.resize(sourceImg, Scalr.Mode.FIT_TO_HEIGHT, size);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(scaleImg, "jpg", baos);

        return makeResponse(baos.toByteArray());
    }

    private ResponseEntity<byte[]> makeResponse(byte[] img) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }
}
