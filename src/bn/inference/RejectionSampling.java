package bn.inference;

import java.util.Random;


import bn.core.Assignment;
import bn.core.BayesianNetwork;
import bn.base.Distribution;
import bn.core.Inferencer;
import bn.core.RandomVariable;
import bn.core.Value;

public class RejectionSampling implements Inferencer{

	@Override
	public Distribution query(RandomVariable X, Assignment e, BayesianNetwork network) {
		return null;
	}
	//The method returns the distribution of the query variable
	public Distribution rejectionsampling(RandomVariable X, Assignment e, BayesianNetwork network, int times) {
		//initialize the variable which counts the consistent samples
		int sample=0;
		//initialize query variable's distribution
		Distribution dis=new Distribution(X);
		//to make the distribution not null, else an exception will be thrown
		for(Value v:X.getDomain()) {
			dis.put(v,0.0);
		}
		//try create consistent samples for the time entered
		for(int i=0;i<times;i++) {
			//use the prior sampling method to generate random samples with the correct possibilities given in the bayesian network
			Assignment test=PriorSampling(network,e);
			//initialize the variable that marks whether this one sample is consistent or not
			boolean c=true;
			//loop over all of the evidence variables
			for(RandomVariable v:e.variableSet()) {
				//if the sample generate is not consistent with the evidence given
				if(!test.get(v).equals(e.get(v))) {
					c=false;
					break;
					}
			}
			//if all of the evidence are satisfies with this sample 
			if(c==true) {
				//then add 1 to the variable that counts the consistent samples
				sample++;
				//add 1 to the right field in the distribution
				double count=dis.get(test.get(X));
				count++;
				dis.put(test.get(X), count);
				
			}
		}
		//if there's some consistent sample generated
		if(sample>0) {
			//order the fields in the distribution in the right way
			for(Value v:X.getDomain()) {
				double n=dis.get(v);
				dis.put(v, n);
			}
			System.out.println("There are "+sample+" consistent samples, out of totally "+ times+" samples.");
			//normalize the distribution
			dis.normalize();
			return dis;
		}
		//if there's no consistent sample generated
		System.out.println("There's no consistent sample generated with the evidence variables using your given sampling time!");
		return null;
	}
	//the method that generates random samples with correct possibility of each random variable in based on the bayesian network
	public Assignment PriorSampling(BayesianNetwork bn, Assignment x) {
		Random r=new Random();
		//make copy of x so we won't change x
		Assignment x1=x.copy();
		//initialize the higherbound and the lowerbound of the interval
		double lb=0.0;
		double hb=0.0;
		//loop over all the random variables of the bayesian network
		for(RandomVariable i:bn.getVariablesSortedTopologically()) {
			lb=0.0;
			hb=0.0;
			//generate a random double between 0.0 and 1.0
			double d=r.nextDouble();
			//try to put the value from the domain
			for(Value v:i.getDomain()) {
				x1.put(i, v);
				double p=bn.getProbability(i, x1);
				//make the correct interval with the corresponding possibility from the bayesian network
				hb=lb+p;
				// if the random double lands in the interval we have built, this loop breaks and the put above is implemented,
				//else it will be overwritten by another put if we fail to land in this interval
				if(d>=lb&&d<hb) {
					break;
				}
				//enter the next interval
				lb=lb+p;
			}
		}
		
		return x1;
	}

}
