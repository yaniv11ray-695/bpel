/*******************************************************************************
 * Copyright (c) 2005 IBM Corporation and others.
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

import org.eclipse.bpel.model.Process;
import org.eclipse.bpel.model.resource.BPELWriter;
import org.eclipse.emf.ecore.EObject;
import org.w3c.dom.Node;



public interface ServiceReferenceSerializer {

	void marshall(Object target, Node parentNode, Process process, EObject parent, BPELWriter writer);
}
