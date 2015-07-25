package com.marco.search.plugin.views;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;

import org.eclipse.core.databinding.DataBindingContext;
import org.eclipse.core.resources.IFile;
import org.eclipse.e4.ui.di.Focus;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.FindReplaceDocumentAdapter;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyAdapter;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Table;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.handlers.IHandlerService;
import org.eclipse.ui.part.ViewPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.eclipse.wb.swt.ResourceManager;

import com.marco.search.plugin.MarcoSearchUtils;
import com.marco.search.plugin.listeners.SearchTableSelectionListener;
import com.marco.search.plugin.provider.SearchLabelProvider;
import com.marco.search.plugin.provider.SearchResultContentProvider;

public class MarcoSearchView extends ViewPart{
	private DataBindingContext m_bindingContext;
	private Combo searchComboBox;
	private Combo replaceComboBox;
	private TableViewer tableViewer;
	private Button replaceButton;
	
	private IFile file;

	public MarcoSearchView() {
	}

	/**
	 * Create contents of the view part.
	 */
	@PostConstruct
	public void createControls(Composite parent) {
		parent.setLayout(new FillLayout(SWT.HORIZONTAL));
		
		Composite composite = new Composite(parent, SWT.NONE);
		GridLayout gl_composite = new GridLayout(3, false);
		gl_composite.horizontalSpacing = 3;
		gl_composite.marginWidth = 2;
		composite.setLayout(gl_composite);
		
		Label lblSearchFor = new Label(composite, SWT.NONE);
		lblSearchFor.setText("Search for: ");
		
		searchComboBox = new Combo(composite, SWT.BORDER);
		searchComboBox.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.keyCode == SWT.CR  || e.keyCode == SWT.KEYPAD_CR) {
					executeSearch();
				}
			}
		});
		searchComboBox.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
		
		Button searchButton = new Button(composite, SWT.NONE);
		searchButton.setToolTipText("Search");
		searchButton.setImage(ResourceManager.getPluginImage("MarcoSearch", "icons/search.gif"));
		searchButton.setLayoutData(new GridData(SWT.RIGHT, SWT.FILL, false, false, 1, 1));
		searchButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeSearch();
			}
		});
		
		Label lblReplaceWith = new Label(composite, SWT.NONE);
		lblReplaceWith.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1));
		lblReplaceWith.setText("Replace with:");
		
		replaceComboBox = new Combo(composite, SWT.NONE);
		replaceComboBox.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		replaceButton = new Button(composite, SWT.NONE);
		replaceButton.setImage(ResourceManager.getPluginImage("MarcoSearch", "icons/synced.png"));
		replaceButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				executeReplace();
			}
		});
		
		tableViewer = new TableViewer(composite, SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI);
		Table table = tableViewer.getTable();
		table.setLinesVisible(true);
		GridData gd_tree = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_tree.heightHint = 0;
		gd_tree.widthHint = 189;
		table.setLayoutData(gd_tree);
		tableViewer.setContentProvider(new SearchResultContentProvider());
		tableViewer.setLabelProvider(new SearchLabelProvider());
		tableViewer.addSelectionChangedListener(new SearchTableSelectionListener());
		GridData gd_table = new GridData(SWT.FILL, SWT.FILL, true, true, 3, 1);
		gd_table.widthHint = 149;
		m_bindingContext = initDataBindings();
	}

	@PreDestroy
	public void dispose() {
	}

	@Focus
	public void setFocus() {
		// TODO	Set the focus to control
	}
	protected DataBindingContext initDataBindings() {
		DataBindingContext bindingContext = new DataBindingContext();
		//
		return bindingContext;
	}

	@Override
	public void createPartControl(Composite parent) {
		createControls(parent);
	}
	
	public void setFocusToSearchTextbox() {
		searchComboBox.setFocus();
	}
	
	public void setSearchTextBoxValue(String value) {
		searchComboBox.setText(value);
		searchComboBox.setSelection(new Point(value.length(), value.length()));
	}
	
	public void highLightValueInSearchTextbox() {
		searchComboBox.setSelection(new Point(0, searchComboBox.getText().length()));
	}
	
	public String getSearchTextBoxValue() {
		return searchComboBox.getText();
	}
	
	public void setFocusToReplaceTextbox() {
		replaceComboBox.setFocus();
	}
	
	public void setReplaceTextBoxValuve(String value) {
		replaceComboBox.setText(value);
	}
	
	public String getReplaceTextBoxValue() {
		return replaceComboBox.getText();
	}
	
	public void displayResult(List<IRegion> searchResults) {
		tableViewer.setInput(searchResults);
	}
	
	public void addSearchOccuranceToSearchTextBox(String occurance) {
		boolean alreadyExist = false;
		for(String item: searchComboBox.getItems()) {
			if(item.equals(occurance)) {
				alreadyExist = true;
			}
		}
		if(!alreadyExist) {
			searchComboBox.getItems();
			searchComboBox.add(occurance);
		}
	}
	
	public void selectSearchResult(int index) {
		tableViewer.getTable().setSelection(index);
		tableViewer.getTable().setFocus();
	}
	
	public void setReplaceButtonEnable(boolean isEnable) {
		replaceButton.setEnabled(isEnable);
	}
	
	public IFile getFileWhereSearchOccured() {
		return file;
	}
	
	private void executeSearch() {
		
		// Clearing search result
		tableViewer.getTable().clearAll();
		
		executeSearch(getSearchTextBoxValue());
	}
	
	private void executeSearch(String stringToSearch) {
		try {
			IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			
			IFile file = null;
			if(editorPart.getEditorInput() instanceof IFileEditorInput) {
				IFileEditorInput fileEditorInput = (IFileEditorInput) editorPart.getEditorInput();
				file = fileEditorInput.getFile();
			}
		
			if(editorPart != null && editorPart instanceof ITextEditor) {
				ITextEditor textEditor = (ITextEditor) editorPart;
				IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
				
				List<IRegion> regions = new ArrayList<IRegion>();
				FindReplaceDocumentAdapter findReplaceAdapter = new FindReplaceDocumentAdapter(doc);
				
				IRegion region = findReplaceAdapter.find(0, stringToSearch, true, false, false, false);
				if(region != null) {
					while (region != null) {
						regions.add(region);
						region = findReplaceAdapter.find(region.getOffset() +  region.getLength(), stringToSearch, true, false, false, false);
					}
				}

			    if(!regions.isEmpty()) {
			    	displayResult(regions);
			    }
				
			}
		} catch (BadLocationException e) {
			e.printStackTrace();
		}
		
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
		
		if(editorPart.getEditorInput() instanceof IFileEditorInput) {
			IFileEditorInput fileEditorInput = (IFileEditorInput) editorPart.getEditorInput();
			file = fileEditorInput.getFile();
		}
	}
	
	private void executeReplace() {
		IHandlerService handlerService = (IHandlerService) getSite().getService(IHandlerService.class);
		List<IRegion> regions = null;
		
		ISelection selection = tableViewer.getSelection();
		if(selection instanceof StructuredSelection) {
			StructuredSelection structuredSelection = (StructuredSelection) selection;
			regions = structuredSelection.toList();
			regions = getRegionsOrderedByOffsetDecreasing(regions);
		}
		
		if (regions != null && !regions.isEmpty()) {
			MarcoSearchUtils.showFileInEditor(file);
			IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
			
			IFile file = null;
			if(editorPart.getEditorInput() instanceof IFileEditorInput) {
				IFileEditorInput fileEditorInput = (IFileEditorInput) editorPart.getEditorInput();
				file = fileEditorInput.getFile();
			}
		
			if(editorPart != null && editorPart instanceof ITextEditor) {
				ITextEditor textEditor = (ITextEditor) editorPart;
				IDocument doc = textEditor.getDocumentProvider().getDocument(textEditor.getEditorInput());
				String stringToReplaceWith = replaceComboBox.getText();
				try {
					String replacedString = doc.get(regions.get(0).getOffset(), regions.get(0).getLength());
					for(IRegion region: regions) {
						doc.replace(region.getOffset(), region.getLength(), stringToReplaceWith);
					}
					
					executeSearch(replacedString);
					
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
			}
			
			
		}
		
		IEditorPart editorPart = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().getActiveEditor();
	}
	
	private List<IRegion> getRegionsOrderedByOffsetDecreasing(List<IRegion> regions) {
		regions.sort(new Comparator<IRegion>() {

			@Override
			public int compare(IRegion region1, IRegion region2) {
				if(region1.getOffset() > region2.getOffset()) {
					return -1;
				}
				else {
					return 1;
				}
			}
		});
		return regions;
	}
}
