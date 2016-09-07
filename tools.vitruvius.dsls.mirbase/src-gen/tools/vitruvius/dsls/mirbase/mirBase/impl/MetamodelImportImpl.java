/**
 * generated by Xtext 2.9.2
 */
package tools.vitruvius.dsls.mirbase.mirBase.impl;

import tools.vitruvius.dsls.mirbase.mirBase.MetamodelImport;
import tools.vitruvius.dsls.mirbase.mirBase.MirBasePackage;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Metamodel Import</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link tools.vitruvius.dsls.mirbase.mirBase.impl.MetamodelImportImpl#getPackage <em>Package</em>}</li>
 *   <li>{@link tools.vitruvius.dsls.mirbase.mirBase.impl.MetamodelImportImpl#getName <em>Name</em>}</li>
 *   <li>{@link tools.vitruvius.dsls.mirbase.mirBase.impl.MetamodelImportImpl#isUseSimpleNames <em>Use Simple Names</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MetamodelImportImpl extends MinimalEObjectImpl.Container implements MetamodelImport
{
  /**
   * The cached value of the '{@link #getPackage() <em>Package</em>}' reference.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getPackage()
   * @generated
   * @ordered
   */
  protected EPackage package_;

  /**
   * The default value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected static final String NAME_EDEFAULT = null;

  /**
   * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #getName()
   * @generated
   * @ordered
   */
  protected String name = NAME_EDEFAULT;

  /**
   * The default value of the '{@link #isUseSimpleNames() <em>Use Simple Names</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUseSimpleNames()
   * @generated
   * @ordered
   */
  protected static final boolean USE_SIMPLE_NAMES_EDEFAULT = false;

  /**
   * The cached value of the '{@link #isUseSimpleNames() <em>Use Simple Names</em>}' attribute.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see #isUseSimpleNames()
   * @generated
   * @ordered
   */
  protected boolean useSimpleNames = USE_SIMPLE_NAMES_EDEFAULT;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected MetamodelImportImpl()
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
    return MirBasePackage.Literals.METAMODEL_IMPORT;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EPackage getPackage()
  {
    if (package_ != null && package_.eIsProxy())
    {
      InternalEObject oldPackage = (InternalEObject)package_;
      package_ = (EPackage)eResolveProxy(oldPackage);
      if (package_ != oldPackage)
      {
        if (eNotificationRequired())
          eNotify(new ENotificationImpl(this, Notification.RESOLVE, MirBasePackage.METAMODEL_IMPORT__PACKAGE, oldPackage, package_));
      }
    }
    return package_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public EPackage basicGetPackage()
  {
    return package_;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setPackage(EPackage newPackage)
  {
    EPackage oldPackage = package_;
    package_ = newPackage;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MirBasePackage.METAMODEL_IMPORT__PACKAGE, oldPackage, package_));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public String getName()
  {
    return name;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setName(String newName)
  {
    String oldName = name;
    name = newName;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MirBasePackage.METAMODEL_IMPORT__NAME, oldName, name));
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public boolean isUseSimpleNames()
  {
    return useSimpleNames;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public void setUseSimpleNames(boolean newUseSimpleNames)
  {
    boolean oldUseSimpleNames = useSimpleNames;
    useSimpleNames = newUseSimpleNames;
    if (eNotificationRequired())
      eNotify(new ENotificationImpl(this, Notification.SET, MirBasePackage.METAMODEL_IMPORT__USE_SIMPLE_NAMES, oldUseSimpleNames, useSimpleNames));
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
      case MirBasePackage.METAMODEL_IMPORT__PACKAGE:
        if (resolve) return getPackage();
        return basicGetPackage();
      case MirBasePackage.METAMODEL_IMPORT__NAME:
        return getName();
      case MirBasePackage.METAMODEL_IMPORT__USE_SIMPLE_NAMES:
        return isUseSimpleNames();
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
      case MirBasePackage.METAMODEL_IMPORT__PACKAGE:
        setPackage((EPackage)newValue);
        return;
      case MirBasePackage.METAMODEL_IMPORT__NAME:
        setName((String)newValue);
        return;
      case MirBasePackage.METAMODEL_IMPORT__USE_SIMPLE_NAMES:
        setUseSimpleNames((Boolean)newValue);
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
      case MirBasePackage.METAMODEL_IMPORT__PACKAGE:
        setPackage((EPackage)null);
        return;
      case MirBasePackage.METAMODEL_IMPORT__NAME:
        setName(NAME_EDEFAULT);
        return;
      case MirBasePackage.METAMODEL_IMPORT__USE_SIMPLE_NAMES:
        setUseSimpleNames(USE_SIMPLE_NAMES_EDEFAULT);
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
      case MirBasePackage.METAMODEL_IMPORT__PACKAGE:
        return package_ != null;
      case MirBasePackage.METAMODEL_IMPORT__NAME:
        return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
      case MirBasePackage.METAMODEL_IMPORT__USE_SIMPLE_NAMES:
        return useSimpleNames != USE_SIMPLE_NAMES_EDEFAULT;
    }
    return super.eIsSet(featureID);
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  @Override
  public String toString()
  {
    if (eIsProxy()) return super.toString();

    StringBuffer result = new StringBuffer(super.toString());
    result.append(" (name: ");
    result.append(name);
    result.append(", useSimpleNames: ");
    result.append(useSimpleNames);
    result.append(')');
    return result.toString();
  }

} //MetamodelImportImpl
