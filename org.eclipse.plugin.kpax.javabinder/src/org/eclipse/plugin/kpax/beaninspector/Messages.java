package org.eclipse.plugin.kpax.beaninspector;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {

	private static final String BUNDLE_NAME = "org.eclipse.plugin.kpax.beaninspector.messages"; //$NON-NLS-1$

	public static String BeanInspector_err_title;

	public static String ContextualBeanInspector_err_build_menu;
	public static String ContextualBeanInspector_err_replace_selection;
	public static String ContextualBeanInspector_messageTitle;
	public static String ContextualBeanInspector_info_no_type;
	public static String ContextualBeanInspector_info_no_property;
	public static String ContextualBeanInspector_err_open;

	public static String ValidatePathHandler_messageTitle;
	public static String ValidatePathHandler_no_text_selected;
	public static String ValidatePathHandler_valid_selection;
	public static String ValidatePathHandler_invalid_selection;
	public static String ValidatePathHandler_no_bean_selected;
	public static String ValidatePathHandler_err_validation;
	public static String ValidatePathHandler_err_messageTitle;

	public static String BindingDialog_label_includeRegex;
	public static String BindingDialog_label_Show_fully_qualified;
	public static String BindingDialog_label_rules;
	public static String BindingDialog_label_open_type;
	public static String BindingDialog_label_reset;
	public static String BindingDialog_label_apply;
	public static String BindingDialog_err_build_tree;
	public static String BindingDialog_title;

	public static String OpenTypeAction_dialogTitle;
	public static String OpenTypeAction_dialogMessage;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
