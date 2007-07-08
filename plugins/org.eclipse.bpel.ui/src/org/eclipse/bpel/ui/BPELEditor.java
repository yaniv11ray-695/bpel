/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.ui;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EventObject;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.bpel.common.extension.model.ExtensionMap;
import org.eclipse.bpel.common.ui.CommonUIPlugin;
import org.eclipse.bpel.common.ui.ICommonUIConstants;
import org.eclipse.bpel.common.ui.command.EditModelCommandFramework;
import org.eclipse.bpel.common.ui.command.ICommandFramework;
import org.eclipse.bpel.common.ui.editmodel.IEditModelListener;
import org.eclipse.bpel.common.ui.editmodel.ResourceInfo;
import org.eclipse.bpel.common.ui.palette.PaletteCategory;
import org.eclipse.bpel.common.ui.tray.AdaptingSelectionProvider;
import org.eclipse.bpel.common.ui.tray.GraphicalEditorWithPaletteAndTray;
import org.eclipse.bpel.common.ui.tray.MultiViewerSelectionProvider;
import org.eclipse.bpel.common.ui.tray.TrayComposite;
import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.CorrelationSet;
import org.eclipse.bpel.model.Flow;
import org.eclipse.bpel.model.PartnerLink;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.Variable;
import org.eclipse.bpel.model.terms.BPELTerms;
import org.eclipse.bpel.ui.actions.AppendNewAction;
import org.eclipse.bpel.ui.actions.AutoArrangeFlowsAction;
import org.eclipse.bpel.ui.actions.BPELAddChildInTrayAction;
import org.eclipse.bpel.ui.actions.BPELCopyAction;
import org.eclipse.bpel.ui.actions.BPELCutAction;
import org.eclipse.bpel.ui.actions.BPELDeleteAction;
import org.eclipse.bpel.ui.actions.BPELPasteAction;
import org.eclipse.bpel.ui.actions.BPELPrintAction;
import org.eclipse.bpel.ui.actions.ChangeTypeAction;
import org.eclipse.bpel.ui.actions.InsertNewAction;
import org.eclipse.bpel.ui.actions.MakePartner2WayAction;
import org.eclipse.bpel.ui.actions.RenameAction;
import org.eclipse.bpel.ui.actions.RevertAction;
import org.eclipse.bpel.ui.actions.ShowPropertiesViewAction;
import org.eclipse.bpel.ui.actions.ToggleAutoFlowLayout;
import org.eclipse.bpel.ui.actions.ToggleShowCompensationHandler;
import org.eclipse.bpel.ui.actions.ToggleShowEventHandler;
import org.eclipse.bpel.ui.actions.ToggleShowFaultHandler;
import org.eclipse.bpel.ui.actions.ToggleShowTerminationHandler;
import org.eclipse.bpel.ui.commands.util.ModelAutoUndoRecorder;
import org.eclipse.bpel.ui.editparts.CorrelationSetsEditPart;
import org.eclipse.bpel.ui.editparts.FlowEditPart;
import org.eclipse.bpel.ui.editparts.PartnerLinksEditPart;
import org.eclipse.bpel.ui.editparts.ProcessTrayEditPart;
import org.eclipse.bpel.ui.editparts.VariablesEditPart;
import org.eclipse.bpel.ui.editparts.util.BPELEditPartFactory;
import org.eclipse.bpel.ui.editparts.util.BPELTrayEditPartFactory;
import org.eclipse.bpel.ui.editparts.util.OutlineTreePartFactory;
import org.eclipse.bpel.ui.factories.AbstractUIObjectFactory;
import org.eclipse.bpel.ui.factories.UIObjectFactoryProvider;
import org.eclipse.bpel.ui.properties.BPELPropertySection;
import org.eclipse.bpel.ui.uiextensionmodel.ProcessExtension;
import org.eclipse.bpel.ui.util.BPELCreationToolEntry;
import org.eclipse.bpel.ui.util.BPELEditModelClient;
import org.eclipse.bpel.ui.util.BPELReader;
import org.eclipse.bpel.ui.util.BPELUtil;
import org.eclipse.bpel.ui.util.IModelVisitor;
import org.eclipse.bpel.ui.util.ModelHelper;
import org.eclipse.bpel.ui.util.TransferBuffer;
import org.eclipse.bpel.ui.util.WSDLImportHelper;
import org.eclipse.bpel.ui.util.ZoominToolEntry;
import org.eclipse.bpel.ui.util.ZoomoutToolEntry;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.draw2d.LightweightSystem;
import org.eclipse.draw2d.Viewport;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.parts.ScrollableThumbnail;
import org.eclipse.draw2d.parts.Thumbnail;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.gef.ContextMenuProvider;
import org.eclipse.gef.DefaultEditDomain;
import org.eclipse.gef.EditPart;
import org.eclipse.gef.EditPartViewer;
import org.eclipse.gef.GraphicalViewer;
import org.eclipse.gef.KeyHandler;
import org.eclipse.gef.KeyStroke;
import org.eclipse.gef.LayerConstants;
import org.eclipse.gef.RootEditPart;
import org.eclipse.gef.commands.CommandStack;
import org.eclipse.gef.palette.MarqueeToolEntry;
import org.eclipse.gef.palette.PaletteContainer;
import org.eclipse.gef.palette.PaletteGroup;
import org.eclipse.gef.palette.PaletteRoot;
import org.eclipse.gef.palette.SelectionToolEntry;
import org.eclipse.gef.ui.actions.ActionRegistry;
import org.eclipse.gef.ui.actions.SelectionAction;
import org.eclipse.gef.ui.actions.ZoomInAction;
import org.eclipse.gef.ui.actions.ZoomOutAction;
import org.eclipse.gef.ui.parts.TreeViewer;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DropTarget;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Canvas;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.IFileEditorInput;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory;
import org.eclipse.ui.dialogs.SaveAsDialog;
import org.eclipse.ui.internal.views.properties.tabbed.view.Tab;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabDescriptor;
import org.eclipse.ui.internal.views.properties.tabbed.view.TabbedPropertyViewer;
import org.eclipse.ui.part.FileEditorInput;
import org.eclipse.ui.part.IPageSite;
import org.eclipse.ui.part.MultiPageEditorSite;
import org.eclipse.ui.part.PageBook;
import org.eclipse.ui.views.properties.IPropertySheetPage;
import org.eclipse.ui.views.properties.tabbed.ISection;
import org.eclipse.ui.views.properties.tabbed.ITabbedPropertySheetPageContributor;
import org.eclipse.wst.wsdl.Definition;
import org.eclipse.wst.wsdl.internal.impl.DefinitionImpl;
import org.eclipse.wst.wsdl.util.WSDLResourceImpl;


/**
 * BPELEditor is the Eclipse editor part for editing BPEL files.
 */
