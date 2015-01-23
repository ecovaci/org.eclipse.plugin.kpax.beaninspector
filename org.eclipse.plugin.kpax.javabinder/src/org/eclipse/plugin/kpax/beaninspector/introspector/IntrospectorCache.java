package org.eclipse.plugin.kpax.beaninspector.introspector;

import java.util.LinkedHashMap;
import java.util.Map;

import org.eclipse.jdt.core.IType;

public class IntrospectorCache extends LinkedHashMap<IType, BeanIntrospector> {
	
	public static final int MAX_ENTRIES = 10;

	private static final long serialVersionUID = 7043078105868243027L;

	private int maxEntries;

	public IntrospectorCache() {
		maxEntries = MAX_ENTRIES;
	}

	public IntrospectorCache(int maxEntries) {
		super();
		this.maxEntries = maxEntries;
	}

	protected boolean removeEldestEntry(Map.Entry<IType, BeanIntrospector> eldest) {
		return size() > maxEntries;
	}

}
