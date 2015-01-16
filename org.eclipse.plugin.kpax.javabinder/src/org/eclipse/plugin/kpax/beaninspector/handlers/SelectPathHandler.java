package org.eclipse.plugin.kpax.beaninspector.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.plugin.kpax.beaninspector.gui.ContextualBeanInspector;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.handlers.HandlerUtil;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * JavaBean Inspector command handler.
 * 
 * @see org.eclipse.core.commands.IHandler
 * @see org.eclipse.core.commands.AbstractHandler
 */
public class SelectPathHandler extends AbstractHandler {

	public Object execute(ExecutionEvent event) throws ExecutionException {
			IWorkbenchPart workbenchPart = HandlerUtil.getActivePartChecked(event);
			if (workbenchPart != null && workbenchPart instanceof ITextEditor) {
				ITextViewer viewer = (ITextViewer) ((ITextEditor) workbenchPart)
						.getAdapter(ITextOperationTarget.class);
				if (viewer != null) {
					StyledText text = viewer.getTextWidget();
					try {
						new ContextualBeanInspector(text.getShell()).showAt(computeLocation(text));
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		
		return null;
	}
	
	private Point computeLocation(StyledText text) {
		Point absolutePosition = text.getDisplay().getCursorLocation();
		Point relativePosition = text.toControl(absolutePosition);
		Point caretLocation = text.getCaret().getLocation();
		int x = caretLocation.x + absolutePosition.x - relativePosition.x;
		int y = caretLocation.y + absolutePosition.y - relativePosition.y + text.getLineHeight();
		return new Point(x, y);
	}
}
