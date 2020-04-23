package proiect;


import proiect.ProcessedImage;


import proiect.AdaptiveTransformer;


public class ApplyFactory {
	
	public IApplyFactory GetAdaptiveProcessor(final ProcessedImage engine) {
		return new IApplyFactory() {
			@Override
			public ApplyINTF Create(final FactoryINTF<ColorCompareINTF> comparator) {
				return engine.new AdaptiveProcessor(comparator.Create());
			}
		};
	}
	
	public IApplyFactory GetAdaptiveTransformer() {
		return new IApplyFactory() {
			@Override
			public ApplyINTF Create(final FactoryINTF<ColorCompareINTF> comparator) {
				return new AdaptiveTransformer(comparator.Create());
			}
		};
	}

	
}
