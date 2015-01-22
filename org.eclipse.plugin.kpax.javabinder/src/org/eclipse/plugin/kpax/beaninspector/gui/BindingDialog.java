/*******************************************************************************
 * Copyright 2015 Eugen Covaci
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.eclipse.plugin.kpax.beaninspector.gui;

import java.util.Collection;

import org.eclipse.jdt.core.IType;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.osgi.util.NLS;
import org.eclipse.plugin.kpax.beaninspector.JavaBeanInspectorPlugin;
import org.eclipse.plugin.kpax.beaninspector.Messages;
import org.eclipse.plugin.kpax.beaninspector.introspector.BeanIntrospector;
import org.eclipse.plugin.kpax.beaninspector.introspector.model.BeanProperty;
import org.eclipse.plugin.kpax.beaninspector.logger.Logger;
import org.eclipse.plugin.kpax.beaninspector.prefs.Settings;
import org.eclipse.plugin.kpax.beaninspector.util.JdtUtils;
import org.eclipse.plugin.kpax.beaninspector.util.WidgetDataUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;

public class BindingDialog extends Dialog {

	private final Logger logger = JavaBeanInspectorPlugin.getLogger();

	private static IType beanType;

	private Tree tree;
	private Text pathText;
	private Text classText;
	private TreeItem lastCheckedItem;
	private Text includeText;
	private Button showFullQualifiedCheckButton;
	private Button applyButton;
	private Button okButton;

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 * @wbp.parser.constructor
	 */
	public BindingDialog(Shell parentShell) {
		super(parentShell);
	}

	/**
	 * Create the dialog.
	 * 
	 * @param parentShell
	 */
	public BindingDialog(Shell parentShell, IType type) {
		super(parentShell);
		beanType = type;
	}

	/**
	 * Create contents of the dialog.
	 * 
	 * @param parent
	 */
	@Override
	protected Control createDialogArea(Composite parent) {
		Composite container = (Composite) super.createDialogArea(parent);
		Composite composite = new Composite(container, SWT.BORDER);
		RowLayout rowLayout = new RowLayout(SWT.HORIZONTAL);
		rowLayout.fill = true;
		composite.setLayout(rowLayout);
		GridData gridData = new GridData(SWT.LEFT, SWT.CENTER, false, false, 1, 1);
		gridData.heightHint = 36;
		composite.setLayoutData(gridData);

		classText = new Text(composite, SWT.BORDER);
		classText.setEditable(false);
		classText.setLayoutData(new RowData(437, SWT.DEFAULT));

		Button searchClassButton = new Button(composite, SWT.NONE);
		searchClassButton.addSelectionListener(new SearchClassSelectionListener());
		searchClassButton.setText(Messages.BindingDialog_label_open_type);
		searchClassButton.setToolTipText(Messages.BindingDialog_tooltip_class);
		searchClassButton.setFocus();

		this.tree = new Tree(container, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData dataGridTree = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		dataGridTree.heightHint = 298;
		dataGridTree.widthHint = 501;
		tree.setLayoutData(dataGridTree);

		this.pathText = new Text(container, SWT.BORDER);
		GridData dataGridPathText = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		dataGridPathText.heightHint = 20;
		dataGridPathText.widthHint = 514;
		pathText.setLayoutData(dataGridPathText);
		pathText.setBounds(0, 270, 290, 20);

		ExpandBar settingsExpandBar = new ExpandBar(container, SWT.NONE);
		GridData settingsGridDataExpandBar = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		settingsGridDataExpandBar.heightHint = 143;
		settingsExpandBar.setLayoutData(settingsGridDataExpandBar);

		ExpandItem settingsExpanditem = new ExpandItem(settingsExpandBar, SWT.NONE);
		settingsExpanditem.setText(Messages.BindingDialog_label_rules);

		Composite settingsComposite = new Composite(settingsExpandBar, SWT.BORDER);
		settingsExpanditem.setControl(settingsComposite);
		settingsComposite.setLayout(new GridLayout(2, false));

		Label lblIncludes = new Label(settingsComposite, SWT.NONE);
		lblIncludes.setText(Messages.BindingDialog_label_includeRegex);

		includeText = new Text(settingsComposite, SWT.BORDER);
		includeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		includeText.addModifyListener(new IncludeRegexModifyListener());
		includeText.setToolTipText(Messages.BindingDialog_tooltip_includeRegex);

		Label lblShowFullPath = new Label(settingsComposite, SWT.NONE);
		lblShowFullPath.setText(Messages.BindingDialog_label_Show_fully_qualified);

		showFullQualifiedCheckButton = new Button(settingsComposite, SWT.CHECK);
		showFullQualifiedCheckButton.setSelection(true);
		showFullQualifiedCheckButton.addSelectionListener(new FullyQualifiedChangeListener());
		showFullQualifiedCheckButton.setToolTipText(Messages.BindingDialog_tooltip_showFull);
		
		applySettings();

		settingsExpanditem.setHeight(settingsExpanditem.getControl().computeSize(SWT.DEFAULT,
				SWT.DEFAULT).y);

		tree.addListener(SWT.Selection, new TreeSelectionListener());

		tree.addListener(SWT.Expand, new ExpandTreeListener());

		beanType = getBeanType();
		if (beanType != null) {
			applyBeanType();
		}
		return container;
	}

	private void buildItemTreeChildren() {
		buildItemTreeChildren(null);
	}

	private void resetLastCheckedItem() {
		lastCheckedItem = null;
		pathText.setText("");
	}

	private void resetTree() {
		tree.removeAll();
		resetLastCheckedItem();
	}

	private void buildItemTreeChildren(TreeItem item) {
		try {
			if (item == null) {
				resetTree();
			}
			IType itemType = WidgetDataUtils.getType(item, beanType);
			if (itemType != null) {
				Collection<BeanProperty> properties = new BeanIntrospector(itemType)
						.getProperties();
				for (BeanProperty property : properties) {
					TreeItem childItem = item != null ? new TreeItem(item, 0) : new TreeItem(tree,
							0);
					fillTreeItem(childItem, item, property);
					if (item == null) {
						buildItemTreeChildren(childItem);
					}
				}
			}
		} catch (Exception e) {
			logger.error(e);
			MessageDialog.openError(getShell(), Messages.BeanInspector_err_title,
					NLS.bind(Messages.BindingDialog_err_build_tree, e.getMessage()));
		}
	}

	private void fillTreeItem(TreeItem item, TreeItem parent, BeanProperty beanProperty) {
		WidgetDataUtils.setProperty(item, beanProperty);
		WidgetDataUtils.setPath(item, parent, beanProperty);
		item.setText(beanProperty.asText(showFullQualifiedCheckButton.getSelection()));
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		okButton = createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		okButton.setToolTipText(Messages.BindingDialog_tooltip_ok);
		applyButton = createButton(parent, IDialogConstants.CLIENT_ID,
				Messages.BindingDialog_label_apply, false);
		applyButton.setToolTipText(Messages.BindingDialog_tooltip_apply);
		applyButton.addSelectionListener(new ApplySelectionListener());
		applyButton.setEnabled(false);
		Button resetButton = createButton(parent, IDialogConstants.CLIENT_ID,
				Messages.BindingDialog_label_reset, false);
		resetButton.addSelectionListener(new ResetSelectionListener());
		resetButton.setToolTipText(Messages.BindingDialog_tooltip_reset);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(610, 622);
	}

	public TreeItem getLastCheckedItem() {
		return lastCheckedItem;
	}

	public static IType getBeanType() {
		if (beanType != null && beanType.exists()) {
			return beanType;
		} else {
			return null;
		}
	}
	
	public static void setBeanType(IType type) {
		beanType = type;
	}

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText(Messages.BindingDialog_title);
	}

	private void applySettings() {
		Settings settings = Settings.getSettings();
		includeText.setText(settings.getIncludeRegex());
		showFullQualifiedCheckButton.setSelection(settings.isShowFullyQualified());
	}

	private void saveSettings() {
		Settings settings = Settings.getSettings();
		settings.setIncludeRegex(includeText.getText());
		settings.setShowFullyQualified(showFullQualifiedCheckButton.getSelection());
		settings.saveSettings();
	}

	private void resetView() {
		beanType = null;
		classText.setText("");
		resetTree();
	}

	private void applyBeanType() {
		classText.setText(beanType.getFullyQualifiedName());
		buildItemTreeChildren();
	}

	private class IncludeRegexModifyListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			if (applyButton != null) {
				applyButton.setEnabled(true);
			}
		}
	}

	private class TreeSelectionListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			TreeItem checkedItem = (TreeItem) event.item;
			if (event.detail == SWT.CHECK) {
				if (checkedItem.getChecked()) {
					if (checkedItem != lastCheckedItem && lastCheckedItem != null
							&& !lastCheckedItem.isDisposed()) {
						lastCheckedItem.setChecked(false);
					}
					lastCheckedItem = checkedItem;
					pathText.setText(WidgetDataUtils.getTextPath(lastCheckedItem));
				} else {
					resetLastCheckedItem();
				}
			}
		}
	}

	private class ExpandTreeListener implements Listener {

		@Override
		public void handleEvent(Event event) {
			TreeItem parentItem = (TreeItem) event.item;
			for (TreeItem item : parentItem.getItems()) {
				buildItemTreeChildren(item);
			}
		}
	}

	private class SearchClassSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent event) {
			IType selectedType = null;
			try {
				selectedType = JdtUtils.chooseType(getShell(), beanType);
			} catch (Exception e) {
				logger.error(e);
				MessageDialog.openError(getShell(), Messages.BeanInspector_err_title,
						NLS.bind(Messages.BindingDialog_err_build_tree, e.getMessage()));
			}
			if (selectedType != null) {
				beanType = selectedType;
				applyBeanType();
				if (tree.getItemCount() > 0) {
					okButton.setFocus();
				} else {
					MessageDialog.openInformation(getShell(),
							Messages.ContextualBeanInspector_messageTitle,
							Messages.BindingDialog_err_not_javabean);
					resetView();
				}
			}
		}
	}

	private class FullyQualifiedChangeListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			applyButton.setEnabled(true);
		}
	}

	private class ResetSelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			Settings.getSettings().reset();
			applySettings();
			resetView();
			applyButton.setEnabled(false);
		}

	}

	private class ApplySelectionListener extends SelectionAdapter {

		@Override
		public void widgetSelected(SelectionEvent e) {
			saveSettings();
			applyButton.setEnabled(false);
			if (getBeanType() != null) {
				buildItemTreeChildren();
			}
		}

	}
}
