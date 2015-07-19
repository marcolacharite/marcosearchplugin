package com.marco.search.plugin.provider;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class SearchResultContentProvider implements IStructuredContentProvider {

	@Override
	public void dispose() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Object[] getElements(Object inputElement) {
		if(inputElement instanceof List) {
			List<?> inputElementList = (List<?>) inputElement;
			List<IRegion> regions = new ArrayList<IRegion>();
			for(Object element: inputElementList) {
				if(element instanceof IRegion) {
					regions.add((IRegion)element);
				}
			}
			return regions.toArray();
		}
		return null;
	}


}
