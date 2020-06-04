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

import org.eclipse.bpel.ui.util.BPELUtil;
import org.eclipse.emf.ecore.EObject;


/**
 * Content provider for PartnerLinks.
 * 
 * Expects a valid context as input.
 */
public class PartnerLinkContentProvider extends AbstractContentProvider  {

	@Override
	public Object[] getElements (Object input) {
		// the input is expected to be a Process or something inside a Process.
		return BPELUtil.getVisiblePartnerLinks((EObject)input);
	}
}
