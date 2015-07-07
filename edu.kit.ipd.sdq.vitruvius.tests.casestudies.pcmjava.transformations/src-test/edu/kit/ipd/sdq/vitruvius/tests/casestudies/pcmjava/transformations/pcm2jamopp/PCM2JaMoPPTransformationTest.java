package edu.kit.ipd.sdq.vitruvius.tests.casestudies.pcmjava.transformations.pcm2jamopp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.change.ChangeDescription;
import org.eclipse.emf.ecore.change.util.ChangeRecorder;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.emftext.language.java.classifiers.Class;
import org.emftext.language.java.classifiers.Classifier;
import org.emftext.language.java.commons.NamedElement;
import org.emftext.language.java.containers.CompilationUnit;
import org.emftext.language.java.imports.ClassifierImport;
import org.emftext.language.java.imports.Import;
import org.emftext.language.java.instantiations.NewConstructorCall;
import org.emftext.language.java.members.Constructor;
import org.emftext.language.java.members.Field;
import org.emftext.language.java.members.Member;
import org.emftext.language.java.members.Method;
import org.emftext.language.java.statements.Statement;
import org.emftext.language.java.types.ClassifierReference;
import org.emftext.language.java.types.NamespaceClassifierReference;
import org.emftext.language.java.types.TypeReference;
import org.junit.Before;

import de.uka.ipd.sdq.pcm.core.composition.AssemblyContext;
import de.uka.ipd.sdq.pcm.core.composition.CompositionFactory;
import de.uka.ipd.sdq.pcm.core.entity.ComposedProvidingRequiringEntity;
import de.uka.ipd.sdq.pcm.core.entity.InterfaceProvidingEntity;
import de.uka.ipd.sdq.pcm.core.entity.InterfaceProvidingRequiringEntity;
import de.uka.ipd.sdq.pcm.repository.BasicComponent;
import de.uka.ipd.sdq.pcm.repository.CollectionDataType;
import de.uka.ipd.sdq.pcm.repository.CompositeComponent;
import de.uka.ipd.sdq.pcm.repository.CompositeDataType;
import de.uka.ipd.sdq.pcm.repository.DataType;
import de.uka.ipd.sdq.pcm.repository.InnerDeclaration;
import de.uka.ipd.sdq.pcm.repository.OperationInterface;
import de.uka.ipd.sdq.pcm.repository.OperationProvidedRole;
import de.uka.ipd.sdq.pcm.repository.OperationRequiredRole;
import de.uka.ipd.sdq.pcm.repository.OperationSignature;
import de.uka.ipd.sdq.pcm.repository.Parameter;
import de.uka.ipd.sdq.pcm.repository.ParameterModifier;
import de.uka.ipd.sdq.pcm.repository.PrimitiveDataType;
import de.uka.ipd.sdq.pcm.repository.PrimitiveTypeEnum;
import de.uka.ipd.sdq.pcm.repository.Repository;
import de.uka.ipd.sdq.pcm.repository.RepositoryFactory;
import de.uka.ipd.sdq.pcm.seff.ResourceDemandingSEFF;
import de.uka.ipd.sdq.pcm.seff.SeffFactory;
import de.uka.ipd.sdq.pcm.system.System;
import edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.PCMJaMoPPNamespace;
import edu.kit.ipd.sdq.vitruvius.casestudies.pcmjava.transformations.pcm2java.repository.DataTypeCorrespondenceHelper;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.datatypes.Change;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.datatypes.CorrespondenceInstance;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.datatypes.EMFModelChange;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.datatypes.FileChange;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.datatypes.FileChange.FileChangeKind;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.datatypes.VURI;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.interfaces.SynchronisationListener;
import edu.kit.ipd.sdq.vitruvius.framework.contracts.interfaces.user.TransformationAbortCause;
import edu.kit.ipd.sdq.vitruvius.framework.metarepository.MetaRepositoryImpl;
import edu.kit.ipd.sdq.vitruvius.framework.run.editor.monitored.emfchange.changedescription2change.ChangeDescription2ChangeConverter;
import edu.kit.ipd.sdq.vitruvius.framework.run.propagationengine.EMFModelPropagationEngineImpl;
import edu.kit.ipd.sdq.vitruvius.framework.run.syncmanager.SyncManagerImpl;
import edu.kit.ipd.sdq.vitruvius.framework.synctransprovider.TransformationExecutingProvidingImpl;
import edu.kit.ipd.sdq.vitruvius.framework.util.bridges.EcoreResourceBridge;
import edu.kit.ipd.sdq.vitruvius.framework.vsum.VSUMImpl;
import edu.kit.ipd.sdq.vitruvius.tests.casestudies.pcmjava.transformations.PCMJaMoPPTransformationTestBase;
import edu.kit.ipd.sdq.vitruvius.tests.casestudies.pcmjava.transformations.jamopp2pcm.TestUserInteractor;
import edu.kit.ipd.sdq.vitruvius.tests.casestudies.pcmjava.transformations.utils.PCM2JaMoPPTestUtils;
import edu.kit.ipd.sdq.vitruvius.tests.jamopppcm.util.JaMoPPPCMTestUtil;
import edu.kit.ipd.sdq.vitruvius.tests.util.TestUtil;

