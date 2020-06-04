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
package org.eclipse.bpel.ui;

import org.eclipse.gef.requests.CreationFactory;

public class BPELLinkCreationFactory implements CreationFactory {
	private Object type = null;

	public class PartnerTempLink {
	}

	public class IncomingVarTempLink {
	}

	public class OutgoingVarTempLink {
	}
	
	public class CompensateTempLink {
	}	

	public BPELLinkCreationFactory(Object type) {
		super();
		this.type = type;
	}

	public Object getNewObject() {
		if (type == PartnerTempLink.class)
			return new PartnerTempLink();
		else if (type == IncomingVarTempLink.class)
			return new IncomingVarTempLink();
		else if (type == OutgoingVarTempLink.class)
			return new OutgoingVarTempLink();
		else if (type == CompensateTempLink.class)
			return new CompensateTempLink();		
		return null;
	}

	public Object getObjectType() {
		return type;
	}

	public void setObjectType(Object type) {
		this.type = type;
	}
}
