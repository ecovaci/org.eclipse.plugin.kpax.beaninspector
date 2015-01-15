package org.eclipse.plugin.kpax.beaninspector.gui;

import java.util.Collection;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.plugin.kpax.beaninspector.bean.BeanIntrospector;
import org.eclipse.plugin.kpax.beaninspector.bean.BeanProperty;
import org.eclipse.plugin.kpax.beaninspector.util.Editor;
import org.eclipse.plugin.kpax.beaninspector.util.Glyphs;
import org.eclipse.plugin.kpax.beaninspector.util.WidgetUtils;
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
			}
		} else {
			MessageDialog
					.openInformation(
							this.shell,
							"JavaBean Inspector Message",
							"First, you have to select a Java type. Use the toolbar button for opening the JavaBean Inspector popup dialog (or press Ctrl+Alt+0)");
		}
	}

	private Menu buildMenuTree(MenuItem item) throws JavaModelException {
		IType beanType = WidgetUtils.getType(item, BindingDialog.getBeanType());
		if (beanType != null) {
			BeanIntrospector beanIntrospector = new BeanIntrospector(beanType);
			Collection<BeanProperty> properties = beanIntrospector.getProperties();
			if (!properties.isEmpty()) {
				Menu menu;
				if (item != null) {
					menu = new Menu(shell, SWT.DROP_DOWN);
					item.setMenu(menu);
					WidgetUtils.addGlyphOnLeft(item, Glyphs.ARROW_RIGHT);
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
		WidgetUtils.setProperty(item, property);
		WidgetUtils.setPath(item, parentItem, property);
		item.setText(property.asText());
		item.addSelectionListener(new ItemSelectionListener());
	}

	private MenuItem createClassMenuItem(MenuItem item) {
		MenuItem classMenuItem = new MenuItem(item.getMenu(), SWT.PUSH);
		WidgetUtils.setPath(classMenuItem, WidgetUtils.getPath(item));
		classMenuItem.setText(Glyphs.glyphOnRight(WidgetUtils.getBeanProperty(item)
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
			if (menuItem.getMenu() != null && !WidgetUtils.isVisited(menuItem)) {
				for (MenuItem childItem : menuItem.getMenu().getItems()) {
					try {
						buildMenuTree(childItem);
						WidgetUtils.setVisited(menuItem, true);
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
				Editor.replaceSelection(WidgetUtils.getPath(event.widget));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
