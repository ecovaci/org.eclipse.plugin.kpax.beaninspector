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

	public boolean hasType() {
		return this.type != null;
	}

	public String asText() {
		return asText(false);
	}

	public String asText(boolean fullyQualified) {
		return this.property
				+ " - "
				+ (fullyQualified && this.typeQualifiedName != null ? this.typeQualifiedName
						: this.typeSimpleName);
	}

	@Override
	public String toString() {
		return "BeanProperty [property=" + property + ", typeName=" + typeName
				+ ", typeSimpleName=" + typeSimpleName + ", typeQualifiedName=" + typeQualifiedName
				+ ", type=" + type + "]";
	}

}
