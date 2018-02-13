import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
//ezamostani
public class Input {

	public static Scanner sc;
	
	//beolvasand�k:
	public static ArrayList<Integer> archLayersSize;	//arch r�tegek m�rete
	private static int usefulNeuronsNum;	// �sszes neuron - "input" r�teg neuronjainak sz�ma
	
	private static ArrayList<ArrayList<Double> > weightBiasVectors;	//s�lyok �s bias, soronk�nt egy vektor
	public static int inputsNum;	//bemeneti �rt�kek/tan�t�mint�k sz�ma
	//private static ArrayList<ArrayList<Double> > inputVectors;	//bemeneti vektorok list�ja
	
	public static int epochs_num;	//tan�t�si epoch-ok sz�ma
	public static double courage_factor;	//b�tors�gi faktor
	private static double pattern_rate;	//tan�t�mint�k ar�nya 
	
	//kell?
	private static int inputDim;
	private static int outputDim;
	
	//architekt�ra beolvas�sa, felt�telezz�k, hogy a scanner m�r meg van nyitva!
	public static ArrayList<Integer> readArch() {
		
		archLayersSize = new ArrayList<Integer>();
		
		//beolvassuk a sort
		String archDef = sc.nextLine();
		
		//feldaraboljuk, vessz� az elv�laszt� - bepakoljuk egy "int" list�ba
		String[] parts = archDef.split(",");
		
		for (int i=0; i < parts.length; ++i ) {
			Integer next = Integer.parseInt(parts[i]);
			archLayersSize.add(next);
		}
		
		//meghat�rozzuk, hogy majd h�ny sornyi s�ly kell �rkezzen!
		//els� r�teg kimarad!
		usefulNeuronsNum = 0;
		for (int i = 1; i < archLayersSize.size(); ++i) {
			
			//bej�rjuk a list�t, �sszesz�moljuk, h�ny neuron s�ly�t kell majd beolvasni...
			usefulNeuronsNum += archLayersSize.get(i);
		}
		
		//elt�roljuk a bemenet �s a kimenet dimenzi�j�t
		inputDim = archLayersSize.get(0);
		outputDim = archLayersSize.get(archLayersSize.size()-1);
		
		return archLayersSize;
	}
	
	//tudnia kell, hogy h�ny random sz�mot gener�ljon!!!!
	// vagyis: ismernie kell a r�tegek m�reteit - ARCHITEKT�RA m�r BEOLVASVA!
	public static ArrayList<ArrayList<Double> > generateRandomWeightZeroBiasVectors() {
		
		//itt �ll�tjuk el�: ArrayList<ArrayList<Double> >!
		weightBiasVectors = new ArrayList<ArrayList<Double> >(); 
		
		//els� r�teg kimarad
		for (int i=1; i < archLayersSize.size(); ++i) {
			
			//annyi db vektor, ah�ny neuron van -> pont az a sz�m, ami a lista jelenlegi eleme!
			for (int j=0; j < archLayersSize.get(i); j++) {
			
				
				
				ArrayList<Double> currentWeightVector = new ArrayList<Double>();
				//ebb�l ^^ lesz annyi db, ah�ny neuron van a r�tegben!
				
				//hosszuk: lista el�z� eleme
				int lastSize = archLayersSize.get(i-1);
				for (int k=0; k < lastSize; k++) {
					
					//gener�ljuk a random sz�mot...
					Random r = new Random();
					double randNum = r.nextGaussian()*0.1;
					currentWeightVector.add(randNum);
					
				}
				
				//bias hozz�ad�sa
				currentWeightVector.add(0.0);
				
				weightBiasVectors.add(currentWeightVector);			
			}
		}
		
		return weightBiasVectors;
			
	}
	
	//s�lyok beolvas�sa, KELL (!) hogy m�r be legyen olvasva az architekt�ra!! 
	public static ArrayList<ArrayList<Double> > readWeights() {
		
		//s�lyvektorok vektora
		weightBiasVectors = new ArrayList<ArrayList<Double> >(); 
		
		//usefulNeuronsNum - ennyi sort kell beolvasni!
		for (int i = 0; i < usefulNeuronsNum; ++i) {
		
			//most k�vetkez� sor
			ArrayList<Double> currentLine = new ArrayList<Double>();
			
			//beolvassuk a k�vetkez� sort(vektor: s�lyok + bias)
			String line = sc.nextLine();
		
			//feldaraboljuk, vessz� az elv�laszt� - bepakoljuk egy list�ba
			// N+1 db - s�lyok + bias!!
			String[] parts = line.split(",");
			
			for (int j=0; j < parts.length; ++j ) {
				Double next = Double.parseDouble(parts[j]);
				currentLine.add(next);
			}
			
			weightBiasVectors.add(currentLine);
		}
		
		return weightBiasVectors;
	}
	
