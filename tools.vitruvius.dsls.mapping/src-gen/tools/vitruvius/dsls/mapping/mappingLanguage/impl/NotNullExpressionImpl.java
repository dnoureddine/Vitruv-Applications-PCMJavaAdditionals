/**
 * generated by Xtext 2.9.2
 */
package tools.vitruvius.dsls.mapping.mappingLanguage.impl;

import tools.vitruvius.dsls.mapping.mappingLanguage.FeatureOfContextVariable;
import tools.vitruvius.dsls.mapping.mappingLanguage.MappingLanguagePackage;
import tools.vitruvius.dsls.mapping.mappingLanguage.NotNullExpression;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Not Null Expression</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.vitruvius.dsls.mapping.mappingLanguage.impl.NotNullExpressionImpl#getNotNullable <em>Not Nullable</em>}</li>
 * </ul>
 *
 * @generated
 */
public class NotNullExpressionImpl extends ConstraintExpressionImpl implements NotNullExpression
{
  /**
   * The cached value of the '{@link #getNotNullable() <em>Not Nullable</em>}' containment reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getNotNullable()
   * @generated
   * @ordered
   */
  protected FeatureOfContextVariable notNullable;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected NotNullExpressionImpl()
  {
    super();
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  protected EClass eStaticClass()
  {
    return MappingLanguagePackage.Literals.NOT_NULL_EXPRESSION;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public FeatureOfContextVariable getNotNullable()
  {
    return notNullable;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public NotificationChain basicSetNotNullable(FeatureOfContextVariable newNotNullable, NotificationChain msgs)
  {
    FeatureOfContextVariable oldNotNullable = notNullable;
    notNullable = newNotNullable;
    if (eNotificationRequired())
    {
      ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, MappingLanguagePackage.NOT_NULL_EXPRESSION__NOT_NULLABLE, oldNotNullable, newNotNullable);
      if (msgs == null) msgs = notification; else msgs.add(notification);
    }
    return msgs;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setNotNullable(FeatureOfContextVariable newNotNullable)
  {
    if (newNotNullable != notNullable)
    {
      NotificationChain msgs = null;
      if (notNullable != null)
        msgs = ((InternalEObject)notNullable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - MappingLanguagePackage.NOT_NULL_EXPRESSION__NOT_NULLABLE, null, msgs);
      if (newNotNullable != null)
        msgs = ((InternalEObject)newNotNullable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - MappingLanguagePackage.NOT_NULL_EXPRESSION__NOT_NULLABLE, null, msgs);
      msgs = basicSetNotNullable(newNotNullable, msgs);
      if (msgs != null) msgs.dispatch();
    }
    else if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MappingLanguagePackage.NOT_NULL_EXPRESSION__NOT_NULLABLE, newNotNullable, newNotNullable));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs)
  {
    switch (featureID)
    {
      case MappingLanguagePackage.NOT_NULL_EXPRESSION__NOT_NULLABLE:
        return basicSetNotNullable(null, msgs);
    }
    return super.eInverseRemove(otherEnd, featureID, msgs);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public Object eGet(int featureID, boolean resolve, boolean coreType)
  {
    switch (featureID)
    {
      case MappingLanguagePackage.NOT_NULL_EXPRESSION__NOT_NULLABLE:
        return getNotNullable();
    }
    return super.eGet(featureID, resolve, coreType);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eSet(int featureID, Object newValue)
  {
    switch (featureID)
    {
      case MappingLanguagePackage.NOT_NULL_EXPRESSION__NOT_NULLABLE:
        setNotNullable((FeatureOfContextVariable)newValue);
        return;
    }
    super.eSet(featureID, newValue);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public void eUnset(int featureID)
  {
    switch (featureID)
    {
      case MappingLanguagePackage.NOT_NULL_EXPRESSION__NOT_NULLABLE:
        setNotNullable((FeatureOfContextVariable)null);
        return;
    }
    super.eUnset(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public boolean eIsSet(int featureID)
  {
    switch (featureID)
    {
      case MappingLanguagePackage.NOT_NULL_EXPRESSION__NOT_NULLABLE:
        return notNullable != null;
    }
    return super.eIsSet(featureID);
  }

} //NotNullExpressionImpl