/**
 * super class for all repository and system tests. Contains helper methods
 *
 * @author Langhamm
 *
 */
public class PCM2JaMoPPTransformationTest extends PCMJaMoPPTransformationTestBase implements SynchronisationListener {

    protected VSUMImpl vsum;
    protected SyncManagerImpl syncManager;
    protected MetaRepositoryImpl metaRepository;
    protected ChangeRecorder changeRecorder;

    private ChangeDescription2ChangeConverter changeDescrition2ChangeConverter;

    protected CorrespondenceInstance correspondenceInstance;

    /**
     * Set up SyncMangaer and metaRepository facility. Creates a fresh VSUM, Metarepository etc.
     * before each test
     *
     * @throws Throwable
     */
    @Before
    public void setUpTest() throws Throwable {
        this.metaRepository = JaMoPPPCMTestUtil.createJaMoPPPCMMetaRepository();
        this.vsum = TestUtil.createVSUM(this.metaRepository);
        final TransformationExecutingProvidingImpl syncTransformationProvider = new TransformationExecutingProvidingImpl();
        final EMFModelPropagationEngineImpl propagatingChange = new EMFModelPropagationEngineImpl(
                syncTransformationProvider);
        this.syncManager = new SyncManagerImpl(this.vsum, propagatingChange, this.vsum, this.metaRepository, this.vsum,
                this);
        this.testUserInteractor = new TestUserInteractor();
        super.setUserInteractor(this.testUserInteractor, this.syncManager);
        this.resourceSet = new ResourceSetImpl();
        this.changeRecorder = new ChangeRecorder();
        this.changeDescrition2ChangeConverter = new ChangeDescription2ChangeConverter();
    }

    @Override
    protected void afterTest() {
        this.correspondenceInstance = null;
    }

    private CorrespondenceInstance getCorrespondenceInstanceForProject(final String projectName) throws Throwable {
        final VURI pcmMMUri = VURI.getInstance(PCMJaMoPPNamespace.PCM.PCM_METAMODEL_NAMESPACE);
        final VURI jaMoPPURI = VURI.getInstance(PCMJaMoPPNamespace.JaMoPP.JAMOPP_METAMODEL_NAMESPACE);
        return this.vsum.getCorrespondenceInstanceOriginal(pcmMMUri, jaMoPPURI);
    }

    @Override
    protected CorrespondenceInstance getCorrespondenceInstance() throws Throwable {
        if (null == this.correspondenceInstance) {
            this.correspondenceInstance = this.getCorrespondenceInstanceForProject(TestUtil.PROJECT_URI);
        }
        return this.correspondenceInstance;
    }

    protected void triggerSynchronization(final VURI vuri) {
        final ChangeDescription cd = this.changeRecorder.endRecording();
        cd.applyAndReverse();
        final List<Change> changes = this.changeDescrition2ChangeConverter.getChanges(cd, vuri);
        cd.applyAndReverse();
        this.syncManager.synchronizeChanges(changes);
        this.changeRecorder.beginRecording(Collections.EMPTY_LIST);
    }

    protected void triggerSynchronization(final EObject eObject) {
        final VURI vuri = VURI.getInstance(eObject.eResource());
        this.triggerSynchronization(vuri);
    }

