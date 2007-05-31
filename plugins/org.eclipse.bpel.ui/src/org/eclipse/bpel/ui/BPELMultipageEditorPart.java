/*******************************************************************************
 * Copyright (c) 2007 Intel Corporation.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Vitaly Tishkov, Intel - Initial API and Implementation
 *
 *******************************************************************************/ 

package org.eclipse.bpel.ui;

import org.eclipse.bpel.model.ExtensibleElement;
import org.eclipse.bpel.ui.uiextensionmodel.StartNode;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.wst.sse.ui.StructuredTextEditor;

import org.w3c.dom.Element;

public class BPELMultipageEditorPart extends MultiPageEditorPart {

	private StructuredTextEditor fTextEditor = null;
	private BPELEditor fDesignViewer = null;
	
	private static int DESIGN_PAGE_INDEX = 0;
	private static int SOURCE_PAGE_INDEX = 1;
	
	/**
	 * Adds the source page of the multi-page editor.
	 */
	private void addSourcePage() throws PartInitException {
	    try
	    {
	    	addPage(SOURCE_PAGE_INDEX, fTextEditor, getEditorInput());
	    	//FIXME I18N
	    	setPageText(SOURCE_PAGE_INDEX, "Source");
	    	firePropertyChange(PROP_TITLE);
	    } catch (PartInitException e) {
	    	ErrorDialog.openError(getSite().getShell(), "Error creating nested text editor", null, e.getStatus()); //$NON-NLS-1$
	    }
	}

	/**
	 * Connects the design viewer with the viewer selection manager. Should be
	 * done after createSourcePage() is done because we need to get the
	 * ViewerSelectionManager from the TextEditor. setModel is also done here
	 * because getModel() needs to reference the TextEditor.
	 */
	private void connectDesignPage() {
		/*
		 * Connect selection from the Design page to the selection provider
		 * for the BPELMultiPageEditorPart so that selection changes in the
		 * Design page will propogate across the workbench
		 */
		/*fDesignViewer.getAdaptingSelectionProvider().addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				((MultiPageSelectionProvider) getSite().getSelectionProvider()).fireSelectionChanged(event);
			}
		});*/

		/*
		 * Connect selection from the Design page to the selection provider of
		 * the Source page so that selection in the Design page will drive
		 * selection in the Source page. 
		 */
		fDesignViewer.getAdaptingSelectionProvider().addSelectionChangedListener(new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				//force selection update if only source page is not active
				if (getActivePage() != SOURCE_PAGE_INDEX) {
					try {
						ISelection sel = fDesignViewer.getSelection();
						Object selectedNode = ((IStructuredSelection)sel).getFirstElement();
						Element selectedNodeElement = null;
						
						if (selectedNode instanceof StartNode) {
							selectedNodeElement = ((StartNode)selectedNode).getProcess().getElement();
						} else if (selectedNode instanceof ExtensibleElement) {
							selectedNodeElement = ((ExtensibleElement)selectedNode).getElement();
						} 
					
						if (selectedNodeElement != null) {
							int charStart = ((Number)selectedNodeElement.getUserData("location.charStart")).intValue();
							//-1 to point to the '<' literal  
							TextSelection textSelection = new TextSelection(charStart - 1, 0);
							getTextEditor().getSelectionProvider().setSelection(textSelection);
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
		});
	}

	private void createAndAddDesignPage() {
		fDesignViewer = new BPELEditor();

		try
	    {
			addPage(DESIGN_PAGE_INDEX, fDesignViewer, getEditorInput());
			//FIXME I18N
			setPageText(DESIGN_PAGE_INDEX, "Design");
			firePropertyChange(PROP_TITLE);
	    } catch (PartInitException e) {
	    	ErrorDialog.openError(getSite().getShell(), "Error creating nested BPEL editor", null, e.getStatus()); //$NON-NLS-1$
	    }
	}

	private void createSourcePage() {
		fTextEditor = new StructuredTextEditor();
	}

	
	/**
	 * Creates the pages of this multi-page editor.
	 */
	protected void createPages() {
		// source page must be created before design page
			
		try {
			createSourcePage();
			createAndAddDesignPage();
			addSourcePage();
			connectDesignPage();
		} catch (PartInitException e) {
			//Logger.logException(e);
			throw new RuntimeException(e);
		} 
	}


	/**
	 * @see org.eclipse.ui.IEditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor progressMonitor) {
		fDesignViewer.doSave(progressMonitor);
		fTextEditor.doSave(progressMonitor);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#doSaveAs()
	 */
	public void doSaveAs() {
		//saveAs is not allowed; do nothing
	}
	
	StructuredTextEditor getTextEditor() {
		return fTextEditor;
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.IEditorPart#init(org.eclipse.ui.IEditorSite,
	 *      org.eclipse.ui.IEditorInput)
	 */
	public void init(IEditorSite site, IEditorInput input) throws PartInitException {
		try {
			super.init(site, input);
		} catch (Exception e) {
			e.printStackTrace();
		}
		setPartName(input.getName());
	}
	
	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}
}

