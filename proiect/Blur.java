package proiect;
import java.awt.image.BufferedImage;

import proiect.KernelINTF;
import static proiect.Main.imagine;
import static proiect.Main.interfata;

public class Blur {
        
	final BufferedImage img; 
	final int [] dataImgOut;
	final KernelINTF kernel; 
	final int nRowBegin; 
	final int nRowEnd;
        final ColorCompareINTF comparator;
	
	
	public Blur(final BufferedImage img, final int [] dataImgOut,
			final KernelINTF kernel, final int nRowBegin, final int nRowEnd, ColorCompareINTF comparator) {
		this.kernel = kernel;
		this.img = img;
		this.nRowBegin = nRowBegin;
		this.nRowEnd = nRowEnd;
		this.dataImgOut = dataImgOut;
                this.comparator=comparator;
        }
public void applyBlur() {
	final float [] mKernel = kernel.GetKernel();
	final int heightK = kernel.GetHeight();
	final int widthK = kernel.GetWidth();
	
	final int width = img.getWidth();

	for (int row = nRowBegin; row < nRowEnd; ++row) {
		for (int col = 0; col < width; ++col) {

			float sumR = 0;
			float sumG = 0;
			float sumB = 0;
			float fNorm = 0; // aduna numerele din kernel care sunt mai mari ca 0
			try {
			final int rgbBase = img.getRGB(col, row); //culoare pe fiecare pixel
			for (int npos = 0; npos < mKernel.length; ++npos) {
				
                            try { 
				final int relativeRow = row +  heightK/2 - npos / widthK; //se pune kernelul
				final int relativeCol = col + widthK/2 - npos % widthK;
				final int iRGB = img.getRGB(relativeCol, relativeRow);
                                if(comparator.Compare(rgbBase,iRGB)!=0) continue;
				sumR += mKernel[npos] * ( (iRGB >> 16) & 0xFF); // aduna culoriile ca sa faca media aritm
				sumG += mKernel[npos] * ( (iRGB >>  8) & 0xFF);
				sumB += mKernel[npos] * ( (iRGB      ) & 0xFF);
				fNorm += (mKernel[npos] > 0) ? mKernel[npos] : 0; // aduna valoriile peste 0
				}
				catch(ArrayIndexOutOfBoundsException e) {
					continue;}
			}
			}
			catch(ArrayIndexOutOfBoundsException e) {
				continue;}
			if(fNorm == 0) {
				fNorm = 1;}
			sumR = sumR / fNorm; // media aritmetica
			sumG = sumG / fNorm;
			sumB = sumB / fNorm;
			if(sumR < 0) { sumR = 0; } else if(sumR > 255) { sumR = 255; } //cazuri speciala, ca sa ramana intre 0 si 255
			if(sumG < 0) { sumG = 0; } else if(sumG > 255) { sumG = 255; }
			if(sumB < 0) { sumB = 0; } else if(sumB > 255) { sumB = 255; }
			final int iSum = ((int) sumR) << 16 | ((int) sumG) << 8 | ((int) sumB) | 0xFF00_0000; //formeaza culoarea
			final int rgbTr = iSum; 
			
			final int npos = (row - nRowBegin ) * width + col; //calculeaza pozitia pe care sa puna culoarea
			dataImgOut [npos] = rgbTr; //baga pixelul
		 
                
                }
	}
}	
}