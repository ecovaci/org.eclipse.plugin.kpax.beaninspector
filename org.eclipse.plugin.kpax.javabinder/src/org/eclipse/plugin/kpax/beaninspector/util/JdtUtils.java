package org.eclipse.plugin.kpax.beaninspector.util;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.search.IJavaSearchScope;
import org.eclipse.jdt.core.search.SearchEngine;
import org.eclipse.jdt.ui.IJavaElementSearchConstants;
import org.eclipse.jdt.ui.JavaUI;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.window.Window;
import org.eclipse.plugin.kpax.beaninspector.Messages;
import org.eclipse.plugin.kpax.beaninspector.introspector.BeanIntrospector;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.dialogs.SelectionDialog;

public class JdtUtils {

	public static IType chooseType(Shell shell, IType currentBeanType) throws CoreException {
		List<IJavaProject> javaProjects = new ArrayList<IJavaProject>();
		IProject[] projects = ResourcesPlugin.getWorkspace().getRoot().getProjects();
		for (IProject project : projects) {
			if (project.isOpen() && project.isNatureEnabled("org.eclipse.jdt.core.javanature")) {
				javaProjects.add(JavaCore.create(project));
			}
		}
		IJavaElement[] elements = javaProjects.toArray(new IJavaElement[projects.length]);
		IJavaSearchScope scope = SearchEngine.createJavaSearchScope(elements);
		SelectionDialog dialog = JavaUI.createTypeDialog(shell, PlatformUI.getWorkbench()
				.getProgressService(), scope,
				IJavaElementSearchConstants.CONSIDER_CLASSES_AND_INTERFACES, false,
				currentBeanType != null ? currentBeanType.getFullyQualifiedName() : "");
		dialog.setTitle(Messages.OpenTypeAction_dialogTitle);
		dialog.setMessage(Messages.OpenTypeAction_dialogMessage);
		if (dialog.open() == Window.OK) {
			Object[] resultArray = dialog.getResult();
			if (resultArray != null && resultArray.length > 0) {
				IType type = (IType) resultArray[0];
				if (new BeanIntrospector(type).hasProperty()) {
					return (IType) type;
				} else {
					boolean confirm = MessageDialog.openConfirm(shell,
							Messages.ContextualBeanInspector_messageTitle,
							Messages.ContextualBeanInspector_info_no_property);
					if (confirm) {
						return chooseType(shell, currentBeanType);
					}
				}
			}
		}
		return null;
	}

}
