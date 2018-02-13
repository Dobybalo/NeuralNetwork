import java.util.ArrayList;
import java.util.Scanner;
//ezamostani
public class NeuralNetwork {

	private ArrayList<Layer> layers = new ArrayList<Layer>();
	
	private int inputVectorNum = 0;
	private boolean isInputVectorNumPrinted = false;
	
	public ArrayList<Layer> getLayers() {
		return layers;
	}
	
	//paraméterek: rétegek méretei, (súlyok+bias)vektorok
	public NeuralNetwork setupArchitecture(ArrayList<Integer> layersSize, 
								ArrayList<ArrayList<Double> > wbVector) {
	
		//ne az eredetit módosítsuk...átcopyzzuk a vektorokat...
		ArrayList<ArrayList<Double> > weightBiasVectors = new ArrayList<ArrayList<Double> >();
		for (ArrayList<Double> al : wbVector) {
			
			ArrayList<Double> result = new ArrayList<Double>();
			for (double d : al) {
				result.add(d);
			}
			
			weightBiasVectors.add(result);
		}
		
		
		//elõzõ réteg mérete - bemeneti rétegnél 0
		int lastLayerSize = 0;
		
		// csinálhatnád, hogy: megnézed mekkora a méret, 
		//		annyi vektort kiszedsz mindig a lista elejérõl!
		
		//beolvassuk a rétegek méreteit 
		for (int layer_num = 0; layer_num < layersSize.size(); ++layer_num) {
			
			boolean isFirst = false;
			boolean isLast = false;
			
			//elmentjük, hogy melyik az elsõ és az utsó réteg 
			if (layer_num == 0) isFirst = true;
			else if (layer_num == layersSize.size()-1) isLast = true;
			
			//hány neuronja lesz a rétegnek
			int currentLayerSize = layersSize.get(layer_num);
			
			//probléma: nem kezeltük külön az elsõ réteget!
			//	-> át kell ugranunk, de adnunk kell neki vmilyen vektort 
			
			//ennyi vektort KISZEDÜNK a listából!
			ArrayList<ArrayList<Double> > currentLayerNeuronWeights = new ArrayList<ArrayList<Double> >();
			
			//kiküsz. elsõ réteg!
			ArrayList<Double> currentVector = new ArrayList<Double>();
			
			for (int vectorNum = 0; vectorNum < currentLayerSize; vectorNum++) {
			
					//elsõ réteg üres vektort kap
					if (!isFirst) currentVector = weightBiasVectors.remove(0);
					
					//hozzáadjuk a jelenlegi réteg neuronjainak súlyát tartalmazó vektorhoz
					currentLayerNeuronWeights.add(currentVector);
				
			}
			
			//felépítjük a réteget: átadjuk neki, hogy mennyi neuront kell tartalmazzon...
			//JAV.: átadjuk neki a beolvasott súlyvektorokat!
			
			Layer l = new Layer(currentLayerSize, currentLayerNeuronWeights, lastLayerSize, isFirst, isLast);
			layers.add(l);
			
			//eltároljuk a réteg méretét, a következõ réteg bemeneteinek számát adja
			lastLayerSize = currentLayerSize;
		}
		
		return this;
	}
	
	//a háló kimenete, 
	//EDIT: meg kell kapja paraméterként az inputból beolvasott bemeneti vektor(listá)t
	//returns: outputVector!
	public ArrayList<Double> countNetworkOutput(ArrayList<Double> inputVector) {
	
		//második elemtõl kezdjük, elsõ réteg kimenete a beolvasott vektor
		//számolunk, számolunk...
		for (int j = 1; j < layers.size(); ++j) {
			inputVector = layers.get(j).countOutputVector(inputVector);  
		}
		
		return inputVector;
	}
	
	// HA ciklussal oldjuk meg...
	//NNFour: a hiba lesz az utsó réteg neuronjain a delta!
	public void calculateAllDeltas(boolean teaching, ArrayList<Double> error) {
		
		//végigmegyünk a rétegeken VISSZAFELÉ,
		//utsó rétegnél másképp számolunk,
		//elsõ réteg a bemenet -> kimarad
		for (int i=layers.size()-1; i > 0; i--) {
			
			//jelenlegi réteg
			Layer currentLayer = layers.get(i);
			
			//utsó rétegnél másképp...
			if (i == layers.size()-1) {
				
				currentLayer.countDeltaVector(null, teaching, error);
				
			}
			else {
				
				Layer nextLayer = layers.get(i+1);
				
				currentLayer.countDeltaVector(nextLayer, teaching, error);
				
			}
			
		}
	}
	
	public ArrayList<ArrayList<Double> > countNNThreeOutput() {
		ArrayList<ArrayList<Double> > results = new ArrayList<ArrayList<Double> >();
		
		//végigmegyünk a rétegeken,
		//	azok hozzáfûzik a "result"-hoz a saját eredményeiket
		//1. réteg kimarad!
		for(int i=1; i < layers.size(); ++i) {
			Layer l = layers.get(i);
			l.countNNThreeOutput(results);
		}
		
		return results;
	}
	
	//súlyok módosítása, bemenetüket eltároltuk rétegenként
	public void modifyWeights() {
		for (Layer l : layers) {
			l.modifyNeuronWeights();
		}
	}
	
	//a paraméterül kapott vektorhoz append-elik a rétegek, neuronok a súlyokat
	public void appendWeights(ArrayList<ArrayList<Double> > weights) {
		for (Layer l : layers) {
			l.getAllNeuronWeights(weights);
		}
	}
	
}
