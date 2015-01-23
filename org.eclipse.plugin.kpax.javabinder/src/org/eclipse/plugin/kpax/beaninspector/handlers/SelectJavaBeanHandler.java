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
package org.eclipse.plugin.kpax.beaninspector.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.plugin.kpax.beaninspector.JavaBeanInspectorPlugin;
import org.eclipse.plugin.kpax.beaninspector.Messages;
import org.eclipse.plugin.kpax.beaninspector.gui.BindingDialog;
import org.eclipse.plugin.kpax.beaninspector.introspector.BeanIntrospector;
import org.eclipse.plugin.kpax.beaninspector.logger.Logger;
import org.eclipse.plugin.kpax.beaninspector.util.JdtUtils;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * JavaBean Inspector command handler.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SelectJavaBeanHandler extends AbstractHandler {

	private final Logger logger = JavaBeanInspectorPlugin.getLogger();

	public Object execute(ExecutionEvent event) throws ExecutionException {
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
		try {
			IType type = JdtUtils.chooseType(shell, BindingDialog.getBeanType());
			if (type != null) {
				if (BeanIntrospector.getInstance(type).hasProperty()) {
					BindingDialog.setBeanType(type);
				} else {
					MessageDialog.openInformation(shell,
							Messages.ContextualBeanInspector_messageTitle,
							Messages.BindingDialog_err_not_javabean);
				}
			}
		} catch (Exception e) {
			logger.error(e);
			MessageDialog.openError(shell, Messages.BeanInspector_err_title,
					NLS.bind(Messages.ContextualBeanInspector_err_selection, e.getMessage()));
		}
		return null;
	}
}
