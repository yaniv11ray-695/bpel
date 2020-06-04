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

/*
 * Menu: Misc > XPath Create
 * License: EPL 1.0
 */
 
function alert(text) {

Packages.org.eclipse.jface.dialogs.MessageDialog.openInformation( 	
	window.getShell(), 	
	"Monkey Dialog", 
	text	
	)

}

var XPathFactory = Packages.javax.xml.xpath.XPathFactory


function main() {
 // alert( XPathFactory.DEFAULT_OBJECT_MODEL_URI ) 
 
 var factory = XPathFactory.newInstance()
 // alert(factory)
 var xpath = factory.newXPath()
 alert(xpath)
}