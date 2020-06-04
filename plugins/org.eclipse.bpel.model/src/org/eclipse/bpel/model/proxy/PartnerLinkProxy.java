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

import org.eclipse.bpel.model.BPELPackage;
import org.eclipse.bpel.model.impl.PartnerLinkImpl;
import org.eclipse.bpel.model.util.BPELProxyURI;
import org.eclipse.emf.common.util.URI;


public class PartnerLinkProxy extends PartnerLinkImpl {

    private BPELProxyURI proxyURI;

    public PartnerLinkProxy(URI baseURI, String name) {
        proxyURI = new BPELProxyURI(BPELPackage.eINSTANCE.getPartnerLink(), baseURI, new QName("process", name));
    }

    @Override
	public boolean eIsProxy() {
        return true;
    }

    @Override
	public URI eProxyURI() {
        return proxyURI.getProxyURI();
    }

    @Override
	public String getName() {
        return proxyURI.getQName().getLocalPart();
    }
}
