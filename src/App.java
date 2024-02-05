import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class App {
    public static void main(String[] args) {
        // ImageProcesser object
        ImageProcesser imageProcesser = new ImageProcesser();

        // Load image file as an int value array
        int[][] img = imageProcesser.loadImageAsValues("images/gradient.png");

        // Turn image array into an ascii art string and print to console
        String consoleArt = imageProcesser.imageToAsciiArt(img, 90, 10, true);
        System.out.print(consoleArt);

        // Turn image array into an ascii art string and save as a file
        String fileArt = imageProcesser.imageToAsciiArt(img, 90, 10, false);
        writeAsciiArtToFile(fileArt, "ascii_art/gradient.txt");
    }

    /*
    Writes the given ascii art string as a file to the computer
     */
    static void writeAsciiArtToFile(String asciiArt, String filePath){
        try {
            Files.write(Paths.get(filePath), asciiArt.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
