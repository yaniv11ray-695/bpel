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

import org.eclipse.bpel.common.extension.model.ExtensionMap;
import org.eclipse.bpel.common.ui.editmodel.IEditModelListener;
import org.eclipse.bpel.common.ui.editmodel.ResourceInfo;
import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.CorrelationSet;
import org.eclipse.bpel.model.ExtensibleElement;
import org.eclipse.bpel.model.PartnerLink;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.ui.BPELEditor.BPELEditorAdapter;
import org.eclipse.bpel.ui.editparts.ProcessTrayEditPart;
import org.eclipse.bpel.ui.editparts.util.OutlineTreePartFactory;
import org.eclipse.bpel.ui.properties.BPELPropertySection;
import org.eclipse.bpel.ui.uiextensionmodel.StartNode;
import org.eclipse.bpel.ui.util.BPELEditModelClient;
import org.eclipse.bpel.ui.util.BPELReader;
import org.eclipse.bpel.ui.util.ModelHelper;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalEditPart;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.editparts.ZoomManager;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.parts.ContentOutlinePage;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.ide.IGotoMarker;
import org.eclipse.ui.internal.views.properties.tabbed.view.Tab;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabDescriptor;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyViewer;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.MultiPageEditorPart;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.sse.ui.StructuredTextEditor;


import org.w3c.dom.Element;

