package org.eclipse.plugin.kpax.javabinder.gui;

import java.util.Collection;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.search.IJavaSearchConstants;
import org.eclipse.jdt.internal.ui.JavaUIMessages;
import org.eclipse.jdt.internal.ui.dialogs.OpenTypeSelectionDialog;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.plugin.kpax.javabinder.JavaBinderPlugin;
import org.eclipse.plugin.kpax.javabinder.util.BeanIntrospector;
import org.eclipse.plugin.kpax.javabinder.util.BeanProperty;
import org.eclipse.plugin.kpax.javabinder.util.WidgetUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.Tree;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;

@SuppressWarnings("restriction")
public class BindingDialog extends Dialog {

	private static final JavaBinderPlugin PLUGIN = JavaBinderPlugin.getInstance();

	private static IType beanType;

	private Tree tree;

	private Text pathText;

	private Text classText;

	private TreeItem lastCheckedItem;

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
		classText.setLayoutData(new RowData(454, SWT.DEFAULT));
		if (beanType != null) {
			classText.setText(beanType.getFullyQualifiedName());
		}
		Button searchClassButton = new Button(composite, SWT.NONE);
		searchClassButton.addSelectionListener(new SelectionAdapter() {
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
						PLUGIN.error(e1);
						MessageDialog.openError(
								getShell(),
								"Error",
								"Something is wrong with the selected class content: "
										+ e1.getMessage());
					}
				}
			}
		});
		searchClassButton.setText("Class");

		this.tree = new Tree(container, SWT.CHECK | SWT.BORDER | SWT.V_SCROLL | SWT.H_SCROLL);
		GridData dataGridTree = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		dataGridTree.heightHint = 317;
		dataGridTree.widthHint = 501;
		tree.setLayoutData(dataGridTree);

		this.pathText = new Text(container, SWT.BORDER | SWT.H_SCROLL);
		GridData dataGridPathText = new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1);
		dataGridPathText.heightHint = 22;
		dataGridPathText.widthHint = 514;
		pathText.setLayoutData(dataGridPathText);
		pathText.setBounds(0, 270, 290, 25);

		tree.addListener(SWT.Selection, new Listener() {
			@Override
			public void handleEvent(Event event) {
				if (event.detail == SWT.CHECK) {
					if (lastCheckedItem != null && !lastCheckedItem.isDisposed()) {
						lastCheckedItem.setChecked(false);
					}
					lastCheckedItem = (TreeItem) event.item;
					pathText.setText(WidgetUtils.getTextPath(lastCheckedItem));
				}
			}
		});

		tree.addListener(SWT.Expand, new Listener() {

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
		});
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
		WidgetUtils.putProperty(item, beanProperty);
		WidgetUtils.putPath(item, parent, beanProperty);
		item.setText(beanProperty.asText());
	}

	@Override
	protected void createButtonsForButtonBar(Composite parent) {
		createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true)
				.addSelectionListener(new SelectionAdapter() {

					@Override
					public void widgetSelected(SelectionEvent e) {
						System.out.println("OK");
					}
				});
		createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
	}

	@Override
	protected Point getInitialSize() {
		return new Point(561, 554);
	}

	public TreeItem getLastCheckedItem() {
		return lastCheckedItem;
	}

	public static IType getBeanType() {
		return beanType;
	}

}
