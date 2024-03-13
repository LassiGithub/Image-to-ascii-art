import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/*
    Class for loading and processing images
 */
public class ImageProcesser {

    /*
    Loads an image from a file path
    Expects a regular color image file (ex. png, jpg)
    Returns a 2D int array containing the average of the RGB values for each pixel. Range: 0-255
     */
    int[][] loadImageAsValues(String filePath){
        // Read image from file as a BufferedImage object
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filePath));
        }
        catch (IOException e) {
            System.out.println("Something went wrong with reading the image file");
            System.out.println("Error:");
            System.out.println(e);
            return null;
        }

        // Convert into a value array
        int imgWidth = img.getWidth();
        int imgHeight = img.getHeight();
        int[][] imgValues = new int[imgWidth][imgHeight];
        for (int i = 0; i < imgWidth; i++) {
            for (int j = 0; j < imgHeight; j++) {
                int pixel = img.getRGB(i,j);
                Color pixelColor = new Color(pixel, true);

                int r = pixelColor.getRed();
                int g = pixelColor.getGreen();
                int b = pixelColor.getBlue();
                int pixelValue = (r+g+b)/3;

                imgValues[i][j] = pixelValue;
            }
        }

        // Return
        return imgValues;
    }

    /*
    Resizes the input image
    The input image should be a 2D int array consisting of 0-255 values (only one channel, aka grayscale)
    Returns the resized image as a similar array
     */
    int[][] resizeImage(int[][] inputImage, int outputWidth, int outputHeight){
        int inputWidth = inputImage.length;
        int inputHeight = inputImage[0].length;
        int stepX = (int) Math.floor(inputWidth / outputWidth);
        int stepY = (int) Math.floor(inputHeight / outputHeight);
        int[][] resizedImage = new int[outputWidth][outputHeight];

        for (int i = 0; i < outputWidth; i++) {
            for (int j = 0; j < outputHeight; j++) {
                int mappedX = (int) (((double) i/outputWidth)*inputWidth);
                int mappedY = (int) (((double) j/outputHeight)*inputHeight);
                int value = regionAverage(inputImage, mappedX, mappedY, stepX, stepY);
                resizedImage[i][j] = value;
            }
        }

        return resizedImage;
    }

    /*
    Averages the values in a specific region of an image
    The input image should be a 2D int array consisting of 0-255 values (only one channel, aka grayscale)
    Returns the average value of that region as an integer 0-255
     */
    int regionAverage(int[][] inputImage, int startX, int startY, int regionWidth, int regionHeight){
        double averageValue = 0;
        double pixelAmount = regionWidth * regionHeight;

        for (int i = 0; i < regionWidth; i++) {
            for (int j = 0; j < regionHeight; j++) {
                int x = startX + i;
                int y = startY + j;
                int currentValue = inputImage[x][y];
                averageValue = averageValue + (currentValue / pixelAmount);
            }
        }

        return (int) averageValue;
    }

    /*
    Converts the input image into ASCII art (string)
    The input image should be a 2D int array consisting of 0-255 values (only one channel, aka grayscale)
    Returns the ASCII art as a string
    If invertBrigthness = false, the empty character is treated as the brightest one (assumes light background for text)
    If invertBrigthness = true, the empty character is the darkest (assumes dark background for text)
     */
    String imageToAsciiArt(int[][] inputImage, int asciiWidth, int asciiHeight, boolean invertBrigthness){
        String asciiCharacters = "@ac-. "; // Should be from "fullest" to "emptiest" character
        String asciiArt = "";

        // In the case we need to invert the brightness, we simply reverse the asciiCharacters string
        if (invertBrigthness){
            asciiCharacters = new StringBuilder(asciiCharacters).reverse().toString();
        }

        // Resize input image
        int[][] resizedImage = resizeImage(inputImage, asciiWidth, asciiHeight);

        // Convert to ascii art string
        for (int j = 0; j < asciiHeight; j++) { //Just pointing out the flipped order of i/j and width/height here
            for (int i = 0; i < asciiWidth; i++) {
                int pixelValue = resizedImage[i][j];
                int asciiCharIndex = (int) ((pixelValue/256.0) * (asciiCharacters.length()));
                char asciiChar = asciiCharacters.charAt(asciiCharIndex);

                asciiArt = asciiArt + asciiChar;
            }
            asciiArt = asciiArt + "\n";
        }

        // Return
        return asciiArt;
    }
}
