package proiect;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.awt.image.Raster;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import proiect.ApplyFactory;
import proiect.ApplyINTF;
import proiect.ColorCompareFactory;
import proiect.ColorCompareINTF;

import proiect.FactoryINTF;

import proiect.IApplyFactory;
import proiect.KernelFactory;
import proiect.KernelINTF;
import proiect.Layer;
import static proiect.Main.imagine;
import static proiect.Main.interfata;

@SuppressWarnings("serial")
public class ProcessedImage extends JPanel {

	BufferedImage img;
	Layer floodLayer;
	Layer connectedComponents;
	protected Vector<Layer> vGrafice = new Vector<> ();
	protected Map<Integer,LabeledComponent> imageObjects= new HashMap<Integer,LabeledComponent> ();
	int[][][] rgb_buffer;
	boolean [][] pixel_visited;
	short [][] non_zero_pixel;
	int iCountWhite = 0;
	
	public ProcessedImage() {
		img=null;
		//floodLayer=null;
	}
	
	public void LoadImage(String FilePath) {
		try {
			img=ImageIO.read(new File(FilePath));
			img = Scale(img, this.getSize());
			floodLayer = new Layer();	
			floodLayer.LoadLayer(img);
			connectedComponents = new Layer();	
			connectedComponents.LoadLayer(img);
			pixel_visited = new boolean[img.getWidth()][img.getHeight()];
			non_zero_pixel = new short[img.getWidth()][img.getHeight()];
			for(int row=0; row<img.getWidth(); row++) {
				for(int col=0; col<img.getHeight(); col++) {
					pixel_visited[row][col] = false;
					non_zero_pixel[row][col] = 0;
					
				}
			}
			rgb_buffer=new int[3][img.getHeight()][img.getWidth()];
		} catch (IOException e) {
			 
		}
	}
	
	public void ReadImagePixels() {
		for(int row=0; row<img.getHeight(); row++) {
			for(int col=0; col<img.getWidth(); col++) {
				Color c= new Color(img.getRGB(col, row));
				rgb_buffer[0][row][col]=c.getRed();
				rgb_buffer[1][row][col]=c.getGreen();
				rgb_buffer[2][row][col]=c.getBlue();
			}
		}
	}
	
	
	private class Dim {
		public int x = -1;
		public int y = -1;
		public int rgbBase;
		public Dim(final int x, final int y, final int rgbBase) {
			this.x = x;
			this.y = y;
			this.rgbBase = rgbBase;
		}
	}
	
