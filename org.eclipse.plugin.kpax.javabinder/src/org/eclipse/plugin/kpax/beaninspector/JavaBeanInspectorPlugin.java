package org.eclipse.plugin.kpax.beaninspector;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.eclipse.plugin.kpax.beaninspector.logger.Logger;
import org.osgi.framework.BundleContext;

public class JavaBeanInspectorPlugin extends Plugin implements Logger {

	public static final String PLUGIN_ID = "org.eclipse.plugin.kpax.beaninspector";
	
	private static BundleContext context;

	private static Logger instance;

	public JavaBeanInspectorPlugin() {
		instance = this;
	}

	public static Logger getInstance() {
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

	@Override
	public void error(String message, Exception e) {
		log(message, Status.WARNING, e);
	}

	private void log(String message, int severity, Exception e) {
		getLog().log(new Status(severity, PLUGIN_ID, Status.OK, message, e));
	}


}
