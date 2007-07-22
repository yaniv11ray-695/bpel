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
package org.eclipse.bpel.ui.commands;

import org.eclipse.bpel.ui.Messages;
import org.eclipse.bpel.ui.adapters.IContainer;
import org.eclipse.bpel.ui.adapters.ILabeledElement;
import org.eclipse.bpel.ui.commands.util.AutoUndoCommand;
import org.eclipse.bpel.ui.util.BPELUtil;
import org.eclipse.draw2d.geometry.Rectangle;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.osgi.util.NLS;

import org.w3c.dom.Element;


/** 
 * This command is used to add a child into a parent object which supports IContainer. 
 */
public class InsertInContainerCommand extends AutoUndoCommand {
	
	private class MyBPELWriter extends  org.eclipse.bpel.model.resource.BPELWriter {
		private MyBPELWriter(org.eclipse.bpel.model.resource.BPELResource bpelResource, org.w3c.dom.Document document) {
			super(bpelResource, document);
		}
		
		public Element activity2XML(org.eclipse.bpel.model.Activity activity) {
			//just make the method public
			return super.activity2XML(activity);
		}
	}

	protected EObject child, parent, before;
	protected Rectangle rect;
	
	public InsertInContainerCommand(EObject parent, EObject child, EObject before) {
		super(Messages.InsertInContainerCommand_Add_Node_1, parent); 
		this.parent = parent;
		this.child = child;
		this.before = before;
		ILabeledElement labeledElement = (ILabeledElement)BPELUtil.adapt(child, ILabeledElement.class);
		String childType = null;
		if (labeledElement != null) childType = labeledElement.getTypeLabel(child);
		if (childType == null) childType = Messages.InsertInContainerCommand_Node_3; 
		setLabel(NLS.bind(Messages.InsertInContainerCommand_Add_1, (new Object[] { childType }))); 
	}

	public boolean canDoExecute() {
		IContainer container = (IContainer)BPELUtil.adapt(parent, IContainer.class);
		return container.canAddObject(parent, child, before);
	}

	public void doExecute() {
		IContainer container = (IContainer)BPELUtil.adapt(parent, IContainer.class);		
		container.addChild(parent, child, before);
		
	    Element parentElement = org.eclipse.bpel.ui.util.BPELEditorUtil.getInstance().getElementForObject(parent);
	    Element beforeElement = null;
	    if (before != null) {
	    	beforeElement = org.eclipse.bpel.ui.util.BPELEditorUtil.getInstance().getElementForObject(before);
	    }
	    
	    MyBPELWriter writer = new MyBPELWriter((org.eclipse.bpel.model.resource.BPELResource)(parent.eResource()),
	    										parentElement.getOwnerDocument());

	    Element childElement = writer.activity2XML(((org.eclipse.bpel.model.Activity)child));

	    parentElement.insertBefore(childElement, beforeElement);
	}
	
	public EObject getChild() { return child; }
}
