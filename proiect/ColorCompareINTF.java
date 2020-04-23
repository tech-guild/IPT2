package proiect;

public interface ColorCompareINTF {
	public int Compare(final int rgbBase, final int rgbOther);
	public default int Compare(final int rgbBase) {
		return 0;
	}
}