    protected void synchronizeFileChange(final FileChangeKind fileChangeKind, final VURI vuri) {
        final FileChange fileChange = new FileChange(fileChangeKind, vuri);
        this.syncManager.synchronizeChange(fileChange);
    }

    /**
     * innerDeclaration must have 3 correspondences: one field, one getter and one setter
     *
     * @param innerDec
     * @throws Throwable
     */
    protected void assertInnerDeclaration(final InnerDeclaration innerDec) throws Throwable {
        final Set<EObject> correspondingObjects = this.getCorrespondenceInstance()
                .getAllCorrespondingEObjects(innerDec);
        int fieldsFound = 0;
        int methodsFound = 0;
        for (final EObject eObject : correspondingObjects) {
            if (eObject instanceof Field) {
                fieldsFound++;
                final Field field = (Field) eObject;
                assertTrue("field name unexpected",
                        field.getName().toLowerCase().contains(innerDec.getEntityName().toLowerCase()));
            } else if (eObject instanceof Method) {
                methodsFound++;
            } else {
                fail("unexpected corresponding object for inner declartion found: " + eObject);
            }
        }
        assertEquals("unexpected number of corresponding fields found", 1, fieldsFound);
        assertEquals("unexpected number of corresponding methods found", 2, methodsFound);
    }

    @SuppressWarnings("unchecked")
    protected void assertDataTypeCorrespondence(final DataType dataType) throws Throwable {
        if (dataType instanceof CollectionDataType) {
            final CollectionDataType cdt = (CollectionDataType) dataType;
            this.assertCorrespondnecesAndCompareNames(cdt, 3, new java.lang.Class[] { CompilationUnit.class,
                    Classifier.class, TypeReference.class },
                    new String[] { cdt.getEntityName() + ".java", cdt.getEntityName(), null });
        } else if (dataType instanceof CompositeDataType) {
            final CompositeDataType cdt = (CompositeDataType) dataType;
            this.assertCorrespondnecesAndCompareNames(cdt, 2, new java.lang.Class[] { CompilationUnit.class,
                    Classifier.class }, new String[] { cdt.getEntityName() + ".java", cdt.getEntityName() });
        } else if (dataType instanceof PrimitiveDataType) {
            final PrimitiveDataType pdt = (PrimitiveDataType) dataType;
            assertTrue("No correspondence exists for DataType " + dataType,
                    null != DataTypeCorrespondenceHelper.claimJaMoPPTypeForPrimitiveDataType(pdt));
        }

    }

    protected <T> Set<NamedElement> assertCorrespondnecesAndCompareNames(
            final de.uka.ipd.sdq.pcm.core.entity.NamedElement pcmNamedElement, final int expectedSize,
            final java.lang.Class<? extends EObject>[] expectedClasses, final String[] expectedNames) throws Throwable {
        final Set<EObject> correspondences = this.getCorrespondenceInstance().claimCorrespondingEObjects(
                pcmNamedElement);
        assertEquals("correspondences.size should be " + expectedSize, expectedSize, correspondences.size());
        final Set<NamedElement> jaMoPPElements = new HashSet<NamedElement>();
        for (int i = 0; i < expectedClasses.length; i++) {
            final java.lang.Class<? extends EObject> expectedClass = expectedClasses[i];
            final EObject correspondingEObject = this.getCorrespondenceInstance()
                    .claimUniqueCorrespondingEObjectByType(pcmNamedElement, expectedClass);
            if (!expectedClass.isInstance(correspondingEObject)) {
                fail("Corresponding EObject " + correspondingEObject + " is not an instance of " + expectedClass);
            }
            final String expectedName = expectedNames[i];
            if (correspondingEObject instanceof NamedElement) {
                final NamedElement jaMoPPElement = (NamedElement) correspondingEObject;
                assertTrue("The name of the jamopp element does not contain the expected name", jaMoPPElement.getName()
                        .contains(expectedName));
                jaMoPPElements.add(jaMoPPElement);
            } else {
                // expected name should be null
                assertTrue("The expected name should be null if the element is not a NamedElement",
                        null == expectedName);
            }
        }
        return jaMoPPElements;
    }

