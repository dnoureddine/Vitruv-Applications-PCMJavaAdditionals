package edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.transformations.packagemapping.java.pcm2java.transformations.system

import com.google.common.collect.Lists
import edu.kit.ipd.sdq.vitruvius.framework.command.TransformationResult
import edu.kit.ipd.sdq.vitruvius.framework.userinteraction.UserInteractionType
import edu.kit.ipd.sdq.vitruvius.applications.pcmjava.packagemapping.javaimplementation.util.transformationexecutor.EmptyEObjectMappingTransformation
import java.util.ArrayList
import java.util.List
import org.eclipse.emf.ecore.EAttribute
import org.eclipse.emf.ecore.EObject
import org.eclipse.emf.ecore.EReference
import org.emftext.language.java.classifiers.Class
import org.emftext.language.java.members.Constructor
import org.emftext.language.java.members.Field
import org.emftext.language.java.types.TypeReference
import org.palladiosimulator.pcm.core.composition.AssemblyContext
import org.palladiosimulator.pcm.repository.RepositoryComponent

import static extension edu.kit.ipd.sdq.vitruvius.framework.util.bridges.CollectionBridge.*
import static extension edu.kit.ipd.sdq.vitruvius.framework.contracts.util.datatypes.CorrespondenceModelUtil.*
import edu.kit.ipd.sdq.vitruvius.casestudies.pcm.util.PCMNamespace
import edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.util.pcm2java.PCM2JaMoPPUtils

class AssemblyContextMappingTransformation extends EmptyEObjectMappingTransformation {

	override getClassOfMappedEObject() {
		return AssemblyContext
	}

	override setCorrespondenceForFeatures() {
		PCM2JaMoPPUtils.addEntityName2NameCorrespondence(featureCorrespondenceMap)
	}

	/**
	 * called when a assembly context has been added
	 * creates a private field with type of enclosed component and name of the assembly context.
	 * 
	 */
	override createEObject(EObject eObject) {
		val AssemblyContext assemblyContext = eObject as AssemblyContext
		val component = assemblyContext.encapsulatedComponent__AssemblyContext
		if (null == component) {

			// we are not able to create a field if the component in the assembly context is null
			// the following methods have to be aware of that
			return null
		}
		return createFieldForAssemblyContext(assemblyContext, component)
	}

	/**
	 * called when a assembly context has been renamed --> rename the field
	 */
	override updateSingleValuedEAttribute(EObject affectedEObject, EAttribute affectedAttribute, Object oldValue,
		Object newValue) {
		PCM2JaMoPPUtils.updateNameAsSingleValuedEAttribute(affectedEObject, affectedAttribute, oldValue, newValue,
			featureCorrespondenceMap, correspondenceModel)
	}

	/**
	 * called when the component within the assembly context has been changed.
	 * Creates a new field2AssemblyCorrespondence if no correspondence exists. 
	 * If a correspondence already exists the TypeReference of the field will be updated
	 */
	override replaceNonContainmentEReference(EObject affectedEObject,
		EReference affectedReference, EObject oldValue, EObject newValue, int index) {
		if(oldValue == newValue){
			// if the object has not changed we do not do anything
			return new TransformationResult 
		}
		if (affectedReference.name.equals(PCMNamespace.ASSEMBLY_CONTEXT_ENCAPSULATED_COMPONENT) &&
			newValue instanceof RepositoryComponent && affectedEObject instanceof AssemblyContext) {
			val typedElementCorrespondences = correspondenceModel.
				getCorrespondingEObjectsByType(affectedEObject as AssemblyContext, Field)
			if (typedElementCorrespondences.nullOrEmpty) {
				val assemblyContext = affectedEObject as AssemblyContext

				//create new correspondences
				val newEObjects = this.createFieldForAssemblyContext(assemblyContext,
					newValue as RepositoryComponent);
				val composedProvidingRequiringEntity = assemblyContext.parentStructure__AssemblyContext
				PCM2JaMoPPUtils.
					handleAssemblyContextAddedAsNonRootEObjectInList(composedProvidingRequiringEntity, assemblyContext,
						newEObjects, correspondenceModel)

			} else {
				val jaMoPPClass = correspondenceModel.
					getCorrespondingEObjectsByType(newValue as RepositoryComponent, Class).claimOne

				//update existing correspondence
				for (typedElement : typedElementCorrespondences) {
					val oldTUID = correspondenceModel.calculateTUIDFromEObject(typedElement)
					typedElement.typeReference = PCM2JaMoPPUtils.createNamespaceClassifierReference(jaMoPPClass)
					correspondenceModel.updateTUID(oldTUID, typedElement)
				}
			}
		}
		return new TransformationResult
	}

	override updateSingleValuedNonContainmentEReference(EObject affectedEObject, EReference affectedReference,
		EObject oldValue, EObject newValue) {
		replaceNonContainmentEReference(affectedEObject, affectedReference, oldValue, newValue, 0)
	}

	def private EObject[] createFieldForAssemblyContext(AssemblyContext assemblyContext, RepositoryComponent component) {
		var Class jaMoPPClass = null
		var Class jaMoPPCompositeClass = null
		try {
			jaMoPPClass = correspondenceModel.getCorrespondingEObjectsByType(component, Class).claimOne
			jaMoPPCompositeClass = correspondenceModel.
				getCorrespondingEObjectsByType(assemblyContext.parentStructure__AssemblyContext, Class).claimOne

		} catch (RuntimeException e) {
			val String msg = "Can not create field for component " + component.entityName +
				" because the component does not have a corresponding class yet."
			userInteracting.showMessage(UserInteractionType.MODELESS, msg)
			return Lists.newArrayList
		}

		val TypeReference typeRef = PCM2JaMoPPUtils.createNamespaceClassifierReference(jaMoPPClass)
		val String name = assemblyContext.entityName

		val List<EObject> newEObjects = new ArrayList<EObject>()

		val field = PCM2JaMoPPUtils.createPrivateField(typeRef, name)
		jaMoPPCompositeClass.members.add(field)

		newEObjects.add(field)

		val constructors = jaMoPPCompositeClass.members.filter(typeof(Constructor))

		if (constructors.nullOrEmpty) {
			newEObjects.add(PCM2JaMoPPUtils.addConstructorToClass(jaMoPPCompositeClass))
		} else {
			newEObjects.addAll(constructors)
		}

		newEObjects.add(PCM2JaMoPPUtils.addImportToCompilationUnitOfClassifier(jaMoPPCompositeClass, jaMoPPClass))

		// add creation of EObject to each constructor
		for (ctor : jaMoPPCompositeClass.members.filter(typeof(Constructor))) {
			newEObjects.addAll(PCM2JaMoPPUtils.createNewForFieldInConstructor(field))
		}

		newEObjects
	}

}
