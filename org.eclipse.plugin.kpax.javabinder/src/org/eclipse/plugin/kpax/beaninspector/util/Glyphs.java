package org.eclipse.plugin.kpax.beaninspector.util;

public abstract class Glyphs {
	
	public static final String ARROW_DOWN = "\u25BE";
	public static final String ARROW_RIGHT = "\u25B8";
	public static final String BULLET = "\u2022";

	public static String glyphOnRight (String value, String glyph) {
		if (value != null) {
			return value + " " + glyph;
		}
		return value;
	}
	
	public static String glyphOnLeft (String value, String glyph) {
		if (value != null) {
			return glyph + " " + value;
		}
		return value;
	}
}
