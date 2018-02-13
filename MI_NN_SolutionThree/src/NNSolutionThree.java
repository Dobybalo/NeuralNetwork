import java.util.ArrayList;
import java.util.Scanner;
//ezamostani
public class NNSolutionThree {

	public static void main(String[] args) {

		NeuralNetwork nn = new NeuralNetwork();
		
		//megnyitjuk a scannert, amit mindenki használni fog!
		Input.sc = new Scanner(System.in);
		
		//elõször arch
		ArrayList<Integer> layersSize = Input.readArch();
		
		//súlyok
		ArrayList<ArrayList<Double> > weightBiasVectors = Input.readWeights();
		
		//bemenetek
		ArrayList<ArrayList<Double> > inputVectors = Input.readInputVectors();
		
		//public NeuralNetwork setupArchitecture(ArrayList<Integer> layersSize, 
		// ArrayList<ArrayList<Double> > weightBiasVectors)
		
		nn = nn.setupArchitecture(layersSize, weightBiasVectors);
		
		//beadogatjuk neki EGYENKÉNT a bemeneti vektorokat!
		//ciklus:  	beolvasunk vektort
		//			rányomjuk a hálózatra -> kimenet
		//			benyomjuk listába az eredményt
		
		ArrayList<ArrayList<Double> > outputVectors = new ArrayList<ArrayList<Double> >();
		for (int i=0; i < Input.inputsNum; ++i) {
			
			//elsõ vektort kivesszük
			ArrayList<Double> currentInputVector = inputVectors.remove(0);
			
			//ráadjuk a hálózatra, megkapjuk a kimenetét
			ArrayList<Double> outputVector = nn.countNetworkOutput(currentInputVector);
		
			//betesszük a kapott vektort listába
			outputVectors.add(outputVector);
			
			//kiszámoljuk a deltákat
			nn.calculateAllDeltas(false, null);
		}
		
		//itt más a kiírás!
		//elõször architektúra
		Output.printArchitecture(layersSize);
		
		//NNThreeOutput
		ArrayList<ArrayList<Double> > NNThreeOutputs = nn.countNNThreeOutput();
		Output.printWeightsAndBias(NNThreeOutputs);
		
		//bezárjuk a scannert!
		Input.sc.close(); 
	}

}
