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
	
	//param�terek: r�tegek m�retei, (s�lyok+bias)vektorok
	public NeuralNetwork setupArchitecture(ArrayList<Integer> layersSize, 
								ArrayList<ArrayList<Double> > wbVector) {
	
		//ne az eredetit m�dos�tsuk...�tcopyzzuk a vektorokat...
		ArrayList<ArrayList<Double> > weightBiasVectors = new ArrayList<ArrayList<Double> >();
		for (ArrayList<Double> al : wbVector) {
			
			ArrayList<Double> result = new ArrayList<Double>();
			for (double d : al) {
				result.add(d);
			}
			
			weightBiasVectors.add(result);
		}
		
		
		//el�z� r�teg m�rete - bemeneti r�tegn�l 0
		int lastLayerSize = 0;
		
		// csin�lhatn�d, hogy: megn�zed mekkora a m�ret, 
		//		annyi vektort kiszedsz mindig a lista elej�r�l!
		
		//beolvassuk a r�tegek m�reteit 
		for (int layer_num = 0; layer_num < layersSize.size(); ++layer_num) {
			
			boolean isFirst = false;
			boolean isLast = false;
			
			//elmentj�k, hogy melyik az els� �s az uts� r�teg 
			if (layer_num == 0) isFirst = true;
			else if (layer_num == layersSize.size()-1) isLast = true;
			
			//h�ny neuronja lesz a r�tegnek
			int currentLayerSize = layersSize.get(layer_num);
			
			//probl�ma: nem kezelt�k k�l�n az els� r�teget!
			//	-> �t kell ugranunk, de adnunk kell neki vmilyen vektort 
			
			//ennyi vektort KISZED�NK a list�b�l!
			ArrayList<ArrayList<Double> > currentLayerNeuronWeights = new ArrayList<ArrayList<Double> >();
			
			//kik�sz. els� r�teg!
			ArrayList<Double> currentVector = new ArrayList<Double>();
			
			for (int vectorNum = 0; vectorNum < currentLayerSize; vectorNum++) {
			
					//els� r�teg �res vektort kap
					if (!isFirst) currentVector = weightBiasVectors.remove(0);
					
					//hozz�adjuk a jelenlegi r�teg neuronjainak s�ly�t tartalmaz� vektorhoz
					currentLayerNeuronWeights.add(currentVector);
				
			}
			
			//fel�p�tj�k a r�teget: �tadjuk neki, hogy mennyi neuront kell tartalmazzon...
			//JAV.: �tadjuk neki a beolvasott s�lyvektorokat!
			
			Layer l = new Layer(currentLayerSize, currentLayerNeuronWeights, lastLayerSize, isFirst, isLast);
			layers.add(l);
			
			//elt�roljuk a r�teg m�ret�t, a k�vetkez� r�teg bemeneteinek sz�m�t adja
			lastLayerSize = currentLayerSize;
		}
		
		return this;
	}
	
	//a h�l� kimenete, 
	//EDIT: meg kell kapja param�terk�nt az inputb�l beolvasott bemeneti vektor(list�)t
	//returns: outputVector!
	public ArrayList<Double> countNetworkOutput(ArrayList<Double> inputVector) {
	
		//m�sodik elemt�l kezdj�k, els� r�teg kimenete a beolvasott vektor
		//sz�molunk, sz�molunk...
		for (int j = 1; j < layers.size(); ++j) {
			inputVector = layers.get(j).countOutputVector(inputVector);  
		}
		
		return inputVector;
	}
	
	// HA ciklussal oldjuk meg...
	//NNFour: a hiba lesz az uts� r�teg neuronjain a delta!
	public void calculateAllDeltas(boolean teaching, ArrayList<Double> error) {
		
		//v�gigmegy�nk a r�tegeken VISSZAFEL�,
		//uts� r�tegn�l m�sk�pp sz�molunk,
		//els� r�teg a bemenet -> kimarad
		for (int i=layers.size()-1; i > 0; i--) {
			
			//jelenlegi r�teg
			Layer currentLayer = layers.get(i);
			
			//uts� r�tegn�l m�sk�pp...
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
		
		//v�gigmegy�nk a r�tegeken,
		//	azok hozz�f�zik a "result"-hoz a saj�t eredm�nyeiket
		//1. r�teg kimarad!
		for(int i=1; i < layers.size(); ++i) {
			Layer l = layers.get(i);
			l.countNNThreeOutput(results);
		}
		
		return results;
	}
	
	//s�lyok m�dos�t�sa, bemenet�ket elt�roltuk r�tegenk�nt
	public void modifyWeights() {
		for (Layer l : layers) {
			l.modifyNeuronWeights();
		}
	}
	
	//a param�ter�l kapott vektorhoz append-elik a r�tegek, neuronok a s�lyokat
	public void appendWeights(ArrayList<ArrayList<Double> > weights) {
		for (Layer l : layers) {
			l.getAllNeuronWeights(weights);
		}
	}
	
}
