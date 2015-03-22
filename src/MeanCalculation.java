import java.util.HashMap;


public class MeanCalculation {
	public static void meancalculate(HashMap<PerceptionKey, PerceptionValue> perceptmap){
		
		float rootmean = 0.0f;
		float absolutemean = 0.0f;
		float rootcalculate = 0.0f;
		float absolutecalculate = 0.0f;
		int count = 0;
		
		for (PerceptionKey perceptionkey : perceptmap.keySet()){
			count++;
			PerceptionValue perceptionvalue = new PerceptionValue();
			perceptionvalue = perceptmap.get(perceptionkey);
			try{
				rootcalculate = rootcalculate + (float)((perceptionvalue.perception - perceptionvalue.rating) * (perceptionvalue.perception - perceptionvalue.rating));
				absolutecalculate = absolutecalculate + (float)(perceptionvalue.perception - perceptionvalue.rating);
			} catch (Exception e){
				rootcalculate = rootcalculate + 0.0f;
				absolutecalculate = absolutecalculate + 0.0f;
			}		
		}
		try{
			rootmean = rootcalculate / count;
			absolutemean = absolutecalculate / count;
			System.out.println("The root mean square perception is: "+rootmean);
			System.out.println("The absolute mean perception is: "+absolutemean);
		} catch (Exception f){
			
		}
	}
}
