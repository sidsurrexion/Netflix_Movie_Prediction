import java.util.HashMap;
import java.util.Map;


public class CollaborativeDriver {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		FileReader filereader = new FileReader();
		Map<PerceptionKey,PerceptionValue> perceptmap = new HashMap<PerceptionKey,PerceptionValue>();
		filereader = FileReader.filereader();
		perceptmap = PerceptionCalculation.calculateperception(filereader);
		MeanCalculation.meancalculate((HashMap<PerceptionKey, PerceptionValue>) perceptmap);
	}

}
