package proiect;

public class ColorCompareFactory {
	
	// ++++ Luminosities ++++
	
	public FactoryINTF<ColorCompareINTF> CompareLuminositiesFactory(final int iCutoff) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareLuminosities(iCutoff);
			}
		};
	}
	public ColorCompareINTF CompareLuminosities(final int iCutoff) {
		final int iCutoffx3 = 3 * iCutoff;
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iLumBase = (iRBase + iGBase + iBBase);
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				final int iLum = (iR + iG + iB);
				
				final int cmp = iLumBase - iLum;
				if(Math.abs(cmp) <= iCutoffx3) {
					return 0;
				}
				return cmp;
			}
			@Override
			public int Compare(final int rgbBase) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iLumBase = (iRBase + iGBase + iBBase);
				final int cmp = iLumBase - iCutoffx3;
				// TODO: hardcoded
				if(Math.abs(cmp) <= 90) {
					return 0;
				}
				return cmp;
			}
		};
	}
	
	// ++++ Luminosities: Threshold ++++
	
	public FactoryINTF<ColorCompareINTF> CompareLumiThreshFactory(final int iCutoff) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareLumiThresh(iCutoff);
			}
		};
	}
	public ColorCompareINTF CompareLumiThresh(final int iCutoff) {
		final int iCutoffx3 = 3 * iCutoff;
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iLumBase = (iRBase + iGBase + iBBase);
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				final int iLum = (iR + iG + iB);
				
				if(iLumBase <= iCutoffx3) {
					if(iLum <= iLumBase) {
						return 0;
					}
					return -1;
				}
				if(iLum >= iLumBase) {
					return 0;
				}
				return 1;
			}
			@Override
			public int Compare(final int rgbBase) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iLumBase = (iRBase + iGBase + iBBase);
				// TODO: which direction?
				if(iLumBase >= iCutoffx3) {
					return 0;
				}
				return iLumBase;
			}
		};
	}
	
	// ++++ Luminosities: GREY ++++

	public FactoryINTF<ColorCompareINTF> CompareGreyFactory(final int iCutoff) {
		return this.CompareGreyFactory(iCutoff, false);
	}
	public FactoryINTF<ColorCompareINTF> CompareGreyRelativeFactory(final int iCutoff) {
		return this.CompareGreyFactory(iCutoff, true);
	}
	public FactoryINTF<ColorCompareINTF> CompareGreyFactory(final int iCutoff, final boolean isRelative) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareGrey(iCutoff, isRelative);
			}
		};
	}
	public ColorCompareINTF CompareGrey(final int iCutoff, final boolean isRelative) {
		final int iCutoffx3 = iCutoff;
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iColorBase =
						Math.max(iBBase, Math.max(iRBase, iGBase)) -
						Math.min(iBBase, Math.min(iRBase, iGBase));
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				final int iColor =
						Math.max(iB, Math.max(iR, iG)) -
						Math.min(iB, Math.min(iR, iG));
				
				if(isRelative) {
					if(iColorBase <= iCutoffx3) {
						if(iColor <= iCutoffx3) {
							return 0;
						}
						return -1;
					} else if(iColor > iCutoffx3) {
						return 0;
					}
					return 1;
				}
				
				if(iColorBase <= iCutoffx3) {
					if(iColor <= iColorBase) {
						return 0;
					}
					return -1;
				}
				if(iColor >= iColorBase) {
					return 0;
				}
				return 1;
			}
		};
	}
	
	// ++++ Luminosities ++++
	
	public FactoryINTF<ColorCompareINTF> CompareLumiColorHalfFactory(final int iCutoff) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareLumiColorHalf(iCutoff);
			}
		};
	}
	public ColorCompareINTF CompareLumiColorHalf(final int iCutoff) {
		final int iCutoffx2 = 2 * iCutoff;
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iLumiHalfBase = Math.max(Math.max(iRBase,  iGBase), iBBase) +
						Math.min(Math.min(iRBase,  iGBase), iBBase);
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				final int iLumiHalf = Math.max(Math.max(iR,  iG), iB) +
						Math.min(Math.min(iR,  iG), iB);
				
				final int cmp = iLumiHalfBase - iLumiHalf;
				if(Math.abs(cmp) < iCutoffx2) {
					return 0;
				}
				return cmp;
			}
			@Override
			public int Compare(final int rgbBase) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iLumiHalfBase = Math.max(Math.max(iRBase,  iGBase), iBBase) +
						Math.min(Math.min(iRBase,  iGBase), iBBase);
				final int cmp = iLumiHalfBase - iCutoffx2;
				// TODO: hardcoded
				if(Math.abs(cmp) <= 90) {
					return 0;
				}
				return cmp;
			}
		};
	}
	
	// ++++ Luminosities: Inverse ++++
	
	public FactoryINTF<ColorCompareINTF> CompareLumiInvFactory(final int iCutoff) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareLumiInv(iCutoff);
			}
		};
	}
	public ColorCompareINTF CompareLumiInv(final int iCutoff) {
		final int iCutoffx3 = 3 * iCutoff;
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iLumBase = (iRBase + iGBase + iBBase);
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				final int iLum = (iR + iG + iB);
				
				final int cmp = iLumBase - iLum;
				if(Math.abs(cmp) > iCutoffx3) {
					return 0;
				}
				if(cmp == 0) {
					return 1;
				}
				return cmp;
			}
			@Override
			public int Compare(final int rgbBase) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iLumBase = (iRBase + iGBase + iBBase);
				final int cmp = iLumBase - iCutoffx3;
				// TODO: hardcoded
				if(Math.abs(cmp) <= 90) {
					return 0;
				}
				return cmp;
			}
		};
	}
	
	// ++++++++++++++++
	
	// ++++ Colors ++++
	
	public FactoryINTF<ColorCompareINTF> CompareColorsFactory(final int iCutoff) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareColors(iCutoff);
			}
		};
	}
	public ColorCompareINTF CompareColors(final int iCutoff) {
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				//
				
				final int cmp = Math.abs(iRBase - iR)
						+ Math.abs(iGBase - iG)
						+ Math.abs(iBBase - iB);
				if(cmp < iCutoff) {
					return 0;
				}
				final int iLumBase = (iRBase + iGBase + iBBase);
				final int iLum = (iR + iG + iB);
				// TODO: how to handle same luminance?
				return iLumBase - iLum;
			}
		};
	}
	
	// ++++ Inverse Colors ++++
	
	public FactoryINTF<ColorCompareINTF> CompareColorsInvFactory(final int iCutoff) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareColorsInv(iCutoff);
			}
		};
	}
	public ColorCompareINTF CompareColorsInv(final int iCutoff) {
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				//
				
				final int cmp = Math.abs(iRBase - iR)
						+ Math.abs(iGBase - iG)
						+ Math.abs(iBBase - iB);
				if(cmp < iCutoff) {
					final int iLumBase = (iRBase + iGBase + iBBase);
					final int iLum = (iR + iG + iB);
					final int iDiff = iLumBase - iLum;
					if(iDiff != 0) {
						return iDiff;
					}
					return 1;
				}
				return 0;
			}
		};
	}
	
	// ++++ Dominant Colors ++++
	
	public FactoryINTF<ColorCompareINTF> CompareColorsRedFactory(final int iCutoff) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareColorsRed(iCutoff);
			}
		};
	}
	public ColorCompareINTF CompareColorsRed(final int iCutoff) {
		final int iCutoffx3 = 3 * iCutoff;
		
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iLumBase = iRBase + iGBase + iBBase;
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				//
				
				final int iLum = iR + iG + iB + iCutoffx3;
				if(3 * iRBase >= iLumBase + iCutoffx3) {
					if(3 * iR >= iLum) {
						return 0;
					}
					return -1;
				}
				if(3 * iR >= iLum) {
					return 1;
				}
				return 0;
			}
		};
	}
	
	// ++++ Color Separation ++++
	
	public FactoryINTF<ColorCompareINTF> CompareColorSeparationFactory(final int iCutoff) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareColorSeparation(iCutoff);
			}
		};
	}
	public ColorCompareINTF CompareColorSeparation(final int iCutoff) {
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iSepBase = Math.max(Math.max(iRBase,  iGBase), iBBase) -
						Math.min(Math.min(iRBase,  iGBase), iBBase);
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				final int iSep = Math.max(Math.max(iR,  iG), iB) -
						Math.min(Math.min(iR,  iG), iB);
				//
				
				final int iDiff = iSepBase - iSep;
				if(Math.abs(iDiff) <= iCutoff) {
					return 0;
				}
				return iDiff;
			}
			@Override
			public int Compare(final int rgbBase) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iSepBase = Math.max(Math.max(iRBase,  iGBase), iBBase) -
						Math.min(Math.min(iRBase,  iGBase), iBBase);
				final int cmp = iSepBase - iCutoff;
				// TODO: hardcoded
				if(Math.abs(cmp) <= 50) {
					return 0;
				}
				return cmp;
			}
		};
	}
	
	// ++++ Color Separation: Color Fading ++++
	
	public FactoryINTF<ColorCompareINTF> CompareColorFadingFactory(final int iCutoff) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareColorFading(iCutoff);
			}
		};
	}
	public ColorCompareINTF CompareColorFading(final int iCutoff) {
		final int iCutoffx2 = 2 * iCutoff;
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iSepBase;
				if(iRBase >= iGBase) {
					if(iGBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iBBase - 2 * iGBase);
					} else if(iRBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iGBase - 2 * iBBase);
					} else {
						iSepBase = Math.abs(iBBase + iGBase - 2 * iRBase);
					}
				} else {
					if(iRBase >= iBBase) {
						iSepBase = Math.abs(iGBase + iBBase - 2 * iRBase);
					} else if(iGBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iGBase - 2 * iBBase);
					} else {
						iSepBase = Math.abs(iBBase + iRBase - 2 * iGBase);
					}
				}
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				final int iSep;
				if(iR >= iG) {
					if(iG >= iB) {
						iSep = Math.abs(iR + iB - 2 * iG);
					} else if(iR >= iB) {
						iSep = Math.abs(iR + iG - 2 * iB);
					} else {
						iSep = Math.abs(iB + iG - 2 * iR);
					}
				} else {
					if(iR >= iB) {
						iSep = Math.abs(iG + iB - 2 * iR);
					} else if(iG >= iB) {
						iSep = Math.abs(iR + iG - 2 * iB);
					} else {
						iSep = Math.abs(iB + iR - 2 * iG);
					}
				}
				//
				
				final int iDiff = iSepBase - iSep;
				if(Math.abs(iDiff) <= iCutoffx2) {
					return 0;
				}
				return iDiff;
			}
			@Override
			public int Compare(final int rgbBase) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iSepBase;
				if(iRBase >= iGBase) {
					if(iGBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iBBase - 2 * iGBase);
					} else if(iRBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iGBase - 2 * iBBase);
					} else {
						iSepBase = Math.abs(iBBase + iGBase - 2 * iRBase);
					}
				} else {
					if(iRBase >= iBBase) {
						iSepBase = Math.abs(iGBase + iBBase - 2 * iRBase);
					} else if(iGBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iGBase - 2 * iBBase);
					} else {
						iSepBase = Math.abs(iBBase + iRBase - 2 * iGBase);
					}
				}
				final int cmp = iSepBase - iCutoffx2;
				// TODO: hardcoded
				if(Math.abs(cmp) <= 50) {
					return 0;
				}
				return cmp;
			}
		};
	}
	
	// ++++ Color Separation: Color Fading Cutoff ++++
	
	public FactoryINTF<ColorCompareINTF> CompareColorFadingMinFactory(final int iCutoff) {
		return new FactoryINTF<ColorCompareINTF> () {
			@Override
			public ColorCompareINTF Create() {
				return ColorCompareFactory.this.CompareColorFadingMin(iCutoff);
			}
		};
	}
	public ColorCompareINTF CompareColorFadingMin(final int iCutoff) {
		final int iCutoffx2 = 2 * iCutoff;
		return new ColorCompareINTF() {
			@Override
			public int Compare(final int rgbBase, final int rgbPixel) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iSepBase;
				if(iRBase >= iGBase) {
					if(iGBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iBBase - 2 * iGBase);
					} else if(iRBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iGBase - 2 * iBBase);
					} else {
						iSepBase = Math.abs(iBBase + iGBase - 2 * iRBase);
					}
				} else {
					if(iRBase >= iBBase) {
						iSepBase = Math.abs(iGBase + iBBase - 2 * iRBase);
					} else if(iGBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iGBase - 2 * iBBase);
					} else {
						iSepBase = Math.abs(iBBase + iRBase - 2 * iGBase);
					}
				}
				//
				final int iR = (rgbPixel >> 16) & 0xFF;
				final int iG = (rgbPixel >>  8) & 0xFF;
				final int iB = (rgbPixel      ) & 0xFF;
				final int iSep;
				if(iR >= iG) {
					if(iG >= iB) {
						iSep = Math.abs(iR + iB - 2 * iG);
					} else if(iR >= iB) {
						iSep = Math.abs(iR + iG - 2 * iB);
					} else {
						iSep = Math.abs(iB + iG - 2 * iR);
					}
				} else {
					if(iR >= iB) {
						iSep = Math.abs(iG + iB - 2 * iR);
					} else if(iG >= iB) {
						iSep = Math.abs(iR + iG - 2 * iB);
					} else {
						iSep = Math.abs(iB + iR - 2 * iG);
					}
				}
				//
				
				final int iDiff = iSepBase - iSep;
				if(iDiff >= iCutoffx2) {
					return 0;
				}
				return iDiff;
			}
			@Override
			public int Compare(final int rgbBase) {
				final int iRBase = (rgbBase >> 16) & 0xFF;
				final int iGBase = (rgbBase >>  8) & 0xFF;
				final int iBBase = (rgbBase      ) & 0xFF;
				final int iSepBase;
				if(iRBase >= iGBase) {
					if(iGBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iBBase - 2 * iGBase);
					} else if(iRBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iGBase - 2 * iBBase);
					} else {
						iSepBase = Math.abs(iBBase + iGBase - 2 * iRBase);
					}
				} else {
					if(iRBase >= iBBase) {
						iSepBase = Math.abs(iGBase + iBBase - 2 * iRBase);
					} else if(iGBase >= iBBase) {
						iSepBase = Math.abs(iRBase + iGBase - 2 * iBBase);
					} else {
						iSepBase = Math.abs(iBBase + iRBase - 2 * iGBase);
					}
				}
				final int cmp = iSepBase - iCutoffx2;
				if(cmp >= 0) {
					return 0;
				}
				return cmp;
			}
		};
	}
}
