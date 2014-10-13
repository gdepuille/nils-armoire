package org.picotteland.sales.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.imgscalr.Scalr;
import org.picotteland.sales.dto.JsonDesc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

/**
 * Created by gdepuille on 27/09/14.
 */
@Slf4j
@Controller
@ResponseBody
@RequestMapping("/images")
public class ImagesController {

    @Autowired
    private File imagesDir;

    private ObjectMapper om = new ObjectMapper();

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE,
            value = "/{fileName}")
    public ResponseEntity<byte[]> image(@PathVariable(value = "fileName") String fileName) throws IOException {
        File imageFile = FileUtils.getFile(imagesDir, fileName + ".jpg");
        JsonDesc desc = om.readValue(FileUtils.getFile(imagesDir, fileName + ".jpg.json"), JsonDesc.class);
        if (desc.isReserve()) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            addTextWatermark("Réservé", imageFile, baos, 64);
            return makeResponse(baos.toByteArray());
        } else {
            return makeResponse(FileUtils.readFileToByteArray(imageFile));
        }
    }

    @RequestMapping(method = RequestMethod.GET, produces = MediaType.IMAGE_JPEG_VALUE,
            value = "/{fileName}/thumbnail")
    public ResponseEntity<byte[]> imageThumbnail(@PathVariable(value = "fileName") final String fileName,
                                                 @RequestParam(value = "size", required = false, defaultValue = "75") final Integer size) throws IOException {
        File imageFile = FileUtils.getFile(imagesDir, fileName + ".jpg");
        JsonDesc desc = om.readValue(FileUtils.getFile(imagesDir, fileName + ".jpg.json"), JsonDesc.class);

        BufferedImage sourceImg = ImageIO.read(imageFile);
        BufferedImage scaleImg = Scalr.resize(sourceImg, Scalr.Mode.FIT_TO_HEIGHT, size);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        if (desc.isReserve()) {
            addTextWatermark("Réservé", scaleImg, baos, 16);
        } else {
            ImageIO.write(scaleImg, "jpg", baos);
        }
        return makeResponse(baos.toByteArray());
    }

    private ResponseEntity<byte[]> makeResponse(byte[] img) {
        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);

        return new ResponseEntity<>(img, headers, HttpStatus.OK);
    }

    private void addTextWatermark(String text, File sourceImageFile, OutputStream dest, int size) {
        try {
            addTextWatermark(text, ImageIO.read(sourceImageFile), dest, size);
        } catch (IOException ex) {
            log.error("Erreur lors de la watermark.", ex);
        }
    }

    private void addTextWatermark(String text, BufferedImage sourceImage, OutputStream dest, int size) {
        try {
            Graphics2D g2d = (Graphics2D) sourceImage.getGraphics();

            // initializes necessary graphic properties
            AlphaComposite alphaChannel = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f);
            g2d.setComposite(alphaChannel);
            g2d.setColor(Color.BLUE);
            g2d.setFont(new Font("Arial", Font.BOLD, size));
            FontMetrics fontMetrics = g2d.getFontMetrics();
            Rectangle2D rect = fontMetrics.getStringBounds(text, g2d);

            // calculates the coordinate where the String is painted
            int centerX = (sourceImage.getWidth() - (int) rect.getWidth()) / 2;
            int centerY = sourceImage.getHeight() / 2;

            // paints the textual watermark
            g2d.drawString(text, centerX, centerY);

            ImageIO.write(sourceImage, "jpg", dest);
            g2d.dispose();

        } catch (IOException ex) {
            log.error("Erreur lors de la watermark.", ex);
        }
    }
}
