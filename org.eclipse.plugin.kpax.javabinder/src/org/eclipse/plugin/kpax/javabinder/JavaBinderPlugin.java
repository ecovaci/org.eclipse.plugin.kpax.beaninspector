package org.eclipse.plugin.kpax.javabinder;

import org.eclipse.core.runtime.Plugin;
import org.eclipse.core.runtime.Status;
import org.osgi.framework.BundleContext;

public class JavaBinderPlugin extends Plugin {

	private static BundleContext context;

	private static JavaBinderPlugin instance;

	public JavaBinderPlugin() {
		instance = this;
	}

	public static JavaBinderPlugin getInstance() {
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
		JavaBinderPlugin.context = bundleContext;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop(BundleContext bundleContext) throws Exception {
		JavaBinderPlugin.context = null;
	}

	public void info(String message) {
		log(message, Status.INFO, null);
	}

	public void warn(String message) {
		log(message, Status.WARNING, null);
	}

	public void error(Exception e) {
		log(e.getMessage(), Status.WARNING, e);
	}
	
	public void error(String message, Exception e) {
		log(message, Status.WARNING, e);
	}

	private void log(String message, int severity, Exception e) {
		getLog().log(new Status(severity, getBundle().getSymbolicName(), Status.OK, message, e));
	}
}
