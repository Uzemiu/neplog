package cn.neptu.neplog.model.enums;

import javax.imageio.ImageIO;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * @author wchlu
 */

public enum ThumbnailGenerateStrategy {

    /**
     *
     */
    SCALE(){
        @Override
        public BufferedImage process(BufferedImage origin, int width, int height) {
            float rateX = width / (float)origin.getWidth();
            float rateY = height / (float)origin.getHeight();
            BufferedImage sheared = origin;
            // shear to ensure scale at proper rate
            if(rateY - rateX > 1e-6){
                sheared = SHEAR.process(origin, (int)(width / rateY), origin.getHeight());
            } else if (rateX - rateY > 1e-6){
                sheared = SHEAR.process(origin, origin.getWidth(), (int)(height / rateX));
            }

            // draw scaled image
            BufferedImage result = new BufferedImage(width,height,BufferedImage.TYPE_INT_RGB);
            float actualRate = Math.max(rateX,rateY);
            AffineTransform at = AffineTransform.getScaleInstance(actualRate,actualRate);
            result.createGraphics().drawRenderedImage(sheared,at);

            return result;
        }
    },
    SHEAR(){
        @Override
        public BufferedImage process(BufferedImage origin, int width, int height) {
            int offsetX = Math.max(0,(origin.getWidth()-width)/2);
            int offsetY = Math.max(0, (origin.getHeight()-height)/2);
            return origin.getSubimage(
                    offsetX,
                    offsetY,
                    Math.min(origin.getWidth(),width),
                    Math.min(origin.getHeight(),height));
        }
    };

    /**
     * {@link #SHEAR} or {@link #SCALE} the original image with certain width and height.
     * If the generate strategy is {@link #SCALE}, it will first shear the image to ensure
     * scale at proper rate.
     * @param origin the original image
     * @param width the width of processed image
     * @param height the height of processed image
     * @return the scaled or sheared image
     */
    public abstract BufferedImage process(BufferedImage origin, int width, int height);

    public static void main(String[] args) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(new File("E:\\picture\\java.bmp"));
            ImageIO.write(SCALE.process(image,256,144),"jpg",new File("E:\\picture\\output.jpg"));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
