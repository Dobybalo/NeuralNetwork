import java.util.ArrayList;
//ezamostani
public class Neuron {
	private ArrayList<Double> weights = new ArrayList<Double>();
	private double bias = 0.0;
	private int input_num = 0; //annyi input, ahány súly
	private boolean isInFirstLayer;
	private boolean isInLastLayer;
	
	private int currentDerivative;
	private double delta;
	
	//interface - ezen keresztül kapja a súlyokat

	//private NumGiver ng;
	
	public Neuron(ArrayList<Double> weightsAndBias, int n, boolean isInFirstLayer, boolean isInLastLayer) {
		
		
		//le kell kezelnünk azt, hogy ha az elsõ rétegben van,
		//	akkor nincsenek súlyvektorai!!
		
		//megoldás: ha isFirst, akkor átugorja a bias-súly részt...
		if (!isInFirstLayer) {
			
			//bias: súlyvektor utolsó eleme
			bias = weightsAndBias.remove(weightsAndBias.size()-1);
		
			//betesszük a súlyokat - csalóka név, bias már KI VAN SZEDVE!
			weights = weightsAndBias;
			
		}
		
		input_num = n;
		
		this.isInFirstLayer = isInFirstLayer;
		this.isInLastLayer = isInLastLayer;
		
	}
	
	
	public boolean getIsInFirstLayer() {
		return isInFirstLayer;
	}
	
	// a réteg bemenetei alapján kiszámolja ennek 
	// a konkrét neuronnak a kimenetét, késõbb összegezni kell
	// az egy rétegben lévõ neuronok kimeneteit
	public double countOutput(ArrayList<Double> inputs) {
		
		double sum = 0;
		
		//összeszorozgatjuk a bemenetet a súlyokkal, hozzáadjuk a szummához
		for (int i = 0; i < inputs.size(); ++i) {
			sum += inputs.get(i) * weights.get(i);  
		}
		
		//hozzáadjuk a bias-t
		sum += bias;
		
		//aktivációs fgv - más, ha utsó réteg...
		if ( (sum > 0) || isInLastLayer) return sum;
		return 0;
		
	}
	
	public void setDelta(double delta) {
		this.delta = delta;
	}
	
	public double getDelta() {
		return delta;
	}
	
	public double countSumElement(int index) {
		return delta * weights.get(index);
	}
	
	//NNThree output - 1 sor
	public ArrayList<Double> countNNThreeOutput(ArrayList<Double> inputVector) {
		
		ArrayList<Double> result = new ArrayList<Double>();
		
		for(int i=0; i < inputVector.size(); i++) {
			
			//kivesszük a köv. elemet, beszorozzuk deltával
			double d = inputVector.get(i);
			d *= delta;
			result.add(d);
			
		}
		
		//a végére tesszük a deltát (bias helyett...)
		result.add(delta);
		
		return result;
	}
	
	//súlyok és bias módosítása
	public void modifyWeights(ArrayList<Double> inputVector) {
		
		//módosítjuk sorban a súlyokat
		for (int i=0; i < weights.size(); i++) {
			double weight = weights.get(i);	//unwrap
			weight += 2 * Input.courage_factor * delta * inputVector.get(i);
			weights.set(i, weight);
		}
		
		//aztán a biast
		bias += 2 * Input.courage_factor * delta;
		
	}
	
	// a paraméterül kapott listához fûzi a súlyvektorát és a biast
	public void appendWeightsTo(ArrayList<ArrayList<Double> > neuronWeights) {
		ArrayList<Double> item = new ArrayList<Double>();
		//sorban hozzáadjuk a súlyokat
		for (double d : weights) {
			item.add(d);
		}
		//és a biast
		item.add(bias);
		
		//végül hozzáfûzzük a súlyvektorok listájához
		neuronWeights.add(item);
		
		
	}
	
}

