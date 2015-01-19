package edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.transformations.java2pcm

import de.uka.ipd.sdq.pcm.core.entity.InterfaceProvidingRequiringEntity
import de.uka.ipd.sdq.pcm.repository.RepositoryComponent
import de.uka.ipd.sdq.pcm.repository.RepositoryFactory
import de.uka.ipd.sdq.pcm.system.SystemFactory
import edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.PCMJaMoPPNamespace
import edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.transformations.pcm2java.PCM2JaMoPPUtils
import edu.kit.ipd.sdq.vitruvius.framework.contracts.datatypes.UserInteractionType
import edu.kit.ipd.sdq.vitruvius.framework.model.monitor.userinteractor.UserInteractor
import edu.kit.ipd.sdq.vitruvius.framework.run.transformationexecuter.EmptyEObjectMappingTransformation
import edu.kit.ipd.sdq.vitruvius.framework.run.transformationexecuter.TransformationUtils
import java.util.ArrayList
import org.apache.log4j.Logger
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.eclipse.emf.ecore.util.EcoreUtil
import org.emftext.language.java.classifiers.Class
import org.emftext.language.java.classifiers.Classifier
import org.emftext.language.java.classifiers.ConcreteClassifier
import org.emftext.language.java.modifiers.Modifier
import org.emftext.language.java.modifiers.Public
import org.emftext.language.java.modifiers.impl.PublicImpl
import edu.kit.ipd.sdq.vitruvius.framework.meta.change.feature.reference.containment.ContainmentFactory
import org.emftext.language.java.containers.ContainersFactory

/**
 * Maps a JaMoPP class to a PCM Components or System. 
 * Triggered when a CUD operation on JaMoPP class is detected.
 */
class ClassMappingTransformation extends EmptyEObjectMappingTransformation {

	//for component/system decision 
	val public static int SELECT_CREATE_BASIC_COMPONENT = 0
	val public static int SELECT_CREATE_SYSTEM = 1
	val public static int SELECT_CREATE_COMPOSITE_COMPONENT = 2
	val public static int SELECT_NO_CORRESPONDENCE = 3

	//for data type decision
	val public static int SELECT_CREATE_COMPOSITE_DATA_TYPE = 0
	val public static int SELECT_CREATE_COLLECTION_DATA_TYPE = 1
	val public static int SELECT_DO_NOT_CREATE_DATA_TYPE = 2

	private static val Logger logger = Logger.getLogger(ClassMappingTransformation.simpleName)

	override getClassOfMappedEObject() {
		return Class
	}

	/**
	 * sets the name correspondece for JaMoPP-class names and PCM-entityName Attribut
	 */
	override setCorrespondenceForFeatures() {
		JaMoPP2PCMUtils.addName2EntityNameCorrespondence(this.featureCorrespondenceMap)
	}

	/**
	 * If a java class has been added we have to check whether this is a class that implements a 
	 * component or a system or a data type
	 * This is checked as follows:
	 * The class does not represents the implementing class if:
	 * 		i) the class is not public, or
	 * 		ii) the component or system corresponding to the package of the class already has an implementing class
	 * The class represents the implementing class, if
	 * 		iii) the class is public, and
	 * 		iv) the component or system corresponding to the package of the class does not has an implementing class yet, and
	 * 		v) a) the class name contains the name of the package,or 
	 * 		v) b) the user says it is the implementing class
	 * The class represents a datatype, if
	 *      vi) it is in the datatypes package
	 */
	override createEObject(EObject eObject) {
		val jaMoPPClass = eObject as Class

		if (!classIsPublic(jaMoPPClass)) {
			return null;
		}

		//ii) + iv)
		val jaMoPPPackage = PCM2JaMoPPUtils.
			getContainingPackageFromCorrespondenceInstance(jaMoPPClass, correspondenceInstance)
		var InterfaceProvidingRequiringEntity pcmComponentOrSystem = null
		if (null != jaMoPPPackage) {
			if (jaMoPPPackage.name.equals("datatypes")) {

				//vi) class is in the datatypes packge --> create a new datatype
				return createDatatype(jaMoPPClass)
			}

			// get corresponding component or system (ask for InterfaceProvidingRequiringEntity cause components 
			// and systems are both InterfaceProvidingRequiringEntitys) 
			pcmComponentOrSystem = correspondenceInstance.
				claimUniqueCorrespondingEObjectByType(jaMoPPPackage, InterfaceProvidingRequiringEntity)
			if (alreadyHasClassCorrespondence(pcmComponentOrSystem)) {
				return null
			}
		}

		if (null == pcmComponentOrSystem) {
			pcmComponentOrSystem = askUserWhetherToCreateComponentOrSystem(jaMoPPClass)
			if(null == pcmComponentOrSystem){
				return null
			}else{
				return pcmComponentOrSystem.toArray
			}
		} else {

			//iii) and iv) --> already checked above 
			//--> the component corresponding to the package of the class does not have a correspoding pcmComponentOrSystem yet
			//v) a)
			if (jaMoPPClass.name.contains(pcmComponentOrSystem.entityName)) {

				//the class is the implementing class
				return pcmComponentOrSystem.toArray
			} else {
				if (super.modalTextYesNoUserInteracting(
					"Should the new public class '" + jaMoPPClass.name + "' be the implementing class for " +
						pcmComponentOrSystem.entityName)) {
					return pcmComponentOrSystem.toArray
				}
			}
		}
		return null
	}
	