public class BPELMultipageEditorPart extends MultiPageEditorPart 
										implements IEditModelListener, 
										           IGotoMarker {

	class OutlinePage extends ContentOutlinePage {
		private PageBook pageBook;
		private Control outline;
		private Canvas overview;
		private IAction showOutlineAction, showOverviewAction;
		static final int ID_OUTLINE  = 0;
		static final int ID_OVERVIEW = 1;
		private Thumbnail thumbnail;

		public OutlinePage(EditPartViewer viewer) {
			super(viewer);
		}

		// increase visibility.
		public EditPartViewer getViewer() {
			return super.getViewer();
		}

		private void configureOutlineViewer() {
			getViewer().setEditDomain(getEditDomain());
			getViewer().setEditPartFactory(new OutlineTreePartFactory());

			fDesignViewer.registerViewer(getViewer());

			//FIXME should we add the same for src tab?
			ContextMenuProvider provider = new ProcessContextMenuProvider(getViewer(), fDesignViewer.getActionRegistry());

			getViewer().setContextMenu(provider);
			getSite().registerContextMenu("org.eclipse.bpel.outline.contextmenu", //$NON-NLS-1$
				provider, 
				getSite().getSelectionProvider());
			getViewer().setKeyHandler(fDesignViewer.getKeyHandler());
			// TODO: Drag and drop support goes here
			// getViewer().addDropTargetListener(new BPELTemplateTransferDropTargetListener(getViewer()));
			IToolBarManager tbm = getSite().getActionBars().getToolBarManager();
			showOutlineAction = new Action() {
				public void run() {
					showPage(ID_OUTLINE);
				}
                
                public String getToolTipText() {
                    return Messages.OutlinePage_showOutlineView;
                }
			};
			showOutlineAction.setImageDescriptor(BPELUIPlugin.getPlugin().getImageDescriptor(IBPELUIConstants.ICON_OUTLINE_16)); 
			tbm.add(showOutlineAction);
			showOverviewAction = new Action() {
				public void run() {
					showPage(ID_OVERVIEW);
				}
                
                public String getToolTipText() {
                    return Messages.OutlinePage_showOverviewView;
                }
			};
			showOverviewAction.setImageDescriptor(BPELUIPlugin.getPlugin().getImageDescriptor(IBPELUIConstants.ICON_OVERVIEW_16)); 	
			tbm.add(showOverviewAction);
			showPage(ID_OUTLINE);
		}

		public Control getControl() {
			return pageBook;
		}

		public void createControl(Composite parent) {
			pageBook = new PageBook(parent, SWT.NONE);
			outline = getViewer().createControl(pageBook);
			overview = new Canvas(pageBook, SWT.NONE);
			pageBook.showPage(outline);
			configureOutlineViewer();
			// TODO: Add to the adapting selection provider
			//getSelectionSynchronizer().addViewer(getViewer());

			getViewer().setContents(getProcess());
		}

		private void initializeOverview() {
			LightweightSystem lws = new LightweightSystem(overview);
			RootEditPart rep = fDesignViewer.getGraphicalViewer().getRootEditPart();
			if (rep instanceof GraphicalBPELRootEditPart) {
				GraphicalBPELRootEditPart root = (GraphicalBPELRootEditPart)rep;
				thumbnail = new ScrollableThumbnail((Viewport)root.getFigure());
				thumbnail.setSource(root.getLayer(LayerConstants.PRINTABLE_LAYERS));
				lws.setContents(thumbnail);
			}
		}

		private void showPage(int id) {
			if (id == ID_OUTLINE) {
				showOutlineAction.setChecked(true);
				showOverviewAction.setChecked(false);
				pageBook.showPage(outline);
				if (thumbnail != null)
					thumbnail.setVisible(false);
			} else if (id == ID_OVERVIEW) {
				initializeOverview();
				showOutlineAction.setChecked(false);
				showOverviewAction.setChecked(true);
				pageBook.showPage(overview);
				thumbnail.setVisible(true);
			}
		}

		public void dispose() {
			super.dispose();
		}
		
		public void init(IPageSite pageSite) {
			super.init(pageSite);
			//should ActionRegistry be here too? 
			ActionRegistry registry = fDesignViewer.getActionRegistry();
			IActionBars bars = pageSite.getActionBars();
			String id = ActionFactory.UNDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.REDO.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.DELETE.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			id = ActionFactory.REVERT.getId();
			bars.setGlobalActionHandler(id, registry.getAction(id));
			bars.updateActionBars();
		}
	}

	private Process process;
	
	private DefaultEditDomain editDomain;
	
	protected ModelListenerAdapter modelListenerAdapter;
	
	private Resource extensionsResource;
	
	private ExtensionMap extensionMap;
	
	private StructuredTextEditor fTextEditor = null;
	private BPELEditor fDesignViewer = null;
	
	// reacts to changes on the BPEL file (e.g. move, rename)
	private IFileChangeListener fileChangeListener;
	
	// refactoring listeners
	protected IResourceChangeListener postBuildRefactoringListener;

	
	private OutlinePage outlinePage;
	protected BPELTabbedPropertySheetPage currentPropertySheetPage;
	
	private static int DESIGN_PAGE_INDEX = 0;
	private static int SOURCE_PAGE_INDEX = 1;

	
	public BPELMultipageEditorPart() {
		super();
		setEditDomain(new BPELEditDomain(this));
	}
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
		fDesignViewer = new BPELEditor(getEditDomain());
		loadModel();
		
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
			initializeFileChangeListener();
			initializeRefactoringListener();
		} catch (PartInitException e) {
			//Logger.logException(e);
			throw new RuntimeException(e);
		} 
		
		//updateTitle();
	}


	public void dispose() {
		/*if (outlinePage != null && outlinePage.getViewer() != null) {
			outlinePage.getViewer().setContents(null);
		}*/
		outlinePage = null;
 		process = null;
 		
		if (fileChangeListener != null) {
			BPELUIPlugin.getPlugin().getResourceChangeListener().removeListener(fileChangeListener);
		}

		if (postBuildRefactoringListener != null) {
			IWorkspace workspace = ResourcesPlugin.getWorkspace();
			workspace.removeResourceChangeListener(postBuildRefactoringListener);
		}

		//FIXME should we call them? Looks like no.
		//fDesignViewer.dispose();
		//fTextEditor.dispose();

		super.dispose();
	}
	
	public void doRevertToSaved(IProgressMonitor monitor) {
		// Do the work within an operation because this is a long running activity that modifies the workbench.
//		WorkspaceModifyOperation operation = new WorkspaceModifyOperation() {
//			protected void execute(IProgressMonitor monitor) throws CoreException {
//				try {
//					getCommandFramework().abortCurrentChange();
//					getCommandStack().flush();
//					
//					// de-select anything selected on the canvas!  Otherwise removing things
//					// will trigger a bunch of behaviour when the selected object(s) are
//					// removed..
//					adaptingSelectionProvider.setSelection(StructuredSelection.EMPTY);
//					
//					process = null;
//					extensionMap = null;
//					extensionsResource = null;
//					lastSelectedEditPart = null;
//					// unload all resources (otherwise they stay around taking up space..?)
//					for (Iterator it = getResourceSet().getResources().iterator(); it.hasNext(); ) {
//						Resource res = (Resource)it.next();
//						res.unload();
//					}
//					loadModel();
//					getGraphicalViewer().setContents(process);
//					getTrayViewer().setContents(process);
//					if (outlinePage != null && outlinePage.getViewer() != null) {
//                      // hack!
//						if (Platform.getWS().equals(Platform.WS_GTK)) {
//							Tree tree = (Tree) outlinePage.getViewer().getControl();
//							if (tree != null) {
//								tree.setRedraw(false);
//								TreeItem[] items = tree.getItems();
//								for (int i = 0; i < items.length; i++) {
//									items[i].dispose();
//								}
//								tree.setRedraw(true);
//							}
//						}
//						outlinePage.getViewer().setContents(process);
//					}
//					selectModelObject(getProcess());
//				}
//				catch (Exception e) {
//					BPELUIPlugin.log(e);
//				}
//			}
//		};
//
//		try {
//			// This runs the options, and shows progress.
//			// (It appears to be a bad thing to fork this onto another thread.)
//			new ProgressMonitorDialog(getSite().getShell()).run(false, false, operation);
//
//			// Refresh the necessary state.
//			firePropertyChange(IEditorPart.PROP_DIRTY);
//		} catch (Exception e) {
//			BPELUIPlugin.log(e);
//		}
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
	
	protected BPELTabbedPropertySheetPage createBPELTabbedPropertySheetPage() {
		//FIXME should the BPELTabbedPropertySheetPage has BPELMultiPageEditorPart as the 2nd argument?
		return new BPELTabbedPropertySheetPage(new ITabbedPropertySheetPageContributor() {
		    public String getContributorId() {
		    	// same as the palette one
		    	//return fDesignViewer.getPaletteAdditionsContributorId();
		    	return IBPELUIConstants.BPEL_EDITOR_ID;
		    }
        }, fDesignViewer);
	}

	
	public Object getAdapter(Class type) {
		if (type == Process.class) {
			return process;
		}
		
		if (type == BPELEditModelClient.class) {
			return process;
		}

		//FIXME should we kill it?
		if (type == ModelListenerAdapter.class) {
			return modelListenerAdapter;
		}

		//FIXME should we kill it?
		if (type == Resource.class) {
			return extensionsResource;
		}

		//FIXME should we kill it?
		if (type == ExtensionMap.class) {
			return extensionMap;
		}

		//FIXME should we kill it?
		if (type == CommandStack.class) {
			return getCommandStack();
		}

		if (type == IContentOutlinePage.class) {
			if (outlinePage == null) {
				outlinePage = new OutlinePage(new TreeViewer());
			}
			return outlinePage;
		}

		if (type == IPropertySheetPage.class) {
			// We can't cache this object because the properties framework needs a new instance
			// every time it calls this method. 
			currentPropertySheetPage = createBPELTabbedPropertySheetPage();
			return currentPropertySheetPage;
		}

	    return super.getAdapter(type);
	  }

	public CommandStack getCommandStack() {
		return getEditDomain().getCommandStack();
	}
	
	/**
	 * Returns the design viewer
	 * @return the design viewer
	 */
	protected BPELEditor getDesignEditor() {
		return fDesignViewer;
	}

	/**
	 * Returns the edit domain.
	 * @return the edit domain
	 */
	protected DefaultEditDomain getEditDomain() {
		return editDomain;
	}

	protected IFile getFileInput() {
		return ((IFileEditorInput) getEditorInput()).getFile();
	}

	public Process getProcess() {
		return process;
	}

	/**
	 * Returns the design viewer
	 * @return the design viewer
	 */
	protected StructuredTextEditor getSourceViewer() {
		return fTextEditor;
	}

	StructuredTextEditor getTextEditor() {
		return fTextEditor;
	}
	
	public void gotoMarker(IMarker marker) {
		
		// One such mechanism is to use the href of the model object
		// generated by the validator or whatever ...
		
		String href = null;
		try {
			href = (String) marker.getAttribute( "address.model" );
		} catch (CoreException ex) {
			BPELUIPlugin.log(ex);
		}		
		
		// lookup by object href in the model.
		EObject modelObject = null;
		
		if (href != null) {
			try {
				modelObject = fDesignViewer.getResource().getEObject( href );
			} catch (Throwable t) {
				BPELUIPlugin.log(t);
			}
		}
		
		if (modelObject == null) {
			return;
		}
		
		gotoMarker ( marker, modelObject );
	}

	
	void gotoMarker ( IMarker marker, EObject modelObject ) {
		
		// TODO: is this bogus now that we have AdaptingSelectionProvider?
				
		
		// The closest parent which has an edit part in the graph view.
		//
		// The following do not have viewers in the graph view:
		//  1) Variables, PartnerLinks, Correlation Sets
		// If it's any of those, then we have to reveal the closest container
		// and then select the model object and show the properties.

		GraphicalViewer graphViewer = fDesignViewer.getGraphicalViewer();
		EObject refObj = null;
		
		EditPart editPart = null;
		if ( modelObject instanceof Variable ||
		     modelObject instanceof PartnerLink ||
		     modelObject instanceof CorrelationSet ) {
			
			refObj = ModelHelper.getContainingScope(modelObject);
			editPart = (EditPart)graphViewer.getEditPartRegistry().get(refObj);
			if (editPart != null) {
				graphViewer.reveal(editPart);
			}			
			fDesignViewer.selectModelObject(modelObject);
			
		} else if (modelObject instanceof Activity) {
			
			// activity objects are on the graphical viewer
			refObj = modelObject;
			editPart = (EditPart)graphViewer.getEditPartRegistry().get(refObj);
			
			if (editPart != null) {
				graphViewer.reveal(editPart);
			}
			
			fDesignViewer.selectModelObject(modelObject);
			
			
		} else {
				
			refObj = modelObject;
			while (refObj != null && !(refObj instanceof Activity) ) {
				refObj = refObj.eContainer();
			}
			
			// select process by default.
			if (refObj == null) {
				refObj = ModelHelper.getProcess( modelObject ) ;
			}
			
			modelObject = refObj;
			
			editPart = (EditPart)graphViewer.getEditPartRegistry().get(modelObject);
			
			if (editPart != null) {
				graphViewer.reveal(editPart);
			}
			
			fDesignViewer.selectModelObject(modelObject);
		}
						
		// If possible, try to display the marker in a property section.
		BPELTabbedPropertySheetPage propertySheetPage = currentPropertySheetPage;
		if (propertySheetPage == null) {
			return;
			// if currentPropertySheetPage is null it means that the properties
			// page hasn't shown yet. Since we only want to show it if we have
			// markers for it we create a new BPELTabbedPropertySheetPage. This
			// new one should only be used to select which tab and section to show.
			// TODO: this doesn't work
			//propertySheetPage = createBPELTabbedPropertySheetPage();
		}
		
		TabbedPropertyViewer viewer = propertySheetPage.getTabbedPropertyViewer();
		
		int j = 0;
		while (true) { // while we don't get an exception...
			TabDescriptor descriptor = null;
			try {
				descriptor = (TabDescriptor)viewer.getElementAt(j++);
			} catch (IndexOutOfBoundsException iobe) {
				break;
			}
			
			if (descriptor == null) {
				break; // no more descriptors
			}
			
			Tab tab = descriptor.createTab();
			ISection[] sections = tab.getSections();
			for (int i = 0; i < sections.length; i++) {
			
				if (BPELPropertySection.class.isInstance( sections[i]) == false) {
					continue;
				}
				
				BPELPropertySection section = (BPELPropertySection)sections[i];

				// HACK: we have to fake the initialization of this section in order to
				// make isValidMarker work. This section should not be used as a normal section.
				section.createControls(new Composite(getSite().getShell(), 0), propertySheetPage);
				section.setInput(this, new StructuredSelection(modelObject));

				if (section.isValidMarker (marker) ) {
					
					// the first section that handles this kind of marker wins
					showPropertiesView();
					// get real viewer, Tab and ISection objects since we are probably using fake ones
					viewer = currentPropertySheetPage.getTabbedPropertyViewer();
					viewer.setSelection(new StructuredSelection(descriptor));
					tab = currentPropertySheetPage.getCurrentTab();
					section = (BPELPropertySection)tab.getSectionAtIndex(i);
					section.gotoMarker(marker);
					return; // ignore other sections and tabs
					
				}
			}					
		}
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
	
	protected void initializeFileChangeListener() {
		fileChangeListener = new IFileChangeListener() {
			public void deleted(IFile file) {
				IFile current = ((IFileEditorInput)getEditorInput()).getFile();
				if (current.equals(file)) {
					// Close the editor.
					Display display = getSite().getShell().getDisplay();
					display.asyncExec(new Runnable() {
						public void run() {
							getSite().getPage().closeEditor(BPELMultipageEditorPart.this, false);
						}
					});
				}
			}
			public void moved(IFile source, final IFile destination) {
//				IFile current = ((IFileEditorInput) getEditorInput()).getFile();
//				if (!current.equals(source)) {
//					return;
//				}
//				// update editors input
//				final IFileEditorInput input = new FileEditorInput(destination);
//				Display display = getDetailsEditor().getSite().getShell().getDisplay();
//				display.syncExec(new Runnable() {
//					public void run() {
//						getBPELDetailsEditor().setInput(input);
//						setInput(input);
//					}
//				});
//				// update resources
//				IPath path = destination.getFullPath();
//				URI uri = URI.createPlatformResourceURI(path.toString());
//				processResource.setURI(uri);
//				// JM: Comment out. We don't want to re-name the process just because
//				// the file name has changed
////				display.syncExec(new Runnable() {
////					public void run() {
////						BPELUtil.updateNameAndNamespace(destination, process);
////					}
////				});
//				path = path.removeFileExtension().addFileExtension(IBPELUIConstants.EXTENSION_MODEL_EXTENSIONS);
//				URI extensionsUri = URI.createPlatformResourceURI(path.toString());
//				extensionsResource = resourceSet.createResource(extensionsUri);
//				extensionsResource.setURI(extensionsUri);
//				try {
//					// JM: Comment out for now. We should re-test this
////					processResource.save(Collections.EMPTY_MAP);
////					destination.refreshLocal(IResource.DEPTH_ZERO, null);
//				} catch (Exception e) {
//					BPELUIPlugin.log(e);
//				}
			}
		};
		BPELUIPlugin.getPlugin().getResourceChangeListener().addListener(fileChangeListener);
	}

	
	/**
	 * Installs the refactoring listener
	 */
	protected void initializeRefactoringListener() {
		final IWorkspace workspace = ResourcesPlugin.getWorkspace();
		postBuildRefactoringListener = new IResourceChangeListener() {
			public void resourceChanged(IResourceChangeEvent event) {
				IFile newFile = ((FileEditorInput)getEditorInput()).getFile();
				final IResourceDelta bpelFileDelta = event.getDelta().findMember(newFile.getFullPath());
				// we only care about the change if it is a move or a rename
				if (bpelFileDelta != null && (bpelFileDelta.getFlags() & IResourceDelta.MOVED_FROM) != 0) {
					getSite().getShell().getDisplay().syncExec(new Runnable() {
						public void run() {
							doRevertToSaved(null);
						}
					});
				}
			}
		};
		workspace.addResourceChangeListener(postBuildRefactoringListener, IResourceChangeEvent.POST_BUILD);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.ISaveablePart#isSaveAsAllowed()
	 */
	public boolean isSaveAsAllowed() {
		return false;
	}

	private void loadModel() {
		//FIXME WSDLEditor has gef command stack; in order to have gef command stack we need to add design page first 
		BPELEditModelClient editModelClient = new BPELEditModelClient(this, ((IFileEditorInput) getEditorInput()).getFile(), this, null);
		fDesignViewer.setEditModelClient(editModelClient);
		getEditDomain().setCommandStack(editModelClient.getCommandStack());

		Resource bpelResource = editModelClient.getPrimaryResourceInfo().getResource();
		IFile file = getFileInput();
		BPELReader reader = new BPELReader();
	    reader.read(bpelResource, file, fDesignViewer.getResourceSet());
	    process = reader.getProcess();
	    
	    if (getEditDomain() != null) {
	    	((BPELEditDomain)getEditDomain()).setProcess(getProcess());
	    }
	    extensionsResource = reader.getExtensionsResource();
	    extensionMap = reader.getExtensionMap();

	    modelListenerAdapter = new ModelListenerAdapter();
	    modelListenerAdapter.setExtensionMap(extensionMap);
		
	}

	public void modelDeleted(ResourceInfo resourceInfo) {
		if (!isDirty()) {
			getSite().getPage().closeEditor(this, false);
		}
	}
	
	public void modelDirtyStateChanged(ResourceInfo resourceInfo) {
		firePropertyChange(PROP_DIRTY);
	}
	
	public void modelLocationChanged(ResourceInfo resourceInfo, IFile movedToFile) {
		// TODO!
		//updateInputFile(movedToFile.getFullPath());
	}

	public void modelReloaded(ResourceInfo resourceInfo) {
		Resource bpelResource = fDesignViewer.getEditModelClient().getPrimaryResourceInfo().getResource();

		IFile file = getFileInput();
		BPELReader reader = new BPELReader();
	    reader.read(bpelResource, file, fDesignViewer.getResourceSet());
		 
	    process = reader.getProcess();
	    if (getEditDomain() != null) {
	    	((BPELEditDomain)getEditDomain()).setProcess(getProcess());
	    }
	    extensionMap = reader.getExtensionMap();
	    
		modelListenerAdapter.setExtensionMap(fDesignViewer.getExtensionMap());
	
		fDesignViewer.getGraphicalViewer().setContents(getProcess());

		// The ProcessTrayEditPart tries to remove its selection listener on deactivate.
		// In this case, it will fail because the edit part can't find the editor because
		// the process no longer belongs to a resource. Help it out and remove the
		// listener manually.
		ProcessTrayEditPart processTrayEditPart = (ProcessTrayEditPart)fDesignViewer.getTrayViewer().getContents();
		fDesignViewer.getGraphicalViewer().removeSelectionChangedListener(processTrayEditPart.getSelectionChangedListener());
		
		fDesignViewer.getTrayViewer().setContents(getProcess());
	}
	

	/**
	 * Sets the EditDomain for this EditorPart.
	 * @param ed the domain
	 */
	protected void setEditDomain(DefaultEditDomain ed) {
		this.editDomain = ed;
	}

	protected void showPropertiesView() {
		IWorkbench workbench = PlatformUI.getWorkbench();
		IWorkbenchPage page = workbench.getActiveWorkbenchWindow().getActivePage();
		try {
			page.showView(IBPELUIConstants.PROPERTY_VIEW_ID);
		} catch (PartInitException e) {
			BPELUIPlugin.log(e);
		}
	}
	
	/**
	 * The editor part name should be the same as the one appearing in the logical view.
	 */
	protected void updateTitle() {
		setPartName(getProcess().getName());
	}

}
