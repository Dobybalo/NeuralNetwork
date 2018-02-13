import java.util.ArrayList;
//ezamostani
public class Layer {
	
	private ArrayList<Neuron> neurons;
	
	private boolean isFirstLayer = false;
	private boolean isLastLayer = false;
	
	//bemenetenk�nt k�l�nb�z� - fut�sonk�nt v�ltozik
	private ArrayList<Double> currentDerivatives;
	
	//r�teghez tartoz� neuronok delt�i
	private ArrayList<Double> deltasVector;
	
	//elt�roljuk a r�teg bemeneti vektor�t
	private ArrayList<Double> inputVector;
	
	public Layer(int layerSize, ArrayList<ArrayList<Double> > weightsAndBiasVectors, int previousLayerSize, 
			boolean isFirst, boolean isLast) { 
		
		neurons = new ArrayList<Neuron>();
		
		//megjegyezz�k, hogy uts� r�teg-e, mert ott m�s a deriv�lt!
		isLastLayer = isLast;
		
		//kell k�s�bb, hogy ez-e az els� r�teg...
		isFirstLayer = isFirst;
		
		for (int i = 0; i < layerSize; ++i) {
			
			//itt p�ld�nyos�tjuk, kik�sz�b�lj�k, hogy az els� r�tegben is 
			//	vannak neuronok, de nincs hozz�juk s�lyvektor
			
			ArrayList<Double> weightBiasVector = new ArrayList<Double>();
			
			//i-edik vektort kapja az i-edik neuron(kiv�ve els� r�teg!)
			if (!isFirst) weightBiasVector = weightsAndBiasVectors.get(i); 
			
			//�j neuron, param�ter: S�LYOK!, inputok sz�ma, azaz el�z� r�teg m�rete!
			Neuron n = new Neuron(weightBiasVector, previousLayerSize, isFirst, isLast);
			neurons.add(n);
		}
	}
	
	
	//bemeneti vektor alapj�n kisz�m�tjuk a kimenetit
	public ArrayList<Double> countOutputVector(ArrayList<Double> inputVector) {
	
		//elt�roljuk, NNThree-hez sz�ks�ges...
		// NE v�gezz�nk rajta m�veleteket, REFERENCIA!!!
		this.inputVector = inputVector;
		
		ArrayList<Double> outputVector = new ArrayList<Double>();
		for (Neuron n : neurons) {
			double d = n.countOutput(inputVector);
			outputVector.add(d);
		}
		
		//elt�roljuk a kimenetek deriv�ltjait - delt�k kisz�m�t�s�hoz kell...
		partialDerivate(outputVector);
		
		return outputVector;
		
	}
	
	
	
	//NNSolutionThree outputja: "s�lyok - s�lyok �s biasok hely�n a parci�lis deriv�ltak" 
	//m�r ki kell sz�molva lennie a delt�knak �s a h�l�zat kimenet�nek
	//param�ter�l kapott list�hoz f�zz�k hozz� az eredm�nyeket
	public void countNNThreeOutput(ArrayList<ArrayList<Double> > results) {
		
		//deleg�lunk a neuronoknak,
		//hozz�adj�k az eredm�nylist�hoz a saj�tjukat
		for (Neuron n : neurons) {
			ArrayList<Double> item = n.countNNThreeOutput(inputVector);
			results.add(item);
		}
	}
	
	/*
	//r�teg neuronjainak delt�i, sorban
	public void countDeltasVector(Layer nextLayer) {
		
		deltasVector = new ArrayList<Double>();
		
		//hogy sz�molni tudjunk,
		// a k�vetkez� r�teg delt�inak m�r ki kell sz�molva lennie (?)
		
		for (int i=0; i < neurons.size(); ++i) {
			double delta_i;
			if (isLastLayer) {
				delta_i = 1;	//k�telez�en
			}
			else {
				delta_i = nextLayer.countDeltaWeightsSum(i) * currentDerivatives.get(i);
				
			}
	
			deltasVector.add(delta_i);
		}
		
		
		// 1 elemre:
		//double delta_i = nextLayer.countDeltaWeightsSum(i) * currentDerivatives.get(i);
		
	}
	*/
	
	//iter�ci�b�l h�vjuk, azaz
	//	felt�telezhetj�k, hogy m�r KI VAN SZ�MOLVA
	//	a k�vetkez� r�teg delta-vektora
	
	//ha teaching==true, akkor uts� r�tegben: delta[i] := error[i]
	public void countDeltaVector(Layer nextLayer, boolean teaching, ArrayList<Double> error) {
		
		deltasVector = new ArrayList<Double>();
		
		for (int i=0; i < neurons.size(); ++i) {
			double delta_i;
			if (isLastLayer) {
				
				if (!teaching) {
					delta_i = 1;	//k�telez�en
				}
				else {
					delta_i = error.get(i);	//uts� neuron delt�ja a hiba lesz
				}
			}
			else {
				
				delta_i = nextLayer.countDeltaWeightsSum(i) * currentDerivatives.get(i);
				
			}
	
			// �tadjuk a neuronoknak a hozz�juk tartoz� delt�t???
			//	lehet felesleges l�p�s...
			neurons.get(i).setDelta(delta_i);
			
			//ki�r�s miatt hasznos lesz...
			deltasVector.add(delta_i);
		}
	
	}
	
	//szumm�z�s - meg kell adnunk, hanyadik neuronhoz tartoz� �rt�ket akarjuk...
	private double countDeltaWeightsSum(int i) {
		
		double sum = 0;
		for (Neuron n : neurons) {
			
			//NEM sz�m�thatjuk ki neurononk�nt, mert VEKTORokkal dolgozunk!
			//	MOST lehet j�...
			sum += n.countSumElement(i);
			
			/* teh�t:
			 * elk�rj�k minden neuront�l a S�LYOKAT,
			 * ITT beszorozgatjuk majd... 
			 */
		}
		
		return sum;
	}
	
	//elt�roljuk r�tegenk�nt, hogy mi az AKTU�LIS kimenetvektor deriv�ltja(vektor)
	private void partialDerivate(ArrayList<Double> outputVector) {
		
		
		currentDerivatives = new ArrayList<Double>();
		
		//deriv�lt: uts� r�tegben: k�telez�en 1, minden neuronhoz;
		// egy�bk�nt ReLu deriv�ltja...
		for (double d : outputVector) {
			
			if (isLastLayer || d > 0) {
				currentDerivatives.add(1.0);
			}
			else {
				currentDerivatives.add(0.0);
			}
		}
	}
	
	//r�teg neuronjaiban m�dos�tjuk a s�lyt
	public void modifyNeuronWeights() {
		
		if (!isFirstLayer)
		for (Neuron n : neurons) {
			n.modifyWeights(inputVector);
		}
	}
	
	//param�ter�l kapott cucchoz f�zi hozz� mindenki a s�lyokat
	public void getAllNeuronWeights(ArrayList<ArrayList<Double> > weights) {
		if (!isFirstLayer) {
			
			for (Neuron n : neurons) {
				
				
				n.appendWeightsTo(weights);
			}
		}
	}
	
}
