import java.util.ArrayList;
import java.util.Scanner;
//ezamostani
public class NNSolutionThree {

	public static void main(String[] args) {

		NeuralNetwork nn = new NeuralNetwork();
		
		//megnyitjuk a scannert, amit mindenki haszn�lni fog!
		Input.sc = new Scanner(System.in);
		
		//el�sz�r arch
		ArrayList<Integer> layersSize = Input.readArch();
		
		//s�lyok
		ArrayList<ArrayList<Double> > weightBiasVectors = Input.readWeights();
		
		//bemenetek
		ArrayList<ArrayList<Double> > inputVectors = Input.readInputVectors();
		
		//public NeuralNetwork setupArchitecture(ArrayList<Integer> layersSize, 
		// ArrayList<ArrayList<Double> > weightBiasVectors)
		
		nn = nn.setupArchitecture(layersSize, weightBiasVectors);
		
		//beadogatjuk neki EGYENK�NT a bemeneti vektorokat!
		//ciklus:  	beolvasunk vektort
		//			r�nyomjuk a h�l�zatra -> kimenet
		//			benyomjuk list�ba az eredm�nyt
		
		ArrayList<ArrayList<Double> > outputVectors = new ArrayList<ArrayList<Double> >();
		for (int i=0; i < Input.inputsNum; ++i) {
			
			//els� vektort kivessz�k
			ArrayList<Double> currentInputVector = inputVectors.remove(0);
			
			//r�adjuk a h�l�zatra, megkapjuk a kimenet�t
			ArrayList<Double> outputVector = nn.countNetworkOutput(currentInputVector);
		
			//betessz�k a kapott vektort list�ba
			outputVectors.add(outputVector);
			
			//kisz�moljuk a delt�kat
			nn.calculateAllDeltas(false, null);
		}
		
		//itt m�s a ki�r�s!
		//el�sz�r architekt�ra
		Output.printArchitecture(layersSize);
		
		//NNThreeOutput
		ArrayList<ArrayList<Double> > NNThreeOutputs = nn.countNNThreeOutput();
		Output.printWeightsAndBias(NNThreeOutputs);
		
		//bez�rjuk a scannert!
		Input.sc.close(); 
	}

}
