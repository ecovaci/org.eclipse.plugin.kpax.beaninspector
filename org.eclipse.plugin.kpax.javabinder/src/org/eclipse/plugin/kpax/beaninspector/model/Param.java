package org.eclipse.plugin.kpax.beaninspector.model;


public class Param extends ModelBase {
	private String name;
	private String label;
	private String defaultValue;
	private String value;

	private int toBeRemoved;
	
	private Param paramToBeRemoved;
	
	/*private String XX;
	private String Xx;*/
	private String xX;
/*	private String xXX;
	private String XXX;*/

	public Param() {
		// Default constructor
	}

	public Param(String name, String label, String value) {
		this.name = name;
		this.label = label;
		this.value = value;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}


	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getDefaultValue() {
		return defaultValue;
	}

	public int getToBeRemoved() {
		return toBeRemoved;
	}

	public void setToBeRemoved(int toBeRemoved) {
		this.toBeRemoved = toBeRemoved;
	}

	public Param getParamToBeRemoved() {
		return paramToBeRemoved;
	}

	public void setParamToBeRemoved(Param paramToBeRemoved) {
		this.paramToBeRemoved = paramToBeRemoved;
	}

	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	}

	//------------
	
	
	
	//-----------
	
	public String[] toStringArray() {
		String[] strArray = new String[5];
		strArray[0] = this.name;
		strArray[2] = this.name;
		strArray[3] = this.defaultValue;
		strArray[4] = this.value;
		return strArray;
	}

	

	public String getxX() {
		return xX;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Param other = (Param) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
