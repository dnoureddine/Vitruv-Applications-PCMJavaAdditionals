package tools.vitruvius.applications.pcmjava.tests.pojotransformations.gplimplementation.java2pcm

import tools.vitruvius.applications.pcmjava.tests.pojotransformations.java2pcm.TypeReferenceMappingTransformationTest

class TypeReferenceMappingGplTransformationTest extends TypeReferenceMappingTransformationTest {
	override protected createChange2CommandTransformingProviding() {
		Change2CommandTransformingProvidingFactory.createJava2PcmGplImplementationTransformingProviding();
	}
}