    protected void assertEmptyCorrespondence(final EObject eObject) throws Throwable {
        try {
            final Set<EObject> correspondences = this.getCorrespondenceInstance().claimCorrespondingEObjects(eObject);
            fail("correspondences.size should be " + 0 + " but is " + correspondences.size());
        } catch (final RuntimeException re) {
            // expected Runtime expception:
        }

    }

    protected void assertEqualsTypeReference(final TypeReference expected, final TypeReference actual) {
        assertTrue("type reference are not from the same type", expected.getClass().equals(actual.getClass()));
        // Note: not necessary to check Primitive type: if the classes are from
        // the same type (e.g.
        // Int) the TypeReferences are equal
        if (expected instanceof ClassifierReference) {
            final ClassifierReference expectedClassifierRef = (ClassifierReference) expected;
            final ClassifierReference actualClassifierRef = (ClassifierReference) actual;
            assertEquals("Target of type reference does not have the same name", expectedClassifierRef.getTarget()
                    .getName(), actualClassifierRef.getTarget().getName());
        }
        if (expected instanceof NamespaceClassifierReference) {
            final NamespaceClassifierReference expectedNamespaceClassifierRef = (NamespaceClassifierReference) expected;
            final NamespaceClassifierReference actualNamespaceClassifierRef = (NamespaceClassifierReference) actual;
            this.assertEqualsTypeReference(expectedNamespaceClassifierRef.getPureClassifierReference(),
                    actualNamespaceClassifierRef.getPureClassifierReference());
        }
    }

    protected OperationSignature createAndSyncOperationSignature(final Repository repo,
            final OperationInterface opInterface) throws IOException {
        final String operationSignatureName = PCM2JaMoPPTestUtils.OPERATION_SIGNATURE_1_NAME;
        return this.createAndSyncOperationSignature(repo, opInterface, operationSignatureName);
    }

    private OperationSignature createAndSyncOperationSignature(final Repository repo,
            final OperationInterface opInterface, final String operationSignatureName) throws IOException {
        final OperationSignature opSig = RepositoryFactory.eINSTANCE.createOperationSignature();
        opSig.setEntityName(operationSignatureName);
        opSig.setInterface__OperationSignature(opInterface);
        EcoreResourceBridge.saveResource(repo.eResource());
        this.triggerSynchronization(VURI.getInstance(repo.eResource()));
        return opSig;
    }

    @SuppressWarnings("unchecked")
    protected NamedElement assertSingleCorrespondence(
            final de.uka.ipd.sdq.pcm.core.entity.NamedElement pcmNamedElement,
            final java.lang.Class<? extends EObject> expectedClass, final String expectedName) throws Throwable {
        final Set<NamedElement> namedElements = this.assertCorrespondnecesAndCompareNames(pcmNamedElement, 1,
                new java.lang.Class[] { expectedClass }, new String[] { expectedName });
        return namedElements.iterator().next();
    }

    protected OperationInterface renameInterfaceAndSync(final OperationInterface opInterface) throws Throwable {
        final String newValue = opInterface.getEntityName() + PCM2JaMoPPTestUtils.RENAME;
        opInterface.setEntityName(newValue);
        EcoreResourceBridge.saveResource(opInterface.eResource());
        this.triggerSynchronization(VURI.getInstance(opInterface.eResource()));
        return opInterface;
    }

    protected BasicComponent addBasicComponentAndSync(final Repository repo, final String name) throws Throwable {
        final BasicComponent basicComponent = PCM2JaMoPPTestUtils.createBasicComponent(repo, name);
        EcoreResourceBridge.saveResource(repo.eResource());
        this.triggerSynchronization(VURI.getInstance(repo.eResource()));
        return basicComponent;
    }

    protected BasicComponent addBasicComponentAndSync(final Repository repo) throws Throwable {
        return this.addBasicComponentAndSync(repo, PCM2JaMoPPTestUtils.BASIC_COMPONENT_NAME);
    }

    protected OperationInterface addInterfaceToReposiotryAndSync(final Repository repo, final String interfaceName)
            throws Throwable {
        final OperationInterface opInterface = RepositoryFactory.eINSTANCE.createOperationInterface();
        opInterface.setRepository__Interface(repo);
        opInterface.setEntityName(interfaceName);
        EcoreResourceBridge.saveResource(repo.eResource());
        this.triggerSynchronization(VURI.getInstance(repo.eResource()));
        return opInterface;
    }

