package org.eclipse.plugin.kpax.javabinder.model;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.ITextEditor;

public class Snippet {
	public static void main(String[] args) {
		try {
			IEditorPart editor = PlatformUI.getWorkbench().getActiveWorkbenchWindow()
					.getActivePage().getActiveEditor();
			if (editor instanceof ITextEditor) {
				final ITextEditor textEditor = (ITextEditor) editor;
				IDocumentProvider documentProvider = textEditor.getDocumentProvider();
				IDocument document = documentProvider.getDocument(textEditor.getEditorInput());
				ISelection selection = textEditor.getSelectionProvider().getSelection();
				if (selection instanceof TextSelection) {
					final TextSelection textSelection = (TextSelection) selection;
					String newText = "/*" + textSelection.getText() + "*/";
					document.replace(textSelection.getOffset(), textSelection.getLength(), newText);
				}
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
