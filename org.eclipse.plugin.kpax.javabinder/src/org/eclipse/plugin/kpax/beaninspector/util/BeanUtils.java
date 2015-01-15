package org.eclipse.plugin.kpax.beaninspector.util;

import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.util.HashMap;
import java.util.Map;

public class BeanUtils {

	public static <T extends Object> Map<String, Class<?>> getBeanProperties(Class<T> clazz) {
		try {
			Map<String, Class<?>> propertyMap = new HashMap<String, Class<?>>();
			if (!clazz.isPrimitive() && !clazz.isEnum() && !clazz.isArray()
					&& !matchesRegex(clazz, "^java(x)?\\.(.)*$")) {
				for (PropertyDescriptor propertyDescriptor : Introspector.getBeanInfo(clazz,
						Object.class).getPropertyDescriptors()) {
					if (propertyDescriptor.getPropertyType() != null) {
						propertyMap.put(propertyDescriptor.getName(),
								propertyDescriptor.getPropertyType());
					}
				}

			}
			return propertyMap;
		} catch (IntrospectionException e) {
			throw new RuntimeException(e);
		}
	}

	public static boolean matchesRegex(Class<?> clazz, String regex) {
		return clazz.getName().matches(regex);
	}
	
	

}
