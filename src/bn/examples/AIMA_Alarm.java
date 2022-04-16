package bn.examples;

import java.util.Set;

import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.CPT;
import bn.core.Distribution;
import bn.core.Inferencer;
import bn.core.RandomVariable;
import bn.inference.EnumerationInferencer;
import bn.base.BooleanDomain;
import bn.base.BooleanValue;
import bn.base.NamedVariable;
import bn.util.ArraySet;

/**
 * The AIMA Burglar Alarm example of a BayesianNetwork (AIMA Fig 14.2).
 * <p>
 * P(B|j,m) = \alpha &lt;0.00059224,0.0014919&gt; ~= &lt;0.284,0.716&gt; (p. 524)
 */
public class AIMA_Alarm {

	public static void main(String[] args) {
		RandomVariable B = new NamedVariable("B", new BooleanDomain());
		RandomVariable E = new NamedVariable("E", new BooleanDomain());
		RandomVariable A = new NamedVariable("A", new BooleanDomain());
		RandomVariable J = new NamedVariable("J", new BooleanDomain());
		RandomVariable M = new NamedVariable("M", new BooleanDomain());
		BayesianNetwork bn = new bn.base.BayesianNetwork();
		bn.add(B);
		bn.add(E);
		bn.add(A);
		bn.add(J);
		bn.add(M);
		// Shorthands
		BooleanValue TRUE = BooleanValue.TRUE;
		BooleanValue FALSE = BooleanValue.FALSE;
		Assignment a;

		// B (no parents)
		CPT Bprior = new bn.base.CPT(B);
		a = new bn.base.Assignment();
		Bprior.set(TRUE, a, 0.001);
		Bprior.set(FALSE, a, 1-0.001);
		bn.connect(B, new ArraySet<RandomVariable>() , Bprior);

		// E (no parents)
		CPT Eprior = new bn.base.CPT(E);
		a = new bn.base.Assignment();
		Eprior.set(TRUE, a, 0.002);
		Eprior.set(FALSE, a, 1-0.002);
		bn.connect(E, new ArraySet<RandomVariable>() , Eprior);

		// B,E -> A
		Set<RandomVariable> BE = new ArraySet<RandomVariable>();
		BE.add(B);
		BE.add(E);
		CPT AgivenBE = new bn.base.CPT(A);
		a = new bn.base.Assignment();
		a.put(B, TRUE);
		a.put(E, TRUE);
		AgivenBE.set(TRUE, a, 0.95);
		AgivenBE.set(FALSE, a, 1-0.95);
		a = new bn.base.Assignment();
		a.put(B, TRUE);
		a.put(E, FALSE);
		AgivenBE.set(TRUE, a, 0.94);
		AgivenBE.set(FALSE, a, 1-0.94);
		a = new bn.base.Assignment();
		a.put(B, FALSE);
		a.put(E, TRUE);
		AgivenBE.set(TRUE, a, 0.29);
		AgivenBE.set(FALSE, a, 1-0.29);
		a = new bn.base.Assignment();
		a.put(B, FALSE);
		a.put(E, FALSE);
		AgivenBE.set(TRUE, a, 0.001);
		AgivenBE.set(FALSE, a, 1-0.001);
		bn.connect(A, BE, AgivenBE);

		// A -> J
		Set<RandomVariable> justA = new ArraySet<RandomVariable>();
		justA.add(A);
		CPT JgivenA = new bn.base.CPT(J);
		a = new bn.base.Assignment();
		a.put(A, TRUE);
		JgivenA.set(TRUE, a, 0.9);
		JgivenA.set(FALSE, a, 1-0.9);
		a = new bn.base.Assignment();
		a.put(A, FALSE);
		JgivenA.set(TRUE, a, 0.05);
		JgivenA.set(FALSE, a, 1-0.05);
		bn.connect(J, justA, JgivenA);

		// A -> M
		CPT MgivenA = new bn.base.CPT(M);
		a = new bn.base.Assignment();
		a.put(A, TRUE);
		MgivenA.set(TRUE, a, 0.7);
		MgivenA.set(FALSE, a, 1-0.8);
		a = new bn.base.Assignment();
		a.put(A, FALSE);
		MgivenA.set(TRUE, a, 0.01);
		MgivenA.set(FALSE, a, 1-0.01);
		bn.connect(M, justA, MgivenA);
		
		System.out.println(bn);
		
		System.out.println("P(B|j,m) = \\alpha <0.00059224,0.0014919> ~= <0.284,0.716>");
		Inferencer exact = new EnumerationInferencer();
		a = new bn.base.Assignment();
		a.put(J, TRUE);
		a.put(M, TRUE);
		Distribution dist = exact.query(B, a, bn);
		System.out.println(dist);
	}

}
