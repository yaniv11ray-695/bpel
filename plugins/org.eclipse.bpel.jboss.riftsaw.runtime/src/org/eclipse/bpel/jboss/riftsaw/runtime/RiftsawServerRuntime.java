/*******************************************************************************
 * Copyright (c) 2008, 2012 IBM Corporation and others.
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
package org.eclipse.bpel.jboss.riftsaw.runtime;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.jst.server.generic.core.internal.GenericServerRuntime;
import org.eclipse.bpel.runtimes.IBPELRuntimeDelegate;

@SuppressWarnings("restriction")
public class RiftsawServerRuntime extends GenericServerRuntime implements IBPELRuntimeDelegate {

	public String getServerAddress() {
		return "localhost";
	}
	
	public String getPort()
	{
		Map m = getAttribute("generic_server_attributes", new HashMap());
		return (String)m.get("port");
	}

	public String getDeployDir() {
		return "";
	}
}