public class BPELEditor extends GraphicalEditorWithPaletteAndTray /*, IGotoMarker */{

	//FIXME can't move it to MultiPageEditor because of chicken and egg problem
	// TODO: comment
	protected BPELEditModelClient editModelClient;

	protected BPELEditorAdapter editorAdapter;
	
	// transfer buffer (clipboard) for graph copies
	private TransferBuffer transferBuffer;

	// This records all model changes for automatic undo/redo purposes.
	private ModelAutoUndoRecorder modelAutoUndoRecorder;

	// Cached key handler used by both the graphical editor and outline view
	private KeyHandler keyHandler;	
	
	// Selection listener on the graphical viewer and tray
	ISelectionChangedListener selectionChangeListener, traySelectionChangeListener;
	
	// This is used by BPELDetailsEditor to implement createDetailsInputCommand() which is used
	// to support Undo/Redo functionality in the details area
	protected EditPart lastSelectedEditPart;
	
	// The selection provider that maps EditParts to their corresponding model objects
	private BPELAdaptingSelectionProvider adaptingSelectionProvider;
	private WeakMultiViewerSelectionProvider weakMultiViewerSelectionProvider;
	// This is necessary for some EditPart-based actions to work correctly.
	private ISelectionProvider filteredEditPartSelectionProvider;
	
	// Drag and drop support
	private DropTarget dropTarget;
	// JM
	private BPELDropTargetListener dropTargetListener;

	// Palette entries that will appear and disappear depending on editor mode
	BPELCreationToolEntry switchEntry, flowEntry, sequenceEntry;
	PaletteCategory controlCategory;
	
	// Lists of actions available for context menus, etc.
	protected Set appendNewActions;
	protected Set insertNewActions;
	protected Set changeTypeActions;
	
	protected String contributorID;
	
	protected ICommandFramework commandFramework;
	
	public BPELEditor(DefaultEditDomain ed) {
		super();
		setEditDomain(ed);

		transferBuffer = new TransferBuffer();

		modelAutoUndoRecorder = new ModelAutoUndoRecorder();
	}
	
	protected class BPELEditorAdapter implements Adapter {
		public Notifier getTarget() { return null; }
		public void setTarget(Notifier newTarget) { }
		public boolean isAdapterForType(Object type) { return (type == BPELEditorAdapter.class); }
		public void notifyChanged(Notification notification) { }
		public BPELEditor getBPELEditor() { return BPELEditor.this; }
	}

	/**
	 * Given a ResourceSet, this helper identifies the BPELEditor (if any) that created it
	 */
	public static BPELEditor getBPELEditor(ResourceSet resourceSet) {
	    Iterator it = resourceSet.eAdapters().iterator();
	    while (it.hasNext()) {
	        Object next = it.next();
	        if (next instanceof BPELEditorAdapter) {
	            return ((BPELEditorAdapter)next).getBPELEditor();
	        }
	    }
	    return null;
	}

	public BPELEditModelClient getEditModelClient() {	
		return editModelClient;
	}
	
	public void setEditModelClient(BPELEditModelClient cl) {	
		this.editModelClient = cl;
	}

	public ResourceSet getResourceSet() {
		return getEditModelClient().getEditModel().getResourceSet();
	}

	public Resource getResource() { return getProcess().eResource(); }
	public ModelAutoUndoRecorder getModelAutoUndoRecorder() { return modelAutoUndoRecorder; }
	
	public TransferBuffer getTransferBuffer() { return transferBuffer; }

	public Set getAppendNewActions() { return appendNewActions; }
	public Set getInsertNewActions() { return insertNewActions; }
	public Set getChangeTypeActions() { return changeTypeActions; }
	
	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#configureGraphicalViewer()
	 */
	protected void configureGraphicalViewer() {
		super.configureGraphicalViewer();
		
		GraphicalViewer viewer = getGraphicalViewer();
		registerViewer(viewer);
		GraphicalBPELRootEditPart root = (GraphicalBPELRootEditPart)viewer.getRootEditPart();
		ZoomInAction zoomInAction = new ZoomInAction(root.getZoomManager());
		zoomInAction.setImageDescriptor(CommonUIPlugin.getDefault().getImageRegistry().getDescriptor(ICommonUIConstants.ICON_ZOOM_IN_TOOL));
		getActionRegistry().registerAction(zoomInAction);
		ZoomOutAction zoomOutAction = new ZoomOutAction(root.getZoomManager());
		zoomOutAction.setImageDescriptor(CommonUIPlugin.getDefault().getImageRegistry().getDescriptor(ICommonUIConstants.ICON_ZOOM_OUT_TOOL));
		getActionRegistry().registerAction(zoomOutAction);
		
		ContextMenuProvider provider = new ProcessContextMenuProvider(getGraphicalViewer(), getActionRegistry());
		getGraphicalViewer().setContextMenu(provider);
		getSite().setSelectionProvider(getAdaptingSelectionProvider());
		getSite().registerContextMenu("org.eclipse.bpel.editor.contextmenu", //$NON-NLS-1$
			provider, getSite().getSelectionProvider());
		getGraphicalViewer().setKeyHandler(new BPELGraphicalKeyHandler(getGraphicalViewer()).setParent(getEditorKeyHandler()));
		
		// Setup the drop target and add our listener to it.
		// This will allow us to accept drops from the navigator.
		
		this.dropTarget = new DropTarget(getGraphicalViewer().getControl(), DND.DROP_NONE | DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK);
		// Set transfers to the types that are specified by the helper
		// JM
		dropTarget.setTransfer(BPELDropTargetListener.getTransferTypes());
		// Create our drop listener and add it to the DropTarget
		// JM
		this.dropTargetListener = new BPELDropTargetListener(getGraphicalViewer(), this);
		dropTarget.addDropListener(dropTargetListener);
	}

