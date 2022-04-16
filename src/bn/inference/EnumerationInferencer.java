package bn.inference;

import java.util.LinkedList;
import java.util.List;

import bn.base.Distribution;
import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.core.Inferencer;
import bn.core.RandomVariable;
import bn.core.Value;

public class EnumerationInferencer implements Inferencer {

	@Override
	//The method to solve exact inference problem
	public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network) {
		
		//Create an empty distribution of X
		Distribution qx = new Distribution(X);
		
		//For each value in the domain of X
		for(Value xi: X.getDomain()) {
			//Make a copy of e since assignemnt will be modifed
			Assignment t = e.copy();
			//Extend t to X = xi
			t.put(X, xi);
			double p = enumerateAll(network.getVariablesSortedTopologically(),t,network);
			//Add to the distribution of a value of X
			qx.put(xi, p);
		}
		//Normalize the distribution
		qx.normalize();
		return qx;
	}
	
	//Recursive helper method for query
	public double enumerateAll(List<RandomVariable> vars, Assignment e, BayesianNetwork network) {
		
		//Base case: if the List is empty, return 1.0
		if(vars.isEmpty() == true) {
			return 1.0;
		}
		List<RandomVariable> cvars = new LinkedList<RandomVariable>();
		for(RandomVariable c:vars) {
			cvars.add(c);
		}
		
		//Get the first element of the LinkedList
		RandomVariable Y = cvars.get(0);
		//Remove the first element of the LinkedList
		cvars.remove(0);
		
		if(e.containsKey(Y)) {//If the variable is evident
			//Times its probability
			return network.getProbability(Y, e) * enumerateAll(cvars,e,network);
		
		}else {//If the variable if hidden
			
			//Variable that holds the value of Marginalization
			Double m = 0.0;
			
			//For all the value in the domain of Y
			for(Value v:Y.getDomain()) {
			
				//Create a copy of the original assignment
				Assignment t = e.copy();
				//Put the Variable into the assignment
				t.put(Y, v);
				//Process of Marginalization
				m = m + network.getProbability(Y, t) * enumerateAll(cvars,t,network);
			}
			return m;
		}
	}
}
