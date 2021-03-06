package tools.vitruv.applications.pcmjava.reconstructionintegration.tests.invariantChecker.test;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.ocl.ParserException;
import org.eclipse.ocl.ecore.Constraint;
import org.eclipse.ocl.ecore.EcoreEnvironmentFactory;
import org.eclipse.ocl.ecore.OCL;
import org.eclipse.ocl.expressions.OCLExpression;
import org.eclipse.ocl.helper.OCLHelper;

import org.palladiosimulator.pcm.repository.Repository;
import org.palladiosimulator.pcm.repository.RepositoryPackage;

import tools.vitruv.domains.pcm.util.RepositoryModelLoader;

 
/**
 * The Class OCLInvariantCheckTestcase.
 *
 * @author Sven Leonhardt Checks self defined invariants on any given model. Return true if model is
 *         conform to the invariant, else false.
 */
public class OCLInvariantCheckTestcase {

    /**
     * The main method.
     *
     * @param args
     *            the arguments
     */
    public static void main(final String[] args) {

        final OCL ocl = OCL.newInstance(EcoreEnvironmentFactory.INSTANCE);
        final OCLHelper<EClassifier, ?, ?, Constraint> helper = ocl.createOCLHelper();

        helper.setContext(RepositoryPackage.Literals.REPOSITORY);

        Constraint inv = null;

        try {
            inv = helper
                    .createInvariant("self.components__Repository->forAll(c | c.entityName.matches('^[a-zA-Z_$]*'))");
        } catch (final ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final String path = "Testmodels/InvalidComponentName.repository";
        final Resource r = RepositoryModelLoader.loadPcmResource(path);
        final Repository repo = (Repository) r.getContents().get(0);

        final boolean check = ocl.check(repo, inv);

        System.out.println("Invariant: " + check);

        Diagnostician.INSTANCE.validate(repo);

        OCLExpression<EClassifier> expression = null;

        try {
            expression = helper.createQuery("self.entityName.startsWith('sdhf')");
        } catch (final ParserException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        final Object result = ocl.evaluate(repo, expression);

        ocl.isInvalid(result);

    }

}
