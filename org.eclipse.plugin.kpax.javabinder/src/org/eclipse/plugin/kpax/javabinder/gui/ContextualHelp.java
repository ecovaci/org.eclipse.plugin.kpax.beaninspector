package org.eclipse.plugin.kpax.javabinder.gui;

import java.util.Collection;

import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.plugin.kpax.javabinder.util.BeanIntrospector;
import org.eclipse.plugin.kpax.javabinder.util.BeanProperty;
import org.eclipse.plugin.kpax.javabinder.util.Editor;
import org.eclipse.plugin.kpax.javabinder.util.WidgetUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class ContextualHelp {

	private Shell shell;

	public ContextualHelp(Shell shell) {
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
			//TODO show BindingDialog
		}
	}

	private Menu buildMenuTree(MenuItem item) throws JavaModelException {
		IType beanType = WidgetUtils.getType(item, BindingDialog
				.getBeanType());
		if (beanType == null) {
			return null;//TODO Refactoring
		}
		
		Collection<BeanProperty> properties = new BeanIntrospector(beanType).getProperties();
		if (!properties.isEmpty()) {

			Menu menu;
			if (item != null) {
				menu = new Menu(shell, SWT.DROP_DOWN);
				item.setMenu(menu);
			} else {
				menu = new Menu(shell);
			}
			for (BeanProperty property : properties) {
				MenuItem menuItem0 = new MenuItem(menu, SWT.CASCADE);
				menuItem0.addSelectionListener(new ItemSelectionListener());
				fillMenuItem(menuItem0, item, property);
				menuItem0.addListener(SWT.Arm, new Listener() {

					@Override
					public void handleEvent(Event event) {
						MenuItem source = (MenuItem) event.widget;
						if (source.getMenu() != null) {
							for (MenuItem childItem : source.getMenu().getItems()) {
								try {
									buildMenuTree(childItem);
								} catch (JavaModelException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						}
						System.out.println("booo");
					}
				});
			}
			return menu;
		}
		return null;
	}

	private void fillMenuItem(MenuItem item, MenuItem parentItem, BeanProperty property) {
		WidgetUtils.putProperty(item, property);
		WidgetUtils.putPath(item, parentItem, property);
		item.setText(property.asText());
	}

	private class ItemSelectionListener implements SelectionListener {

		@Override
		public void widgetSelected(SelectionEvent event) {
			System.out.println("selected");
			Editor editor = new Editor();
			editor.parse();
			try {
				editor.replaceSelection(WidgetUtils.getPath(event.widget));
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			
		}
		
	}
}
