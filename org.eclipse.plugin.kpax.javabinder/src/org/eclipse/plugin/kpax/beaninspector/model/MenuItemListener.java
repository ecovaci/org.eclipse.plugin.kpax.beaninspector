package org.eclipse.plugin.kpax.beaninspector.model;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.Clipboard;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;

public class MenuItemListener {

  public static void main(String[] args) {
    Display display = new Display();
    final Clipboard cb = new Clipboard(display);
    Shell shell = new Shell(display);
    shell.setLayout(new FillLayout());
    final Text text = new Text(shell, SWT.BORDER | SWT.MULTI | SWT.WRAP);
    Menu menu = new Menu(shell, SWT.POP_UP);
    final MenuItem copyItem = new MenuItem(menu, SWT.PUSH);
    
    copyItem.setText("Copy");
    copyItem.addSelectionListener(new SelectionAdapter() {
      public void widgetSelected(SelectionEvent e) {
        System.out.println("copy");
      }
    });
    
    text.setMenu(menu);

    shell.setSize(200, 200);
    shell.open();
    while (!shell.isDisposed()) {
      if (!display.readAndDispatch())
        display.sleep();
    }
    cb.dispose();
    display.dispose();
  }
}