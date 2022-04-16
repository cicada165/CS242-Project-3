package bn.inference;

import java.io.FileInputStream;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import bn.base.Assignment;
import bn.base.Value;
import bn.core.BayesianNetwork;
import bn.core.Distribution;
import bn.core.Inferencer;
import bn.core.RandomVariable;
import bn.parser.BIFParser;
import bn.parser.XMLBIFParser;

public class main {
	
	//Driver Method
	public static void main(String[]args) throws IOException, ParserConfigurationException, SAXException {
		
		//If the User choose exact inference
		if(args[0].equals("EnumerationInferencer")) {
			//If the File is in .xml form
			if(args[1].contains("xml")) {
				//The second argument is the name of the file
				String filename = args[1];
				
				//Create an instance of XMLBIFParser
				XMLBIFParser p = new XMLBIFParser();
				//Read the file and retrieve the BayesianNetwork from it 
				BayesianNetwork bn = p.readNetworkFromFile(filename);
				//Get the Query variable from the BayesianNetwork
				RandomVariable QVariable = bn.getVariableByName(args[2]);
				//Create an instance of Assignment
				Assignment a = new Assignment();
				
				//Add the evident variables into assignment
				for(int i = 3; i < args.length;i = i + 2) {
					a.put(bn.getVariableByName(args[i]), new Value<String>(args[i+1]));
					}	
				//Create an instance of exact inferencer
				Inferencer exact = new EnumerationInferencer();
				//Call for exact inferencer and get distribution
				Distribution dist = exact.query(QVariable, a, bn);
				//Print the distribution
				System.out.print(dist);
			}else if(args[1].contains("bif")) {
				//The second argument is the name of the file
				String filename = args[1];
				
				//Create an instance of BIFParser
				BIFParser p = new BIFParser(new FileInputStream(filename));
				//Read the file and retrieve the BayesianNetwork from it 
				BayesianNetwork bn = p.parseNetwork();
				//Get the Query variable from the BayesianNetwork
				RandomVariable QVariable = bn.getVariableByName(args[2]);
				
				//Create an instance of Assignment
				Assignment a = new Assignment();
				
				//Add the evident variables into assignment
				for(int i = 3; i < args.length;i = i + 2) {
					a.put(bn.getVariableByName(args[i]), new Value<String>(args[i+1]));
					}	
				//Create an instance of exact inferencer
				Inferencer exact = new EnumerationInferencer();
				//Call for exact inferencer and get distribution
				Distribution dist = exact.query(QVariable, a, bn);
				//Print the distribution
				System.out.print(dist);
			}
		}else if(args[0].equals("RejectionSampling")) {
			String print="";
			int times=Integer.parseInt(args[1]);
			//If the File is in .xml form
			if(args[2].contains("xml")) {
				//The second argument is the name of the file
				String filename = args[2];
				
				//Create an instance of XMLBIFParser
				XMLBIFParser p = new XMLBIFParser();
				//Read the file and retrieve the BayesianNetwork from it 
				BayesianNetwork bn = p.readNetworkFromFile(filename);
				//Get the Query variable from the BayesianNetwork
				RandomVariable QVariable = bn.getVariableByName(args[3]);
				//Create an instance of Assignment
				Assignment a = new Assignment();
				
				//Add the evident variables into assignment
				for(int i = 4; i < args.length;i = i + 2) {
					a.put(bn.getVariableByName(args[i]), new Value<String>(args[i+1]));
					print=print+args[i]+" ";
					}	
				//Create an instance of exact inferencer
				RejectionSampling aprox = new RejectionSampling();
				//Call for exact inferencer and get distribution
				Distribution dist = aprox.rejectionsampling(QVariable, a, bn,times);
				//Print the distribution
				
				System.out.print("The pobability distribution of the query variable you have entered is: "+QVariable+"|"+print+dist);
				
			}else if(args[1].contains("bif")) {
				//The second argument is the name of the file
				String filename = args[2];
				
				//Create an instance of BIFParser
				BIFParser p = new BIFParser(new FileInputStream(filename));
				//Read the file and retrieve the BayesianNetwork from it 
				BayesianNetwork bn = p.parseNetwork();
				//Get the Query variable from the BayesianNetwork
				RandomVariable QVariable = bn.getVariableByName(args[3]);
				
				//Create an instance of Assignment
				Assignment a = new Assignment();
				
				//Add the evident variables into assignment
				for(int i = 4; i < args.length;i = i + 2) {
					a.put(bn.getVariableByName(args[i]), new Value<String>(args[i+1]));
					print=print+args[i]+" ";
					}	
				//Create an instance of exact inferencer
				RejectionSampling aprox = new RejectionSampling();
				//Call for exact inferencer and get distribution
				Distribution dist = aprox.rejectionsampling(QVariable, a, bn,times);
				//Print the distribution
				System.out.print("The pobability distribution of the query variable you have entered is: "+QVariable+"|"+print+dist);
			}
		}else if(args[0].equals("LikelihoodWeighting")) {
			String print="";
			int times=Integer.parseInt(args[1]);
			//If the File is in .xml form
			if(args[2].contains("xml")) {
				//The second argument is the name of the file
				String filename = args[2];
				
				//Create an instance of XMLBIFParser
				XMLBIFParser p = new XMLBIFParser();
				//Read the file and retrieve the BayesianNetwork from it 
				BayesianNetwork bn = p.readNetworkFromFile(filename);
				//Get the Query variable from the BayesianNetwork
				RandomVariable QVariable = bn.getVariableByName(args[3]);
				//Create an instance of Assignment
				Assignment a = new Assignment();
				
				//Add the evident variables into assignment
				for(int i = 4; i < args.length;i = i + 2) {
					a.put(bn.getVariableByName(args[i]), new Value<String>(args[i+1]));
					print=print+args[i]+" ";
					}	
				//Create an instance of exact inferencer
				LikelihoodWeighting aprox = new LikelihoodWeighting();
				//Call for exact inferencer and get distribution
				Distribution dist = aprox.likelihoodweighting(QVariable, a, bn,times);
				//Print the distribution
				
				System.out.print("The pobability distribution of the query variable you have entered is: "+QVariable+"|"+print+dist);
				
			}else if(args[1].contains("bif")) {
				//The second argument is the name of the file
				String filename = args[2];
				
				//Create an instance of BIFParser
				BIFParser p = new BIFParser(new FileInputStream(filename));
				//Read the file and retrieve the BayesianNetwork from it 
				BayesianNetwork bn = p.parseNetwork();
				//Get the Query variable from the BayesianNetwork
				RandomVariable QVariable = bn.getVariableByName(args[3]);
				
				//Create an instance of Assignment
				Assignment a = new Assignment();
				//Add the evident variables into assignment
				for(int i = 4; i < args.length;i = i + 2) {
					a.put(bn.getVariableByName(args[i]), new Value<String>(args[i+1]));
					print=print+args[i]+" ";
					}	
				//Create an instance of exact inferencer
				LikelihoodWeighting aprox = new LikelihoodWeighting();
				//Call for exact inferencer and get distribution
				Distribution dist = aprox.likelihoodweighting(QVariable, a, bn,times);
				//Print the distribution
				System.out.print("The pobability distribution of the query variable you have entered is: "+QVariable+"|"+print+dist);
			}
		}
	}
}
