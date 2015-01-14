package org.eclipse.plugin.kpax.javabinder.util;

import org.eclipse.jdt.core.IType;
import org.eclipse.swt.widgets.Widget;

public class WidgetUtils {
	
	private static final String PROPERTY_DATA = "property";

	private static final String PATH_DATA = "path";
	
	public static IType getType(Widget widget, IType defaultType) {
		if (widget != null) {
			BeanProperty property = (BeanProperty) widget.getData(PROPERTY_DATA);
			return property != null ? property.getType() : null;
		}
		return defaultType;
	}

	public static void putProperty(Widget widget, BeanProperty property) {
		widget.setData(PROPERTY_DATA, property);
	}

	public static void putPath(Widget widget, Widget parentWidget, BeanProperty property) {
		widget.setData(PATH_DATA, parentWidget != null ? parentWidget.getData(PATH_DATA)
				+ "." + property.getProperty() : property.getProperty());
	}

	public static String getPath (Widget widget) {
		return widget != null ?  (String)widget.getData(PATH_DATA) : null;
	}
	
	public static String getTextPath (Widget widget) {
		return widget != null ?  (String)widget.getData(PATH_DATA) : "";
	}
}
