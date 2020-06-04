/*******************************************************************************
 * Copyright (c) 2006 IBM Corporation and others.
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
package org.eclipse.bpel.model.extensions;

import javax.xml.namespace.QName;

import org.w3c.dom.Node;

import org.eclipse.bpel.model.Activity;
import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.resource.BPELWriter;

/**
 * This interface should be implemented by classes which serialize
 * extension-specific instances of Activity into a DOM
 * element.
 */
public interface BPELActivitySerializer {

	/**
	 * This method serializes extension-specific instances of
	 * Activity into the given parent element.
	 */
	public void marshall(QName elementType, Activity activity, Node parentNode, Process process, BPELWriter bpelWriter);
}
