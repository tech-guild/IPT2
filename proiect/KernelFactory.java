package proiect;

public class KernelFactory {
	

	
	public KernelINTF GetBlur(final int width, final int height, final int iValueOrigin, final int iValueBlur) {
		final int lenWidth  = 2 * width + 1;
		final int lenHeight = 2 * height + 1;
		final float [] mKernel = new float [lenWidth * lenHeight];
		for(int npos=0; npos < lenWidth * lenHeight; npos++) {
			mKernel [npos] = iValueBlur;
		}
		mKernel[lenWidth * height + width + 1] = iValueOrigin;
		
		final float [] mKernelNormalized = this.Normalize(mKernel);
		
		return new KernelINTF() {
			@Override
			public float[] GetKernel() {
				return mKernelNormalized;
			}
			@Override
			public int GetWidth() {
				return lenWidth;
			}
			@Override
			public int GetHeight() {
				return lenHeight;
			}
			@Override
			public int[] GetOrigin() {
				// first = (0, 0)
				return new int [] {width, height};
			}
		};
	}
	
	

	// ++++ helper ++++
	
	public float [] Normalize(final float [] mMatrix) {
		float sum = 0;
		for(final float fVal : mMatrix) {
			sum += fVal;
		}
		return this.Normalize(mMatrix, sum);
	}
	public float [] Normalize(final float [] mMatrix, final float sum) {
		for(int npos=0; npos < mMatrix.length; npos++) {
			mMatrix[npos] /= sum;
		}
		return mMatrix;
	}
}
