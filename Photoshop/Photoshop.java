// package Q2;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.event.*;
import java.awt.*;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;


public class Photoshop extends JFrame implements ActionListener {

    JMenuBar menuBar;
    JMenu coreMenu; 
    JMenu optionalMenu;
    JMenuItem openItem;
    JMenuItem exitItem; 
    JMenuItem grayscaleItem;
    JMenuItem OrderedDitheringItem;
    JMenuItem autoLevelItem;
    JMenuItem invertItem;
    JMenuItem floydSteinbergItem;
    JMenuItem gammaCorrectionItem;
    JMenuItem brightnessItem;
    JMenuItem sepiaItem;
    JLabel originalLabel;
    JLabel editedLabel;
    BufferedImage originalImage;
    BufferedImage editedImage;


    Photoshop(){
        this.setTitle("Mini Photoshop");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1408,576);
        this.setLayout(new FlowLayout());
        
        menuBar = new JMenuBar();
        coreMenu = new JMenu("Core Operations");
        optionalMenu = new JMenu("Optional Operations");

        openItem = new JMenuItem("Open File");
        exitItem = new JMenuItem("Exit");

        openItem.addActionListener(this);
        exitItem.addActionListener(this);

        coreMenu.add(openItem);
        coreMenu.add(exitItem);
        

        grayscaleItem = new JMenuItem("Grayscale");
        grayscaleItem.addActionListener(this);
        coreMenu.add(grayscaleItem);

        OrderedDitheringItem = new JMenuItem("Ordered Dithering");
        OrderedDitheringItem.addActionListener(this);
        coreMenu.add(OrderedDitheringItem);

        autoLevelItem = new JMenuItem("Auto Level");
        autoLevelItem.addActionListener(this);
        coreMenu.add(autoLevelItem);

        invertItem = new JMenuItem("Invert");
        invertItem.addActionListener(this);
        optionalMenu.add(invertItem);

        floydSteinbergItem = new JMenuItem("Floyd-Steinberg Dithering");
        floydSteinbergItem.addActionListener(this);
        optionalMenu.add(floydSteinbergItem);

        gammaCorrectionItem = new JMenuItem("Gamma Correction");
        gammaCorrectionItem.addActionListener(this);
        optionalMenu.add(gammaCorrectionItem);

        brightnessItem = new JMenuItem("Brightness");
        brightnessItem.addActionListener(this);
        optionalMenu.add(brightnessItem);

        sepiaItem = new JMenuItem("Sepia Tone");
        sepiaItem.addActionListener(this);
        optionalMenu.add(sepiaItem);

        menuBar.add(coreMenu);
        menuBar.add(optionalMenu);

        this.setJMenuBar(menuBar);

