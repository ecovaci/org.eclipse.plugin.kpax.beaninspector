package org.eclipse.plugin.kpax.javabinder.model;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MainClass {

  public static void main(String[] a) {
    Display d = new Display();
    Shell s = new Shell(d);

    Menu m = new Menu(s);
    
    MenuItem radio = new MenuItem(m, SWT.CASCADE); /* bar is the menu bar */
    radio.setText("Radio");
    
    radio.addSelectionListener(new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			System.out.println("vvvvvvv");
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			System.out.println("cccccccc");
		}
	});

    Menu menu = new Menu(radio);
    radio.setMenu(menu);

    MenuItem mntmOption_1 = new MenuItem(menu, SWT.RADIO);
    mntmOption_1.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            System.out.println("Option 1 selected");
        }
    });
    mntmOption_1.setText("Option1");

    MenuItem mntmOption_2 = new MenuItem(menu, SWT.RADIO);
    mntmOption_2.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
            System.out.println("Option 2 selected");
        }
    });
    mntmOption_2.setText("Option2");
    
    
    // create a file menu and add an exit item
    final MenuItem file = new MenuItem(m, SWT.CASCADE);
    file.setText("File");
    final MenuItem file2 = new MenuItem(m, SWT.CASCADE);
    file2.setText("File2");
    
    final Menu filemenu = new Menu(s, SWT.DROP_DOWN);
    filemenu.addMenuListener(new MenuListener() {
		
		@Override
		public void menuShown(MenuEvent e) {
			// TODO Auto-generated method stub
			System.out.println("cccccc");
		}
		
		@Override
		public void menuHidden(MenuEvent e) {
			// TODO Auto-generated method stub
			System.out.println("vvvvv");
		}
	});
    file.setMenu(filemenu);
    
    // create an open menu and to sub-menu items
    final MenuItem openItem = new MenuItem(filemenu, SWT.CASCADE);
    openItem.setText("Open");
    final Menu submenu = new Menu(s, SWT.DROP_DOWN);
    openItem.setMenu(submenu);
    final MenuItem childItem = new MenuItem(submenu, SWT.PUSH);
    childItem.setText("Child");
    final MenuItem dialogItem = new MenuItem(submenu, SWT.PUSH);
    dialogItem.setText("Dialog");
    // add a separator
    final MenuItem separator = new MenuItem(filemenu, SWT.SEPARATOR);
    // create an exit menu item
    final MenuItem exitItem = new MenuItem(filemenu, SWT.PUSH);
    exitItem.setText("Exit");

    s.setMenu(m);

    s.open();
    while (!s.isDisposed()) {
      if (!d.readAndDispatch())
        d.sleep();
    }
    d.dispose();
  }
}
