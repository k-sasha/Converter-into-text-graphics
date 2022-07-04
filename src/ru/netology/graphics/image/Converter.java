package ru.netology.graphics.image;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.net.URL;

public class Converter implements TextGraphicsConverter {
    private int maxWidth = 0;
    private int maxHeight = 0;
    private double maxRatio = 0;
    private TextColorSchema schema = new Schema();

    @Override
    public String convert(String url) throws IOException, BadImageSizeException {
        BufferedImage img = ImageIO.read(new URL(url)); // скачаем картинку из интернета
        if (maxRatio != 0) { // Если попросили проверять на макс. допуст. соот-ие сторон изображения
            double ratio = ((double) img.getHeight()) / img.getWidth();
            if (ratio > maxRatio) throw new BadImageSizeException(ratio, maxRatio);
        }

        int newWidth = img.getWidth();
        int newHeight = img.getHeight();
        if (maxWidth != 0) { // Если конвертеру выставили максимально допустимую ширину
            if (img.getWidth() > maxWidth) {
                double proportion = img.getWidth() / (double) maxWidth;
                newWidth = maxWidth;
                newHeight = (int) (img.getHeight() / proportion);
            }
        }
        if (maxHeight != 0) { // Если конвертеру выставили максимально допустимую высоту
            if (img.getHeight() > maxHeight) {
                double proportion = img.getHeight() / (double) maxHeight;
                newHeight = maxHeight;
                newWidth = (int) (img.getWidth() / proportion);
            }
        }
        Image scaledImage = img.getScaledInstance(newWidth, newHeight, BufferedImage.SCALE_SMOOTH);

        // Теперь сделаем её чёрно-белой:
        // Создадим новую пустую картинку нужных размеров, заранее указав последним
        // параметром чёрно-белую цветовую палитру:
        BufferedImage bwImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_BYTE_GRAY);
        Graphics2D graphics = bwImg.createGraphics();  // Попросим у этой картинки инструмент для рисования на ней
        graphics.drawImage(scaledImage, 0, 0, null); //скопир-ем содержимое из нашей суженной картинки
        // Теперь в bwImg у нас лежит чёрно-белая картинка нужных нам размеров.

        WritableRaster bwRaster = bwImg.getRaster(); //для прохода по пикселям
        char[][] picture = new char[bwRaster.getHeight()][bwRaster.getWidth()];

        for (int h = 0; h < bwRaster.getHeight(); h++) {
            for (int w = 0; w < bwRaster.getWidth(); w++) {
                int color = bwRaster.getPixel(w, h, new int[3])[0];
                char c = schema.convert(color);
                picture[h][w] = c;
            }
        }

        StringBuilder text = new StringBuilder();
        for (char[] h : picture) {
            for (char w : h) {
                text.append(w).append(w);
            }
            text.append(" \n ");
        }

        return text.toString();
    }

    @Override
    public void setMaxWidth(int width) {
        maxWidth = width;
    }

    @Override
    public void setMaxHeight(int height) {
        maxHeight = height;
    }

    @Override
    public void setMaxRatio(double maxRatio) {
        this.maxRatio = maxRatio;
    }

    @Override
    public void setTextColorSchema(TextColorSchema schema) {
        this.schema = schema;
    }
}