        originalLabel = new JLabel();
        editedLabel = new JLabel();
        JPanel imagePanel = new JPanel(new GridLayout(1,2));
        imagePanel.add(originalLabel);
        imagePanel.add(editedLabel);
        this.add(imagePanel, BorderLayout.CENTER);

        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getSource()==openItem){
            JFileChooser fileChooser = new JFileChooser();
            FileNameExtensionFilter filter = new FileNameExtensionFilter("BMP files", "bmp");
            fileChooser.setFileFilter(filter);

            int response = fileChooser.showOpenDialog(null);

            if (response == JFileChooser.APPROVE_OPTION){
                File file = fileChooser.getSelectedFile();

                if (!file.getName().endsWith(".bmp")) {
                    JOptionPane.showMessageDialog(null, "Please select a BMP file.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                 }

                try{
                   originalImage =  ImageIO.read(file);
                   
                   if (originalImage == null) {
                    throw new IOException("Unsupported image format.");
                    }

                    originalLabel.setIcon(new ImageIcon(originalImage));
                    editedLabel.setIcon(null);
                    this.pack();

                }   
                catch(IOException exception){
                    JOptionPane.showMessageDialog(null, "Failed to process file", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        }

        if (e.getSource() == exitItem){
            System.exit(0);
        }

        if (e.getSource() == grayscaleItem){
            if (originalImage != null){
                BufferedImage editImage = convertToGrayScale(originalImage);
                editedLabel.setIcon(new ImageIcon(editImage));
            }
        }

        if (e.getSource() == OrderedDitheringItem){
            if (originalImage != null){
                BufferedImage grayImage = convertToGrayScale(originalImage);
                BufferedImage ditheredImage = orderedDitheringImage(originalImage);
                originalLabel.setIcon(new ImageIcon(grayImage));
                editedLabel.setIcon(new ImageIcon(ditheredImage));
            }
        }

        if (e.getSource() == autoLevelItem){
            if (originalImage != null){
                BufferedImage editImage = autoLevelImage(originalImage);
                originalLabel.setIcon(new ImageIcon(originalImage));
                editedLabel.setIcon(new ImageIcon(editImage));
            }
        }

        if (e.getSource() == invertItem){
            if (originalImage != null){
                BufferedImage editImage = invertImage(originalImage);
                originalLabel.setIcon(new ImageIcon(originalImage));
                editedLabel.setIcon(new ImageIcon(editImage));
            }
        }

        if (e.getSource() == floydSteinbergItem){
            if (originalImage != null){
                BufferedImage grayImage = convertToGrayScale(originalImage);
                BufferedImage ditheredImage = floydSteinbergDithering(grayImage);
                originalLabel.setIcon(new ImageIcon(grayImage));
                editedLabel.setIcon(new ImageIcon(ditheredImage));
            }
        }

        if (e.getSource() == gammaCorrectionItem){
            if (originalImage != null){
                String gammaVal = JOptionPane.showInputDialog(this, "Enter gamma value");

                try{
                    double gamma = Double.parseDouble(gammaVal);
                    BufferedImage editImage = gammaCorrection(originalImage, gamma);
                    originalLabel.setIcon(new ImageIcon(originalImage));
                    editedLabel.setIcon(new ImageIcon(editImage));
                }
                catch(NumberFormatException exception){
                    JOptionPane.showMessageDialog(this, "Invalid gamma value", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }

        if (e.getSource() == brightnessItem){
            if (originalImage != null){
                String brightnessVal = JOptionPane.showInputDialog(this, "Enter brightness value");

                try{
                    int brightness = Integer.parseInt(brightnessVal);
                    BufferedImage editImage = brightnessAdjustment(originalImage, brightness);
                    originalLabel.setIcon(new ImageIcon(originalImage));
                    editedLabel.setIcon(new ImageIcon(editImage));
                }
                catch(NumberFormatException exception){
                    JOptionPane.showMessageDialog(this, "Invalid brightness value", "Error", JOptionPane.ERROR_MESSAGE);
                }

            }
        }

        if (e.getSource() == sepiaItem){
            if (originalImage != null){
                BufferedImage editImage = sepiaTone(originalImage);
                originalLabel.setIcon(new ImageIcon(originalImage));
                editedLabel.setIcon(new ImageIcon(editImage));
            }
        }

    }

    private BufferedImage convertToGrayScale(BufferedImage image){
        BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int gray = (int)(0.299 * ((rgb >> 16) & 0xff) + 0.587 * ((rgb >> 8) & 0xff) + 0.114 * (rgb & 0xff));
                int grayscale = (gray << 16) | (gray << 8) | gray;
                grayImage.setRGB(x, y, grayscale);
            }
        }
        return grayImage;
    }

    private BufferedImage orderedDitheringImage(BufferedImage image){


        BufferedImage grayScaleImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int gray = (int)(0.299 * ((rgb >> 16) & 0xff) + 0.587 * ((rgb >> 8) & 0xff) + 0.114 * (rgb & 0xff));
                int grayscale = (gray << 16) | (gray << 8) | gray;
                grayScaleImage.setRGB(x, y, grayscale);
            }
        }

    // int[][] ditherMatrix = {
    //     {0, 8, 2, 10},
    //     {12, 4, 14, 6},
    //     {3, 11, 1, 9},
    //     {15, 7, 13, 5}
    // };

    int[][] ditherMatrix = {
        {0, 128, 32, 160},
        {192, 64, 224, 96},
        {48, 176, 16, 144},
        {240, 112, 208, 80}
    };
    
    for (int x = 0; x < grayScaleImage.getWidth(); x++) {
        for (int y = 0; y < grayScaleImage.getHeight(); y++) {
            int i = x % 4;
            int j = y % 4;
            int grayValue = (grayScaleImage.getRGB(x, y) & 0xff);

            if (grayValue > ditherMatrix[i][j]){
                grayScaleImage.setRGB(x, y, 0xffffffff);
            }
            else{
                grayScaleImage.setRGB(x, y, 0xff000000);
            }
        }
    }
    return grayScaleImage;
}

    private BufferedImage autoLevelImage(BufferedImage image){
        BufferedImage autoLeveledImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
    
        int minR = 255, maxR = 0;
        int minG = 255, maxG = 0;
        int minB = 255, maxB = 0;
    
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
    
                if (r < minR) minR = r;
                if (r > maxR) maxR = r;
                if (g < minG) minG = g;
                if (g > maxG) maxG = g;
                if (b < minB) minB = b;
                if (b > maxB) maxB = b;
            }
        }
    
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;
    
                int newR = (r - minR) * 255 / (maxR - minR);
                int newG = (g - minG) * 255 / (maxG - minG);
                int newB = (b - minB) * 255 / (maxB - minB);
    
                int newPixel = (newR << 16) | (newG << 8) | newB;
                autoLeveledImage.setRGB(x, y, newPixel);
            }
        }
        return autoLeveledImage;
    }  

    private BufferedImage invertImage(BufferedImage image){
        BufferedImage invertedImage= new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);

        for (int x = 0; x < image.getWidth(); x++){
            for (int y = 0; y < image.getHeight(); y++){
                int rgb = image.getRGB(x, y);
                int r = 255 - (rgb >> 16) & 0xff;
                int g = 255 - (rgb >> 8) & 0xff;
                int b = 255 - rgb & 0xff;

                int newPixel = (r << 16) | (g << 8) | b;
                invertedImage.setRGB(x, y, newPixel);
            }
        }

        return invertedImage;
    }

    private BufferedImage floydSteinbergDithering(BufferedImage image){
        BufferedImage ditheredImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);

        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                int rgb = image.getRGB(x, y);
                int gray = (int)(0.299 * ((rgb >> 16) & 0xff) + 0.587 * ((rgb >> 8) & 0xff) + 0.114 * (rgb & 0xff));
                int grayscale = (gray << 16) | (gray << 8) | gray;
                ditheredImage.setRGB(x, y, grayscale);
            }
        }

        for (int x = 0; x < ditheredImage.getWidth(); x++) {
            for (int y = 0; y < ditheredImage.getHeight(); y++) {
                int oldPixel = ditheredImage.getRGB(x, y) & 0xff;
                int newPixel = oldPixel < (255/2) ? 0 : 255;
                int quantError = oldPixel - newPixel;
                
                ditheredImage.setRGB(x, y, (newPixel << 16) | (newPixel << 8) | newPixel);
    
                if (x + 1 < ditheredImage.getWidth()) {
                    distributeError(ditheredImage, x + 1, y, quantError, 7.0 / 16);
                }
                if (x - 1 >= 0 && y + 1 < ditheredImage.getHeight()) {
                    distributeError(ditheredImage, x - 1, y + 1, quantError, 3.0 / 16);
                }
                if (y + 1 < ditheredImage.getHeight()) {
                    distributeError(ditheredImage, x, y + 1, quantError, 5.0 / 16);
                }
                if (x + 1 < ditheredImage.getWidth() && y + 1 < ditheredImage.getHeight()) {
                    distributeError(ditheredImage, x + 1, y + 1, quantError, 1.0 / 16);
                }

            }
        
        }
        return ditheredImage;
    }

    private void distributeError(BufferedImage image, int x, int y, int error, double factor) {
        int pixel = image.getRGB(x, y) & 0xff; 
        int newPixel = (int)Math.max(0, Math.min(255, pixel + error * factor)); 
        image.setRGB(x, y, (newPixel << 16) | (newPixel << 8) | newPixel); 
    }

    private BufferedImage gammaCorrection(BufferedImage image, double gamma){
        BufferedImage gammaCorrectedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        double  gammaValue = 1.0 / gamma;

        for (int x = 0; x < image.getWidth(); x++){
            for (int y =0; y < image.getHeight(); y++){
                int rgb = (int) (image.getRGB(x, y));
                int a = (rgb >> 24) & 0xff;
                int r = ((rgb >> 16) & 0xff);
                int g = ((rgb >> 8) & 0xff);
                int b = (rgb & 0xff);

                int newR = (int) (255 * Math.pow(r /255.0, gammaValue));
                int newG = (int) (255 * Math.pow(g /255.0, gammaValue));
                int newB = (int) (255 * Math.pow(b /255.0, gammaValue));

                r = Math.min(255, newR);
                g = Math.min(255, newG);
                b = Math.min(255, newB);

                int newPixel = (a << 24) | (r << 16) | (g << 8) | b;

                gammaCorrectedImage.setRGB(x, y, newPixel);

            }
        }
        return gammaCorrectedImage;
    } 

    private BufferedImage brightnessAdjustment(BufferedImage image, int brightness){
        BufferedImage brightenedImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        
        for (int x = 0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int rgb = image.getRGB(x, y);

                int r = (rgb >> 16) & 0xff;
                int g = (rgb >> 8) & 0xff;
                int b = rgb & 0xff;

                r = Math.min(Math.max(r + brightness, 0), 255);
                g = Math.min(Math.max(g + brightness, 0), 255);
                b = Math.min(Math.max(b + brightness, 0), 255);

                brightenedImage.setRGB(x, y, (r << 16) | (g << 8) | b);
            }

        }

        return brightenedImage;
        
    }

    private BufferedImage sepiaTone(BufferedImage image){
        BufferedImage sepiaImage = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());

        for (int x =0; x < image.getWidth(); x++){
            for(int y = 0; y < image.getHeight(); y++){
                int rgb = image.getRGB(x, y);
                int r = (rgb >> 16) & 0xff;
                int g =  (rgb >> 8) & 0xff;
                int b = rgb & 0xff;

                int newRed = (int) (0.393 * r + 0.769 * g + 0.189 * b);
                int newGreen = (int) (0.349 * r + 0.686 * g + 0.168 * b);
                int newBlue = (int) (0.272 * r + 0.534 * g + 0.131 * b);

                if (newRed > 255){
                    r = 255;
                }
                else{
                    r = newRed;
                }

                if (newGreen > 255){
                    g = 255;
                }
                else{
                    g = newGreen;
                }

                if (newBlue > 255){
                    b = 255;
                }
                else{
                    b = newBlue;
                }

                sepiaImage.setRGB(x, y, (r << 16) | (g << 8) | b);
            }
        }
        return sepiaImage;
    }

}






    