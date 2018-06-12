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

import org.eclipse.jdt.core.IType;
import org.eclipse.plugin.kpax.beaninspector.introspector.model.BeanProperty;
import org.eclipse.swt.widgets.Item;
import org.eclipse.swt.widgets.Widget;

public class WidgetDataUtils {

	private static final String PROPERTY_DATA = "property";
	private static final String PATH_DATA = "path";
	private static final String VISITED_DATA = "visited";

	public static IType getType(Widget widget, IType defaultType) {
		if (widget != null) {
			BeanProperty property = (BeanProperty) widget.getData(PROPERTY_DATA);
			return property != null ? property.getType() : null;
		}
		return defaultType;
	}

	public static void setProperty(Widget widget, BeanProperty property) {
		widget.setData(PROPERTY_DATA, property);
	}

	public static void setPath(Widget widget, Widget parentWidget, BeanProperty property) {
		widget.setData(PATH_DATA, parentWidget != null ? parentWidget.getData(PATH_DATA) + "."
				+ property.getProperty() : property.getProperty());
	}

	public static String getPath(Widget widget) {
		return widget != null ? (String) widget.getData(PATH_DATA) : null;
	}

	public static void setPath(Widget widget, String path) {
		if (widget != null) {
			widget.setData(PATH_DATA, path);
		}
	}

	public static BeanProperty getBeanProperty(Widget widget) {
		return widget != null ? (BeanProperty) widget.getData(PROPERTY_DATA) : null;
	}

	public static String getTextPath(Widget widget) {
		return widget != null ? (String) widget.getData(PATH_DATA) : "";
	}
	
	public static void setVisited (Widget widget, boolean isVisited) {
		if (widget != null) {
			 widget.setData(VISITED_DATA, isVisited);
		}
	}
	
	public static boolean isVisited (Widget widget) {
		if (widget != null) {
			 Boolean isVisited = (Boolean)widget.getData(VISITED_DATA);
			 return isVisited != null && isVisited;
		}
		return true;
	}
	
	public static void addGlyphOnLeft (Item item, String glyph) {
		if (item != null) {
			item.setText(Glyphs.glyphOnLeft(item.getText(), glyph));
		}
	}
	
	public static void addGlyphOnRight (Item item, String glyph) {
		if (item != null) {
			item.setText(Glyphs.glyphOnRight(item.getText(), glyph));
		}
	}
}
