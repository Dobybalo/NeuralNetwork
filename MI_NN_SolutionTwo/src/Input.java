import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
//ezamostani
public class Input {

	public static Scanner sc;
	
	//beolvasandók:
	public static ArrayList<Integer> archLayersSize;	//arch rétegek mérete
	private static int usefulNeuronsNum;	// összes neuron - "input" réteg neuronjainak száma
	
	private static ArrayList<ArrayList<Double> > weightBiasVectors;	//súlyok és bias, soronként egy vektor
	public static int inputsNum;	//bemeneti értékek/tanítóminták száma
	//private static ArrayList<ArrayList<Double> > inputVectors;	//bemeneti vektorok listája
	
	public static int epochs_num;	//tanítási epoch-ok száma
	public static double courage_factor;	//bátorsági faktor
	private static double pattern_rate;	//tanítóminták aránya 
	
	//kell?
	private static int inputDim;
	private static int outputDim;
	
	//architektúra beolvasása, feltételezzük, hogy a scanner már meg van nyitva!
	public static ArrayList<Integer> readArch() {
		
		archLayersSize = new ArrayList<Integer>();
		
		//beolvassuk a sort
		String archDef = sc.nextLine();
		
		//feldaraboljuk, vesszõ az elválasztó - bepakoljuk egy "int" listába
		String[] parts = archDef.split(",");
		
		for (int i=0; i < parts.length; ++i ) {
			Integer next = Integer.parseInt(parts[i]);
			archLayersSize.add(next);
		}
		
		//meghatározzuk, hogy majd hány sornyi súly kell érkezzen!
		//elsõ réteg kimarad!
		usefulNeuronsNum = 0;
		for (int i = 1; i < archLayersSize.size(); ++i) {
			
			//bejárjuk a listát, összeszámoljuk, hány neuron súlyát kell majd beolvasni...
			usefulNeuronsNum += archLayersSize.get(i);
		}
		
		//eltároljuk a bemenet és a kimenet dimenzióját
		inputDim = archLayersSize.get(0);
		outputDim = archLayersSize.get(archLayersSize.size()-1);
		
		return archLayersSize;
	}
	
	//tudnia kell, hogy hány random számot generáljon!!!!
	// vagyis: ismernie kell a rétegek méreteit - ARCHITEKTÚRA már BEOLVASVA!
	public static ArrayList<ArrayList<Double> > generateRandomWeightZeroBiasVectors() {
		
		//itt állítjuk elõ: ArrayList<ArrayList<Double> >!
		weightBiasVectors = new ArrayList<ArrayList<Double> >(); 
		
		//elsõ réteg kimarad
		for (int i=1; i < archLayersSize.size(); ++i) {
			
			//annyi db vektor, ahány neuron van -> pont az a szám, ami a lista jelenlegi eleme!
			for (int j=0; j < archLayersSize.get(i); j++) {
			
				
				
				ArrayList<Double> currentWeightVector = new ArrayList<Double>();
				//ebbõl ^^ lesz annyi db, ahány neuron van a rétegben!
				
				//hosszuk: lista elõzõ eleme
				int lastSize = archLayersSize.get(i-1);
				for (int k=0; k < lastSize; k++) {
					
					//generáljuk a random számot...
					Random r = new Random();
					double randNum = r.nextGaussian()*0.1;
					currentWeightVector.add(randNum);
					
				}
				
				//bias hozzáadása
				currentWeightVector.add(0.0);
				
				weightBiasVectors.add(currentWeightVector);			
			}
		}
		
		return weightBiasVectors;
			
	}
	
	//súlyok beolvasása, KELL (!) hogy már be legyen olvasva az architektúra!! 
	public static ArrayList<ArrayList<Double> > readWeights() {
		
		//súlyvektorok vektora
		weightBiasVectors = new ArrayList<ArrayList<Double> >(); 
		
		//usefulNeuronsNum - ennyi sort kell beolvasni!
		for (int i = 0; i < usefulNeuronsNum; ++i) {
		
			//most következõ sor
			ArrayList<Double> currentLine = new ArrayList<Double>();
			
			//beolvassuk a következõ sort(vektor: súlyok + bias)
			String line = sc.nextLine();
		
			//feldaraboljuk, vesszõ az elválasztó - bepakoljuk egy listába
			// N+1 db - súlyok + bias!!
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

		//bemenetek száma - inputsNum változó beállítva...
		readInputsNum();
		
		//súlyvektorok vektora
		
		//edit: lokális változó...problémás lehet
		ArrayList<ArrayList<Double> >inputVectors = new ArrayList<ArrayList<Double> >(); 
		
		//inputsNum - ennyi sort kell beolvasni!
		for (int i = 0; i < inputsNum; ++i) {
		
			//most következõ sor
			ArrayList<Double> currentLine = new ArrayList<Double>();
			
			//beolvassuk a következõ sort(vektor: inputok)
			String line = sc.nextLine();
		
			//feldaraboljuk, vesszõ az elválasztó - bepakoljuk egy listába
			String[] parts = line.split(",");
			
			for (int j=0; j < parts.length; ++j ) {
				Double next = Double.parseDouble(parts[j]);
				currentLine.add(next);
			}
			
			inputVectors.add(currentLine);
		}
		
		return inputVectors;
			
	}
	
	
	//tanítási paraméterek beolvasása - scanner nyitva kell legyen...
	//eltároljuk tagváltozókban a beolvasottakat
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
	
	
	//tanítóminták beolvasása,
	//tanítási paraméterek már beolvasva!
	//architektúra beolvasva!
	//paraméterül kap 2 listát, ezekbe kell beletenni, értelemszerûen...
	public static void readTeachingPatterns(ArrayList<ArrayList<Double> > teachingPatterns,
											ArrayList<ArrayList<Double> > validatingPatterns) {
		 
		//elõször: minták száma,
		//	ebbõl -> sT = floor(S*R)...,
		//	sT darab tanítóminta lesz, többi validációs
		
		int S=0;	//minták száma
		int sT = 0;	//tanítóminták száma
		try {
		    S = Integer.parseInt(sc.nextLine());
		} catch (NumberFormatException e) {
		    e.printStackTrace();
		}
		
		sT = (int)Math.floor(S*pattern_rate);
		
		//minták beolvasása
		//elõször sT db tanítóminta
		
		for (int i = 0; i < sT; ++i) {
			
			//most következõ sor
			ArrayList<Double> currentLine = new ArrayList<Double>();
			
			//beolvassuk a következõ sort(vektor: inputok)
			String line = sc.nextLine();
		
			//feldaraboljuk, vesszõ az elválasztó - bepakoljuk egy listába
			String[] parts = line.split(",");
			
			for (int j=0; j < parts.length; ++j ) {
				Double next = Double.parseDouble(parts[j]);
				currentLine.add(next);
			}
			
			teachingPatterns.add(currentLine);
		}
		
		//utána: (s-sT) db validációs minta
		for (int i = 0; i < S-sT; ++i) {
			
			//most következõ sor
			ArrayList<Double> currentLine = new ArrayList<Double>();
			
			//beolvassuk a következõ sort(vektor: inputok)
			String line = sc.nextLine();
		
			//feldaraboljuk, vesszõ az elválasztó - bepakoljuk egy listába
			String[] parts = line.split(",");
			
			for (int j=0; j < parts.length; ++j ) {
				Double next = Double.parseDouble(parts[j]);
				currentLine.add(next);
			}
			
			validatingPatterns.add(currentLine);
		}
	}

}
