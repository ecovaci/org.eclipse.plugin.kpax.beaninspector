package org.eclipse.plugin.kpax.beaninspector.util;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

/**
 * @author eugen.covaci.q@gmail.com
 *
 */
public class Editor {

	public static boolean replaceSelection (String newValue) throws BadLocationException {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		if (editor instanceof ITextEditor) {
			final ITextEditor textEditor = (ITextEditor) editor;
			IDocumentProvider documentProvider = textEditor.getDocumentProvider();
			IDocument document = documentProvider.getDocument(textEditor.getEditorInput());
			ISelection selection = textEditor.getSelectionProvider().getSelection();
			if (selection instanceof TextSelection) {
				final TextSelection textSelection = (TextSelection) selection;
				document.replace(textSelection.getOffset(), textSelection.getLength(), newValue);
				textEditor.selectAndReveal(textSelection.getOffset() + newValue.length() , 0);
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}
}
