package org.eclipse.plugin.kpax.beaninspector.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.plugin.kpax.beaninspector.gui.BindingDialog;
import org.eclipse.plugin.kpax.beaninspector.gui.ContextualBeanInspector;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * Our sample handler extends AbstractHandler, an IHandler base class.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class BinderHandler extends AbstractHandler {

	private static final String SELECT_CLASS_COMMAND_ID = "org.eclipse.plugin.kpax.beaninspector.commands.javabinderCommand_selectClass";

	private static final String SELECT_PATH_COMMAND_ID = "org.eclipse.plugin.kpax.beaninspector.commands.javabinderCommand_selectPath";

	public Object execute(ExecutionEvent event) throws ExecutionException {
		if (SELECT_CLASS_COMMAND_ID.equals(event.getCommand().getId())) {
			IWorkbenchWindow window = HandlerUtil.getActiveWorkbenchWindowChecked(event);

			BindingDialog bindingDialog = new BindingDialog(window.getShell());
			bindingDialog.open();
		} else if (SELECT_PATH_COMMAND_ID.equals(event.getCommand().getId())) {
			ISelection selection = HandlerUtil.getCurrentSelection(event);
			if (selection instanceof TextSelection) {
				TextSelection textSelection = (TextSelection) selection;
				System.out.println("Selection " + textSelection);
			}

			IWorkbenchPart workbenchPart = HandlerUtil.getActivePartChecked(event);
			if (workbenchPart != null && workbenchPart instanceof ITextEditor) {
				ITextViewer viewer = (ITextViewer) ((ITextEditor) workbenchPart)
						.getAdapter(ITextOperationTarget.class);
				if (viewer != null) {
					StyledText text = viewer.getTextWidget();
					Point caretLocation = computeContextualInspectorLocation(text);
					try {
						new ContextualBeanInspector(text.getShell()).showAt(caretLocation);
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		return null;
	}

	private Point computeContextualInspectorLocation(StyledText text) {
		Point absolutePosition = text.getDisplay().getCursorLocation();
		Point relativePosition = text.toControl(absolutePosition);
		Point caretLocation = text.getCaret().getLocation();
		int x = caretLocation.x + absolutePosition.x - relativePosition.x;
		int y = caretLocation.y + absolutePosition.y - relativePosition.y + text.getLineHeight();
		return new Point(x, y);
	}
}
