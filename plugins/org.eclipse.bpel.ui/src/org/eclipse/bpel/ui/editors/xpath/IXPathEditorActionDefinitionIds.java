/*******************************************************************************
 * Copyright (c) 2006, 2012 Oracle Corporation and others.
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
package org.eclipse.bpel.ui.editors.xpath;

import org.eclipse.ui.texteditor.ITextEditorActionDefinitionIds;

/**
 * @author Michal Chmielewski (michal.chmielewski@oracle.com)
 * @date Nov 1, 2006
 *
 */
@SuppressWarnings("nls")

public interface IXPathEditorActionDefinitionIds extends
		ITextEditorActionDefinitionIds {

	/**
	 * Variable Picker action definition id.
	 */
	
	String VARIABLE_PICKER = "org.eclipse.bpel.ui.editors.xpath.VariablePicker";

}