    protected Repository createAndSyncRepository(final ResourceSet resourceSet, final String repositoryName)
            throws IOException {
        final Repository repo = PCM2JaMoPPTestUtils.createRepository(resourceSet, repositoryName);
        this.changeRecorder.beginRecording(Collections.singletonList(repo));
        this.synchronizeFileChange(FileChangeKind.CREATE, VURI.getInstance(repo.eResource()));
        return repo;
    }

    protected OperationSignature createAndSyncRepoInterfaceAndOperationSignature() throws IOException, Throwable {
        final Repository repo = this.createAndSyncRepository(this.resourceSet, PCM2JaMoPPTestUtils.REPOSITORY_NAME);
        final OperationInterface opInterface = this.addInterfaceToReposiotryAndSync(repo,
                PCM2JaMoPPTestUtils.INTERFACE_NAME);
        final OperationSignature opSig = this.createAndSyncOperationSignature(repo, opInterface);
        return opSig;
    }

    protected Parameter addAndSyncParameterWithPrimitiveTypeToSignature(final OperationSignature opSig) {
        final PrimitiveDataType dataType = RepositoryFactory.eINSTANCE.createPrimitiveDataType();
        dataType.setType(PrimitiveTypeEnum.INT);
        return this.addAndSyncParameterToSignature(opSig, dataType, PCM2JaMoPPTestUtils.PARAMETER_NAME);
    }

    protected Parameter addAndSyncParameterToSignature(final OperationSignature opSig, final DataType dataType,
            final String parameterName) {
        final Parameter param = RepositoryFactory.eINSTANCE.createParameter();
        param.setParameterName(parameterName);
        param.setDataType__Parameter(dataType);
        param.setModifier__Parameter(ParameterModifier.IN);
        param.setOperationSignature__Parameter(opSig);
        opSig.getParameters__OperationSignature().add(param);
        final VURI vuri = VURI.getInstance(opSig.eResource());
        this.triggerSynchronization(vuri);
        return param;
    }

    protected CompositeDataType createAndSyncCompositeDataType(final Repository repo, final String name) {
        final CompositeDataType cdt = this.createCompositeDataType(repo, name);
        this.triggerSynchronization(VURI.getInstance(repo.eResource()));
        return cdt;
    }

    protected CompositeDataType createCompositeDataType(final Repository repo, final String name) {
        final CompositeDataType cdt = RepositoryFactory.eINSTANCE.createCompositeDataType();
        cdt.setEntityName(name);
        cdt.setRepository__DataType(repo);
        return cdt;
    }

    protected CompositeDataType createAndSyncCompositeDataType(final Repository repo) {
        return this.createAndSyncCompositeDataType(repo, PCM2JaMoPPTestUtils.COMPOSITE_DATA_TYPE_NAME);
    }

    protected Parameter createAndSyncRepoOpSigAndParameter() throws IOException, Throwable {
        final OperationSignature opSig = this.createAndSyncRepoInterfaceAndOperationSignature();

        final Parameter param = this.addAndSyncParameterWithPrimitiveTypeToSignature(opSig);
        return param;
    }

    protected Parameter createAndSyncRepoOpSigAndParameterWithDataTypeName(final String compositeDataTypeName,
            final String parameterName) throws Throwable {
        final OperationSignature opSig = this.createAndSyncRepoInterfaceAndOperationSignature();
        final CompositeDataType cdt = this.createAndSyncCompositeDataType(opSig.getInterface__OperationSignature()
                .getRepository__Interface(), compositeDataTypeName);
        final Parameter param = this.addAndSyncParameterToSignature(opSig, cdt, parameterName);
        return param;
    }

    protected InnerDeclaration createAndSyncRepositoryCompositeDataTypeAndInnerDeclaration() throws IOException {
        final Repository repo = this.createAndSyncRepository(this.resourceSet, PCM2JaMoPPTestUtils.REPOSITORY_NAME);
        final CompositeDataType cdt = this.createAndSyncCompositeDataType(repo);
        final InnerDeclaration innerDec = this.addInnerDeclaration(cdt);
        this.triggerSynchronization(VURI.getInstance(repo.eResource()));
        return innerDec;
    }

