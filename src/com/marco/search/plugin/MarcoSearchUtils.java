package com.marco.search.plugin;

import org.eclipse.core.resources.IFile;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

public class MarcoSearchUtils {
	public static void showFileInEditor(IFile file) {
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if(editorPart == null) {
			try {
				IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file, true);
			} catch (PartInitException e) {
				e.printStackTrace();
			}
		}
		else {
			IFileEditorInput fileEditorInput = (IFileEditorInput) editorPart.getEditorInput();
			IFile fileOpened = fileEditorInput.getFile();		
			if( fileOpened != file) {
				try {
					IDE.openEditor(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage(), file, true);
				} catch (PartInitException e) {
					e.printStackTrace();
				}
				
			}
		}
		
		editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}
}
