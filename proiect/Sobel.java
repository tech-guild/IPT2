
package proiect;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import  static proiect.Main.imagine;
import  static proiect.Main.interfata;

/**
 *
 * @author User
 */
public class Sobel {
    
    
    
    void Operator(int[][] datele) throws IOException{
         
  
        
        BufferedImage image = imagine.getImage();

        int x = image.getWidth();
        int y = image.getHeight();

        int maxGval = 0;
        int[][] edgeColors = new int[x][y];
        int maxGradient = -1;

        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {

                int val00 = getGrayScale(image.getRGB(i - 1, j - 1));
                int val01 = getGrayScale(image.getRGB(i - 1, j));
                int val02 = getGrayScale(image.getRGB(i - 1, j + 1));

                int val10 = getGrayScale(image.getRGB(i, j - 1));
                int val11 = getGrayScale(image.getRGB(i, j));
                int val12 = getGrayScale(image.getRGB(i, j + 1));

                int val20 = getGrayScale(image.getRGB(i + 1, j - 1));
                int val21 = getGrayScale(image.getRGB(i + 1, j));
                int val22 = getGrayScale(image.getRGB(i + 1, j + 1));
                
                int gx =  ((datele[0][0] * val00) + (datele[0][1] * val01) + (datele[0][2] * val02)) 
                        + ((datele[1][0] * val10) + (datele[1][1] * val11) + (datele[1][2] * val12))
                        + ((datele[2][0] * val20) + (datele[2][1] * val21) + (datele[2][2] * val22));

               int gy =  ((datele[0][2] * val00) + (datele[1][2] * val01) + (datele[2][2] * val02))
                        + ((datele[0][1] * val10) + (datele[1][1] * val11) + (datele[2][1] * val12))
                        + ((datele[0][0] * val20) + (datele[1][0] * val21) + (datele[2][0] * val22));

                double gval = Math.sqrt((gx * gx) + (gy * gy));
                int g = (int) gval;
                       
                if(maxGradient < g) {
                    maxGradient = g;
                }

                edgeColors[i][j] = g;
            }
        }

        double scale = 255.0 / maxGradient;

        for (int i = 1; i < x - 1; i++) {
            for (int j = 1; j < y - 1; j++) {
                int edgeColor = edgeColors[i][j];
                edgeColor = (int)(edgeColor * scale);
                edgeColor = 0xff000000 | (edgeColor << 16) | (edgeColor << 8) | edgeColor;

                image.setRGB(i, j, edgeColor);
            }
        }

      
        interfata.DisplayImage(image);
        
       
 
        
    }

    public static int  getGrayScale(int rgb) {
        int r = (rgb >> 16) & 0xff;
        int g = (rgb >> 8) & 0xff;
        int b = (rgb) & 0xff;

        //from https://en.wikipedia.org/wiki/Grayscale, calculating luminance
        int gray = (int)(0.2126 * r + 0.7152 * g + 0.0722 * b);
      
        return gray;
    }
        
        
    }
    
    

