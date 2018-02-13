import java.util.ArrayList;
//ezamostani
public class Output {

	
	public static void printArchitecture(ArrayList<Integer> arch) {
	
		//igaz�b�l 1az1-ben ki�rhatjuk az Input oszt�lyban l�v� beolvasott cuccost...nem v�ltozik
		for (int i=0; i < arch.size(); ++i) {
			
			System.out.print(arch.get(i));
			
			//uts� ut�n nem �runk vessz�t
			if (i != arch.size()-1)
				System.out.print(",");
			
		}
		System.out.print("\n");
	}
	
	public static void printWeightsAndBias(ArrayList<ArrayList<Double> > weightVectors) {
		
		for (int i=0; i < weightVectors.size(); i++) {
			
			//egy sornyi ki�r�s: 
			for (int j=0; j < weightVectors.get(i).size(); j++) {
			
				//ki�rjuk az aktu�lis elemet
				double d = weightVectors.get(i).get(j);
				System.out.print(d);
				
					
				//uts�ig mind ut�n �runk vessz�t
				if (j != weightVectors.get(i).size()-1)
					System.out.print(",");
				
			}

			System.out.print("\n");
		}
	}
	
	//csak deleg�l...
	public static void printOutputVectors(ArrayList<ArrayList<Double> > outputVector) {
		System.out.println(Input.inputsNum);
		printWeightsAndBias(outputVector);
	}
	
	//egyszer� ki�rat�s
	public static void printAverageError(double d) {
		System.out.println(d);
	}
}
