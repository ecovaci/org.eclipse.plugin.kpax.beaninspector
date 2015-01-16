package org.eclipse.plugin.kpax.beaninspector.logger;

import org.eclipse.core.runtime.Status;

public interface Logger {

	public abstract void info(String message);

	public abstract void warn(String message);

	public abstract void error(Exception e);

	public abstract void error(String message, Exception e);

}