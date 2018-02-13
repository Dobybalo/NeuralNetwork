import java.util.ArrayList;
//ezamostani
public class Layer {
	
	private ArrayList<Neuron> neurons;
	
	private boolean isFirstLayer = false;
	private boolean isLastLayer = false;
	
	//bemenetenként különbözõ - futásonként változik
	private ArrayList<Double> currentDerivatives;
	
	//réteghez tartozó neuronok deltái
	private ArrayList<Double> deltasVector;
	
	//eltároljuk a réteg bemeneti vektorát
	private ArrayList<Double> inputVector;
	
	public Layer(int layerSize, ArrayList<ArrayList<Double> > weightsAndBiasVectors, int previousLayerSize, 
			boolean isFirst, boolean isLast) { 
		
		neurons = new ArrayList<Neuron>();
		
		//megjegyezzük, hogy utsó réteg-e, mert ott más a derivált!
		isLastLayer = isLast;
		
		//kell késõbb, hogy ez-e az elsõ réteg...
		isFirstLayer = isFirst;
		
		for (int i = 0; i < layerSize; ++i) {
			
			//itt példányosítjuk, kiküszöböljük, hogy az elsõ rétegben is 
			//	vannak neuronok, de nincs hozzájuk súlyvektor
			
			ArrayList<Double> weightBiasVector = new ArrayList<Double>();
			
			//i-edik vektort kapja az i-edik neuron(kivéve elsõ réteg!)
			if (!isFirst) weightBiasVector = weightsAndBiasVectors.get(i); 
			
			//új neuron, paraméter: SÚLYOK!, inputok száma, azaz elõzõ réteg mérete!
			Neuron n = new Neuron(weightBiasVector, previousLayerSize, isFirst, isLast);
			neurons.add(n);
		}
	}
	
	
	//bemeneti vektor alapján kiszámítjuk a kimenetit
	public ArrayList<Double> countOutputVector(ArrayList<Double> inputVector) {
	
		//eltároljuk, NNThree-hez szükséges...
		// NE végezzünk rajta mûveleteket, REFERENCIA!!!
		this.inputVector = inputVector;
		
		ArrayList<Double> outputVector = new ArrayList<Double>();
		for (Neuron n : neurons) {
			double d = n.countOutput(inputVector);
			outputVector.add(d);
		}
		
		//eltároljuk a kimenetek deriváltjait - delták kiszámításához kell...
		partialDerivate(outputVector);
		
		return outputVector;
		
	}
	
	
	
	//NNSolutionThree outputja: "súlyok - súlyok és biasok helyén a parciális deriváltak" 
	//már ki kell számolva lennie a deltáknak és a hálózat kimenetének
	//paraméterül kapott listához fûzzük hozzá az eredményeket
	public void countNNThreeOutput(ArrayList<ArrayList<Double> > results) {
		
		//delegálunk a neuronoknak,
		//hozzáadják az eredménylistához a sajátjukat
		for (Neuron n : neurons) {
			ArrayList<Double> item = n.countNNThreeOutput(inputVector);
			results.add(item);
		}
	}
	
	/*
	//réteg neuronjainak deltái, sorban
	public void countDeltasVector(Layer nextLayer) {
		
		deltasVector = new ArrayList<Double>();
		
		//hogy számolni tudjunk,
		// a következõ réteg deltáinak már ki kell számolva lennie (?)
		
		for (int i=0; i < neurons.size(); ++i) {
			double delta_i;
			if (isLastLayer) {
				delta_i = 1;	//kötelezõen
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
	
	//iterációból hívjuk, azaz
	//	feltételezhetjük, hogy már KI VAN SZÁMOLVA
	//	a következõ réteg delta-vektora
	
	//ha teaching==true, akkor utsó rétegben: delta[i] := error[i]
	public void countDeltaVector(Layer nextLayer, boolean teaching, ArrayList<Double> error) {
		
		deltasVector = new ArrayList<Double>();
		
		for (int i=0; i < neurons.size(); ++i) {
			double delta_i;
			if (isLastLayer) {
				
				if (!teaching) {
					delta_i = 1;	//kötelezõen
				}
				else {
					delta_i = error.get(i);	//utsó neuron deltája a hiba lesz
				}
			}
			else {
				
				delta_i = nextLayer.countDeltaWeightsSum(i) * currentDerivatives.get(i);
				
			}
	
			// átadjuk a neuronoknak a hozzájuk tartozó deltát???
			//	lehet felesleges lépés...
			neurons.get(i).setDelta(delta_i);
			
			//kiírás miatt hasznos lesz...
			deltasVector.add(delta_i);
		}
	
	}
	
	//szummázás - meg kell adnunk, hanyadik neuronhoz tartozó értéket akarjuk...
	private double countDeltaWeightsSum(int i) {
		
		double sum = 0;
		for (Neuron n : neurons) {
			
			//NEM számíthatjuk ki neurononként, mert VEKTORokkal dolgozunk!
			//	MOST lehet jó...
			sum += n.countSumElement(i);
			
			/* tehát:
			 * elkérjük minden neurontól a SÚLYOKAT,
			 * ITT beszorozgatjuk majd... 
			 */
		}
		
		return sum;
	}
	
	//eltároljuk rétegenként, hogy mi az AKTUÁLIS kimenetvektor deriváltja(vektor)
	private void partialDerivate(ArrayList<Double> outputVector) {
		
		
		currentDerivatives = new ArrayList<Double>();
		
		//derivált: utsó rétegben: kötelezõen 1, minden neuronhoz;
		// egyébként ReLu deriváltja...
		for (double d : outputVector) {
			
			if (isLastLayer || d > 0) {
				currentDerivatives.add(1.0);
			}
			else {
				currentDerivatives.add(0.0);
			}
		}
	}
	
	//réteg neuronjaiban módosítjuk a súlyt
	public void modifyNeuronWeights() {
		
		if (!isFirstLayer)
		for (Neuron n : neurons) {
			n.modifyWeights(inputVector);
		}
	}
	
	//paraméterül kapott cucchoz fûzi hozzá mindenki a súlyokat
	public void getAllNeuronWeights(ArrayList<ArrayList<Double> > weights) {
		if (!isFirstLayer) {
			
			for (Neuron n : neurons) {
				
				
				n.appendWeightsTo(weights);
			}
		}
	}
	
}
