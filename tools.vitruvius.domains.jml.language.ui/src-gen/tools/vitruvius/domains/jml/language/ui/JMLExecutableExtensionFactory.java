/*
 * generated by Xtext
 */
package tools.vitruvius.domains.jml.language.ui;

import org.eclipse.xtext.ui.guice.AbstractGuiceAwareExecutableExtensionFactory;
import org.osgi.framework.Bundle;

import com.google.inject.Injector;

import tools.vitruvius.domains.jml.language.ui.internal.JMLActivator;

/**
 * This class was generated. Customizations should only happen in a newly
 * introduced subclass. 
 */
public class JMLExecutableExtensionFactory extends AbstractGuiceAwareExecutableExtensionFactory {

	@Override
	protected Bundle getBundle() {
		return JMLActivator.getInstance().getBundle();
	}
	
	@Override
	protected Injector getInjector() {
		return JMLActivator.getInstance().getInjector(JMLActivator.EDU_KIT_IPD_SDQ_VITRUVIUS_DOMAINS_JML_LANGUAGE_JML);
	}
	
}
