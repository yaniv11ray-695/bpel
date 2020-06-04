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
package org.eclipse.bpel.ui.util;

/** 
 * A visitor for model objects.
 * 
 * TODO: GET RID OF THIS CLASS and surrounding machinery.  Just use eAllContents().
 * The IContainer machinery now basically matches the EditPart tree, and should not
 * be used for e.g. model manipulations that have to consider every model object.
 */
public interface IModelVisitor {
	
	/**
	 * Visits the given model object.
	 * 
	 * Returns true if the children of this model object should also be visited.
	 * Returns false if the children do not need to be visited.
	 */
	boolean visit(Object modelObject);
}
