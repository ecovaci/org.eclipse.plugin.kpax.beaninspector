package org.eclipse.plugin.kpax.javabinder.model;
import java.lang.reflect.Field;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MenuDetectEvent;
import org.eclipse.swt.events.MenuDetectListener;
import org.eclipse.swt.events.MenuEvent;
import org.eclipse.swt.events.MenuListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;

public class MenuTest
{
    private static Point point;

    public static void main( String[] args )
    {
        Display display = new Display( );
        final Shell shell = new Shell( display );
        shell.setLayout( new GridLayout( 1, false ) );

        final Menu menu = new Menu( shell );
        MenuItem item = new MenuItem( menu, SWT.CHECK );
        item.setText( "Check 1" );
        item.addSelectionListener( new SelectionAdapter( )
        {
            public void widgetSelected( final SelectionEvent e )
            {
                if ( point == null ) return;

                Display.getDefault( ).asyncExec( new Runnable( )
                {
                    @Override
                    public void run( )
                    {
                        menu.setLocation( point );
                        menu.setVisible( true );
                    }
                } );
            }
        } );

        shell.addMenuDetectListener( new MenuDetectListener( )
        {
            public void menuDetected( MenuDetectEvent e )
            {
                point = new Point( e.x, e.y );
            }
        } );

        menu.addMenuListener( new MenuListener( )
        {
            @Override
            public void menuHidden( MenuEvent event )
            {
                try
                {
                    Field field = Menu.class.getDeclaredField( "hasLocation" );
                    field.setAccessible( true );
                    field.set( menu, false );
                }
                catch ( Exception e )
                {
                    e.printStackTrace();
                }
            }

            @Override
            public void menuShown( MenuEvent event )
            {
            }
        });

        shell.setMenu( menu );

        shell.open( );
        while ( !shell.isDisposed( ) )
        {
            if ( !display.readAndDispatch( ) ) display.sleep( );
        }
        display.dispose( );
    }
}