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
package org.eclipse.plugin.kpax.beaninspector.introspector;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;
import org.eclipse.plugin.kpax.beaninspector.introspector.model.BeanProperty;
import org.eclipse.plugin.kpax.beaninspector.prefs.Settings;

public class BeanIntrospector {
	private final Map<String, BeanProperty> properties = new HashMap<String, BeanProperty>();

	private IType type;

	private Collection<BeanProperty> typedProperties;

	public BeanIntrospector(IType type) throws JavaModelException {
		this.type = type;
		this.typedProperties = new ArrayList<BeanProperty>();
		if (this.type.exists() && (this.type.isClass() || this.type.isInterface())) {
			introspect();
		}
	}

	public Collection<BeanProperty> getProperties() {
		return properties.values();
	}

	public BeanProperty getProperty(String name) {
		return properties.get(name);
	}

	public boolean hasProperty(String name) {
		return properties.containsKey(name);
	}
	
	public boolean hasProperty() {
		return !properties.isEmpty();
	}

	public Collection<BeanProperty> getTypedProperties() {
		return typedProperties;
	}

	public boolean hasTypedProperties() {
		return !typedProperties.isEmpty();
	}

	private void introspect() throws JavaModelException {
		IType currentType = this.type;
		while (isAccepted(currentType)) {
			for (IMethod method : currentType.getMethods()) {
				if (method.getNumberOfParameters() == 0 && hasCorrectModifiers(method)) {
					String typeName = method.getReturnType();
					if ("V".equalsIgnoreCase(typeName)) {//returns void, not interesting
						continue;
					}
					String methodName = method.getElementName();
					String property = null;
					if (methodName.startsWith("get")) {
						property = uncapitalize(methodName.substring(3));
					} else if (methodName.startsWith("is")) {
						property = uncapitalize(methodName.substring(2));
					}
					if (property != null) {
						String typeSimpleName = Signature.getSignatureSimpleName(typeName);
						BeanProperty beanProperty = new BeanProperty(property, typeName,
								typeSimpleName);
						if (!properties.containsKey(property)) {
							properties.put(property, beanProperty);
							if (beanProperty.hasType()) {
								typedProperties.add(beanProperty);
							}
						}
						String[][] allResults = currentType.resolveType(typeSimpleName);
						if (allResults != null && allResults.length > 0) {
							String[] nameParts = allResults[0];
							if (nameParts != null && nameParts.length > 0) {
								StringBuffer fullName = new StringBuffer();
								for (int i = 0; i < nameParts.length; i++) {
									if (nameParts[i] != null) {
										if (fullName.length() > 0) {
											fullName.append(".");
										}
										fullName.append(nameParts[i]);
									}
								}
								if (fullName.length() > 0) {
									beanProperty.setTypeQualifiedName(fullName.toString());
									beanProperty.setType(this.type.getJavaProject().findType(
											beanProperty.getTypeQualifiedName()));
								}
							}
						}
					}
				}
			}
			currentType = findSuperclass(currentType);
		}
	}

	private boolean hasCorrectModifiers(IMethod method) throws JavaModelException {
		int flags = method.getFlags();
		return (flags & Flags.AccPublic) != 0 && (flags & Flags.AccStatic) == 0;
	}

	public boolean isValidPath(String path) throws JavaModelException {
		if (StringUtils.isNotBlank(path)) {
			if (!path.endsWith(".")) {
				BeanIntrospector introspector = this;
				for (String element : path.split("[\\.]")) {
					if (StringUtils.isNotBlank(element)) {
						if (introspector.hasProperty(element)) {
							BeanProperty property = introspector.getProperty(element);
							IType propertyType = property.getType();
							if (propertyType != null) {
								introspector = new BeanIntrospector(propertyType);
							}
						} else {
							return false;
						}
					} else {
						return false;
					}

				}
				return true;
			}
		}
		return false;
	}

	public static IType findSuperclass(IType type) throws JavaModelException {
		String superClassSignature = type.getSuperclassTypeSignature();
		if (superClassSignature != null) {
			String simpleName = Signature.getSignatureSimpleName(superClassSignature);
			String[][] allResults = type.resolveType(simpleName);
			if (allResults != null && allResults.length > 0) {
				String[] nameParts = allResults[0];
				if (nameParts != null && nameParts.length > 0) {
					StringBuffer fullName = new StringBuffer();
					for (int i = 0; i < nameParts.length; i++) {
						if (nameParts[i] != null) {
							if (fullName.length() > 0) {
								fullName.append(".");
							}
							fullName.append(nameParts[i]);
						}
					}
					if (fullName.length() > 0) {
						return type.getJavaProject().findType(fullName.toString());
					}
				}
			}
		}
		return null;
	}

	private String uncapitalize(String value) {
		if (value.length() < 2) {
			return value.toLowerCase();
		} else if (Character.isUpperCase(value.charAt(0)) && Character.isUpperCase(value.charAt(1))) {
			return value;
		} else {
			return Character.toLowerCase(value.charAt(0)) + value.substring(1);
		}
	}
	
	private boolean isAccepted(IType type) {
		if (type != null) {
			String fullyQualifiedName = type.getFullyQualifiedName();
			if (!fullyQualifiedName.matches("java(x)?\\.(.)+")) {
				Settings settings = Settings.getSettings();
				if (StringUtils.isNotEmpty(settings.getIncludeRegex())) {
					return fullyQualifiedName.matches(settings.getIncludeRegex());
				} 
				return true;
			}
		}
		return false;
	}

}