	def getJaMoPPPackage(Class jaMoPPClass) {
		val jaMoPPPackage = ContainersFactory.eINSTANCE.createPackage
		jaMoPPPackage.namespaces.addAll(jaMoPPClass.containingPackageName)
		return jaMoPPPackage
	}

	/**
	 * checks whether the component or system already has a corresponding class
	 * if yes, no correspondence class ill be created
	 */
	def boolean alreadyHasClassCorrespondence(InterfaceProvidingRequiringEntity pcmComponentOrSystem) {
		var correspondencesForPCMCompOrSystem = correspondenceInstance.getAllCorrespondingEObjects(pcmComponentOrSystem)
		val hasClassCorrespondence = correspondencesForPCMCompOrSystem.filter[correspondence|
			correspondence instanceof Class].size
		return 0 < hasClassCorrespondence
	}

	/**
	 * called when a child object (e.g. a Method or a field) is added to the class
	 * Creates the correspondences and returns the TransformationChangeResult object containing the PCM element that should be saved
	 */
	override createNonRootEObjectInList(EObject newAffectedEObject, EObject oldAffectedEObject, EReference affectedReference, EObject newValue,
		int index, EObject[] newCorrespondingEObjects) {
		logger.info("createNonRootEObjectInList called")

		//TODO: implement code here
		return null
	}

	/**
	 * Remove class: 
	 * Check if class has corresponding elements and
	 * Remove CorrespondingInstance for class and compilation unit
	 * Also removes basicComponent on PCM.
	 * If the class is not the only class in the package ask the user whether to remove the component
	 * and package and all classes 
	 */
	override removeEObject(EObject eObject) {
		val jaMoPPClass = eObject as Class
		val correspondences = correspondenceInstance.getAllCorrespondences(jaMoPPClass);
		var eObjectsToDelete = new ArrayList<EObject>()
		if (null != correspondences && 0 < correspondences.size) {
			val classifiersInSamePackage = jaMoPPClass.containingCompilationUnit.classifiersInSamePackage
			if (null != classifiersInSamePackage && 1 < classifiersInSamePackage.size) {

				//TODO: ask user whether to remove also this classifiers
				var boolean removeAllClassifiers = false;
				if (removeAllClassifiers) {
					eObjectsToDelete.addAll(classifiersInSamePackage)
				}
				eObjectsToDelete.add(jaMoPPClass.containingCompilationUnit)
				correspondences.forEach[correspondingObj|EcoreUtil.remove(correspondingObj)]
				eObjectsToDelete.addAll(correspondences)
			}
			correspondenceInstance.removeAllCorrespondences(jaMoPPClass)
		}
		return null
	}

	/**
	 * we do not really need the method deleteNonRootEObjectInList in InterfaceMappingTransformation because the deletion of the 
	 * object has already be done in removeEObject.
	 * We just return an empty TransformationChangeResult 
	 */
	override deleteNonRootEObjectInList(EObject newAffectedEObject, EObject oldAffectedEObject, EReference affectedReference, EObject oldValue,
		int index, EObject[] oldCorrespondingEObjectsToDelete) {
		val components = correspondenceInstance.getCorrespondingEObjectsByType(newAffectedEObject, RepositoryComponent)
		var EObject eObjectToSave = null
		val affectedClass = newAffectedEObject as ConcreteClassifier
		if (!components.nullOrEmpty &&
			PCMJaMoPPNamespace.JaMoPP.JAMOPP_ANNOTATIONS_AND_MODIFIERS_REFERENCE_NAME.equals(affectedReference.name) &&
			(oldValue instanceof PublicImpl)) {
			val component = components.get(0)
			val userInteractor = new UserInteractor()
			val msg = "Public modifier has been removed from " + affectedClass.name + "."
			val choice = userInteractor.selectFromMessage(UserInteractionType.MODAL, msg, "Undo change",
				"Remove component " + component.entityName)
			switch choice {
				case 0: {
					affectedClass.addModifier(oldValue as Modifier)
					eObjectToSave = newAffectedEObject
				}
				case 1: {
					eObjectToSave = component.eContainer
					correspondenceInstance.removeAllCorrespondences(component)
					EcoreUtil.remove(component)
				}
			}
		}
		return TransformationUtils.createTransformationChangeResultForEObjectsToSave(eObjectToSave.toArray)
	}

