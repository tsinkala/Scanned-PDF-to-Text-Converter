/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package converters;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import javax.imageio.ImageIO;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.ImageType;
import org.apache.pdfbox.rendering.PDFRenderer;

/**
 *
 * @author Trevor.Sinkala
 */
public class NewClass {

    public static void main(String[] args) throws Exception {
        NewClass demo = new NewClass();
        demo.run();

    }

    private void run() throws Exception {
        File rootFolder = new File("C:\\Users\\trevor.sinkala\\Downloads\\Assessments");
        File outFolder = new File("C:\\Users\\trevor.sinkala\\Downloads\\Assessments\\output");
        
        String fileName = "test.pdf";
        
        PDDocument document = PDDocument.load(new File(rootFolder, fileName));
        String text = extractTextFromScannedDocument(document);
//      //System.out.println(text);
        try {
            FileWriter myWriter = new FileWriter(new File(outFolder, fileName + ".txt"));
            myWriter.write(text);
            myWriter.close();
            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    private String extractTextFromScannedDocument(PDDocument document) throws IOException, TesseractException {

        // Extract images from file
        PDFRenderer pdfRenderer = new PDFRenderer(document);
        StringBuilder out = new StringBuilder();

        ITesseract _tesseract = new Tesseract();
        _tesseract.setDatapath("tessdata");
        _tesseract.setLanguage("eng");

        for (int page = 0; page < document.getNumberOfPages(); page++) {
            BufferedImage bufferedImage = pdfRenderer.renderImageWithDPI(page, 300, ImageType.RGB);

            // Create a temp image file
            File tempFile = File.createTempFile("tempfile_" + page, ".png");
            ImageIO.write(bufferedImage, "png", tempFile);

            String result = _tesseract.doOCR(tempFile);
            out.append(result);

            // Delete temp file
            tempFile.delete();

        }

        return out.toString();

    }
}
