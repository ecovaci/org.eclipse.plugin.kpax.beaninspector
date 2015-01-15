package org.eclipse.plugin.kpax.beaninspector.util;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IClassFile;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaModelException;

public class ClassPathUtils {

	public static Class<?> loadClass(IType type) throws JavaModelException, ClassNotFoundException,
			MalformedURLException {
		StringBuffer path = new StringBuffer("file:///");
		IClassFile classFile = type.getClassFile();
		if (classFile != null) {
			path.append(classFile.getPath().toOSString());
		} else {
			IClasspathEntry[] classpathEntries = type.getJavaProject().getResolvedClasspath(false);
			IClasspathEntry sourceClasspathEntry = null;
			for (IClasspathEntry classpathEntry : classpathEntries) {
				if (classpathEntry.getEntryKind() == IClasspathEntry.CPE_SOURCE
						&& type.getPackageFragment().getParent().getPath()
								.equals(classpathEntry.getPath())) {
					sourceClasspathEntry = classpathEntry;
					break;
				}
			}
			IPath outputPath = sourceClasspathEntry.getOutputLocation();
			if (outputPath == null) {
				outputPath = type.getJavaProject().getOutputLocation();
			}
			path.append(
					ResourcesPlugin.getWorkspace().getRoot().getLocation().toString()
							+ outputPath.toOSString()).append(File.separator);
		}
		URLClassLoader urlClassLoader = null;
		try {
			URL[] classLoaderUrls = new URL[] { new URL(path.toString()) };
			urlClassLoader = new URLClassLoader(classLoaderUrls);
			return urlClassLoader.loadClass(type.getFullyQualifiedName());
		} finally {
			if (urlClassLoader != null) {
				try {
					urlClassLoader.close();
				} catch (Exception ee) {
					//Ignore
				}
			}
		}
	}
}