	/**
	 * if the class is renamed rename the corresponding objects on PCM side 
	 */
	override updateSingleValuedEAttribute(EObject affectedEObject, EAttribute affectedAttribute, Object oldValue,
		Object newValue) {
		JaMoPP2PCMUtils.updateNameAsSingleValuedEAttribute(affectedEObject, affectedAttribute, oldValue, newValue,
			featureCorrespondenceMap, correspondenceInstance)
	}

	override createNonRootEObjectSingle(EObject affectedEObject, EReference affectedReference, EObject newValue,
		EObject[] newCorrespondingEObjects) {
		logger.warn("method should not be called for ClassMappingTransformation transformation")
		return null
	}

	def private createDatatype(Classifier jaMoPPClass) {
		val String msg = "Class " + jaMoPPClass.name +
			"has been created in the datatypes pacakage. Please decide which kind of data type should be created."
		switch (selection : super.modalTextUserinteracting(msg, #["Create CompositeDataType", "CreateCollectionDataType", "Do not create a data type (not recommended)"])) {
			case SELECT_CREATE_COMPOSITE_DATA_TYPE: {
				val repo = JaMoPP2PCMUtils.getRepository(correspondenceInstance)
				val compositeDataType = RepositoryFactory.eINSTANCE.createCompositeDataType
				compositeDataType.entityName = jaMoPPClass.name
				compositeDataType.setRepository__DataType(repo)
				return compositeDataType.toArray
			}
			case SELECT_CREATE_COLLECTION_DATA_TYPE: {
				val repo = JaMoPP2PCMUtils.getRepository(correspondenceInstance)
				val collectionDataType = RepositoryFactory.eINSTANCE.createCollectionDataType
				collectionDataType.entityName = jaMoPPClass.name
				collectionDataType.setRepository__DataType(repo)
				return collectionDataType.toArray
			}
			case SELECT_DO_NOT_CREATE_DATA_TYPE: {
				return null
			}
		}
		return null
	}

	/**
	 * checks whether the classifier is public
	 * if it is not public no corresponding object will be created for the class
	 */
	def private boolean classIsPublic(Class jaMoPPClass) {

		// i) + iii)
		val hasPublicAnnotation = jaMoPPClass.annotationsAndModifiers.filter[aam|aam instanceof Public].size
		return 0 < hasPublicAnnotation
	}

	def private InterfaceProvidingRequiringEntity askUserWhetherToCreateComponentOrSystem(Classifier jaMoPPClass) {
		var int selection = super.modalTextUserinteracting(
			"The created class is 'public' and in a package that has no corresponding architectural element yet." +
				" Should a component or a system be created for the package and the class '" + jaMoPPClass.name +
				"' used as the component implementing class?", "Create BasicComponent", "Create System",
			"Create Composite Component", "Do not create a Component or System correspondence for the class")
		switch (selection) {
			case SELECT_CREATE_BASIC_COMPONENT: {
				val repo = JaMoPP2PCMUtils.getRepository(correspondenceInstance)
				val basicComponent = RepositoryFactory.eINSTANCE.createBasicComponent
				basicComponent.entityName = jaMoPPClass.name
				basicComponent.repository__RepositoryComponent = repo
				return basicComponent
			}
			case SELECT_CREATE_SYSTEM: {
				val pcmSystem = SystemFactory.eINSTANCE.createSystem
				pcmSystem.entityName = jaMoPPClass.name
				return pcmSystem
			}
			case SELECT_CREATE_COMPOSITE_COMPONENT: {
				val repo = JaMoPP2PCMUtils.getRepository(correspondenceInstance)
				val compositeComponent = RepositoryFactory.eINSTANCE.createCompositeComponent
				compositeComponent.entityName = jaMoPPClass.name
				compositeComponent.repository__RepositoryComponent = repo
				return compositeComponent
			}
			case SELECT_NO_CORRESPONDENCE: {
				return null
			}
		}
		return null
	}

}
