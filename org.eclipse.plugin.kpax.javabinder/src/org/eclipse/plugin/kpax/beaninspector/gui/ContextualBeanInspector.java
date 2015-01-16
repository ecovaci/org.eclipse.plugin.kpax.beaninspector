package org.eclipse.plugin.kpax.beaninspector.gui;

import java.util.Collection;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.plugin.kpax.beaninspector.Messages;
import org.eclipse.plugin.kpax.beaninspector.introspector.BeanIntrospector;
import org.eclipse.plugin.kpax.beaninspector.introspector.model.BeanProperty;
import org.eclipse.plugin.kpax.beaninspector.util.Glyphs;
import org.eclipse.plugin.kpax.beaninspector.util.TextEditorUtils;
import org.eclipse.plugin.kpax.beaninspector.util.WidgetDataUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class ContextualBeanInspector {

	private Shell shell;

	public ContextualBeanInspector(Shell shell) {
		this.shell = shell;
	}

	public void showAt(Point location) throws JavaModelException {
		IType beanType = BindingDialog.getBeanType();
		if (beanType != null) {
			Menu rootMenu = buildMenuTree(null);
			if (rootMenu != null && rootMenu.getItemCount() > 0) {
				for (MenuItem item : rootMenu.getItems()) {
					buildMenuTree(item);
				}
				rootMenu.setLocation(location.x, location.y);
				rootMenu.setVisible(true);
			} else {
				MessageDialog.openWarning(this.shell, Messages.ContextualBeanInspector_messageTitle,
						Messages.ContextualBeanInspector_info_no_property);
			}
		} else {
			MessageDialog.openInformation(this.shell,
					Messages.ContextualBeanInspector_messageTitle,
					Messages.ContextualBeanInspector_messageTitle);
		}
	}

	private Menu buildMenuTree(MenuItem item) throws JavaModelException {
		IType beanType = WidgetDataUtils.getType(item, BindingDialog.getBeanType());
		if (beanType != null) {
			BeanIntrospector beanIntrospector = new BeanIntrospector(beanType);
			Collection<BeanProperty> properties = beanIntrospector.getProperties();
			if (!properties.isEmpty()) {
				Menu menu;
				if (item != null) {
					menu = new Menu(shell, SWT.DROP_DOWN);
					item.setMenu(menu);
					WidgetDataUtils.addGlyphOnLeft(item, Glyphs.ARROW_RIGHT);
					createClassMenuItem(item);
				} else {
					menu = new Menu(shell);
					createRootClassMenuItem(menu, beanType);
				}

				for (BeanProperty property : properties) {
					MenuItem childMenuItem = new MenuItem(menu, SWT.CASCADE);
					fillMenuItem(childMenuItem, item, property);
					childMenuItem.addListener(SWT.Arm, new ArmMenuItemListener());
				}
				return menu;
			}
		}
		return null;
	}

	private void fillMenuItem(MenuItem item, MenuItem parentItem, BeanProperty property) {
		WidgetDataUtils.setProperty(item, property);
		WidgetDataUtils.setPath(item, parentItem, property);
		item.setText(property.asText());
		item.addSelectionListener(new ItemSelectionListener());
	}

	private MenuItem createClassMenuItem(MenuItem item) {
		MenuItem classMenuItem = new MenuItem(item.getMenu(), SWT.PUSH);
		WidgetDataUtils.setPath(classMenuItem, WidgetDataUtils.getPath(item));
		classMenuItem.setText(Glyphs.glyphOnRight(WidgetDataUtils.getBeanProperty(item)
				.getTypeQualifiedName(), Glyphs.ARROW_DOWN));
		classMenuItem.addSelectionListener(new ItemSelectionListener());
		new MenuItem(item.getMenu(), SWT.SEPARATOR);
		return classMenuItem;
	}

	private MenuItem createRootClassMenuItem(Menu menu, IType beanType) {
		MenuItem classMenuItem = new MenuItem(menu, SWT.PUSH);
		classMenuItem.setText(Glyphs.glyphOnRight(beanType.getFullyQualifiedName(),
				Glyphs.ARROW_DOWN));
		new MenuItem(menu, SWT.SEPARATOR);
		return classMenuItem;
	}

	private class ArmMenuItemListener implements Listener {
		@Override
		public void handleEvent(Event event) {
			MenuItem menuItem = (MenuItem) event.widget;
			if (menuItem.getMenu() != null && !WidgetDataUtils.isVisited(menuItem)) {
				for (MenuItem childItem : menuItem.getMenu().getItems()) {
					try {
						buildMenuTree(childItem);
						WidgetDataUtils.setVisited(menuItem, true);
					} catch (JavaModelException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
	}

	private class ItemSelectionListener extends SelectionAdapter {
		@Override
		public void widgetSelected(SelectionEvent event) {
			try {
				TextEditorUtils.replaceSelection(WidgetDataUtils.getPath(event.widget));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
