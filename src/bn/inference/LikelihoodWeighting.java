package bn.inference;
import java.util.Random;
import bn.core.BayesianNetwork;
import bn.base.Assignment;
import bn.base.Distribution;
import bn.core.Inferencer;
import bn.core.RandomVariable;
import bn.core.Value;


public  class LikelihoodWeighting implements Inferencer{
	double weight = 1.0;//Set Starting Weight
	public Distribution likelihoodweighting (RandomVariable X, Assignment e, BayesianNetwork network, int N) {
		
		Distribution dis = new Distribution(X);
		Assignment test = new Assignment();
		for(Value v: X.getDomain()) {//Initialize a empty assignment
			dis.put(v,0.0);
		}
		
		for(int i=1;i<=N;i++) {//Accept assignment according to constraint (Only accept evidences that are true according to the input)
			weight = 1.0;
			test = WeightedSampling(network,e);  //Multiplied by weight
			double Wvalue = dis.get(test.get(X))+weight; //Add weight value
			dis.put(test.get(X),Wvalue);//Put weighted value into distribution
		}
		//order the fields in the distribution in the right way
		for(Value v:X.getDomain()) {
			double n=dis.get(v);
			dis.put(v, n);
		}
		dis.normalize();//Normalize after sampling
		return dis;
	}
	
	//the method that generates random samples with correct possibility of each random variable in based on the bayesian network
		public Assignment WeightedSampling(BayesianNetwork bn, Assignment e) {
			Random r=new Random();
			//make copy of x so we won't change x
			Assignment x1=e.copy();
			//initialize the higherbound and the lowerbound of the interval
			double lb=0.0;
			double ub=0.0;
			//loop over all the random variables of the bayesian network
			for(RandomVariable i:bn.getVariablesSortedTopologically()) {
				if(e.containsKey(i)) {
					x1.put(i,e.get(i));
					weight = weight*(bn.getProbability(i, x1));
				}else {
				lb=0.0;//lowerbound
				ub=0.0;//upperbound
				//generate a random double between 0.0 and 1.0
				double d=r.nextDouble();
				//try to put the value from the domain
				for(Value v:i.getDomain()) {
					x1.put(i, v);
					double p=bn.getProbability(i, x1);//make the correct interval with the corresponding possibility from the bayesian network
					ub=lb+p;
			     
					if(lb<=d&&d<=ub) { // if the random double lands in the interval we have built, this loop breaks and the put above is implemented,else it will be overwritten by another put if we fail to land in this interval
						break;
					}
					//enter the next interval
					lb=lb+p;
				}
			}
		}
			
			return x1;
	}

	@Override
	public bn.core.Distribution query(RandomVariable X, bn.core.Assignment e, BayesianNetwork network) {
		// TODO Auto-generated method stub
		return null;
	}


		

}
