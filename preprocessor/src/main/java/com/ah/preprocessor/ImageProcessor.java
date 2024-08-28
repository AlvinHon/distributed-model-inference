package com.ah.preprocessor;

import java.awt.Color;
import java.awt.image.BufferedImage;

// modified from
// https://blog.nuculabs.dev/posts/2023/2023-07-04-using-an-onnx-resnet-model-in-java/
public class ImageProcessor {
    private BufferedImage image;

    public ImageProcessor(BufferedImage image) {
        this.image = image;
    }

    public float[][][][] getNormalizedRGBFloats() {
        float[][][][] tensorData = new float[1][3][image.getHeight()][image.getWidth()];
        // values taken from preprocessor_config.json
        final float[] mean = new float[] { 0.485f, 0.456f, 0.406f };
        final float[] standardDeviation = new float[] { 0.229f, 0.224f, 0.225f };

        for (var h = 0; h < image.getHeight(); h++) {
            for (var w = 0; w < image.getWidth(); w++) {
                Color c = new Color(image.getRGB(w, h));
                // Get RGB values
                tensorData[0][0][w][h] = ((c.getRed()) / 255f - mean[0]) / standardDeviation[0];
                tensorData[0][1][w][h] = ((c.getGreen()) / 255f - mean[1]) / standardDeviation[1];
                tensorData[0][2][w][h] = ((c.getBlue()) / 255f - mean[2]) / standardDeviation[2];
            }
        }

        return tensorData;
    }

    public ImageProcessor crop() {
        int width = image.getWidth();
        int height = image.getHeight();
        int startX = 0;
        int startY = 0;
        if (width > height) {
            startX = (width - height) / 2;
            width = height;
        } else {
            startY = (height - width) / 2;
            height = width;
        }
        image = image.getSubimage(startX, startY, width, height);
        return this;
    }

    public ImageProcessor resize(int width, int height) {
        // Resize image
        var resizedImage = image.getScaledInstance(width, height, java.awt.Image.SCALE_SMOOTH);

        // Process image
        BufferedImage scaledImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        scaledImage.getGraphics().drawImage(resizedImage, 0, 0, null);

        image = scaledImage;
        return this;
    }
}
