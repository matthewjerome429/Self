package com.cathaypacific.mmbbizrule.verifier;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.function.Consumer;

public abstract class Verifier<T> {

	protected Consumer<T>[] propVerifiers;
	
	public void verify(T obj) {
		for (Consumer<T> propVerifier : propVerifiers) {
			propVerifier.accept(obj);
		}
	}
	
	protected static <P> void verifyList(Verifier<P>[] verifiers, List<P> values) {
		assertEquals(verifiers.length, values.size());
		
		for (int i = 0; i < verifiers.length; i++) {
			verifiers[i].verify(values.get(i));
		}
	}

}
