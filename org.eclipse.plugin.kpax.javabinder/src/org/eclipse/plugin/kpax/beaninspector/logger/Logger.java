package org.eclipse.plugin.kpax.beaninspector.logger;

public interface Logger {

	public abstract void info(String message);

	public abstract void warn(String message);

	public abstract void error(Exception e);

}