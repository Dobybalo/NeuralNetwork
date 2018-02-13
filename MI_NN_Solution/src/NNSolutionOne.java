import java.util.Scanner;
import java.util.ArrayList;
import java.util.Random;
//ezvégleges
public class NNSolutionOne {
	
	
	public static void main(String args[]){

		NeuralNetwork nn = new NeuralNetwork();
		
		//megnyitjuk a scannert!
		Input.sc = new Scanner(System.in);
		
		//beolv
		//arch
		ArrayList<Integer> layersSize = Input.readArch();
		
		//súlyok
		ArrayList<ArrayList<Double> > weightBiasVectors = Input.generateRandomWeightZeroBiasVectors();
		
		//setup-?
		nn = nn.setupArchitecture(layersSize, weightBiasVectors);
		
		//kiírás
		//arch
		Output.printArchitecture(layersSize);
		
				
		//súlyok
		Output.printWeightsAndBias(weightBiasVectors);
		
		
		
		//bezárjuk a scannert!!
		Input.sc.close();
	}

}
