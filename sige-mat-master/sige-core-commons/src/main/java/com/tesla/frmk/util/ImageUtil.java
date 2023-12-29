package com.tesla.frmk.util;


import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import javax.imageio.ImageIO;
import org.imgscalr.Scalr;

public class ImageUtil{

    public static byte[] resize(InputStream icon, String extension) {
        try {
           BufferedImage originalImage = ImageIO.read(icon);

           //originalImage= Scalr.resize(originalImage, Scalr.Method.QUALITY, Scalr.Mode.FIT_EXACT, 128, 153);
            //To save with original ratio uncomment next line and comment the above.
            //originalImage= Scalr.resize(originalImage, 153, 128);
           originalImage= Scalr.resize(originalImage, Scalr.Method.QUALITY,Scalr.Mode.FIT_TO_HEIGHT, 128);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(originalImage, extension, baos);
            baos.flush();
            byte[] imageInByte = baos.toByteArray();
            baos.close();
            return imageInByte;
        } catch (Exception e) {
        	
        	e.printStackTrace();
        	
            return null;
        }


    }
}