    protected InnerDeclaration addInnerDeclaration(final CompositeDataType cdt) {
        final InnerDeclaration innerDec = RepositoryFactory.eINSTANCE.createInnerDeclaration();
        final PrimitiveDataType pdt = RepositoryFactory.eINSTANCE.createPrimitiveDataType();
        pdt.setType(PrimitiveTypeEnum.INT);
        innerDec.setDatatype_InnerDeclaration(pdt);
        innerDec.setCompositeDataType_InnerDeclaration(cdt);
        innerDec.setEntityName(PCM2JaMoPPTestUtils.INNER_DEC_NAME);
        cdt.getInnerDeclaration_CompositeDataType().add(innerDec);
        return innerDec;
    }

    protected OperationProvidedRole createAndSyncRepoOpIntfOpSigBasicCompAndOperationProvRole() throws IOException,
    Throwable {
        final OperationSignature opSig = this.createAndSyncRepoInterfaceAndOperationSignature();
        final OperationInterface opInterface = opSig.getInterface__OperationSignature();
        final BasicComponent basicComponent = this.addBasicComponentAndSync(opInterface.getRepository__Interface());

        return this.createAndSyncOperationProvidedRole(opInterface, basicComponent);
    }

    protected OperationProvidedRole createAndSyncOperationProvidedRole(final OperationInterface opInterface,
            final InterfaceProvidingEntity interfaceProvidingEntity) {
        final OperationProvidedRole operationProvidedRole = RepositoryFactory.eINSTANCE.createOperationProvidedRole();
        operationProvidedRole.setEntityName(interfaceProvidingEntity.getEntityName() + "_provides_"
                + opInterface.getEntityName());
        operationProvidedRole.setProvidedInterface__OperationProvidedRole(opInterface);
        operationProvidedRole.setProvidingEntity_ProvidedRole(interfaceProvidingEntity);
        final VURI vuri = VURI.getInstance(opInterface.eResource());
        this.triggerSynchronization(vuri);
        return operationProvidedRole;
    }

    protected OperationRequiredRole createAndSyncRepoBasicCompInterfaceAndOperationReqiredRole() throws IOException,
    Throwable {
        final OperationSignature opSig = this.createAndSyncRepoInterfaceAndOperationSignature();
        final OperationInterface opInterface = opSig.getInterface__OperationSignature();
        final BasicComponent basicComponent = this.addBasicComponentAndSync(opInterface.getRepository__Interface());

        final OperationRequiredRole operationRequiredRole = this.createAndSyncOperationRequiredRole(opInterface,
                basicComponent);
        return operationRequiredRole;
    }

    protected OperationRequiredRole createAndSyncOperationRequiredRole(final OperationInterface opInterface,
            final InterfaceProvidingRequiringEntity iprovidingRequiringEntity) throws Throwable {
        final OperationRequiredRole operationRequiredRole = RepositoryFactory.eINSTANCE.createOperationRequiredRole();
        operationRequiredRole.setEntityName(opInterface.getEntityName().toLowerCase());
        operationRequiredRole.setRequiredInterface__OperationRequiredRole(opInterface);
        operationRequiredRole.setRequiringEntity_RequiredRole(iprovidingRequiringEntity);
        EcoreResourceBridge.saveResource(iprovidingRequiringEntity.eResource());
        final VURI vuri = VURI.getInstance(iprovidingRequiringEntity.eResource());
        this.triggerSynchronization(vuri);
        return operationRequiredRole;
    }

    protected System createAndSyncSystem(final String name) throws Throwable {
        final System system = PCM2JaMoPPTestUtils.createSystem(this.resourceSet, name);
        this.changeRecorder.beginRecording(Collections.singletonList(system));
        this.synchronizeFileChange(FileChangeKind.CREATE, VURI.getInstance(system.eResource()));
        return system;
    }

    protected AssemblyContext createAndSyncAssemblyContext(
            final ComposedProvidingRequiringEntity composedProvidingRequiringEntity, final BasicComponent basicComponent)
                    throws IOException {
        final AssemblyContext assemblyContext = CompositionFactory.eINSTANCE.createAssemblyContext();
        assemblyContext.setEntityName(PCM2JaMoPPTestUtils.ASSEMBLY_CONTEXT_NAME);
        assemblyContext.setEncapsulatedComponent__AssemblyContext(basicComponent);
        assemblyContext.setParentStructure__AssemblyContext(composedProvidingRequiringEntity);
        EcoreResourceBridge.saveResource(composedProvidingRequiringEntity.eResource());
        this.triggerSynchronization(composedProvidingRequiringEntity);
        return assemblyContext;
    }