	private void createBPELPaletteEntries(PaletteContainer palette) {
		BPELPackage bpelPackage = BPELPackage.eINSTANCE;
		UIObjectFactoryProvider provider = UIObjectFactoryProvider.getInstance();
		
		PaletteCategory basicActionsCategory = new PaletteCategory("Actions"); //$NON-NLS-1$
		String term = BPELTerms.getString("Empty"); //$NON-NLS-1$
		basicActionsCategory.add(new BPELCreationToolEntry(term, term,
		    provider.getFactoryFor(bpelPackage.getEmpty())));
		basicActionsCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Invoke_35, 
			Messages.BPELEditor_Invoke_36, 
			provider.getFactoryFor(bpelPackage.getInvoke())));
		basicActionsCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Receive_4, 
			Messages.BPELEditor_Receive_5, 
			provider.getFactoryFor(bpelPackage.getReceive())));
		basicActionsCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Reply_6, 
			Messages.BPELEditor_Reply_7, 
			provider.getFactoryFor(bpelPackage.getReply())));
		basicActionsCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Assign_10, 
			Messages.BPELEditor_Assign_11, 
			provider.getFactoryFor(bpelPackage.getAssign())));
		basicActionsCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Validate_XML_12, 
			Messages.BPELEditor_Validate_XML_13, 
			provider.getFactoryFor(bpelPackage.getValidate())));
		palette.add(basicActionsCategory);

		controlCategory = new PaletteCategory("Control"); //$NON-NLS-1$
		switchEntry = new BPELCreationToolEntry(
			Messages.BPELEditor_If_17, 
			Messages.BPELEditor_If_18, 
			provider.getFactoryFor(bpelPackage.getIf()));
		controlCategory.add(switchEntry);
		controlCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Pick_8, 
			Messages.BPELEditor_Pick_9, 
			provider.getFactoryFor(bpelPackage.getPick())));
		controlCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_While_19, 
			Messages.BPELEditor_While_20, 
			provider.getFactoryFor(bpelPackage.getWhile())));
		controlCategory.add(new BPELCreationToolEntry(
				Messages.BPELEditor_ForEach_19, 
				Messages.BPELEditor_ForEach_20, 
				provider.getFactoryFor(bpelPackage.getForEach())));
		controlCategory.add(new BPELCreationToolEntry(
				Messages.BPELEditor_Repeat_Until_42, 
				Messages.BPELEditor_Repeat_Until_43, 
				provider.getFactoryFor(bpelPackage.getRepeatUntil())));
		controlCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Wait_21, 
			Messages.BPELEditor_Wait_22, 
			provider.getFactoryFor(bpelPackage.getWait())));
		sequenceEntry = new BPELCreationToolEntry(
			Messages.BPELEditor_Sequence_23, 
			Messages.BPELEditor_Sequence_24, 
			provider.getFactoryFor(bpelPackage.getSequence()));
		controlCategory.add(sequenceEntry);
		controlCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Scope_1, 
			Messages.BPELEditor_Scope_2, 
			provider.getFactoryFor(bpelPackage.getScope())));
		flowEntry = new BPELCreationToolEntry(
			Messages.BPELEditor_Flow_1, 
			Messages.BPELEditor_Flow_2, 
			provider.getFactoryFor(bpelPackage.getFlow()));
		controlCategory.add(flowEntry);
		palette.add(controlCategory);

		PaletteCategory faultCategory = new PaletteCategory("Faults"); //$NON-NLS-1$
		faultCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Exit_26, 
			Messages.BPELEditor_Exit_27, 
			provider.getFactoryFor(bpelPackage.getExit())));
		faultCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Throw_28, 
			Messages.BPELEditor_Throw_29, 
			provider.getFactoryFor(bpelPackage.getThrow())));
		faultCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_ReThrow_1, 
			Messages.BPELEditor_ReThrow_2, 
			provider.getFactoryFor(bpelPackage.getRethrow())));
		faultCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_Compensate_1, 
			Messages.BPELEditor_Compensate_2, 
			provider.getFactoryFor(bpelPackage.getCompensate())));
		faultCategory.add(new BPELCreationToolEntry(
			Messages.BPELEditor_CompensateScope_1, 
			Messages.BPELEditor_CompensateScope_2, 
			provider.getFactoryFor(bpelPackage.getCompensateScope())));
		
		palette.add(faultCategory);
					
	}

	private void createTopControlPaletteEntries(PaletteRoot root) {
		PaletteGroup controlGroup = new PaletteGroup(Messages.BPELEditor_Top_Control_Group_37); 

		SelectionToolEntry selectionTool = new SelectionToolEntry(Messages.BPELEditor_Selection_Tool_38); 
		controlGroup.add(selectionTool);

		MarqueeToolEntry marqueeTool = new MarqueeToolEntry(Messages.BPELEditor_Marquee_Tool); 
		controlGroup.add(marqueeTool);

		root.add(controlGroup);
		root.setDefaultEntry(selectionTool);
	}

	/**
	 * @see org.eclipse.ui.IWorkbenchPart#dispose()
	 */
	public void dispose() {
		
		if (editModelClient != null) {
			editModelClient.getEditModel().getResourceSet().eAdapters().remove(getEditorAdapter());
			editModelClient.dispose();
			editModelClient = null;
		}

		if (getGraphicalViewer() != null) {
			if (selectionChangeListener != null) {
				getGraphicalViewer().removeSelectionChangedListener(selectionChangeListener);
			}
			
			getGraphicalViewer().setContents(null);		

			if (getGraphicalViewer().getControl() != null && !getGraphicalViewer().getControl().isDisposed()) {
				getGraphicalViewer().getControl().dispose();
				getGraphicalViewer().setControl(null);
			}
		}
		if (getTrayViewer() != null) {
			getTrayViewer().setContents(null);
		}
		if (getEditDomain() != null) {
			getEditDomain().setPaletteViewer(null);
		}
		
		if (getPaletteViewer() != null) {
			getPaletteViewer().setContents(null);
		}
		
		lastSelectedEditPart = null;

		super.dispose();
		
		if (dropTarget != null) {
			dropTarget.dispose();
			dropTarget = null;
		}

//		ResourceSet resourceSet = getResourceSet();
		
		if (adaptingSelectionProvider != null) {
			getSite().setSelectionProvider(null);
			adaptingSelectionProvider = null;
		}
		
		// TODO unhook BPELEditorAdapter from the resource set to avoid unnecessary cyclic garbage.
		//BPELEditorAdapter bpelEditorAdapter = (BPELEditorAdapter)BPELUtil.adapt(
		//	resourceSet, BPELEditorAdapter.class);
		//if (bpelEditorAdapter != null) resourceSet.eAdapters().remove(bpelEditorAdapter);

		// Hacks to work around memory leaks: break references to large structures.
		// Various objects are holding onto a BPELEditor or ResourceSet and that's hard to fix.
		transferBuffer.setContents(null);

		if (getEditDomain() != null) {
			((BPELEditDomain)getEditDomain()).setProcess(null);
		}
//		processResource = null;
//		if (resourceSet != null) resourceSet.getResources().clear();
//		resourceSet = null;
//		extensionsResource = null;
//		if (extensionMap != null) extensionMap.clear();
//		extensionMap = null;
		modelAutoUndoRecorder = null;

		if (getSelectionActions() != null) {
			getSelectionActions().clear();
		}
		if (getPropertyActions() != null) {
			getPropertyActions().clear();
		}
		if (getActionRegistry() != null) {
			getActionRegistry().dispose();
			setActionRegistry(null);
		}
	
		// JM: Replace this with lifecycle extensions
		//cleanupGeneratedFiles();
	}

	// JM: Replace this with lifecycle extensions
