package org.eclipse.plugin.kpax.beaninspector;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.plugin.kpax.beaninspector.messages"; //$NON-NLS-1$

	public static String ContextualBeanInspector_messageTitle;
	public static String ContextualBeanInspector_info_no_type;
	public static String ContextualBeanInspector_info_no_property;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
