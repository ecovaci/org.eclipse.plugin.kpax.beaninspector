package org.eclipse.plugin.kpax.beaninspector.prefs;

import org.apache.commons.lang.Validate;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.plugin.kpax.beaninspector.JavaBeanInspectorPlugin;
import org.eclipse.plugin.kpax.beaninspector.logger.Logger;
import org.osgi.service.prefs.BackingStoreException;

public class Settings {
	
	private static final String INCLUDE_REGEX_KEY = "includeRegex";
	private static final String SHOW_FULLYQUALIFIED_KEY = "showFullyQualified";

	private final Logger logger = JavaBeanInspectorPlugin.getLogger();

	private static Settings settings;

	private String includeRegex = "";
	private boolean showFullyQualified = true;

	private Settings() {
		// TODO Auto-generated constructor stub
	}

	public String getIncludeRegex() {
		return includeRegex;
	}

	public void setIncludeRegex(String includeRegex) {
		this.includeRegex = includeRegex;
	}

	public boolean isShowFullyQualified() {
		return showFullyQualified;
	}

	public void setShowFullyQualified(boolean showFullyQualified) {
		this.showFullyQualified = showFullyQualified;
	}

	public void reset() {
		includeRegex = "";
		showFullyQualified = true;
	}

	public static Settings getSettings() {
		if (settings == null) {
			(settings = new Settings()).loadPluginSettings();
		}
		return settings;
	}

	public void saveSettings() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE
				.getNode(JavaBeanInspectorPlugin.PLUGIN_ID);
		Validate.notNull(preferences, "Eclipse preferences not found");
		preferences.put(INCLUDE_REGEX_KEY, includeRegex);
		preferences.putBoolean(SHOW_FULLYQUALIFIED_KEY, isShowFullyQualified());

		try {
			preferences.flush();
		} catch (Exception e) {
			logger.error("Error on saving preferences", e);
		}
	}

	private void loadPluginSettings() {
		IEclipsePreferences preferences = InstanceScope.INSTANCE
				.getNode(JavaBeanInspectorPlugin.PLUGIN_ID);
		Validate.notNull(preferences, "Eclipse preferences not found");
		/*try {
			preferences.sync();
		} catch (BackingStoreException e) {
			logger.error("Error on refreshing preferences", e);
		}*/
		includeRegex = preferences.get(INCLUDE_REGEX_KEY, "");
		showFullyQualified = preferences.getBoolean(SHOW_FULLYQUALIFIED_KEY, true);
	}
}
