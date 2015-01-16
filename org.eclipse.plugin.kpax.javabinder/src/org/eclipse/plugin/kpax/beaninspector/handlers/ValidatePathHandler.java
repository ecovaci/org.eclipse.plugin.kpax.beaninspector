package org.eclipse.plugin.kpax.beaninspector.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.plugin.kpax.beaninspector.gui.BindingDialog;
import org.eclipse.plugin.kpax.beaninspector.introspector.BeanIntrospector;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;

/**
 * JavaBean Inspector command handler.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class ValidatePathHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
		ISelection selection = HandlerUtil.getCurrentSelection(event);
		IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);
		if (selection instanceof TextSelection) {
			TextSelection textSelection = (TextSelection) selection;
			System.out.println("Selection " + textSelection);
			if (textSelection.isEmpty()) {
				MessageDialog.openInformation(window.getShell(), "Validation Message",
						"No text selected, nothing to validate!");
			} else {
				IType beanType = BindingDialog.getBeanType();
				if (beanType != null) {
					try {
						boolean isValid = new BeanIntrospector(beanType).isValidPath(textSelection
								.getText());
						if (isValid) {
							MessageDialog.openInformation(window.getShell(),
									"Path Validation Message",
									"Validate [" + textSelection.getText() + "] against ["
											+ beanType.getFullyQualifiedName()
											+ "] type.\nThe selected path is valid.");
						} else {
							MessageDialog.openWarning(window.getShell(), "Path Validation Message",
									"Validate [" + textSelection.getText() + "] against ["
											+ beanType.getFullyQualifiedName()
											+ "] type.\nThe selected path is not valid.");
						}
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				} else {
					MessageDialog.openInformation(window.getShell(), "Path Validation Message",
							"Select a Java type using JavaBean Inspector then try again!");
				}
			}
		} else {
			MessageDialog.openInformation(window.getShell(), "Path Validation Message",
					"Select a path to validate!");
		}
		return null;
	}

}
