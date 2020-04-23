package proiect;

import java.awt.image.BufferedImage;

import proiect.ApplyINTF;
import proiect.ColorCompareINTF;
import proiect.KernelINTF;

//+++ Adaptive Processor +++

public class AdaptiveTransformer implements ApplyINTF {
	protected final ColorCompareINTF comparator;

	// ++++++++ CONSTRUCTOR +++++++++++
	public AdaptiveTransformer(final ColorCompareINTF comparator) {
		this.comparator = comparator;
	}

	public BufferedImage Apply(final BufferedImage img, final int [] dataImgOut,
			final KernelINTF kernel, final int nRowBegin, final int nRowEnd) {
		// includes in Kernel ONLY pixels with a specified property
		final float [] mKernel = kernel.GetKernel();
		final int heightK = kernel.GetHeight();
		final int widthK = kernel.GetWidth();
		final int [] iOrigin = kernel.GetOrigin();
		final int offsetCol = iOrigin[0]; // - 1; if Origin starts at(1, 1)
		final int offsetRow = iOrigin[1]; // - 1;

		final int width = img.getWidth();


		for (int row = nRowBegin; row < nRowEnd - heightK; ++row) {
			for (int col = 0; col < width - widthK; ++col) {

				final int rgbBase = img.getRGB(col + offsetCol, row + offsetRow);

				final int rgbTr = this.Process(rgbBase);
				//
				final int npos = ((row - nRowBegin + offsetRow) * width) + ((col + offsetCol));
				dataImgOut [npos] = rgbTr;
			}
		}

		return null;
	}

	public int Process(final int rgbBase) {
		final int iRBase = (rgbBase >> 16) & 0xFF;
		final int iGBase = (rgbBase >>  8) & 0xFF;
		final int iBBase = (rgbBase      ) & 0xFF;
		final int rgbMin;
		final int rgbMax;
		final int rgbMid;
		final int idMid;
		if(iRBase >= iGBase) {
			if(iGBase >= iBBase) {
				rgbMax = iRBase;
				rgbMin = iBBase;
				rgbMid = iGBase;
				idMid  = 1;
			} else if(iRBase >= iBBase) {
				rgbMax = iRBase;
				rgbMin = iGBase;
				rgbMid = iBBase;
				idMid  = 2;
			} else {
				rgbMax = iBBase;
				rgbMin = iGBase;
				rgbMid = iRBase;
				idMid  = 0;
			}
		} else {
			if(iRBase >= iBBase) {
				rgbMax = iGBase;
				rgbMin = iBBase;
				rgbMid = iRBase;
				idMid  = 0; // GRB
			} else if(iGBase >= iBBase) {
				rgbMax = iGBase;
				rgbMin = iRBase;
				rgbMid = iBBase;
				idMid  = 2; // GBR
			} else {
				rgbMax = iBBase;
				rgbMin = iRBase;
				rgbMid = iGBase;
				idMid  = 1;
			}
		}
		final int rgbMidCorrect;
		if(rgbMax - rgbMid >= rgbMid - rgbMin) {
			rgbMidCorrect = rgbMax;
		} else {
			rgbMidCorrect = rgbMin;
		}

		if(idMid == 0) {
			return ((int) rgbMidCorrect) << 16 | ((int) iGBase) << 8 | ((int) iBBase) | 0xFF00_0000;
		} else if(idMid == 1) {
			return ((int) iRBase) << 16 | ((int) rgbMidCorrect) << 8 | ((int) iBBase) | 0xFF00_0000;
		} else {
			return ((int) iRBase) << 16 | ((int) iGBase) << 8 | ((int) rgbMidCorrect) | 0xFF00_0000;
		}
	}
}
