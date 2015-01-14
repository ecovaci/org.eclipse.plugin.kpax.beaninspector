package org.eclipse.plugin.kpax.javabinder.util;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class Editor {
	private IDocument document;
	private ISelection selection;

	public boolean parse() {
		IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
				.getActiveEditor();
		if (editor instanceof ITextEditor) {
			final ITextEditor textEditor = (ITextEditor) editor;
			IDocumentProvider documentProvider = textEditor.getDocumentProvider();
			this.document = documentProvider.getDocument(textEditor.getEditorInput());
			this.selection = textEditor.getSelectionProvider().getSelection();
			return true;
		} else {
			return false;
		}
	}

	public ISelection getSelection() {
		return selection;
	}

	public boolean replaceSelection (String newValue) throws BadLocationException {
		if (selection instanceof TextSelection) {
			final TextSelection textSelection = (TextSelection) selection;
			document.replace(textSelection.getOffset(), textSelection.getLength(), newValue);
			return true;
		} else {
			return false;
		}
	}
}