	private class LabeledComponent {
		public int x = -1;
		public int y = -1;
		public int label = -1;
		public int size = 0;
		public LabeledComponent(final int x, final int y, final int label,final int size) {
			this.x = x;
			this.y = y;
			this.label = label;
			this.size = 0;
		}
	}
	
	
	protected int ColorShift(final int rgbBase, final int iShift) {
		final int iRBase = (rgbBase >> 16) & 0xFF;
		final int iGBase = (rgbBase >>  8) & 0xFF;
		final int iBBase = (rgbBase      ) & 0xFF;
		// TODO: negative SHIFT
		final int iLIMIT = 255 - iShift;
		// final int iR = (iRBase > iLIMIT) ? 255 : iRBase + iShift;
		final int iR = (iRBase > 250) ? 255 : iRBase + 5;
		final int iG = (iGBase > iLIMIT) ? 255 : iGBase + iShift;
		final int iB = (iBBase > iLIMIT) ? 255 : iBBase + iShift;
		//
		final int rgbNext = (iR << 16) | (iG << 8) | iB | 0xF000_0000;
		return rgbNext;
	}
	protected void Alpha(final BufferedImage img,final BufferedImage floodLayer) {
		final int width = img.getWidth();
		final int height = img.getHeight();
		for(int y=0; y < height; y++) {
			for(int x=0; x < width; x++) {
				final int rgb = img.getRGB(x, y);
				img.setRGB(x, y, rgb | 0xFF00_0000);
				
			}
		}
	}
	public BufferedImage Create(final int imageWidth, final int imageHeight, final int [] pixelsNew) {
		final BufferedImage processedImage = new BufferedImage(imageWidth, imageHeight, BufferedImage.TYPE_INT_ARGB);
	    processedImage.setData(
	    		Raster.createRaster(processedImage.getSampleModel(),
	    				new DataBufferInt(pixelsNew, pixelsNew.length), null));
	    
	    return processedImage;
	}
	public BufferedImage Join(final Future<int []> [] futureResult,
			final int iWidth, final int iHeight, final int nRowsPerThread, final int heightKOrigin, final int nThreads) {
		// TODO: unim rezultatele
		final int [] pixelsNew = new int [iWidth * iHeight];
		final boolean [] arrReady = new boolean [nThreads];
		while(true) {
			boolean isReady = true;
			for(int idThread=0; idThread < nThreads; idThread++) {
				if(futureResult[idThread].isDone()) {
					final int idConstThread = idThread;
					if( ! arrReady[idConstThread]) {
						arrReady[idConstThread] = true;
						try {
							final int[] dataResult = futureResult[idConstThread].get();
							
							final int offsetStart = (idConstThread == 0) ? 0 : iWidth;
							final int offsetEnd = dataResult.length;
							final int nBegin = idConstThread * nRowsPerThread * iWidth;
							for(int npos = offsetStart; npos < offsetEnd; npos++) {
								pixelsNew[npos + nBegin] = dataResult[npos];
								
							}
						} catch (InterruptedException | ExecutionException e) {
							e.printStackTrace();
						}
					}
				} else {
					isReady = false;
					try {
						Thread.sleep(300);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			if(isReady) {
				break;
			}
		}
		return this.Create(iWidth, iHeight, pixelsNew);
	}
	public class AdaptiveProcessor implements ApplyINTF {
		protected final ColorCompareINTF comparator;
		
		// ++++++++ CONSTRUCTOR +++++++++++
		public AdaptiveProcessor(final ColorCompareINTF comparator) {
			this.comparator = comparator;
		}

		public BufferedImage Apply(final BufferedImage img, final int [] dataImgOut,
				final KernelINTF kernel, final int nRowBegin, final int nRowEnd) {
			// includes in Kernel ONLY pixels with a specified property
			Blur b=new Blur(img, dataImgOut, kernel,nRowBegin,nRowEnd,this.comparator);
                        b.applyBlur();
			return null;
		}
	}
	public BufferedImage ApplyThread(final BufferedImage img, final KernelINTF kernel,
			final FactoryINTF<ColorCompareINTF> comparator, final IApplyFactory processorFactory) {
		final int iHeight = img.getHeight();
		final int iWidth  = img.getWidth();
		
		final int nThreads = 3;
		final ExecutorService executor = Executors.newFixedThreadPool(nThreads);
		final Future<int []> [] futureResult = new Future [nThreads]; // limitare Java

		final int heightKernel = kernel.GetHeight();
		final int lastRow = iHeight % nThreads /*- heightKernel*/;
		final int nRowsPerThread = iHeight / nThreads;
		
		for(int idThread=0; idThread < nThreads; idThread++) {
			final ApplyINTF processor = processorFactory.Create(comparator);
			
			final int constID = idThread;
			futureResult[constID] = executor.submit(
					new Callable<int []>() {
						@Override
						public int [] call() throws Exception {
							final int offsetStart = (constID == 0) ? 0 : 1;
							final int offsetNext = (constID < nThreads - 1) ? kernel.GetHeight() : 0;
							final int offsetLast = (constID < nThreads - 1) ? 1 : lastRow; // row overhead of last part
							//
							final int nRowBegin = constID * nRowsPerThread /*+ offsetStart*/;
							final int nRowEnd = nRowBegin + nRowsPerThread /*+ offsetNext*/ + offsetLast;
							// Output
							final int nOutRows = nRowsPerThread /*+ kernel.GetOrigin()[1]*/ + offsetLast;
							final int [] dataOutImg = new int [nOutRows * iWidth];
							// System.out.println("Rows Read = " + (nRowEnd - nRowBegin + 1));
							// System.out.println("Rows = " + nOutRows);
							// System.out.println("Pixels = " + dataOutImg.length);
							
							processor.Apply(img, dataOutImg, kernel, nRowBegin, nRowEnd);
							System.out.println("Finished thread: " + constID);
							// TODO: import imagine transformata
							return dataOutImg;
                                                       
						}
					}
					);
		}

		// TODO: unim rezultatele
		return this.Join(futureResult, iWidth, iHeight, nRowsPerThread, kernel.GetOrigin()[1], nThreads);
	}
	public BufferedImage ApplyThread(final BufferedImage img, final KernelINTF kernel, final FactoryINTF<ColorCompareINTF> comparator) {
		return this.ApplyThread(img, kernel, comparator, new ApplyFactory().GetAdaptiveProcessor(this));
	}
	public BufferedImage ProcessNoise(final BufferedImage img) {
		final KernelFactory factory = new KernelFactory();
		final KernelINTF kernel = factory.GetBlur(1, 20, 0, 1);
		//
		final ColorCompareFactory comparator = new ColorCompareFactory();
		final FactoryINTF<ColorCompareINTF> factoryCmp = comparator.CompareColorsFactory(80);
		//
		return this.ApplyThread(img, kernel, factoryCmp);
	}
	public BufferedImage Process(final BufferedImage img) {
		
		return this.ProcessNoise(img);
		
		
	}
	
	public void Process() {
		// TODO: arhitectura de procesare
		
		

		img = Process(img);
		repaint();
	}
	
	
	public void Dilate(final BufferedImage imgIn, final short[][] kernel, final int nToleranceErode,final int n,final int oy,final int ox) {
		final int widthImg = imgIn.getWidth();
		final int heightImg = imgIn.getHeight();
		
		// Input: weighted Mask + Tolerance
		//final int [] mask = kernel.GetMatrix();
		final int [] mask = new int[n*n];

		for(int i = 0; i < n; i++) {
			for(int j = 0; j < n; j++) {
				mask[i*n + j] =(int) kernel[i][j];
			}
		}
		//final int widthM = kernel.Width();
		//final int heightM = kernel.Height();
		
		final int nCountME = this.SumElements(mask);
		final int [] elMask = new int [nCountME];

		final int nTolDilate = nCountME + nToleranceErode - 1;
		final int nTolerance = (nToleranceErode < 0) ? nTolDilate : nToleranceErode;
		
		// new Image Data
		final BufferedImage imgNew = new BufferedImage(widthImg, heightImg, BufferedImage.TYPE_INT_ARGB);
		imgNew.getGraphics().drawImage(imgNew.getScaledInstance(imgNew.getWidth(), imgNew.getHeight(), BufferedImage.SCALE_SMOOTH), 0, 0, null);
		//final int [] iiOrigin = kernel.Origin();
		
		// new
		
		
		/*for(int y = 0; y < heightImg - n; y++) {
			for(int x = 0; x < widthImg - n; x++) {
				int nposMaskedElement = 0;
				for(int yM = 0; yM < n; yM++) {
					for(int xM = 0; xM < n; xM++) {
						position in the MaskMatrix (WeightMatrix)
						final int nposM = yM * n + xM;
						if(mask[nposM] > 0) {
							final int iRGB = imgIn.getRGB(x + xM, y + yM);
							for(int nWeight=0; nWeight < mask[nposM]; nWeight++) {
								elMask[nposMaskedElement++] =  iRGB;
							}
						}
					}
				}
				
				for(int i = 0; i < nCountME - 1; i++) {
					for(int j = i + 1; j < nCountME; j++) {
						int aux;
						if(this.Gray(elMask[i]) > this.Gray(elMask[j])) {
							aux = elMask[i];
							elMask[i] = elMask[j];
							elMask[j] = aux;
						}
					}
				}
				
				imgNew.setRGB(x + ox, y + oy,elMask[nTolerance]);
				
			}
		}*/
		for(int y = -oy; y < heightImg; y++) {
			for(int x = -ox; x < widthImg; x++) {
				int nposMaskedElement = 0;
				for(int yM = 0; yM < n; yM++) {
					for(int xM = 0; xM < n; xM++) {
						// position in the MaskMatrix (WeightMatrix)
						
						final int nposM = yM * n + xM;
						if(mask[nposM] > 0) {
							try {
								final int iRGB = imgIn.getRGB(x + xM, y + yM);
								for(int nWeight=0; nWeight < mask[nposM]; nWeight++) {
									elMask[nposMaskedElement++] = /*this.Gray(iRGB)*/ iRGB;
								}
							}
							catch(ArrayIndexOutOfBoundsException e) {
								continue;
							}

							
						}
					}
				}
				// extract processed Value from elMask
				for(int i = 0; i < nCountME - 1; i++) {
					for(int j = i + 1; j < nCountME; j++) {
						int aux;
						if(this.Gray(elMask[i]) > this.Gray(elMask[j])) {
							aux = elMask[i];
							elMask[i] = elMask[j];
							elMask[j] = aux;
						}
					}
				}
				//Arrays.sort(elMask);
				try {
				imgNew.setRGB(x + ox, y + oy,/*toRGB(*/elMask[nTolerance]/*)*/);
				}
				catch(ArrayIndexOutOfBoundsException e) {
					continue;
				}
				// [nTol] for Erosion; [last - nTol] for Dilation
			}
		}
		img = imgNew;
		// TODO: process Borders
		System.out.println("White = " + iCountWhite);
               
		imagine.img=img;
                interfata.DisplayImage(imagine.img);
        
       
	}
	
	public int Gray(final int iRGB) {
		final int r = (iRGB >> 16) & 0xFF;
		final int g = (iRGB >>  8) & 0xFF;
		final int b = iRGB & 0xFF;
		if(r + g + b > 600) {
			iCountWhite ++;
		}
		return (r + g + b) / 3;
	}

	public int SumElements(final int [] iMask) {
		int iSum = 0;
		// TODO: should negative Weight be accepted?
		for(final int iE : iMask) {
			if(iE > 0) {
				iSum += iE;
			}
		}
		System.out.println("no. Elements = " + iSum);
		return iSum;
	}
	

	public void NoNoise(final BufferedImage imgIn,final int Tol) {
		short[][] matrix = {{0, 1, 1, 1, 0},
							{1, 2, 2, 2, 1}, 
							{1, 2, 5, 2, 1}, 
							{1, 2, 2, 2, 1}, 
							{0, 1, 1, 1, 0}};
		
		Dilate(imgIn,matrix,-Tol,5,3,3);
		Dilate(imgIn,matrix,Tol + 10,5,3,3);
		
		
	}
	public void NoImageNoise(int Tol) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ReadImagePixels();
				NoNoise(img,Tol);
				
				repaint();
			}
		}).start();
		
	}
	protected boolean fits(final short[][] template,final int x,final int y,final int n) {
		/*if(x < (n/2) || x > connectedComponents.layerImage.getWidth() - (n/2) - 1  || y < (n/2) || y > connectedComponents.layerImage.getHeight() - (n/2) - 1 ) {
			return false;
		}*/
		
		
		
		
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				try {
				if(pixel_visited[i + x - (n/2)][j + y - (n/2)] == true)
					continue;
				if(template[i][j] > 0 && non_zero_pixel[i + x - (n/2)][j + y - (n/2)] == 0 )
					return false;
				}
				catch(ArrayIndexOutOfBoundsException e) {
					continue;
				}
			}
		}
		
		return true;
	}
	protected boolean hits(final short[][] template,final int x,final int y,final int n) {
		for(int i=0; i<n; i++) {
			for(int j=0; j<n; j++) {
				try {
				if(/*(!((i + x - (n/2)) < (n/2) || (i + x - (n/2)) > connectedComponents.layerImage.getWidth() - (n/2) - 1 || (j + y - (n/2)) < (n/2)|| (j + y - (n/2)) > connectedComponents.layerImage.getHeight() - (n/2) - 1 )) &&*/ template[i][j] > 0 && 
						non_zero_pixel[i + x - (n/2)][j + y - (n/2)] > 0 )
					return true;
				}
				catch(ArrayIndexOutOfBoundsException e) {
					continue;
				}
				
			}
		}
		
		return false;
	}
	protected void Erosion(final BufferedImage floodLayer, int newC) {
		
		short[][] matrix = {{0, 1, 1, 1, 0},
							{1, 1, 1, 1, 1}, 
							{1, 1, 1, 1, 1}, 
							{1, 1, 1, 1, 1}, 
							{0, 1, 1, 1, 0}};
	
		Color c= new Color(255,255,255,0);
	
		for(int row=0; row<floodLayer.getWidth(); row++) {
			for(int col=0; col<floodLayer.getHeight(); col++) {
				if(fits(matrix,row,col,5) == true) {
					floodLayer.setRGB(row,col, newC);
				}
				else {
					floodLayer.setRGB(row,col,c.getRGB());
				}
			}
		}
		for(int row=0; row<floodLayer.getWidth(); row++) {
			for(int col=0; col<floodLayer.getHeight(); col++) {
				if(floodLayer.getRGB(row,col) == newC) {
					non_zero_pixel[row][col] = 1;
				}
				else {
					non_zero_pixel[row][col] = 0;
				}
			}
		}
	}
	protected void Dilation(final BufferedImage floodLayer, int newC) {
		
		short[][] matrix = {{0, 1, 1, 1, 0},
							{1, 1, 1, 1, 1}, 
							{1, 1, 1, 1, 1}, 
							{1, 1, 1, 1, 1}, 
							{0, 1, 1, 1, 0}};
		
		
		Color c= new Color(255,255,255,0);
		for(int row=0; row<floodLayer.getWidth(); row++) {
			for(int col=0; col<floodLayer.getHeight(); col++) {
				

				if(hits(matrix,row,col,5) == true) {
					floodLayer.setRGB(row,col, newC);
				}
				else {
					floodLayer.setRGB(row,col,c.getRGB());
				}
				
			}
		}
		for(int row=0; row<floodLayer.getWidth(); row++) {
			for(int col=0; col<floodLayer.getHeight(); col++) {
				if(floodLayer.getRGB(row,col) == newC) {
					non_zero_pixel[row][col] = 1;
				}
				else {
					non_zero_pixel[row][col] = 0;
				}
			}
		}
	
        }
	class DisjSet { 
	    int[] rank, parent;
	    int n; 
	    // Constructor to create and 
	    // initialize sets of n items 
	    DisjSet(int n) 
	    { 
	        rank = new int[n]; 
	        parent = new int[n]; 
	        this.n = n; 
	        makeSet(); 
	    } 
	  
	    // Creates n single item sets 
	    void makeSet() 
	    { 
	        for (int i = 0; i < n; i++) { 
	            parent[i]=i; 
	        } 
	    } 
	  
	    // Finds set of given item x 
	    int find(int x) 
	    { 
	        if (parent[x] != x) { 
	            parent[x] = find(parent[x]); 
	        } 
	  
	        return parent[x]; 
	    } 
	    void Union(int x, int y) 
	    { 
	        int xset = find(x); 
	        int yset = find(y); 
	        if (xset == yset) 
	            return; 

	        if (rank[xset] < rank[yset]) { 
	            parent[xset] = yset; 
	        } 
	        else if (rank[xset] > rank[yset]) { 
	            parent[yset] = xset; 
	        } 
	        else { 
	            parent[yset] = xset; 
	            rank[xset] = rank[xset] + 1; 
	        } 
	    } 
	};
	public void ConnectedComponents(final BufferedImage floodLayer,int newC) {
		
		short label;
		DisjSet obj = new DisjSet((floodLayer.getWidth()*floodLayer.getHeight())/3);
		boolean ok = false;
		label = 2;
		
		for(int row=0; row<floodLayer.getWidth(); row++){
			//
			ok = false;
			for(int col=0; col<floodLayer.getHeight(); col++) {
				if(row == 0 && col == 0) {
					if(non_zero_pixel[row][col] != 1) {
						non_zero_pixel[row][col] = label++;
						ok = true;
					}
					
					if(non_zero_pixel[row][col] == 1 && ok == true)
					{
						//label++;
						ok = false;
					}
				}
				else
				if(row == 0) {
					if(non_zero_pixel[row][col] != 1) {
						if(non_zero_pixel[row][col - 1] != 1) {					
							non_zero_pixel[row][col] = (short) obj.parent[non_zero_pixel[row][col - 1]];
						}
						
						if(non_zero_pixel[row][col - 1] == 1) {					
							non_zero_pixel[row][col] =  label++;
						}
						ok = true;
					}
					else
					if(non_zero_pixel[row][col] == 1 && ok == true)
					{
						//label++;
						ok = false;
					}
				}
				else
				if(col == 0) {
					if(non_zero_pixel[row][col] != 1) {
						if(non_zero_pixel[row - 1][col] != 1) {
							
							non_zero_pixel[row][col] = (short) obj.parent[non_zero_pixel[row - 1][col]];
						}
						else	
						{
							
							non_zero_pixel[row][col] = label++;
						}
						ok = true;
					}
					else
					if(non_zero_pixel[row][col] == 1 && ok == true)
					{
						//label++;
						ok = false;
					}
				}
				else
				{
					if(non_zero_pixel[row][col] != 1) {
						if(non_zero_pixel[row][col - 1] != 1) {			
							non_zero_pixel[row][col] = (short) obj.parent[non_zero_pixel[row][col - 1]];
						}
						if(non_zero_pixel[row][col - 1] != 1 && non_zero_pixel[row - 1][col] != 1) {			
							obj.Union(non_zero_pixel[row][col - 1], non_zero_pixel[row - 1][col]);
							non_zero_pixel[row][col] = (short) obj.parent[non_zero_pixel[row][col - 1]];		
						}
						
						if(non_zero_pixel[row][col - 1] == 1 && non_zero_pixel[row - 1][col] != 1) {
							non_zero_pixel[row][col] = (short) obj.parent[non_zero_pixel[row - 1][col]];
						}
						
						if(non_zero_pixel[row][col - 1] == 1 && non_zero_pixel[row - 1][col] == 1) {
							non_zero_pixel[row][col] =  label++;
						}
						
						ok = true;
					}
					else
					if(non_zero_pixel[row][col] == 1 && ok == true)
					{
						//label++;
						ok = false;
					}
				}
				
				
					//floodLayer.setRGB(row,col,newC);
			}
		}
		for(int row=0; row<floodLayer.getWidth(); row++) {
			for(int col=0; col<floodLayer.getHeight(); col++) {
				if(non_zero_pixel[row][col] != 1) {
					non_zero_pixel[row][col] = (short) obj.parent[non_zero_pixel[row][col]];
					if(imageObjects.containsKey(new Integer(non_zero_pixel[row][col])) == false) {
						imageObjects.put(new Integer(non_zero_pixel[row][col]),new LabeledComponent(row,col,non_zero_pixel[row][col],1));
					}
					else
					{
						imageObjects.get(new Integer(non_zero_pixel[row][col])).size++;
					}
					
					int objC = 0xFF0000FF;
					if(non_zero_pixel[row][col] % 2 == 0)
						floodLayer.setRGB(row,col,newC);
					else
						floodLayer.setRGB(row,col,objC);
				}
				else
				{
					int objC = 0x00000000;
					floodLayer.setRGB(row,col,objC);
				}

				
			}
		
		}
		for(int row=0; row<floodLayer.getWidth(); row++) {
			for(int col=0; col<floodLayer.getHeight(); col++) {
				if(non_zero_pixel[row][col] != 1) {	
					
					if(imageObjects.get(new Integer(non_zero_pixel[row][col])).size<150) {
						int objC = 0xFF00FF00;
						floodLayer.setRGB(row,col,objC);
					}
					else
					if(imageObjects.get(new Integer(non_zero_pixel[row][col])).size>=150&&imageObjects.get(new Integer(non_zero_pixel[row][col])).size<400) {
						int objC = 0xFFFFFF00;
						floodLayer.setRGB(row,col,objC);	
					}
					else
					if(imageObjects.get(new Integer(non_zero_pixel[row][col])).size>=400&&imageObjects.get(new Integer(non_zero_pixel[row][col])).size<800) {
						int objC = 0xFF0000FF;
						floodLayer.setRGB(row,col,objC);		
					}
					else
					if(imageObjects.get(new Integer(non_zero_pixel[row][col])).size>=800) {
						int objC = 0xFFFF0000;
						floodLayer.setRGB(row,col,objC);			
					}
								
					
				}
				else
				{
					int objC = 0x00000000;
					floodLayer.setRGB(row,col,objC);
				}

				
			}
		
		}
	}

	public void floodFill(final BufferedImage img, final BufferedImage Layer, final int xOrig, final int yOrig,
			final int rgbOriginal, int newC, final int iToleranta, int nrVecini, int fAFactor) {
		final Queue<Dim> queFill = new LinkedList<>();
		queFill.add(new Dim(xOrig, yOrig, rgbOriginal));
		 
		final boolean isAdaptive = false;
		
		while(queFill.size() > 0) {
			
			final Dim pixel = queFill.poll();
			final int x = pixel.x;
			final int y = pixel.y;
		
			if(x < 0 || x >= img.getWidth() || y < 0 || y >= img.getHeight())
				continue;
			if(pixel_visited[x][y] == true)
				continue;
			//
			final int rgbPixel = img.getRGB(x, y);
			final int iR = (rgbPixel >> 16) & 0xFF;
			final int iG = (rgbPixel >>  8) & 0xFF;
			final int iB = (rgbPixel      ) & 0xFF;
			if(isAdaptive && (rgbPixel & 0xFF00_0000) == 0xF000_0000) {
				continue;
			}
			//
			final int rgbBase = pixel.rgbBase;
			final int iRBase = (rgbBase >> 16) & 0xFF;
			final int iGBase = (rgbBase >>  8) & 0xFF;
			final int iBBase = (rgbBase      ) & 0xFF;
			
			final int cmp = Math.abs(iRBase - iR)
					+ Math.abs(iGBase - iG)
					+ Math.abs(iBBase - iB);
			
			if(cmp > iToleranta) {
				continue;
			}
			
				
			non_zero_pixel[x][y] = 1;
			
			
			final int newCShifted;
			if(isAdaptive) {
				newCShifted = ColorShift(rgbPixel, 8);
			} else {
				newCShifted = newC;
			}
			Layer.setRGB(x, y, newCShifted); // newC
			// TODO: optiune pentru factorul fA (media dintre pixelul curent si pixelul nou/vecin)
			final float fA = fAFactor/100.0f;
			final float fN = 1.01f - fA;
			final int r = (int) (fA * iR + fN * iRBase);
			final int g = (int) (fA * iG + fN * iGBase);
			final int b = (int) (fA * iB + fN * iBBase);
			//
			final int rgbNext = (r << 16) | (g << 8) | b;
			// final int rgbNext = rgbPixel; // rgbBase;
			
			queFill.add(new Dim(x+1, y, rgbNext));
			queFill.add(new Dim(x-1, y, rgbNext));
			queFill.add(new Dim(x, y+1, rgbNext));
			queFill.add(new Dim(x, y-1, rgbNext));
			
			if(nrVecini == 8) {
				queFill.add(new Dim(x+1, y+1, rgbNext));
				queFill.add(new Dim(x-1, y-1, rgbNext));
				queFill.add(new Dim(x-1, y+1, rgbNext));
				queFill.add(new Dim(x+1, y-1, rgbNext));
			}
			System.out.println("works");
			pixel_visited[x][y] = true;
		}
		this.Alpha(img,Layer);
                this.Dilation(Layer, newC);
		this.Erosion(Layer, newC);
	}
	
	public void floodFill(BufferedImage img, BufferedImage Layer, int x, int y, int newC, int Toleranta, int nrVecini,int fAFactor) {
		System.out.println("Started Floodfill");
		final int prevC = img.getRGB(x, y);
		floodFill(img, Layer, x, y, prevC, newC, Toleranta, nrVecini,fAFactor);
		System.out.println("Finished Floodfill");
                
	}
	
	public void FloodFillImage(int x, int y, int Toleranta, int nrVecini,int fAFactor) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ReadImagePixels();
				final int newC = 0xFF00FFA0; // 1691033;
				final int objC = 0xFFFF0000; 
				floodFill(img, floodLayer.layerImage, x, y, newC, Toleranta, nrVecini, fAFactor);
				repaint();
			}
		}).start();
	}
	public void ConnectedComponentsImage(int x, int y, int Toleranta, int nrVecini,int fAFactor) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ReadImagePixels();
				final int newC = 0xFF00FFA0; // 1691033;
				final int objC = 0xFFFF0000; 
				floodFill(img, connectedComponents.layerImage, x, y, newC, Toleranta, nrVecini, fAFactor);
				ConnectedComponents(connectedComponents.layerImage,objC);
				repaint();
			}
		}).start();
	}
	public void BackToImage(String sPath) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				ReadImagePixels();
				LoadImage(sPath);
				repaint();
			}
		}).start();
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		if(img!=null) {
			g.drawImage(img, 0, 0, null);
			floodLayer.paintComponent(g);
			connectedComponents.paintComponent(g);
		}


	}
	
	public BufferedImage Scale(final BufferedImage imgOriginal, final Dimension dim) {
		final BufferedImage imgScaled = new BufferedImage(
				dim.width, dim.height,
				BufferedImage.TYPE_INT_ARGB);
		imgScaled.getGraphics().drawImage(
				imgOriginal.getScaledInstance(dim.width, dim.height, BufferedImage.SCALE_SMOOTH), 0, 0, null);
		return imgScaled;
	}

}
