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
package org.eclipse.bpel.ui.properties;

import org.eclipse.bpel.common.ui.flatui.FlatFormAttachment;
import org.eclipse.bpel.common.ui.flatui.FlatFormData;
import org.eclipse.bpel.ui.Messages;
import org.eclipse.bpel.ui.util.MultiObjectAdapter;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;


/**
 * Details section for the CatchAll
 */
public class CatchAllSection extends BPELPropertySection {
	
	@Override
	protected void createClient(Composite parent) {
		Composite composite = createFlatFormComposite(parent);
		FlatFormData ffdata;
		
		Label nameLabel = fWidgetFactory.createLabel(composite, Messages.CatchAllDetails_0); 
		
		ffdata = new FlatFormData();
		ffdata.left = new FlatFormAttachment(0, 0);
		ffdata.top = new FlatFormAttachment(0, 0);
		ffdata.right = new FlatFormAttachment(100, 0);
		nameLabel.setLayoutData(ffdata);
	}
	
	@Override
	protected MultiObjectAdapter[] createAdapters() {
		return new MultiObjectAdapter[0];
	}

	@Override
	public Object getUserContext() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void restoreUserContext(Object userContext) {
		// TODO Auto-generated method stub
	}
}
