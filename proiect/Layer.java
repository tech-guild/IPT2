package proiect;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
public class Layer implements ILayer {
	public BufferedImage layerImage;
	public Layer() {
		layerImage=null;
	}
	public void LoadLayer(BufferedImage img) {

		layerImage = new BufferedImage(img.getWidth(),img.getHeight(),BufferedImage.TYPE_INT_ARGB);
		//layerImage.getGraphics().drawImage(layerImage.getScaledInstance(layerImage.getWidth(), layerImage.getHeight(), BufferedImage.SCALE_SMOOTH), 0, 0, null);
		for(int row=0; row<layerImage.getWidth(); row++) {
			for(int col=0; col<layerImage.getHeight(); col++) {
				
				Color c= new Color(255,255,255,0);
				layerImage.setRGB(row,col,c.getRGB());
			}
		}	
	}
	@Override
	public void paintComponent(Graphics g) {
	
		if(layerImage!=null) {
			g.drawImage(layerImage,0, 0, null);
		}
	}


}
