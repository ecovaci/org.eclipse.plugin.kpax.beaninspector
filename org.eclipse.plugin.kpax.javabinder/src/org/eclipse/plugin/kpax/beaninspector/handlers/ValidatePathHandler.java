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
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.osgi.util.NLS;
import org.eclipse.plugin.kpax.beaninspector.JavaBeanInspectorPlugin;
import org.eclipse.plugin.kpax.beaninspector.Messages;
import org.eclipse.plugin.kpax.beaninspector.gui.BindingDialog;
import org.eclipse.plugin.kpax.beaninspector.introspector.BeanIntrospector;
import org.eclipse.plugin.kpax.beaninspector.logger.Logger;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * JavaBean Inspector command handler.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ValidatePathHandler extends AbstractHandler {
	private final Logger logger = JavaBeanInspectorPlugin.getLogger();
	
	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		Shell shell = HandlerUtil.getActiveWorkbenchWindowChecked(event).getShell();
		if (selection instanceof TextSelection) {
			TextSelection textSelection = (TextSelection) selection;
			if (textSelection.isEmpty()) {
				MessageDialog.openInformation(shell, Messages.ValidatePathHandler_messageTitle,
						Messages.ValidatePathHandler_no_text_selected);
			} else {
				IType beanType = BindingDialog.getBeanType();
				if (beanType != null) {
					try {
						boolean isValid = new BeanIntrospector(beanType).isValidPath(textSelection
								.getText());
						if (isValid) {
							MessageDialog.openInformation(shell,
									Messages.ValidatePathHandler_messageTitle, NLS.bind(
											Messages.ValidatePathHandler_valid_selection,
											textSelection.getText(),
											beanType.getFullyQualifiedName()));
						} else {
							MessageDialog.openWarning(shell,
									Messages.ValidatePathHandler_messageTitle, NLS.bind(
											Messages.ValidatePathHandler_invalid_selection,
											textSelection.getText(),
											beanType.getFullyQualifiedName()));
						}
					} catch (Exception e) {
						logger.error("Error on validation path", e);
						MessageDialog.openError(shell,
								Messages.ValidatePathHandler_err_messageTitle, NLS.bind(
										Messages.ValidatePathHandler_err_validation,
										e.getMessage()));
					}
				} else {
					MessageDialog.openInformation(shell, Messages.ValidatePathHandler_messageTitle,
							Messages.ValidatePathHandler_no_bean_selected);
				}
			}
		} else {
			MessageDialog.openInformation(shell, Messages.ValidatePathHandler_messageTitle,
					Messages.ValidatePathHandler_no_text_selected);
		}
		return null;
	}

}
