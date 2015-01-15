package org.eclipse.plugin.kpax.beaninspector;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.plugin.kpax.beaninspector.logger.Logger;
import org.osgi.framework.BundleContext;
import org.osgi.service.prefs.BackingStoreException;

public class JavaBeanInspectorPlugin extends Plugin implements Logger {

	private static final String PLUGIN_ID = "org.eclipse.plugin.kpax.beaninspector";
	
	private static BundleContext context;

	private static JavaBeanInspectorPlugin instance;

	public JavaBeanInspectorPlugin() {
		instance = this;
	}

	public static JavaBeanInspectorPlugin getInstance() {
		return instance;
	}

	public static Logger getLogger() {
		return instance;
	}

	static BundleContext getContext() {
		return context;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext
	 * )
	 */
	public void start(BundleContext bundleContext) throws Exception {
		JavaBeanInspectorPlugin.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		JavaBeanInspectorPlugin.context = null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.plugin.kpax.beaninspector.Logger#info(java.lang.String)
	 */
	@Override
	public void info(String message) {
		log(message, Status.INFO, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.plugin.kpax.beaninspector.Logger#warn(java.lang.String)
	 */
	@Override
	public void warn(String message) {
		log(message, Status.WARNING, null);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.plugin.kpax.beaninspector.Logger#error(java.lang.Exception)
	 */
	@Override
	public void error(Exception e) {
		log(e.getMessage(), Status.WARNING, e);
	}

	public void error(String message, Exception e) {
		log(message, Status.WARNING, e);
	}

	private void log(String message, int severity, Exception e) {
		getLog().log(new Status(severity, PLUGIN_ID, Status.OK, message, e));
	}

	private void savePluginSettings() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(PLUGIN_ID);

		preferences.put("KEY1", "VAL1");
		preferences.put("KEY2", "VAL2");

		try {
			preferences.flush();
		} catch (BackingStoreException e) {
			error("Error on saving preferences", e);
		}
	}

	private void loadPluginSettings() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE.getNode(PLUGIN_ID);
		// you might want to call prefs.sync() if you're worried about others changing your settings
		//this.someStr = prefs.get(KEY1);
		//this.someBool= prefs.getBoolean(KEY2);
	}
}