//	/**
//	 * If we don't have references to the generated files anymore we should delete them.
//	 */
//	protected void cleanupGeneratedFiles() {
//		IFile bpelFile = getFileInput();
//		if (!bpelFile.exists()) return; // if the bpel file is gone there's nothing to be done
//		
//		// force and wait the index handler to run so we don't delete the wrong files
//		IIndexManager.INSTANCE.addFileToIndex(bpelFile, null);
//		List candidates = new ArrayList(Arrays.asList(StampHelper.findGeneratedFiles(bpelFile)));
//	 	IndexSearcher indexSearcher = new IndexSearcher();
//		try {
//			FileRefInfo[] matches = indexSearcher.findFileReferences(bpelFile, IIndexSearch.ANY_FILE, null, new NullProgressMonitor());
//			if (matches.length > 0) {
//				FileInfo[] infos = matches[0].getReferencedFiles();
//				for (int i = 0; i < infos.length; i++) {
//					FileInfo info = infos[i];
//					candidates.remove(info.getFile());
//				}
//			}
//		} catch (InterruptedException e) {
//			BPELUIPlugin.log(e);
//		}
//		
//		if (!candidates.isEmpty()) {
//			try {
//				ResourcesPlugin.getWorkspace().delete((IFile[]) candidates.toArray(new IFile[candidates.size()]), true, null);
//			} catch (CoreException e) {
//				BPELUIPlugin.log(e);
//			}
//		}
//	}


	/**
	 * @see org.eclipse.ui.IEditorPart#doSave(org.eclipse.core.runtime.IProgressMonitor)
	 */
	public void doSave(IProgressMonitor progressMonitor) {
		getCommandFramework().applyCurrentChange();

		removeUnusedExtensions();
		// Add all imports and namespaces to the artifacts file before saving.
		if (getEditModelClient().getArtifactsResourceInfo() != null) {
			Resource artifactsResource = getEditModelClient().getArtifactsResourceInfo().getResource();
			
			if (artifactsResource instanceof WSDLResourceImpl) {
				// Add any missing imports/namespaces before saving!
				if (!artifactsResource.getContents().isEmpty()) {
					Definition def = (Definition)artifactsResource.getContents().get(0);
					// --- WDG: temp hack!
					String TNS = def.getTargetNamespace();
					if (TNS == null) {
						TNS = def.getNamespace("tns"); //$NON-NLS-1$
						if (TNS == null)  TNS = "UGGH";
						def.setTargetNamespace(TNS);
					} else {
						def.addNamespace("tns", TNS); //$NON-NLS-1$
					}
					// ---
					
					def.getEImports().clear();
					
					WSDLImportHelper.addAllImportsAndNamespaces(def,
						getEditModelClient().getPrimaryResourceInfo().getFile());
					
					// This doesn't seem to introduce an updateElement automatically,
					// so do it manually now, so that RolePortTypes (for example) who
					// are affected by the new namespace will know about it.
					((DefinitionImpl)def).updateElement();
				}
			}
		}
		try {
			getExtensionsResource().setModified(false);			
			getEditModelClient().saveAll(progressMonitor);					
			
			//getModelDirtyTracker().markSaveLocation();

		} finally {						
			updateTitle();
		}
		// Put the timestamp of the bpel file into the bpelex file.
		IFile bpelFile = BPELUtil.getBPELFile(getProcess());
		long modificationStamp = bpelFile.getLocalTimeStamp();
		ProcessExtension processExtension = (ProcessExtension)ModelHelper.getExtension(getProcess());
   		processExtension.setModificationStamp(modificationStamp);
   		getExtensionsResource().setModified(true);
   		getEditModelClient().saveAll(progressMonitor); 
	}

	// Disable our Save As functionality.
	public boolean isSaveAsAllowed() {
		return false;
	}
	
	/**
	 * @see org.eclipse.ui.IEditorPart#doSaveAs()
	 */
	public void doSaveAs() {
		getCommandFramework().applyCurrentChange();
		performSaveAs();
		updateTitle();
	}

	protected boolean performSaveAs() {
		SaveAsDialog saveAsDialog = new SaveAsDialog(getSite().getShell());
		saveAsDialog.setOriginalFile(getFileInput());
		saveAsDialog.open();
		
		IPath path = saveAsDialog.getResult();
		if (path != null) {
			return getEditModelClient().savePrimaryResourceAs(
				ResourcesPlugin.getWorkspace().getRoot().getFile(path), 
				getEditorSite().getActionBars().getStatusLineManager().getProgressMonitor());	
		}
		
		return false;
	}

	
	protected PaletteRoot createPaletteRoot() {
		PaletteRoot paletteRoot = new PaletteRoot();
		createTopControlPaletteEntries(paletteRoot);
		createBPELPaletteEntries(paletteRoot);
		createBottomControlPaletteEntries(paletteRoot);
		return paletteRoot;
	}
	
	/**
	 * @see org.eclipse.ui.IEditorPart#isDirty()
	 */
	/*public boolean isDirty() {
		return getCommandStack().isDirty();
	}*/

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#initializeGraphicalViewer()
	 */
	protected void initializeGraphicalViewer() {
		///FIXME moved to multi
		//initializeFileChangeListener();
		//initializeRefactoringListeners();
		
		getGraphicalViewer().setEditPartFactory(new BPELEditPartFactory());
		
		BPELUIPlugin.getPlugin().getPreferenceStore().setValue(IBPELUIConstants.PREF_SHOW_FREEFORM_FLOW, true);
		
		getGraphicalViewer().setContents(getProcess());

		this.selectionChangeListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection selection = (StructuredSelection)event.getSelection();
				if (selection.isEmpty()) {
					return;
				}
				// if this is a multi-selection we should not present anything on the details area
				if (selection.size() > 1) {
					// TODO: This doesn't work
//					getBPELDetailsEditor().getDetailsArea().setInput(null);
				} else {
					final Object o = selection.getFirstElement();
					if (o instanceof EditPart) {
						// CAREFUL: must setInput() on the DetailsArea *before* we remember
						// the lastSelectedEditPart (because setInput() might execute a pending
						// command for an IOngoingChange whose wrapper will not execute correctly
						// unless lastSelectedEditPart has its old value).
						lastSelectedEditPart = (EditPart)o;
					}
				}
			}
		};
		this.traySelectionChangeListener = new ISelectionChangedListener() {
			public void selectionChanged(SelectionChangedEvent event) {
				StructuredSelection selection = (StructuredSelection)event.getSelection();
				if (selection.isEmpty()) {
					return;
				}
				// if this is a multi-selection we should not present anything on the details area
				if (selection.size() > 1) {
					// TODO: This doesn't work.
//					getBPELDetailsEditor().getDetailsArea().setInput(null);
				} else {
					final Object o = selection.getFirstElement();
					if (o instanceof EditPart) {
						// CAREFUL: must setInput() on the DetailsArea *before* we remember
						// the lastSelectedEditPart (because setInput() might execute a pending
						// command for an IOngoingChange whose wrapper will not execute correctly
						// unless lastSelectedEditPart has its old value).
						lastSelectedEditPart = (EditPart)o;
					}
				}
			}
		};
		getGraphicalViewer().addSelectionChangedListener(this.selectionChangeListener);
		arrangeEditParts(getGraphicalViewer());
	}
	public void arrangeEditParts(GraphicalViewer graphicalViewer) {
		// Make a list of all FlowEditParts whose children all have no positional metadata
		List flowsToArrange = new ArrayList();

		for (TreeIterator it = getProcess().eAllContents(); it.hasNext(); ) {
			Object model = it.next();
			if (model instanceof Flow) {
				boolean missingLoc = false;
				for (Iterator it2 = ((Flow)model).getActivities().iterator();
					!missingLoc && it2.hasNext(); )
				{
					Activity child = (Activity)it2.next();
					if (child == null) continue;
					Point loc = ModelHelper.getLocation(child);
					if (loc.x == Integer.MIN_VALUE)  missingLoc = true;
				}
				if (missingLoc) {
					EditPart editPart = (EditPart)graphicalViewer.getEditPartRegistry().get(model); 
					if (editPart instanceof FlowEditPart)  flowsToArrange.add(editPart);		
				}
			}
		}
		
		BPELUtil.sortFlowList(flowsToArrange);

		for (Iterator it = flowsToArrange.iterator(); it.hasNext(); ) {
			((FlowEditPart)it.next()).doImmediateAutoLayout();
		}
	}
	

	public void commandStackChanged(EventObject event) {
		firePropertyChange(IEditorPart.PROP_DIRTY);
		super.commandStackChanged(event);
	}
	
	/**
	 * Returns the KeyHandler with common bindings for both the Outline and Graphical Views.
	 */
	protected KeyHandler getKeyHandler(){
		if (keyHandler == null) {
			keyHandler = new KeyHandler();
			keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
		}
		return keyHandler;
	}
	
	/**
	 * Returns the KeyHandler with common bindings for both the Outline and Graphical Views.
	 */
	protected KeyHandler getEditorKeyHandler(){
		if (keyHandler == null) {
			keyHandler = new KeyHandler();
			keyHandler.put(KeyStroke.getPressed(SWT.DEL, 127, 0), getActionRegistry().getAction(ActionFactory.DELETE.getId()));
			keyHandler.put(KeyStroke.getPressed(SWT.PAGE_UP, 0), new Action() {
				public void run() {
					((ScrollingBPELGraphicalViewer)getGraphicalViewer()).scrollVertical(true);
				}
			});
			keyHandler.put(KeyStroke.getPressed(SWT.PAGE_DOWN, 0), new Action() {
				public void run() {
					((ScrollingBPELGraphicalViewer)getGraphicalViewer()).scrollVertical(false);
				}
			});
			keyHandler.put(KeyStroke.getPressed(SWT.HOME, 0), new Action() {
				public void run() {
					((ScrollingBPELGraphicalViewer)getGraphicalViewer()).scrollHorizontal(true);
				}
			});
			keyHandler.put(KeyStroke.getPressed(SWT.END, 0), new Action() {
				public void run() {
					((ScrollingBPELGraphicalViewer)getGraphicalViewer()).scrollHorizontal(false);
				}
			});
		}
		return keyHandler;
	}

	/**
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(Class)
	 */
	public Object getAdapter(Class type) {
//		if (type == ZoomManager.class)
//			return ((ScalableFreeformRootEditPart) getGraphicalViewer().getRootEditPart())
//				.getZoomManager();
		return super.getAdapter(type);
	}
	
	protected void replaceSelectionAction(ActionRegistry registry, IAction action) {
		IAction oldAction = registry.getAction(action.getId());
		if (oldAction != null) {
			registry.removeAction(oldAction);
			getSelectionActions().remove(oldAction.getId());
		}
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}

	/**
	 * @see org.eclipse.gef.ui.parts.GraphicalEditor#createActions()
	 */
	protected void createActions() {
		super.createActions();	
		ActionRegistry registry = getActionRegistry();
		
		// hook up some custom actions that are specialized versions of 
		// the default actions.
		IAction action = new BPELDeleteAction(this);
		makeSelectionActionBPELOnly(action);
		replaceSelectionAction(registry, action);

		action = new BPELCutAction(this);
		makeSelectionActionBPELOnly(action);
		replaceSelectionAction(registry, action);
	
		action = new BPELCopyAction(this);
		makeSelectionActionBPELOnly(action);
		replaceSelectionAction(registry, action);
		
		action = new BPELPasteAction(this);
		makeSelectionActionBPELOnly(action);
		replaceSelectionAction(registry, action);

		action = new RevertAction(this);
		registry.registerAction(action);
		getPropertyActions().add(action.getId());

		action = new RenameAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new ToggleAutoFlowLayout(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new ToggleShowFaultHandler(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new ToggleShowCompensationHandler(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new ToggleShowTerminationHandler(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new ToggleShowEventHandler(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new AutoArrangeFlowsAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		// show properties action
		action = new ShowPropertiesViewAction();
		registry.registerAction(action);

		action = new BPELPrintAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new BPELAddChildInTrayAction(this, 
				PartnerLinksEditPart.class, 
				"Add Partner Link",  
				BPELUIPlugin.getPlugin().getImageDescriptor(IBPELUIConstants.ICON_PARTNER_IN_16));
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	
//		action = new BPELAddChildInTrayAction(this, 
//				PartnerLinksEditPart.class, 
//				Messages.BPELEditor_addInterfacePartner,  
//				BPELUIPlugin.getPlugin().getImageDescriptor(IBPELUIConstants.ICON_PARTNER_IN_16));
//		registry.registerAction(action);
//		getSelectionActions().add(action.getId());
//		
//		action = new BPELAddChildInTrayAction(this, 
//				ReferencePartnerLinksEditPart.class, 
//				Messages.BPELEditor_addReferencePartner,  
//				BPELUIPlugin.getPlugin().getImageDescriptor(IBPELUIConstants.ICON_PARTNER_OUT_16));
//		registry.registerAction(action);
//		getSelectionActions().add(action.getId());
		
		action = new BPELAddChildInTrayAction(this, 
				VariablesEditPart.class, 
				Messages.BPELEditor_addVariable,  
				BPELUIPlugin.getPlugin().getImageDescriptor(IBPELUIConstants.ICON_VARIABLE_16));
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
		
		action = new BPELAddChildInTrayAction(this, 
				CorrelationSetsEditPart.class, 
				Messages.BPELEditor_addCorrelationSet,  
				BPELUIPlugin.getPlugin().getImageDescriptor(IBPELUIConstants.ICON_CORRELATIONSET_16));
		registry.registerAction(action);
		getSelectionActions().add(action.getId());

		action = new MakePartner2WayAction(this);
		registry.registerAction(action);
		getSelectionActions().add(action.getId());
	}

	/**
	 * Create the Add, Insert and Change Type actions to be used in pop-up menus.
	 * They reflect what is available from the palette.
	 */
	protected void createPaletteDependentActions() {
		Comparator actionComparator = new Comparator() {
			Collator collator = Collator.getInstance();
			public int compare(Object o1, Object o2) {
				if (o1 instanceof IAction && o2 instanceof IAction) {
					IAction action1 = (IAction)o1;
					IAction action2 = (IAction)o2;
					return collator.compare(action1.getText(), action2.getText());
				}
				return -1;
			}
		};
		
		appendNewActions = new TreeSet(actionComparator);
		insertNewActions = new TreeSet(actionComparator);
		changeTypeActions = new TreeSet(actionComparator);
		createPaletteDependentActions(getPaletteRoot());
	}

	protected void createPaletteDependentActions(PaletteContainer container) {
		ActionRegistry registry = getActionRegistry();
		for (Iterator iter = container.getChildren().iterator(); iter.hasNext();) {
			Object element = iter.next();
			if (element instanceof PaletteContainer) {
				createPaletteDependentActions((PaletteContainer)element);
			} else if (element instanceof BPELCreationToolEntry) {
				BPELCreationToolEntry entry = (BPELCreationToolEntry) element;
				AbstractUIObjectFactory factory = entry.getUIObjectFactory();

				// append
				IAction action = new AppendNewAction(this, factory);
				makeSelectionActionBPELOnlyEP(action);
				appendNewActions.add(action);
				registry.registerAction(action);
				getSelectionActions().add(action.getId());

				// insert
				action = new InsertNewAction(this, factory);
				makeSelectionActionBPELOnlyEP(action);
				registry.registerAction(action);
				insertNewActions.add(action);
				getSelectionActions().add(action.getId());
				
				// change type
				action = new ChangeTypeAction(this, factory);
				makeSelectionActionBPELOnlyEP(action);
				registry.registerAction(action);
				changeTypeActions.add(action);
				getSelectionActions().add(action.getId());
			}
		}
	}

	// Most of our SelectionActions can't handle selections
	// in other editors properly.  TODO: Note that they'll still get *notified* and
	// calculateEnabled() will be called on them; maybe we should try to make some of
	// those less expensive (especially AppendNewAction.calculateEnabled() which does
	// lots of work and gets called for around 20 instances on each selection)?
	// TODO: TEMPORARY HACK!
	protected void makeSelectionActionBPELOnlyEP(IAction action) {
		if (!(action instanceof SelectionAction))  throw new IllegalArgumentException();
		((SelectionAction)action).setSelectionProvider(getFilteredEditPartSelectionProvider());  
	}
	
	protected void makeSelectionActionBPELOnly(IAction action) {
		if (!(action instanceof SelectionAction))  throw new IllegalArgumentException();
		((SelectionAction)action).setSelectionProvider(getAdaptingSelectionProvider());  
	}
	
	public Process getProcess() {
		IWorkbenchPartSite editorSite = getSite();
		if (editorSite instanceof MultiPageEditorSite) {
			return (Process)((org.eclipse.ui.part.MultiPageEditorSite)getSite()).getMultiPageEditor().getAdapter(Process.class);
		}
		return null;
	}

	public Resource getExtensionsResource (){
		IWorkbenchPartSite editorSite = getSite();
		if (editorSite instanceof MultiPageEditorSite) {
			return (Resource)((org.eclipse.ui.part.MultiPageEditorSite)getSite()).getMultiPageEditor().getAdapter(Resource.class);
		}
		return null;
	}

	public ExtensionMap getExtensionMap (){
		IWorkbenchPartSite editorSite = getSite();
		if (editorSite instanceof MultiPageEditorSite) {
			return (ExtensionMap)((org.eclipse.ui.part.MultiPageEditorSite)getSite()).getMultiPageEditor().getAdapter(ExtensionMap.class);
		}
		return null;
	}

	//FIXME should we kill it and move all the model listener adapter related
	//things to BPELMultipageEditorPart?
	public ModelListenerAdapter getModelListenerAdapter() { 
		IWorkbenchPartSite editorSite = getSite();
		if (editorSite instanceof MultiPageEditorSite) {
			return (ModelListenerAdapter)((org.eclipse.ui.part.MultiPageEditorSite)getSite()).getMultiPageEditor().getAdapter(ModelListenerAdapter.class);
		}
		return null;
	}
	
	/**
	 * Overriding to use BPELMultipageEditorPart command stack
	 */
	public CommandStack getCommandStack() {
		IWorkbenchPartSite editorSite = getSite();
		if (editorSite instanceof MultiPageEditorSite) {
			return (CommandStack)((org.eclipse.ui.part.MultiPageEditorSite)getSite()).getMultiPageEditor().getAdapter(CommandStack.class);
		}
		return null;
	}

	public void setAutoFlowLayout(boolean autoLayout) {
		BPELUIPlugin.getPlugin().getPreferenceStore().setValue(IBPELUIConstants.PREF_AUTO_FLOW_LAYOUT, autoLayout);
	}
	
	public boolean getAutoFlowLayout() {
		return BPELUIPlugin.getPlugin().getPreferenceStore().getBoolean(IBPELUIConstants.PREF_AUTO_FLOW_LAYOUT);
	}

	private void createBottomControlPaletteEntries(PaletteContainer palette) {
		PaletteGroup controlGroup = new PaletteGroup(Messages.BPELEditor_Bottom_Control_Group_39); 
	
		ZoominToolEntry zoomIn = new ZoominToolEntry(Messages.BPELEditor_Zoom_In_40); 
		zoomIn.setSmallIcon(CommonUIPlugin.getDefault().getImageRegistry().getDescriptor(ICommonUIConstants.ICON_ZOOM_IN_TOOL));
		controlGroup.add(zoomIn);
		ZoomoutToolEntry zoomOut = new ZoomoutToolEntry(Messages.BPELEditor_Zoom_Out_41); 
		zoomOut.setSmallIcon(CommonUIPlugin.getDefault().getImageRegistry().getDescriptor(ICommonUIConstants.ICON_ZOOM_OUT_TOOL));
		controlGroup.add(zoomOut);
	
		palette.add(controlGroup);
	}

	/**
	 * Override createGraphicalViewer. We have to have our own graphical viewer so that
	 * we can put extra layers in the root edit part
	 */
	protected void createGraphicalViewer(Composite parent) {
		GraphicalViewer viewer = new ScrollingBPELGraphicalViewer();
		viewer.createControl(parent);
		setGraphicalViewer(viewer);
		configureGraphicalViewer();
		hookGraphicalViewer();
		initializeGraphicalViewer();
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(
			parent, IHelpContextIds.EDITOR_CANVAS);	
	}
	
	protected void hookGraphicalViewer() {
		// Do nothing. Don't call super because it will override our
		// preference for selection provider and synchronizer.
	}	
	/**
	 * Override to make public.
	 */
	public void setGraphicalViewer(GraphicalViewer viewer) {
	    super.setGraphicalViewer(viewer);
	}
	
	protected void createPaletteViewer(final Composite parent) {
		super.createPaletteViewer(parent);
		PlatformUI.getWorkbench().getHelpSystem().setHelp(
			parent, IHelpContextIds.EDITOR_PALETTE);	
	}
	
	public void refreshGraphicalViewer() {
		BPELUtil.regenerateVisuals(getProcess(), getGraphicalViewer());
	}
	
	/**
	 * Helper to set canvas selection to a specific model object (e.g. for details area)
	 * @param modelObject
	 */
	public void selectModelObject(Object modelObject) {
		setFocus(); // give focus to the editor so that it can notify the properties view of input change
		adaptingSelectionProvider.setSelection(new StructuredSelection(modelObject));
	}

	/**
	 * Return the selected model object(s). The debugger will call
	 * this.
	 */
	public ISelection getSelection() {
		ISelection editPartSelection = getGraphicalViewer().getSelection();
		if (editPartSelection == null || !(editPartSelection instanceof StructuredSelection) || editPartSelection.isEmpty()) {
			return StructuredSelection.EMPTY;
		}
		List list = new ArrayList();
		Iterator it = ((StructuredSelection)editPartSelection).iterator();
		while (it.hasNext()) {
			Object o = it.next();
			if (o instanceof EditPart) {
				list.add(((EditPart)o).getModel());
			}
		}
		return new StructuredSelection(list);
	}
	
	public AdaptingSelectionProvider getAdaptingSelectionProvider() {
		if (adaptingSelectionProvider == null) {
			adaptingSelectionProvider = new BPELAdaptingSelectionProvider();
		}
		return adaptingSelectionProvider;
	}
	
	private MultiViewerSelectionProvider getMultiViewerSelectionProvider() {
		if (weakMultiViewerSelectionProvider == null) {
			weakMultiViewerSelectionProvider = new WeakMultiViewerSelectionProvider() {
				protected ISelection cachedAdaptingSelection;
				public ISelection getSelection() {
					// HACK to fix selection coherency problems:
					// If the AdaptingSelectionProvider selection has changed, assume ours
					// has changed as well!
					if (getAdaptingSelectionProvider().getSelection() != cachedAdaptingSelection) {
						cachedSelection = null;  // force super to recalculate
					}
					return super.getSelection();
				}
			};
		}
		return weakMultiViewerSelectionProvider;
	}

	private ISelectionProvider getFilteredEditPartSelectionProvider() {
		if (filteredEditPartSelectionProvider == null) {
			filteredEditPartSelectionProvider = new ISelectionProvider() {
				public void addSelectionChangedListener(ISelectionChangedListener listener) { }
				public void removeSelectionChangedListener(ISelectionChangedListener listener) { }
				public ISelection getSelection() {
					return adaptingSelectionProvider.getEditPartSelection();
				}
				public void setSelection(ISelection selection) { }
			};
		}
		return filteredEditPartSelectionProvider;
	}

	protected BPELEditorAdapter getEditorAdapter() {
		return editorAdapter;
	}

	/**
	 * Override getGraphicalViewer() to make the method public
	 */
	public GraphicalViewer getGraphicalViewer() {
		return super.getGraphicalViewer();
	}
	
	/**
	 * Cause the edit part for the given model object to refresh its hover help.
	 */
	public void refreshHoverHelp(EObject modelObject) {
		EditPart editPart = (EditPart)getGraphicalViewer().getEditPartRegistry().get(modelObject);
		if (editPart instanceof IHoverHelperSupport) {
			((IHoverHelperSupport)editPart).refreshHoverHelp();
		}
	}
	
	/**
	 * Cause all edit parts to refresh their hover help.
	 */
	public void refreshHoverHelp() {
		BPELUtil.visitModelDepthFirst(getProcess(), new IModelVisitor() {
			public boolean visit(Object modelObject) {
				EditPart editPart = (EditPart)getGraphicalViewer().getEditPartRegistry().get(modelObject);
				if (editPart == null) editPart = (EditPart)getTrayViewer().getEditPartRegistry().get(modelObject);
				if (editPart instanceof IHoverHelperSupport) {
					((IHoverHelperSupport)editPart).refreshHoverHelp();
				}
				return true;
			}
		});
	}
	
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		getTrayComposite().setState(TrayComposite.STATE_EXPANDED);
		getTrayComposite().setTrayWidth(150);

		selectModelObject(getProcess());
	}

	protected IFile getFileInput() {
		return ((IFileEditorInput) getEditorInput()).getFile();
	}

	/**
	 * This is called during startup.
	 */
	public void init(IEditorSite site, IEditorInput editorInput) throws PartInitException {
		
		if (editorInput instanceof IFileEditorInput) {
			try {
				super.init(site, editorInput);

				// remove the listener on the command stack created by the graphical editor
				getCommandStack().removeCommandStackListener(this);

				ResourceSet resourceSet = getEditModelClient().getEditModel().getResourceSet();

				// TODO: does this stuff have to go?  it won't work with shared models..
				resourceSet.eAdapters().add(getModelListenerAdapter());
				resourceSet.eAdapters().add(editorAdapter = new BPELEditorAdapter());
				getModelListenerAdapter().setLinkNotificationAdapter(new LinkNotificationAdapter(this));
				getCommandStack().addCommandStackListener(getModelListenerAdapter());
				
				commandFramework = new EditModelCommandFramework(getEditModelClient().getCommandStack());

				// add a listener to the shared command stack
				getCommandStack().addCommandStackListener(this);

				// Scan the model to see if there are any invalid elements that need to be
				// removed (there are some elements that if invalid will not appear in 
				// the UI and thus need to be removed automatically).
				// Do this after the command framework is created and the listener
				// is added to it.
				// JM
//				if (BPELUtil.removeInvalidElements(process)) {
//					MessageDialog.openInformation(getSite().getShell(),
//							Messages.getString("validationInfo.dialog.title"), //$NON-NLS-1$
//							Messages.getString("validationInfo.invalidElementsRemoved")); //$NON-NLS-1$
//					getCommandFramework().execute(new DummyCommand());
//				}
//		   		
				
				// these can only be created after we load the model
				// since it affects the available items in the palette
				createPaletteDependentActions();
				
			} catch (RuntimeException e) {
				BPELUIPlugin.log(e);
				// Some actions and listeners can might instances of the editor.
				// In this case we want to dispose of so we don't leak editor instances.
				dispose();
				throw new PartInitException(Messages.BPELEditor_Cant_read_input_file_1, e); 
			}
		} else {
			throw new PartInitException(Messages.BPELEditor_Cant_read_input_file_1); 
		}
	}

	// This is for loading the model within the editor
	// REMINDER: Any changes to this method, consider corresponding changes to
	// the loadModelWithoutEditModel() method!
	/*protected void loadModel() {
		// TODO: This two lines are a workaround for https://bugs.eclipse.org/bugs/show_bug.cgi?id=72565
		EcorePackage instance = EcorePackage.eINSTANCE;
		instance.eAdapters();*/

		/*Resource bpelResource = editModelClient.getPrimaryResourceInfo().getResource();

		IFile file = getFileInput();
		BPELReader reader = new BPELReader();
	    reader.read(bpelResource, file, getResourceSet());
	    
	    this.process = reader.getProcess();
	    if (getEditDomain() != null) {
	    	((BPELEditDomain)getEditDomain()).setProcess(process);
	    }
	    this.extensionsResource = reader.getExtensionsResource();
	    this.extensionMap = reader.getExtensionMap();
	    
		this.modelListenerAdapter.setExtensionMap(extensionMap);*/

//		ProcessExtension processExtension = (ProcessExtension)ModelHelper.getExtension(process);
//		long stamp = processExtension.getModificationStamp();
//   		// Be nice if the file is old or doesn't yet have a stamp.
//   		if (stamp != 0) {
//   		    long actualStamp = file.getLocalTimeStamp();
//   		    if (stamp != actualStamp) {
//   		        // Inform the user that visual information will be discarded,
//   		        // and null out the extension map. 
//   		        // Only inform the user if we actually have a shell; headless clients
//   		        // will not be informed.
//   		        if (getSite() != null) {
//   		            MessageDialog.openWarning(getSite().getShell(), Messages.getString("BPELEditor.Process_Out_Of_Sync_2"), Messages.getString("BPELEditor.Process_has_been_modified_3")); //$NON-NLS-1$ //$NON-NLS-2$
//   		        }
//   		        // TODO: Null out and recreate the extension map. Perhaps we need
//   		        // to preserve some interesting bits of info about spec compliance
//   		        // and implicit sequences. Don't null it out yet until we understand
//   		        // all the cases in which this could occur.
//  		        //extensionMap = null;
//   		    }
//   		}
	/*}*/
	
	public ICommandFramework getCommandFramework() { return commandFramework; }

	// Make the method public so the properties sections can access it
	public ActionRegistry getActionRegistry() { return super.getActionRegistry(); }

	protected void initializeTrayViewer() {
		getTrayViewer().setEditPartFactory(new BPELTrayEditPartFactory());
		getTrayViewer().setEditDomain(getEditDomain());
		getEditDomain().addViewer(getTrayViewer());
		getTrayViewer().addSelectionChangedListener(this.traySelectionChangeListener);
		registerViewer(getTrayViewer());
		ContextMenuProvider provider = new ProcessContextMenuProvider(getTrayViewer(), getActionRegistry());
		getTrayViewer().setContextMenu(provider);
		getSite().registerContextMenu("org.eclipse.bpel.editor.tray.contextmenu", //$NON-NLS-1$
			provider, getSite().getSelectionProvider());
		getTrayViewer().setContents(getProcess());

		// Setup the drop target and add our listener to it.
		// This will allow us to accept drops from the navigator.
		final DropTarget trayDropTarget = new DropTarget(getTrayViewer().getControl(), DND.DROP_NONE | DND.DROP_COPY | DND.DROP_MOVE | DND.DROP_LINK);
		// Set transfers to the types that are specified by the helper
		// JM
		trayDropTarget.setTransfer(BPELDropTargetListener.getTransferTypes());
		// Create our drop listener and add it to the DropTarget
		// JM
		trayDropTarget.addDropListener(new BPELDropTargetListener(getTrayViewer(), this));
		
		getTrayViewer().getControl().addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent e) {
				trayDropTarget.dispose();
			}
		});
		
		PlatformUI.getWorkbench().getHelpSystem().setHelp(
			getTrayViewer().getControl(), IHelpContextIds.TRAY_DESCRIPTION);	
	}
	
	// Register a viewer with the selection providers
	protected void registerViewer(EditPartViewer viewer) {
		getAdaptingSelectionProvider().addViewer(viewer);
		getMultiViewerSelectionProvider().addViewer(viewer);		
	}
	
	/**
	 * The editor part name should be the same as the one appearing in the logical view.
	 */
	//FIXME should be moved to multi or even removed at all
	protected void updateTitle() {
		setPartName(getProcess().getName());
	}

	protected boolean updateInputFile(IPath filePath) {
		if (filePath == null) {
			return false;
		}
		
		IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(filePath);
		if (file == null) {
			return false;
		}
		
		// WDG: changed setInput to setInputWithNotify here.  is updateTitle still needed?
		setInputWithNotify(new FileEditorInput(file));
		updateTitle();
		return true;
	}
	
	/** 
	 * because our extensions are managed independently of the model and stored in a separate map, we have to make sure
	 * that any extensions that are not used are removed before we serialize
	 * 
	 * the easiest way to do that is to make sure that all extensions have eResource attributes set and thus
	 * we know that the extension is mapped to a model object.
	 * 
	 * TODO: this is bogus though because it will mess up undo/redo!  A better solution
	 * would be to create a second map and save that one, leaving the original unchanged.
	 */
	protected void removeUnusedExtensions() {
		ExtensionMap map = getExtensionMap();
		Set keyset = map.keySet();
		Vector deleted = new Vector();
		for (Iterator i = keyset.iterator(); i.hasNext(); ) {
			EObject model = (EObject) i.next();
			try {
				if (model.eResource() == null)
					deleted.add(model);
			}
			catch (Exception e) {
				// object is definitely not valid;
				deleted.add(model);
			}
		}
		
		for (int j = 0; j < deleted.size(); j++)
			map.remove(deleted.get(j));
	}
	
	public Definition getArtifactsDefinition() {
		// TODO: handle the case where resource doesn't exist or is empty
		Resource artifactsResource = getEditModelClient().getArtifactsResourceInfo().getResource();
		return (Definition)artifactsResource.getContents().get(0);
	}

	/**
	 * The contributor id is cached because we do not support dynamic modification of
	 * the spec-compliance mode. The user is required to re-start the editor in order
	 * to start using the new mode.
	 */
	protected String getPaletteAdditionsContributorId() {
		return IBPELUIConstants.BPEL_EDITOR_ID;
		/*if (contributorID == null) {
	    	ProcessExtension extension = (ProcessExtension) getExtensionMap().get(getProcess());
	    	if (extension.isSpecCompliant()) {
	    		contributorID = IBPELUIConstants.BPEL_SPEC_COMPLIANT_EDITOR_ID;
	    	} else {
	    		contributorID = getEditorSite().getId();
	    	}
		}
		return contributorID;*/
	}
}