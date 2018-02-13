import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
//ezv�gleges
public class NNSolutionOne {
	
	
	public static void main(String args[]){

		NeuralNetwork nn = new NeuralNetwork();
		
		//megnyitjuk a scannert!
		Input.sc = new Scanner(System.in);
		
		//beolv
		//arch
		ArrayList<Integer> layersSize = Input.readArch();
		
		//s�lyok
		ArrayList<ArrayList<Double> > weightBiasVectors = Input.generateRandomWeightZeroBiasVectors();
		
		//setup-?
		nn = nn.setupArchitecture(layersSize, weightBiasVectors);
		
		//ki�r�s
		//arch
		Output.printArchitecture(layersSize);
		
				
		//s�lyok
		Output.printWeightsAndBias(weightBiasVectors);
		
		
		
		//bez�rjuk a scannert!!
		Input.sc.close();
	}

}
