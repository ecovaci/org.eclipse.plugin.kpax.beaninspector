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
	public static String ContextualBeanInspector_err_selection;

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
	public static String BindingDialog_err_not_javabean;
	
	public static String BindingDialog_tooltip_class;
	public static String BindingDialog_tooltip_includeRegex;
	public static String BindingDialog_tooltip_showFull;

	public static String BindingDialog_tooltip_ok;
	public static String BindingDialog_tooltip_reset; 
	public static String BindingDialog_tooltip_apply;

	public static String OpenTypeAction_dialogTitle;
	public static String OpenTypeAction_dialogMessage;

	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
	
}
