import java.util.ArrayList;
//ezamostani
public class Neuron {
	private ArrayList<Double> weights = new ArrayList<Double>();
	private double bias = 0.0;
	private int input_num = 0; //annyi input, ah�ny s�ly
	private boolean isInFirstLayer;
	private boolean isInLastLayer;
	
	private int currentDerivative;
	private double delta;
	
	//interface - ezen kereszt�l kapja a s�lyokat

	//private NumGiver ng;
	
	public Neuron(ArrayList<Double> weightsAndBias, int n, boolean isInFirstLayer, boolean isInLastLayer) {
		
		
		//le kell kezeln�nk azt, hogy ha az els� r�tegben van,
		//	akkor nincsenek s�lyvektorai!!
		
		//megold�s: ha isFirst, akkor �tugorja a bias-s�ly r�szt...
		if (!isInFirstLayer) {
			
			//bias: s�lyvektor utols� eleme
			bias = weightsAndBias.remove(weightsAndBias.size()-1);
		
			//betessz�k a s�lyokat - csal�ka n�v, bias m�r KI VAN SZEDVE!
			weights = weightsAndBias;
			
		}
		
		input_num = n;
		
		this.isInFirstLayer = isInFirstLayer;
		this.isInLastLayer = isInLastLayer;
		
	}
	
	
	public boolean getIsInFirstLayer() {
		return isInFirstLayer;
	}
	
	// a r�teg bemenetei alapj�n kisz�molja ennek 
	// a konkr�t neuronnak a kimenet�t, k�s�bb �sszegezni kell
	// az egy r�tegben l�v� neuronok kimeneteit
	public double countOutput(ArrayList<Double> inputs) {
		
		double sum = 0;
		
		//�sszeszorozgatjuk a bemenetet a s�lyokkal, hozz�adjuk a szumm�hoz
		for (int i = 0; i < inputs.size(); ++i) {
			sum += inputs.get(i) * weights.get(i);  
		}
		
		//hozz�adjuk a bias-t
		sum += bias;
		
		//aktiv�ci�s fgv - m�s, ha uts� r�teg...
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
			
			//kivessz�k a k�v. elemet, beszorozzuk delt�val
			double d = inputVector.get(i);
			d *= delta;
			result.add(d);
			
		}
		
		//a v�g�re tessz�k a delt�t (bias helyett...)
		result.add(delta);
		
		return result;
	}
	
	//s�lyok �s bias m�dos�t�sa
	public void modifyWeights(ArrayList<Double> inputVector) {
		
		//m�dos�tjuk sorban a s�lyokat
		for (int i=0; i < weights.size(); i++) {
			double weight = weights.get(i);	//unwrap
			weight += 2 * Input.courage_factor * delta * inputVector.get(i);
			weights.set(i, weight);
		}
		
		//azt�n a biast
		bias += 2 * Input.courage_factor * delta;
		
	}
	
	// a param�ter�l kapott list�hoz f�zi a s�lyvektor�t �s a biast
	public void appendWeightsTo(ArrayList<ArrayList<Double> > neuronWeights) {
		ArrayList<Double> item = new ArrayList<Double>();
		//sorban hozz�adjuk a s�lyokat
		for (double d : weights) {
			item.add(d);
		}
		//�s a biast
		item.add(bias);
		
		//v�g�l hozz�f�zz�k a s�lyvektorok list�j�hoz
		neuronWeights.add(item);
		
		
	}
	
}

