/*******************************************************************************
 * Copyright 2015 Eugen Covaci
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package org.eclipse.plugin.kpax.beaninspector.prefs;

import org.apache.commons.lang.Validate;
import org.eclipse.core.runtime.preferences.IEclipsePreferences;
import org.eclipse.core.runtime.preferences.InstanceScope;
import org.eclipse.plugin.kpax.beaninspector.JavaBeanInspectorPlugin;
import org.eclipse.plugin.kpax.beaninspector.logger.Logger;

public class Settings {
	
	private static final String INCLUDE_REGEX_KEY = "includeRegex";
	private static final String SHOW_FULLYQUALIFIED_KEY = "showFullyQualified";

	private final Logger logger = JavaBeanInspectorPlugin.getLogger();

	private static Settings settings;

	private String includeRegex = "";
	private boolean showFullyQualified;

	private Settings() {
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
	
	public boolean isDefault () {
		return !showFullyQualified && "".equals(includeRegex);
	}

	public void setShowFullyQualified(boolean showFullyQualified) {
		this.showFullyQualified = showFullyQualified;
	}

	public void reset() {
		includeRegex = "";
		showFullyQualified = false;
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
		showFullyQualified = preferences.getBoolean(SHOW_FULLYQUALIFIED_KEY, false);
	}
}
