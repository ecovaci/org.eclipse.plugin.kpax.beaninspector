package org.eclipse.plugin.kpax.beaninspector.model;
import org.eclipse.swt.*;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.widgets.*;

public class Snippet238 {

public static void main(String[] args) {
	Display display = new Display ();
	Shell shell = new Shell (display);
	Composite composite = new Composite (shell, SWT.BORDER);
	Rectangle clientArea = shell.getClientArea();
	composite.setBounds (clientArea.x, clientArea.y, 100, 100);
	Menu menu = new Menu (shell, SWT.POP_UP);
	
	MenuItem item1 = new MenuItem (menu, SWT.RADIO);
	item1.setText ("Push Item");
	MenuItem item2 = new MenuItem (menu, SWT.CASCADE);
	//item2.setText ( "\u21b3 field - Type ");
	item2.setText ( "\u25B8 field - Type ");
	item2.addSelectionListener(new SelectionListener() {
		
		@Override
		public void widgetSelected(SelectionEvent e) {
			// TODO Auto-generated method stub
			System.out.println("Cascade Item selected");
		}
		
		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			System.out.println("Cascade Item default selected");
			// TODO Auto-generated method stub
			
		}
	});
	Menu subMenu = new Menu (menu);
	subMenu.addListener(SWT.Selection, new Listener() {
		
		@Override
		public void handleEvent(Event event) {
			System.out.println("XXXX");
			
		}
	});
	item2.setMenu (subMenu);
	
	MenuItem subItem0 = new MenuItem (subMenu, SWT.PUSH);
	subItem0.setText ("\u25BE Type");
	
	MenuItem subItemSep = new MenuItem (subMenu, SWT.SEPARATOR);
	subItemSep.setText("");
	
	MenuItem subItem1 = new MenuItem (subMenu, SWT.PUSH);
	//subItem1.setText ("\u2022 Subitem 1");
	subItem1.setText ("\u2022 Subitem 1");
	MenuItem subItem2 = new MenuItem (subMenu, SWT.PUSH);
	subItem2.setText ("\u2022 Subitem 2");
	composite.setMenu (menu);
	shell.setMenu (menu);
	shell.setSize (300, 300);
	shell.open ();
	while (!shell.isDisposed ()) {
		if (!display.readAndDispatch ()) display.sleep ();
	}
	display.dispose ();
}
}