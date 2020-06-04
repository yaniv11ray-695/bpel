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
package org.eclipse.bpel.model.messageproperties.util;

import org.eclipse.bpel.model.BPELPlugin;
import org.eclipse.bpel.model.messageproperties.MessagepropertiesFactory;
import org.eclipse.wst.wsdl.ExtensibilityElement;
import org.eclipse.wst.wsdl.WSDLFactory;
import org.eclipse.wst.wsdl.util.ExtensibilityElementFactory;


public class MessagepropertiesExtensibilityElementFactory implements ExtensibilityElementFactory
{
    public ExtensibilityElement createExtensibilityElement(String namespace, String localName)
    {
        if (MessagepropertiesConstants.isMessagePropertiesNamespace(namespace))
        {
            if (MessagepropertiesConstants.PROPERTY_ELEMENT_TAG.equals(localName))
            {
                return MessagepropertiesFactory.eINSTANCE.createProperty();
            }
            else if (MessagepropertiesConstants.PROPERTY_ALIAS_ELEMENT_TAG.equals(localName))
            {
                return MessagepropertiesFactory.eINSTANCE.createPropertyAlias();
            }
            else
            {
				BPELPlugin.getPlugin().log("MessagepropertiesExtensibilityElementFactory: Unhandled localName: " + localName);
                return WSDLFactory.eINSTANCE.createUnknownExtensibilityElement();
            }
        }
        else
        {
        	BPELPlugin.getPlugin().log("PartnerlinktypeExtensibilityElementFactory: Unhandled namespace: " + namespace);
            return WSDLFactory.eINSTANCE.createUnknownExtensibilityElement();
        }
    }
}
