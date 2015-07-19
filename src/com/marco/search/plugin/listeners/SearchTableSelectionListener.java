package com.marco.search.plugin.listeners;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

import com.marco.search.plugin.MarcoSearchUtils;
import com.marco.search.plugin.views.MarcoSearchView;

public class SearchTableSelectionListener implements ISelectionChangedListener {
	
	@Override
	public void selectionChanged(SelectionChangedEvent event) {
		try {
			MarcoSearchView marcoSearchView = (MarcoSearchView) PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().showView("marcosearch.views.MarcoSearchView");
			StructuredSelection treeSelection = (StructuredSelection) event.getSelection();	
			if(treeSelection != null && !treeSelection.isEmpty()) {
				marcoSearchView.setReplaceButtonEnable(true);
				IRegion region = (IRegion) treeSelection.getFirstElement();			
				
				IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
				
				MarcoSearchUtils.showFileInEditor(marcoSearchView.getFileWhereSearchOccured());
		
				// Setting selection in editor
				if(editorPart != null && editorPart instanceof ITextEditor) {
					if(treeSelection.getFirstElement() != null && treeSelection.getFirstElement() instanceof IRegion) {
						TextSelection textSelection = new TextSelection(region.getOffset(), region.getLength());
						editorPart.getSite().getSelectionProvider().setSelection(textSelection);
					}
				}
			}
			else {
	
				marcoSearchView.setReplaceButtonEnable(false);
			}	
			
		} catch (PartInitException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
			
	}
}
