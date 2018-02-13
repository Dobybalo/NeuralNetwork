import java.util.ArrayList;
import java.util.Scanner;

public class NNSolutionFour {

	public static void main(String[] args) {
		
		NeuralNetwork nn = new NeuralNetwork();
		
		//megnyitjuk a scannert, amit mindenki haszn�lni fog!
		Input.sc = new Scanner(System.in);
		
		//BEOLVAS�S:
		
		//el�sz�r: tan. param�terek, elt�rolja az input oszt�ly
		Input.readTeachingParameters();
		
		//arch
		ArrayList<Integer> layersSize = Input.readArch();

		//s�lyok
		ArrayList<ArrayList<Double> > weightBiasVectors = Input.readWeights();

		//setup
		nn.setupArchitecture(layersSize, weightBiasVectors);
		
		//bemeneti mint�k
		ArrayList<ArrayList<Double> > originalTeachingPatterns = new ArrayList<ArrayList<Double> >();
		ArrayList<ArrayList<Double> > originalValidatingPatterns = new ArrayList<ArrayList<Double> >();
		
		Input.readTeachingPatterns(originalTeachingPatterns, originalValidatingPatterns);
		
		
		//PROBLEM:
		
		
		//1 epoch
		for (int i=0; i < Input.epochs_num; ++i) {
			
			//minden epoch-ban �JRA el kell "helyezn�nk" a tan�t�si mint�kat...
			// kl�noznunk kell a mint�k vektorait...
			ArrayList<ArrayList<Double> > teachingPatterns = new ArrayList<ArrayList<Double> >();
			ArrayList<ArrayList<Double> > validatingPatterns = new ArrayList<ArrayList<Double> >();
			
			//teaching patterns kl�noz�sa
			for (ArrayList<Double> arrlist : originalTeachingPatterns) {
				
				ArrayList<Double> item = new ArrayList<Double>();
				for (double d : arrlist) {
					item.add(d);
				}
				teachingPatterns.add(item);
			}
			
			//validating patterns kl�noz�sa
			for (ArrayList<Double> arrlist : originalValidatingPatterns) {
				
				ArrayList<Double> item = new ArrayList<Double>();
				for (double d : arrlist) {
					item.add(d);
				}
				validatingPatterns.add(item);
			}
			
			//minden epoch sor�n minden minta 1x r�ker�l a h�l�zatra
			int tpSize = teachingPatterns.size();
			for (int j=0; j < tpSize; j++) {
//	<HIB�S?>		
				//1. k�vetkez� tan�t�minta
				
					//kivessz�k a k�vetkez� mint�t, benne van az elv�rt kimenet IS!
					ArrayList<Double> expectedOutput = teachingPatterns.remove(0);
				
					//tan�t�mint�k v�g�n ott az elv�rt kimenet, hossza: kimeneti dimenzi�k sz�ma
					int inputDim = layersSize.get(0);
					
					// kivessz�k az elej�r�l a tan�t�mint�k param�tereit, �s elt�roljuk
					// a v�g�n csak az marad az "expected"-ben, ami kell
					ArrayList<Double> currentTeachingPattern = new ArrayList<Double>();
					
					//kivesz�nk annyi elemet, amennyi a bemenet dimenzi�ja
					for (int k=0; k < inputDim; k++) {
						double d = expectedOutput.remove(0);
						currentTeachingPattern.add(d);
					}
 // </HIB�S>
			
				//2. kimenet sz�m�t�sa	
					
					ArrayList<Double> outputVector = nn.countNetworkOutput(currentTeachingPattern);
					
				//3. hiba sz�m�t�sa - vektor
					
					ArrayList<Double> errorVector = new ArrayList<Double>();
					for (int k=0; k < expectedOutput.size(); k++) {
						double d = expectedOutput.get(k) - outputVector.get(k);
						errorVector.add(d);
					}
					
				//4. k�lts�gfgv sz�m�t�sa
					
					//?????
					
				//5. hibavisszaterjeszt�s - delt�k...

					nn.calculateAllDeltas(true, errorVector);
					
				//6. s�lyok m�dos�t�sa
					
					nn.modifyWeights();
	
			}
			
		//VALID�CI�
			
			//itt t�roljuk a kimeneteket
			//ArrayList<ArrayList<Double> > outputVectors = new ArrayList<ArrayList<Double> >();
			//ArrayList<ArrayList<Double> > errorVectors = new ArrayList<ArrayList<Double> >();
			
			//csak a n�gyzetes hib�k �rt�k�t t�roljuk el (hozz�adjuk), nem kell a vektor
			double errorSum = 0;
			
			int vpSize = validatingPatterns.size();
			for (int j=0; j < vpSize; j++) {
				
				// k�vetkez� valid�ci�s minta
				
					//kivessz�k a k�vetkez� mint�t, benne van az elv�rt kimenet IS!
					ArrayList<Double> expectedValidationOutput = validatingPatterns.remove(0);
				
					//tan�t�mint�k v�g�n ott az elv�rt kimenet, hossza: kimeneti dimenzi�k sz�ma
					int inputDim = layersSize.get(0);
					
					// kivessz�k az elej�r�l a valid�ci�s mint�k param�tereit, �s elt�roljuk
					// a v�g�n csak az marad az "expected"-ben, ami kell
					ArrayList<Double> currentValidatingPattern = new ArrayList<Double>();
					
					//kivesz�nk annyi elemet, amennyi a bemenet dimenzi�ja
					for (int k=0; k < inputDim; k++) {
						double d = expectedValidationOutput.remove(0);
						currentValidatingPattern.add(d);
					}
			
				// kimenet sz�m�t�sa	
					
					ArrayList<Double> outputVector = nn.countNetworkOutput(currentValidatingPattern);
					
				// n�gyzetes(!) hib�k sz�m�t�sa
					
					//mindig hozz�adjuk a n�gyzetes hib�kat errorSum-hoz
					for (int k=0; k < expectedValidationOutput.size(); k++) {
						double d = expectedValidationOutput.get(k) - outputVector.get(k);
						d *= d;	//n�gyzetre emelj�k
						errorSum += d;
					}
			
			}
			
			//�TLAGoljuk a n�gyzetes hib�kat!
			
			//kimenet dimenzi�ja
			int outputDim = layersSize.get(layersSize.size()-1);
			
			//�tlagoljuk az �sszeget (sV * M)-mel
			errorSum = errorSum / (vpSize * outputDim);
			
			//KI�R�S:

			//�tlagos n�gyzetes hib�k, a valid�ci�s mint�k alapj�n
			Output.printAverageError(errorSum);
	
			//a s�lyokat �s az architekt�r�t csak a v�g�n kell, 1x ki�ratni...
			
		}
		
		//architekt�ra
		Output.printArchitecture(layersSize);
		
		//s�lyok ki�rat�sa
		ArrayList<ArrayList<Double> > modifiedWeights = new ArrayList<ArrayList<Double> >();
		
		nn.appendWeights(modifiedWeights);
			
		Output.printWeightsAndBias(modifiedWeights);
	
	}

}
