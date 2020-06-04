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
package org.eclipse.bpel.ui.details.providers;

import java.util.Collection;

import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;

/**
 * Filter which selects from a list of properties only those that are unused 
 */
public class UnusedPropertyFilter extends ViewerFilter {

	Collection rejectCandidates, keepAllOfThese;

	public void setCandidates(Collection rejectCandidates, Collection keepAllOfThese) {
		this.rejectCandidates = rejectCandidates;
		this.keepAllOfThese = keepAllOfThese;
	}
	
	@Override
	public boolean select(Viewer viewer, Object parentElement, Object element) {
		if (keepAllOfThese != null && keepAllOfThese.contains(element))  return true;
		if (rejectCandidates != null && rejectCandidates.contains(element))  return false;
		return true;
	}
}
