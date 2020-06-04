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
package org.eclipse.bpel.model.proxy;

import javax.xml.namespace.QName;

import org.eclipse.bpel.model.messageproperties.MessagepropertiesPackage;
import org.eclipse.bpel.model.messageproperties.impl.PropertyImpl;
import org.eclipse.bpel.model.util.BPELProxyURI;
import org.eclipse.emf.common.util.URI;


public class PropertyProxy extends PropertyImpl implements IBPELServicesProxy
{
    private BPELProxyURI proxyURI;

    public PropertyProxy(URI baseURI, QName qname)
    {
        proxyURI = new BPELProxyURI(MessagepropertiesPackage.eINSTANCE.getProperty(), baseURI, qname);
    }

    @Override
	public boolean eIsProxy()
    {
        return true;
    }

    @Override
	public URI eProxyURI()
    {
        return proxyURI.getProxyURI();
    }

    @Override
	public QName getQName()
    {
        return proxyURI.getQName();
    }

	@Override
	public String getName() {
		return getQName().getLocalPart();
	}
}