    protected CompositeComponent createAndSyncCompositeComponent(final Repository repo, final String name)
            throws Throwable {
        final CompositeComponent compositeComponent = PCM2JaMoPPTestUtils.createCompositeComponent(repo, name);
        EcoreResourceBridge.saveResource(repo.eResource());
        this.triggerSynchronization(VURI.getInstance(repo.eResource()));
        return compositeComponent;
    }

    @Override
    public void syncStarted() {

    }

    @Override
    public void syncFinished() {

    }

    @Override
    public void syncAborted(final EMFModelChange abortedChange) {

    }

    @Override
    public void syncAborted(final TransformationAbortCause cause) {

    }

    /**
     * a operation provided is represented by the main class implementing the interface and an
     * import
     *
     * @param operationProvidedRole
     * @throws Throwable
     */
    protected void assertOperationProvidedRole(final OperationProvidedRole operationProvidedRole) throws Throwable {
        final Set<EObject> correspondingEObjects = this.getCorrespondenceInstance().getAllCorrespondingEObjects(
                operationProvidedRole);
        int namespaceClassifierReferenceFound = 0;
        int importFound = 0;
        for (final EObject eObject : correspondingEObjects) {
            if (eObject instanceof NamespaceClassifierReference) {
                namespaceClassifierReferenceFound++;
            } else if (eObject instanceof ClassifierImport) {
                importFound++;
            } else {
                fail("operation provided role corresponds to unexpected object: " + eObject);
            }
        }
        assertEquals("unexpected size of corresponding imports", 1, importFound);
        assertEquals("unexpected size of corresponding namespace classifier references", 1,
                namespaceClassifierReferenceFound);
    }

    /**
     * ap operation required role is represented by one constructor parameter (per constructor), one
     * assignment in the constructor (per constructor) and a field with the type of the interface as
     * well as the import of the required interface in the components main class
     *
     * @param operationRequiredRole
     * @throws Throwable
     */
    protected void assertOperationRequiredRole(final OperationRequiredRole operationRequiredRole) throws Throwable {
        final Set<EObject> correspondingEObjects = this.getCorrespondenceInstance().getAllCorrespondingEObjects(
                operationRequiredRole);
        int importFounds = 0;
        int constructorParameterFound = 0;
        int fieldsFound = 0;
        int assignmentOperatorsFound = 0;
        int expectedConstrucotrParameters = 0;
        for (final EObject correspondingEObj : correspondingEObjects) {
            if (correspondingEObj instanceof Import) {
                importFounds++;
            } else if (correspondingEObj instanceof org.emftext.language.java.parameters.Parameter) {
                constructorParameterFound++;
                final org.emftext.language.java.parameters.Parameter param = (org.emftext.language.java.parameters.Parameter) correspondingEObj;
                assertTrue("Corresponding parameter has wrong name",
                        param.getName().equalsIgnoreCase(operationRequiredRole.getEntityName()));

            } else if (correspondingEObj instanceof Statement) {
                assignmentOperatorsFound++;
            } else if (correspondingEObj instanceof Field) {
                fieldsFound++;
                final Field field = (Field) correspondingEObj;
                final Class jaMoPPClass = (Class) field.getContainingConcreteClassifier();
                for (final Member member : jaMoPPClass.getMembers()) {
                    if (member instanceof Constructor) {
                        expectedConstrucotrParameters++;
                    }
                }
                assertTrue("Corresponding field has wrong name",
                        field.getName().equalsIgnoreCase(operationRequiredRole.getEntityName()));

            } else {
                fail("operation required role corresponds to unexpected object: " + correspondingEObj);
            }
        }
        assertEquals("Unexpected number of imports found", 1, importFounds);
        assertEquals("Unexpected number of constructorParameters found", expectedConstrucotrParameters,
                constructorParameterFound);
        assertEquals("Unexpected number of fields found", 1, fieldsFound);
        // we currently do not synchronize the assignment statements
        // assertEquals("Unexpected number of imports found", assignmentOperatorsFound,
        // constructorParameterFound);
    }

