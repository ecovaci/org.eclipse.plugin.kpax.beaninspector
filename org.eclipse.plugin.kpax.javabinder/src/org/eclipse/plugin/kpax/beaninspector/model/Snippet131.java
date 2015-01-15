package org.eclipse.plugin.kpax.beaninspector.model;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class Snippet131 {
	public static void main(String[] args) {
		final Display display = new Display();
		final Shell shell = new Shell(display);

		shell.addListener(SWT.MenuDetect, new Listener() {
			public void handleEvent(Event event) {
				final Menu menu = new Menu(shell, SWT.POP_UP);
				
				for (int i = 0; i < 10; i++) {
					final MenuItem item = new MenuItem(menu, SWT.PUSH);
					item.setText("Menu Item " + i);
					item.addListener(SWT.Arm, new Listener() {

						@Override
						public void handleEvent(Event event) {
							// TODO Auto-generated method stub
							System.out.println("booo");
						}
					});
					
					item.addListener(SWT.Selection, new Listener() {

						@Override
						public void handleEvent(Event event) {
							System.out.println("selected");
						}
					});
				}
				/*
				 * item.addListener(SWT.MouseHover, new Listener() { public void
				 * handleEvent(Event e) { System.out.println("Item Selected"); }
				 * });
				 */
				menu.setLocation(event.x, event.y);
				menu.setVisible(true);
				while (!menu.isDisposed() && menu.isVisible()) {
					if (!display.readAndDispatch())
						display.sleep();
				}
				menu.dispose();
			}
		});
		shell.pack();
		shell.open();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
}