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
package org.eclipse.bpel.extensionssample.ui.palette;

import org.eclipse.bpel.common.ui.palette.IPaletteProvider;
import org.eclipse.bpel.common.ui.palette.PaletteCategory;
import org.eclipse.bpel.extensionsample.model.ModelPackage;
import org.eclipse.bpel.extensionssample.ui.factories.ExtensionSampleUIObjectFactory;
import org.eclipse.bpel.ui.util.BPELCreationToolEntry;
import org.eclipse.gef.palette.PaletteRoot;

public class ExtensionSamplePaletteProvider implements IPaletteProvider {

	@Override
	public void contributeItems(PaletteRoot paletteRoot) {

		PaletteCategory category = new PaletteCategory("ExtensionSample");
		category.setCategoryId("extensionsample");
		category.setOrder(40);

		category
				.add(new BPELCreationToolEntry("Sample Simple Activity", "Sample Simple Activity",
						new ExtensionSampleUIObjectFactory(ModelPackage.eINSTANCE
								.getSampleSimpleActivity())));
		
		category.add(new BPELCreationToolEntry("Sample Structured Activity",
				"Sample Structured Activity", new ExtensionSampleUIObjectFactory(
						ModelPackage.eINSTANCE.getSampleStructuredActivity())));

		paletteRoot.add(category);
	}

}