    /**
     * AssemblyContext should correspond to a field, a constructor, an import and to a new
     * constructor call
     *
     * @param assemblyContext
     * @throws Throwable
     */
    protected void assertAssemblyContext(final AssemblyContext assemblyContext) throws Throwable {
        final Set<EObject> correspondingEObjects = this.getCorrespondenceInstance().getAllCorrespondingEObjects(
                assemblyContext);
        boolean fieldFound = false;
        boolean importFound = false;
        boolean newConstructorCallFound = false;
        boolean constructorFound = false;
        for (final EObject correspondingEObject : correspondingEObjects) {
            if (correspondingEObject instanceof Field) {
                final Field field = (Field) correspondingEObject;
                assertEquals("The name of the field has to be the same as the name of the assembly context",
                        assemblyContext.getEntityName(), field.getName());
                fieldFound = true;
            }
            if (correspondingEObject instanceof Import) {
                importFound = true;
            }
            if (correspondingEObject instanceof NewConstructorCall) {
                newConstructorCallFound = true;
            }
            if (correspondingEObject instanceof Constructor) {
                constructorFound = true;
            }
        }
        assertTrue("Could not find all necessary corresponding objects", constructorFound && importFound
                && newConstructorCallFound && fieldFound);
    }

    @SuppressWarnings("unused")
    public Repository createMediaStore(final String mediaStoreName, final String webGUIName,
            final String downloadMethodName, final String uploadMethodName) throws Throwable {

        this.setUpTest();

        // create repo
        final Repository repo = this.createAndSyncRepository(this.resourceSet, "mediastorerepo");

        // create component
        final BasicComponent mediaStoreBC = this.addBasicComponentAndSync(repo, mediaStoreName);
        final BasicComponent webGUIBC = this.addBasicComponentAndSync(repo, webGUIName);

        // create interfaces
        final OperationInterface iMediaStoreIf = this.addInterfaceToReposiotryAndSync(repo, "I" + mediaStoreName);
        final OperationInterface iwebGUIIf = this.addInterfaceToReposiotryAndSync(repo, "I" + webGUIName);

        // create signatures
        final OperationSignature downloadMediaStore = this.createAndSyncOperationSignature(repo, iMediaStoreIf,
                downloadMethodName);
        final OperationSignature uploadMediaStore = this.createAndSyncOperationSignature(repo, iMediaStoreIf,
                uploadMethodName);
        final OperationSignature downloadWebGUI = this.createAndSyncOperationSignature(repo, iwebGUIIf, "http"
                + downloadMethodName);
        final OperationSignature uploadWebGUI = this.createAndSyncOperationSignature(repo, iwebGUIIf, "http"
                + uploadMethodName);

        // create provided roles
        final OperationProvidedRole mediaStore2IMediaStore = this.createAndSyncOperationProvidedRole(iMediaStoreIf,
                mediaStoreBC);
        final OperationProvidedRole webGUI2IWebGUI = this.createAndSyncOperationProvidedRole(iwebGUIIf, webGUIBC);

        // create required role
        final OperationRequiredRole webGui2MediaStore = this
                .createAndSyncOperationRequiredRole(iMediaStoreIf, webGUIBC);

        // Create seff for provided roles
        this.createAndSyncSeff(mediaStoreBC, downloadMediaStore);
        this.createAndSyncSeff(mediaStoreBC, uploadMediaStore);

        this.createAndSyncSeff(webGUIBC, downloadWebGUI);
        this.createAndSyncSeff(webGUIBC, uploadWebGUI);

        return repo;

    }

    protected ResourceDemandingSEFF createAndSyncSeff(final BasicComponent basicComponent,
            final OperationSignature describedSignature) throws Throwable {
        final ResourceDemandingSEFF rdSEFF = SeffFactory.eINSTANCE.createResourceDemandingSEFF();
        rdSEFF.setBasicComponent_ServiceEffectSpecification(basicComponent);
        rdSEFF.setDescribedService__SEFF(describedSignature);
        EcoreResourceBridge.saveResource(basicComponent.eResource());
        this.triggerSynchronization(VURI.getInstance(basicComponent.eResource()));
        return rdSEFF;

    }
}