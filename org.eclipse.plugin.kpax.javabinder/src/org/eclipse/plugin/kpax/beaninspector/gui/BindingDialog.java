package org.eclipse.plugin.kpax.beaninspector.gui;

import java.util.Collection;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.internal.ui.JavaUIMessages;
import org.eclipse.jdt.internal.ui.dialogs.OpenTypeSelectionDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.plugin.kpax.beaninspector.JavaBeanInspectorPlugin;
import org.eclipse.plugin.kpax.beaninspector.bean.BeanIntrospector;
import org.eclipse.plugin.kpax.beaninspector.bean.BeanProperty;
import org.eclipse.plugin.kpax.beaninspector.logger.Logger;
import org.eclipse.plugin.kpax.beaninspector.util.WidgetUtils;
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
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;

@SuppressWarnings("restriction")
public class BindingDialog extends Dialog {

	private static final Logger LOGGER = JavaBeanInspectorPlugin.getLogger();

	private static IType beanType;

	private Tree tree;

	private Text pathText;

	private Text classText;

	private TreeItem lastCheckedItem;
	private Text excludeText;
	private Text includeText;

	private Button applyButton;

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
		classText.setLayoutData(new RowData(454, SWT.DEFAULT));

		beanType = getBeanType();

		if (beanType != null) {
			classText.setText(beanType.getFullyQualifiedName());
		}

		Button searchClassButton = new Button(composite, SWT.NONE);
		searchClassButton.addSelectionListener(new SearchClassSelectionListener());
		searchClassButton.setText("Class");
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
		settingsExpanditem.setText("Settings");

		Composite settingsComposite = new Composite(settingsExpandBar, SWT.BORDER);
		settingsExpanditem.setControl(settingsComposite);
		settingsComposite.setLayout(new GridLayout(2, false));

		Label lblIncludes = new Label(settingsComposite, SWT.NONE);
		lblIncludes.setText("Include regex:");

		includeText = new Text(settingsComposite, SWT.BORDER);
		includeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		includeText.addModifyListener(new IncludeRegexModifyListener());

		Label excludeLabel = new Label(settingsComposite, SWT.NONE);
		GridData excludeGridLabel = new GridData(SWT.RIGHT, SWT.CENTER, false, false, 1, 1);
		excludeGridLabel.widthHint = 163;
		excludeLabel.setLayoutData(excludeGridLabel);
		excludeLabel.setText("Exclude regex:");

		excludeText = new Text(settingsComposite, SWT.BORDER);
		excludeText.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		excludeText.addModifyListener(new ExcludeRegexModifyListener());

		Label lblShowFullPath = new Label(settingsComposite, SWT.NONE);
		lblShowFullPath.setText("Show fully qualified:");

		Button showFullQualifiedCheckButton = new Button(settingsComposite, SWT.CHECK);
		showFullQualifiedCheckButton.setSelection(true);
		showFullQualifiedCheckButton.addListener(SWT.CHANGED, new FullyQualifiedChangeListener());

		settingsExpanditem.setHeight(settingsExpanditem.getControl().computeSize(SWT.DEFAULT,
				SWT.DEFAULT).y);

		tree.addListener(SWT.Selection, new TreeSelectionListener());

		tree.addListener(SWT.Expand, new ExpandTreeListener());

		try {
			buildItemTreeChildren();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return container;
	}

	private void buildItemTreeChildren() throws JavaModelException {
		if (beanType != null) {
			buildItemTreeChildren(null);
		}
	}

	private void resetLastCheckedItem() {
		lastCheckedItem = null;
		pathText.setText("");
	}

	private void resetTree() {
		tree.removeAll();
		resetLastCheckedItem();
	}

	private void buildItemTreeChildren(TreeItem item) throws JavaModelException {
		if (item == null) {
			resetTree();
		}
		IType itemType = WidgetUtils.getType(item, beanType);
		if (itemType != null) {
			Collection<BeanProperty> properties = new BeanIntrospector(itemType).getProperties();
			System.out.println("propertyMap " + properties);
			for (BeanProperty property : properties) {
				TreeItem childItem = item != null ? new TreeItem(item, 0) : new TreeItem(tree, 0);
				fillTreeItem(childItem, item, property);
				if (item == null) {
					buildItemTreeChildren(childItem);
				}
			}
		}
	}

	private void fillTreeItem(TreeItem item, TreeItem parent, BeanProperty beanProperty) {
		WidgetUtils.setProperty(item, beanProperty);
		WidgetUtils.setPath(item, parent, beanProperty);
		item.setText(beanProperty.asText());
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
		applyButton = createButton(parent, IDialogConstants.CLIENT_ID, "Apply", false);
		applyButton.setEnabled(false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(552, 645);
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

	protected void configureShell(Shell shell) {
		super.configureShell(shell);
		shell.setText("JavaBean Inspector");
	}

	private class IncludeRegexModifyListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			String text = ((Text) e.widget).getText();
			if ("".equals(text)) {
				excludeText.setEnabled(true);
			} else {
				excludeText.setText("");
				excludeText.setEnabled(false);
			}
			applyButton.setEnabled(true);
		}
	}

	private class ExcludeRegexModifyListener implements ModifyListener {

		@Override
		public void modifyText(ModifyEvent e) {
			applyButton.setEnabled(true);
		}
	}

	private class TreeSelectionListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			TreeItem checkedItem = (TreeItem) event.item;
			if (event.detail == SWT.CHECK) {
				System.out.println("checkedItem " + checkedItem.getChecked());
				if (checkedItem.getChecked()) {
					if (checkedItem != lastCheckedItem && lastCheckedItem != null
							&& !lastCheckedItem.isDisposed()) {
						lastCheckedItem.setChecked(false);
					}
					lastCheckedItem = checkedItem;
					pathText.setText(WidgetUtils.getTextPath(lastCheckedItem));
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
				try {
					buildItemTreeChildren(item);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}
	}

	private class SearchClassSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent e) {
			SelectionDialog dialog = new OpenTypeSelectionDialog(getShell(), false, PlatformUI
					.getWorkbench().getProgressService(), null,
					IJavaSearchConstants.CLASS_AND_INTERFACE);
			dialog.setTitle(JavaUIMessages.OpenTypeAction_dialogTitle);
			dialog.setMessage(JavaUIMessages.OpenTypeAction_dialogMessage);
			if (dialog.open() == SelectionDialog.OK) {
				try {
					beanType = (IType) dialog.getResult()[0];
					classText.setText(beanType.getFullyQualifiedName());
					buildItemTreeChildren();
				} catch (Exception e1) {
					LOGGER.error(e1);
					MessageDialog.openError(
							getShell(),
							"Error",
							"Something is wrong with the selected class content: "
									+ e1.getMessage());
				}
			}
		}
	}

	private class FullyQualifiedChangeListener implements Listener {

		@Override
		public void handleEvent(Event event) {
			applyButton.setVisible(true);
		}

	}
}
