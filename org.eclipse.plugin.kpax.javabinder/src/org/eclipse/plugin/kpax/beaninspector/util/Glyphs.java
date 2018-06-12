/*******************************************************************************
 * Copyright 2015 Eugen Covaci
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
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
