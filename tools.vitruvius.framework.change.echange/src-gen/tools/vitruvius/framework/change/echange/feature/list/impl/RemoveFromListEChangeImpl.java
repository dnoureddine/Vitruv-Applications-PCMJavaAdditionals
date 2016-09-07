/**
 */
package tools.vitruvius.framework.change.echange.feature.list.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import tools.vitruvius.framework.change.echange.feature.list.ListPackage;
import tools.vitruvius.framework.change.echange.feature.list.RemoveFromListEChange;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Remove From List EChange</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class RemoveFromListEChangeImpl<A extends EObject, F extends EStructuralFeature> extends UpdateSingleListEntryEChangeImpl<A, F> implements RemoveFromListEChange<A, F> {
    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    protected RemoveFromListEChangeImpl() {
		super();
	}

    /**
	 * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
	 * @generated
	 */
    @Override
    protected EClass eStaticClass() {
		return ListPackage.Literals.REMOVE_FROM_LIST_ECHANGE;
	}

} //RemoveFromListEChangeImpl
