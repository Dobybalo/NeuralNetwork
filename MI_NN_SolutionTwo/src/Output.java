import java.util.ArrayList;
//ezamostani
public class Output {

	
	public static void printArchitecture(ArrayList<Integer> arch) {
	
		//igazából 1az1-ben kiírhatjuk az Input osztályban lévõ beolvasott cuccost...nem változik
		for (int i=0; i < arch.size(); ++i) {
			
			System.out.print(arch.get(i));
			
			//utsó után nem írunk vesszõt
			if (i != arch.size()-1)
				System.out.print(",");
			
		}
		System.out.print("\n");
	}
	
	public static void printWeightsAndBias(ArrayList<ArrayList<Double> > weightVectors) {
		
		for (int i=0; i < weightVectors.size(); i++) {
			
			//egy sornyi kiírás: 
			for (int j=0; j < weightVectors.get(i).size(); j++) {
			
				//kiírjuk az aktuális elemet
				double d = weightVectors.get(i).get(j);
				System.out.print(d);
				
					
				//utsóig mind után írunk vesszõt
				if (j != weightVectors.get(i).size()-1)
					System.out.print(",");
				
			}

			System.out.print("\n");
		}
	}
	
	//csak delegál...
	public static void printOutputVectors(ArrayList<ArrayList<Double> > outputVector) {
		System.out.println(Input.inputsNum);
		printWeightsAndBias(outputVector);
	}
	
	//egyszerû kiíratás
	public static void printAverageError(double d) {
		System.out.println(d);
	}
}
