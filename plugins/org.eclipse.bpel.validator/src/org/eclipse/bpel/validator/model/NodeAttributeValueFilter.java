/*******************************************************************************
 * Copyright (c) 2006 Oracle Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License 2.0
 * which accompanies this distribution, and is available at
 * https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Oracle Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.bpel.validator.model;

import javax.xml.namespace.QName;

/**
 * Filter that filters based on attribute name and its value.
 * For example, select part where part "name" has some value.
 * 
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Dec 7, 2006
 *
 */

public class NodeAttributeValueFilter extends Filter implements IFilter<INode> {
	
	String fValue;
	QName fAttributeName;
	
	/**
	 * Constructor for the name filter.
	 * @param attrName attribute name
	 * @param attrValue attribute value
	 */
	
	public NodeAttributeValueFilter (QName attrName, String attrValue) {
		fAttributeName = attrName;
		fValue = attrValue;			
	}
	
	/** (non-Javadoc)
	 * @param node 
	 * @return true if the node is to be selected, false otherwise.
	 * @see org.eclipse.bpel.validator.model.IFilter#select(java.lang.Object)
	 */
	public boolean select (INode node) {
		return fValue.equals ( node.getAttribute(fAttributeName ) );
	}
}