package com.marco.search.plugin.handlers;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import com.marco.search.plugin.views.MarcoSearchView;

public class ShowMarcoSearchViewHandler extends AbstractHandler {

	@Override
	public Object execute(ExecutionEvent event) throws ExecutionException {
		try {
			IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			if(editorPart != null && editorPart instanceof ITextEditor) {
				ITextEditor textEditor = (ITextEditor) editorPart;
				
				MarcoSearchView marcoSearchView = (MarcoSearchView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("marcosearch.views.MarcoSearchView");
				marcoSearchView.setFocusToSearchTextbox();

				ISelection sel = textEditor.getSelectionProvider().getSelection();
				if (sel instanceof TextSelection)
				{
					ITextSelection textSel = (ITextSelection) sel;
					marcoSearchView.setSearchTextBoxValuve(textSel.getText());
				}
			}	
		} catch (PartInitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
