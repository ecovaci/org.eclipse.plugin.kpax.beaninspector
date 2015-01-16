package org.eclipse.plugin.kpax.beaninspector.introspector.model;

import org.eclipse.jdt.core.IType;

public class BeanProperty {

	private String property;

	private String typeName;

	private String typeSimpleName;

	private String typeQualifiedName;

	private IType type;

	public BeanProperty(String property, String typeName, String typeSimpleName) {
		super();
		this.property = property;
		this.typeName = typeName;
		this.typeSimpleName = typeSimpleName;
	}

	public String getProperty() {
		return property;
	}

	public String getTypeName() {
		return typeName;
	}

	public String getTypeSimpleName() {
		return typeSimpleName;
	}

	public String getTypeQualifiedName() {
		return typeQualifiedName;
	}

	public void setTypeQualifiedName(String typeQualifiedName) {
		this.typeQualifiedName = typeQualifiedName;
	}

	public IType getType() {
		return type;
	}

	public void setType(IType type) {
		this.type = type;
	}
	
	public boolean hasType () {
		return this.type != null;
	}
	
	public String asText () {
		return this.property + " - " + this.typeSimpleName;
	}

	@Override
	public String toString() {
		return "BeanProperty [property=" + property + ", typeName=" + typeName
				+ ", typeSimpleName=" + typeSimpleName + ", typeQualifiedName=" + typeQualifiedName
				+ ", type=" + type + "]";
	}

}
