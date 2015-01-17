package org.eclipse.plugin.kpax.beaninspector.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.text.ITextOperationTarget;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.osgi.util.NLS;
import org.eclipse.plugin.kpax.beaninspector.JavaBeanInspectorPlugin;
import org.eclipse.plugin.kpax.beaninspector.Messages;
import org.eclipse.plugin.kpax.beaninspector.gui.ContextualBeanInspector;
import org.eclipse.plugin.kpax.beaninspector.logger.Logger;
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

	private final Logger logger = JavaBeanInspectorPlugin.getLogger();

	public Object execute(ExecutionEvent event) throws ExecutionException {
		IWorkbenchPart workbenchPart = HandlerUtil.getActivePartChecked(event);
		if (workbenchPart != null && workbenchPart instanceof ITextEditor) {
			ITextViewer viewer = (ITextViewer) ((ITextEditor) workbenchPart)
					.getAdapter(ITextOperationTarget.class);
			if (viewer != null) {
				StyledText text = viewer.getTextWidget();
				try {
					new ContextualBeanInspector(text.getShell()).showAt(computeLocation(text));
				} catch (Exception e) {
					logger.error(e);
					MessageDialog.openError(HandlerUtil.getActiveWorkbenchWindowChecked(event)
							.getShell(), Messages.BeanInspector_err_title, NLS.bind(
							Messages.ContextualBeanInspector_err_build_menu, e.getMessage()));
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
