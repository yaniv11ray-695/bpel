/*******************************************************************************
 * Copyright (c) 2005, 2012 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.ui.adapters;

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.Expression;
import org.eclipse.bpel.model.While;
import org.eclipse.bpel.ui.adapters.delegates.ActivityContainer;
import org.eclipse.bpel.ui.editparts.OutlineTreeEditPart;
import org.eclipse.bpel.ui.editparts.SequenceEditPart;
import org.eclipse.gef.EditPart;


public class WhileAdapter extends ContainerActivityAdapter implements IAnnotatedElement {

	/**
	 * @see org.eclipse.bpel.ui.adapters.ContainerActivityAdapter#createContainerDelegate()
	 */
	/* IContainer delegate */

	@Override
	public IContainer createContainerDelegate() {
		return new ActivityContainer(BPELPackage.eINSTANCE.getWhile_Activity());
	}

	/**
	 * @see org.eclipse.bpel.ui.adapters.ActivityAdapter#createEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	@Override
	public EditPart createEditPart(EditPart context, Object model) {
		EditPart result = new SequenceEditPart();
		result.setModel(model);
		return result;
	}

	/**
	 * @see org.eclipse.bpel.ui.adapters.ActivityAdapter#createOutlineEditPart(org.eclipse.gef.EditPart, java.lang.Object)
	 */
	@Override
	public EditPart createOutlineEditPart(EditPart context, Object model) {
		EditPart result = new OutlineTreeEditPart();
		result.setModel(model);
		return result;
	}

	/* IAnnotatedElement */

	public String[] getAnnotation(Object object) {
		Expression expression = ((While)object).getCondition();
		return new String[] {
			Messages.CONDITION, AnnotationHelper.getAnnotation(expression)
		};
	}
}
