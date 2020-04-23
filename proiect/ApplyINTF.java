package proiect;

import java.awt.image.BufferedImage;

public interface ApplyINTF {
	public BufferedImage Apply(final BufferedImage img, final int [] imgCopy,
			final KernelINTF kernel, final int nRowBegin, final int nRowEnd);

	public default BufferedImage Apply(final BufferedImage img, final int[] dataOutImg,
			final KernelINTF kernel1, final KernelINTF kernel2,
			final int nRowBegin, final int nRowEnd) {
		return null;
	}
}