	//felt. hogy scanner meg van nyitva!
	private static void readInputsNum() {
		
		inputsNum = 0;
		try {
		    inputsNum = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
		    e.printStackTrace();
		}
		
	}
	
	
	public static ArrayList<ArrayList<Double> > readInputVectors() {

		//bemenetek sz�ma - inputsNum v�ltoz� be�ll�tva...
		readInputsNum();
		
		//s�lyvektorok vektora
		
		//edit: lok�lis v�ltoz�...probl�m�s lehet
		ArrayList<ArrayList<Double> >inputVectors = new ArrayList<ArrayList<Double> >(); 
		
		//inputsNum - ennyi sort kell beolvasni!
		for (int i = 0; i < inputsNum; ++i) {
		
			//most k�vetkez� sor
			ArrayList<Double> currentLine = new ArrayList<Double>();
			
			//beolvassuk a k�vetkez� sort(vektor: inputok)
			String line = sc.nextLine();
		
			//feldaraboljuk, vessz� az elv�laszt� - bepakoljuk egy list�ba
			String[] parts = line.split(",");
			
			for (int j=0; j < parts.length; ++j ) {
				Double next = Double.parseDouble(parts[j]);
				currentLine.add(next);
			}
			
			inputVectors.add(currentLine);
		}
		
		return inputVectors;
			
	}
	
	
	//tan�t�si param�terek beolvas�sa - scanner nyitva kell legyen...
	//elt�roljuk tagv�ltoz�kban a beolvasottakat
	public static void readTeachingParameters()  {
		
		epochs_num = 0;
		courage_factor = 0;
		pattern_rate = 0;
		
		String line = sc.nextLine();
		String[] parts = line.split(",");
		
		epochs_num = Integer.parseInt(parts[0]);
		courage_factor = Double.parseDouble(parts[1]);
		pattern_rate = Double.parseDouble(parts[2]);
		
	}
	
	
	//tan�t�mint�k beolvas�sa,
	//tan�t�si param�terek m�r beolvasva!
	//architekt�ra beolvasva!
	//param�ter�l kap 2 list�t, ezekbe kell beletenni, �rtelemszer�en...
	public static void readTeachingPatterns(ArrayList<ArrayList<Double> > teachingPatterns,
											ArrayList<ArrayList<Double> > validatingPatterns) {
		 
		//el�sz�r: mint�k sz�ma,
		//	ebb�l -> sT = floor(S*R)...,
		//	sT darab tan�t�minta lesz, t�bbi valid�ci�s
		
		int S=0;	//mint�k sz�ma
		int sT = 0;	//tan�t�mint�k sz�ma
		try {
		    S = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
		    e.printStackTrace();
		}
		
		sT = (int)Math.floor(S*pattern_rate);
		
		//mint�k beolvas�sa
		//el�sz�r sT db tan�t�minta
		
		for (int i = 0; i < sT; ++i) {
			
			//most k�vetkez� sor
			ArrayList<Double> currentLine = new ArrayList<Double>();
			
			//beolvassuk a k�vetkez� sort(vektor: inputok)
			String line = sc.nextLine();
		
			//feldaraboljuk, vessz� az elv�laszt� - bepakoljuk egy list�ba
			String[] parts = line.split(",");
			
			for (int j=0; j < parts.length; ++j ) {
				Double next = Double.parseDouble(parts[j]);
				currentLine.add(next);
			}
			
			teachingPatterns.add(currentLine);
		}
		
		//ut�na: (s-sT) db valid�ci�s minta
		for (int i = 0; i < S-sT; ++i) {
			
			//most k�vetkez� sor
			ArrayList<Double> currentLine = new ArrayList<Double>();
			
			//beolvassuk a k�vetkez� sort(vektor: inputok)
			String line = sc.nextLine();
		
			//feldaraboljuk, vessz� az elv�laszt� - bepakoljuk egy list�ba
			String[] parts = line.split(",");
			
			for (int j=0; j < parts.length; ++j ) {
				Double next = Double.parseDouble(parts[j]);
				currentLine.add(next);
			}
			
			validatingPatterns.add(currentLine);
		}
	}

}
