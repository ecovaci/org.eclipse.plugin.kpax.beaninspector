package org.eclipse.plugin.kpax.beaninspector.bean;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.jdt.core.Flags;
import org.eclipse.jdt.core.IMethod;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.Signature;

public class BeanIntrospector {
	private final Map<String, BeanProperty> properties = new HashMap<String, BeanProperty>();

	private IType type;

	private Collection<BeanProperty> typedProperties;

	public BeanIntrospector(IType type) throws JavaModelException {
		this.type = type;
		typedProperties = new ArrayList<BeanProperty>();
		if (type.isClass() || type.isInterface()) {
			introspect();
		}
	}

	public Collection<BeanProperty> getProperties() {
		return properties.values();
	}

	public Collection<BeanProperty> getTypedProperties() {
		return typedProperties;
	}
	
	public boolean hasTypedProperties() {
		return !typedProperties.isEmpty();
	}

	private void introspect() throws JavaModelException {
		IType currentType = this.type;
		while (currentType != null
				&& !currentType.getFullyQualifiedName().matches("^java(x)?\\.(.)*$")) {
			System.out.println("Introspect [" + currentType.getFullyQualifiedName() + "]");
			for (IMethod method : currentType.getMethods()) {
				if (method.getNumberOfParameters() == 0 && hasCorrectModifiers(method)) {
					String typeName = method.getReturnType();
					if ("V".equalsIgnoreCase(typeName)) {//return void
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
						if (!properties.containsKey(properties)) {
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
			currentType = getSuperclass(currentType);
		}
	}

	private boolean hasCorrectModifiers(IMethod method) throws JavaModelException {
		int flags = method.getFlags();
		return (flags & Flags.AccPublic) != 0 && (flags & Flags.AccStatic) == 0;
	}

	public static String getReturnType(IMethod method) throws JavaModelException {
		String name = method.getReturnType();
		if (name != null) {
			String simpleName = Signature.getSignatureSimpleName(name);
			IType type = method.getDeclaringType();
			String[][] allResults = type.resolveType(simpleName);
			if (allResults != null) {
				String[] nameParts = allResults[0];
				if (nameParts != null) {
					StringBuffer fullName = new StringBuffer();
					for (int i = 0; i < nameParts.length; i++) {
						if (nameParts[i] != null) {
							if (fullName.length() > 0) {
								fullName.append(".");
							}
							fullName.append(nameParts[i]);
						}
					}
					return fullName.toString();
				}

			}
		}
		return null;
	}

	public static IType getSuperclass(IType type) throws JavaModelException {
		String name = type.getSuperclassTypeSignature();
		if (name != null) {
			String simpleName = Signature.getSignatureSimpleName(name);
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

}
