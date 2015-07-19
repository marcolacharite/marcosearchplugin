package com.marco.search.plugin.provider;

import org.eclipse.core.resources.IFile;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.DelegatingStyledCellLabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.StyledString;
import org.eclipse.jface.viewers.StyledString.Styler;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.TextStyle;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.texteditor.ITextEditor;

public class SearchLabelProvider extends DelegatingStyledCellLabelProvider  {
	
	public SearchLabelProvider() {
        super(new IStyledLabelProvider() {
        	
        
			@Override
			public void addListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
		
			}
		
			@Override
			public void dispose() {
				// TODO Auto-generated method stub
		
			}
		
			@Override
			public boolean isLabelProperty(Object element, String property) {
				// TODO Auto-generated method stub
				return false;
			}
		
			@Override
			public void removeListener(ILabelProviderListener listener) {
				// TODO Auto-generated method stub
		
			}
		
			@Override
			public Image getImage(Object element) {
				// TODO Auto-generated method stub
				return null;
			}
		
			@Override
			public StyledString getStyledText(Object element) {
				if(element instanceof IRegion) {
					IRegion region = (IRegion) element;
					StyledString styledString = null;
					try {
						String line = getLine(region);
						int lineNumber = getLineNumber(region);
						int lineOffset = getLineOffset(region);
						String lineResultToShow = "Line " + lineNumber + " - ";
						styledString = new StyledString(lineResultToShow + line);
						styledString.setStyle(region.getOffset() - lineOffset + lineResultToShow.length(), region.getLength(), new Styler() {
							
							@Override
							public void applyStyles(TextStyle textStyle) {
								textStyle.font = JFaceResources.getFontRegistry().getBold("");
								
							}
						});
					} catch (BadLocationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					return styledString;
				}
				return null;
			}
			
			private String getLine(IRegion region) throws BadLocationException {
				IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			
				if(editorPart != null && editorPart instanceof ITextEditor) {
					ITextEditor textEditor = (ITextEditor) editorPart;
					IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
					int lineNumber = doc.getLineOfOffset(region.getOffset());
					int lineOffset = doc.getLineOffset(lineNumber);
					int lineLength = doc.getLineLength(lineNumber);
					
					return doc.get(lineOffset, lineLength);
				}
				
				return null;
			}
			
			private int getLineNumber(IRegion region) throws BadLocationException {
				IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			
				if(editorPart != null && editorPart instanceof ITextEditor) {
					ITextEditor textEditor = (ITextEditor) editorPart;
					IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
					int lineNumber = doc.getLineOfOffset(region.getOffset());
					
					return lineNumber;
				}
				
				return -1;
			}
			

			private int getLineOffset(IRegion region) throws BadLocationException {
				IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			
				if(editorPart != null && editorPart instanceof ITextEditor) {
					ITextEditor textEditor = (ITextEditor) editorPart;
					IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
					int lineNumber = doc.getLineOfOffset(region.getOffset());
					int lineOffset = doc.getLineOffset(lineNumber);
					
					return lineOffset;
				}
				
				return -1;
			}
			

			private int getLineLength(IRegion region) throws BadLocationException {
				IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			
				if(editorPart != null && editorPart instanceof ITextEditor) {
					ITextEditor textEditor = (ITextEditor) editorPart;
					IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
					int lineNumber = doc.getLineOfOffset(region.getOffset());
					int lineLength = doc.getLineLength(lineNumber);
					
					return lineLength;
				}
				
				return -1;
			}
			
			
	    });
	
	}
}
