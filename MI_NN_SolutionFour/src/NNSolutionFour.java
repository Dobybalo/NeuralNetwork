import java.util.ArrayList;
import java.util.Scanner;

public class NNSolutionFour {

	public static void main(String[] args) {
		
		NeuralNetwork nn = new NeuralNetwork();
		
		//megnyitjuk a scannert, amit mindenki használni fog!
		Input.sc = new Scanner(System.in);
		
		//BEOLVASÁS:
		
		//elõször: tan. paraméterek, eltárolja az input osztály
		Input.readTeachingParameters();
		
		//arch
		ArrayList<Integer> layersSize = Input.readArch();

		//súlyok
		ArrayList<ArrayList<Double> > weightBiasVectors = Input.readWeights();

		//setup
		nn.setupArchitecture(layersSize, weightBiasVectors);
		
		//bemeneti minták
		ArrayList<ArrayList<Double> > originalTeachingPatterns = new ArrayList<ArrayList<Double> >();
		ArrayList<ArrayList<Double> > originalValidatingPatterns = new ArrayList<ArrayList<Double> >();
		
		Input.readTeachingPatterns(originalTeachingPatterns, originalValidatingPatterns);
		
		
		//PROBLEM:
		
		
		//1 epoch
		for (int i=0; i < Input.epochs_num; ++i) {
			
			//minden epoch-ban ÚJRA el kell "helyeznünk" a tanítási mintákat...
			// klónoznunk kell a minták vektorait...
			ArrayList<ArrayList<Double> > teachingPatterns = new ArrayList<ArrayList<Double> >();
			ArrayList<ArrayList<Double> > validatingPatterns = new ArrayList<ArrayList<Double> >();
			
			//teaching patterns klónozása
			for (ArrayList<Double> arrlist : originalTeachingPatterns) {
				
				ArrayList<Double> item = new ArrayList<Double>();
				for (double d : arrlist) {
					item.add(d);
				}
				teachingPatterns.add(item);
			}
			
			//validating patterns klónozása
			for (ArrayList<Double> arrlist : originalValidatingPatterns) {
				
				ArrayList<Double> item = new ArrayList<Double>();
				for (double d : arrlist) {
					item.add(d);
				}
				validatingPatterns.add(item);
			}
			
			//minden epoch során minden minta 1x rákerül a hálózatra
			int tpSize = teachingPatterns.size();
			for (int j=0; j < tpSize; j++) {
//	<HIBÁS?>		
				//1. következõ tanítóminta
				
					//kivesszük a következõ mintát, benne van az elvárt kimenet IS!
					ArrayList<Double> expectedOutput = teachingPatterns.remove(0);
				
					//tanítóminták végén ott az elvárt kimenet, hossza: kimeneti dimenziók száma
					int inputDim = layersSize.get(0);
					
					// kivesszük az elejérõl a tanítóminták paramétereit, és eltároljuk
					// a végén csak az marad az "expected"-ben, ami kell
					ArrayList<Double> currentTeachingPattern = new ArrayList<Double>();
					
					//kiveszünk annyi elemet, amennyi a bemenet dimenziója
					for (int k=0; k < inputDim; k++) {
						double d = expectedOutput.remove(0);
						currentTeachingPattern.add(d);
					}
 // </HIBÁS>
			
				//2. kimenet számítása	
					
					ArrayList<Double> outputVector = nn.countNetworkOutput(currentTeachingPattern);
					
				//3. hiba számítása - vektor
					
					ArrayList<Double> errorVector = new ArrayList<Double>();
					for (int k=0; k < expectedOutput.size(); k++) {
						double d = expectedOutput.get(k) - outputVector.get(k);
						errorVector.add(d);
					}
					
				//4. költségfgv számítása
					
					//?????
					
				//5. hibavisszaterjesztés - delták...

					nn.calculateAllDeltas(true, errorVector);
					
				//6. súlyok módosítása
					
					nn.modifyWeights();
	
			}
			
		//VALIDÁCIÓ
			
			//itt tároljuk a kimeneteket
			//ArrayList<ArrayList<Double> > outputVectors = new ArrayList<ArrayList<Double> >();
			//ArrayList<ArrayList<Double> > errorVectors = new ArrayList<ArrayList<Double> >();
			
			//csak a négyzetes hibák értékét tároljuk el (hozzáadjuk), nem kell a vektor
			double errorSum = 0;
			
			int vpSize = validatingPatterns.size();
			for (int j=0; j < vpSize; j++) {
				
				// következõ validációs minta
				
					//kivesszük a következõ mintát, benne van az elvárt kimenet IS!
					ArrayList<Double> expectedValidationOutput = validatingPatterns.remove(0);
				
					//tanítóminták végén ott az elvárt kimenet, hossza: kimeneti dimenziók száma
					int inputDim = layersSize.get(0);
					
					// kivesszük az elejérõl a validációs minták paramétereit, és eltároljuk
					// a végén csak az marad az "expected"-ben, ami kell
					ArrayList<Double> currentValidatingPattern = new ArrayList<Double>();
					
					//kiveszünk annyi elemet, amennyi a bemenet dimenziója
					for (int k=0; k < inputDim; k++) {
						double d = expectedValidationOutput.remove(0);
						currentValidatingPattern.add(d);
					}
			
				// kimenet számítása	
					
					ArrayList<Double> outputVector = nn.countNetworkOutput(currentValidatingPattern);
					
				// négyzetes(!) hibák számítása
					
					//mindig hozzáadjuk a négyzetes hibákat errorSum-hoz
					for (int k=0; k < expectedValidationOutput.size(); k++) {
						double d = expectedValidationOutput.get(k) - outputVector.get(k);
						d *= d;	//négyzetre emeljük
						errorSum += d;
					}
			
			}
			
			//ÁTLAGoljuk a négyzetes hibákat!
			
			//kimenet dimenziója
			int outputDim = layersSize.get(layersSize.size()-1);
			
			//átlagoljuk az összeget (sV * M)-mel
			errorSum = errorSum / (vpSize * outputDim);
			
			//KIÍRÁS:

			//átlagos négyzetes hibák, a validációs minták alapján
			Output.printAverageError(errorSum);
	
			//a súlyokat és az architektúrát csak a végén kell, 1x kiíratni...
			
		}
		
		//architektúra
		Output.printArchitecture(layersSize);
		
		//súlyok kiíratása
		ArrayList<ArrayList<Double> > modifiedWeights = new ArrayList<ArrayList<Double> >();
		
		nn.appendWeights(modifiedWeights);
			
		Output.printWeightsAndBias(modifiedWeights);
	
	}

}
