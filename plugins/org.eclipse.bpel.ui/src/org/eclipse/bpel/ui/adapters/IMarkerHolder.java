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

import org.eclipse.core.resources.IMarker;

/**
 * IMarkerHolder provides access to markers attached to model objects.
 * 
 * generate the refId according to the following rule:
 *    The marker strategy for BPEL objects is as follows:
 * 		 		"activity:activityId"
 *              "variable:variableName"
 *				"correlationSet:correlationSetName"
 *				"partnerLink:partnerName"
 *				"link:sourceActivityId.linkName"
 *				"case:switchActivityId.caseIndex"
 *				"onMessage:pickActivityId.onMessageIndex"
 *				"process:"
 *
 */
public interface IMarkerHolder {
	/**
	 * 
	 * Return an array of all markers attached to the given model object.
	 * @param object the object (model object)
	 * 
	 * @return the markers it holds on to. 
	 */
	public IMarker[] getMarkers (Object object) ;
}
