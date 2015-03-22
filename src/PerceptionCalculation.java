import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class PerceptionCalculation {
	public static HashMap<PerceptionKey, PerceptionValue> calculateperception(FileReader filereader){
		int increment = 0;
		String filepath = new File("").getAbsolutePath();
		filepath = filepath + "\\" + "TestingRatings.txt";
		
		String line;
		String set = null;
		String movie = null;
		String user = null;
		
		float actual_perception = 0.0f;
		
		double rating = 0.0;
		double perception_movie = 0.0;
		double perception_new_movie = 0.0;
		
		double perception_user = 0.0;
		double perception_new_user = 0.0;
		double combine_numerator = 0.0;
		
		double perception_true_user = 0.0;
		double perception_user_new = 0.0;
		double perception_denominator = 0.0;
		double perception_denom_1 = 0.0;
		double perception_denom_2 = 0.0;
		
		float weight = 0.0f;
		float super_weight = 0.0f;
		float large_sum = 0.0f;		
		float k = 0.0f;
		
		Boolean movie_flag = false;
		Boolean user_flag = false;
		Boolean rating_flag = false;
		Map<PerceptionKey,PerceptionValue> perceptmap = new HashMap<PerceptionKey,PerceptionValue>();
		Map<String, Map<String,Float>> weightbucket = new HashMap<String, Map<String,Float>>(); 
		
	    BufferedReader reader = null;
	    FileInputStream fis = null;
	    try{
	    	fis = new FileInputStream(filepath);
	    	reader = new BufferedReader(new InputStreamReader(fis));
	    	StringTokenizer stringtokenizer = null;
	    	while ((line = reader.readLine()) != null && increment <= 50){
	    		increment++;
	    		large_sum = 0.0f;
	    		stringtokenizer = new StringTokenizer(line, ",");
	    		while(stringtokenizer.hasMoreElements()){
	    			set = stringtokenizer.nextToken();
		    		if (movie_flag.equals(false)){
		    			movie_flag = true;
		    			movie = set;
		    		} else if (user_flag.equals(false)){
		    			user_flag = true;
		    			user = set;
		    		} else if (rating_flag.equals(false)){
		    			rating_flag = true;
		    			rating = Double.parseDouble(set);
		    		}
	    		}
	    		
	    		movie_flag = false;
	    		user_flag = false;
	    		rating_flag = false;
	    		
	    		if(filereader.User_Statistics.containsKey(user)){
	    			UserValues uservalues = new UserValues();
	    			uservalues = filereader.User_Statistics.get(user);
	    			perception_movie = uservalues.mean;
	    		} else {
	    			continue;
	    		}
	    		
	    		for (String users: filereader.User_MovieRating.keySet()){
	    			if (user == users){
	    				continue;
	    			} else {	    				
	    				if(!weightbucket.isEmpty()){
	    					if (weightbucket.containsKey(users)){
	    						Map<String, Float> weight_relation = new HashMap<String,Float>();
	    						weight_relation = weightbucket.get(users);
	    						if(weight_relation.containsKey(user)){
	    							weight = weight_relation.get(user);
	    							if (weight != 0.0f){
	    								
	    								Map<String, Double> movie_rating = new HashMap<String, Double>();
	    								movie_rating = filereader.User_MovieRating.get(users);
	    								
	    								UserValues new_uservalue = new UserValues();
	    			    				new_uservalue = filereader.User_Statistics.get(users);
	    			    				perception_new_movie = new_uservalue.mean;
	    							
	    			    				super_weight = super_weight + weight;
	    			    				
	    			    				if (movie_rating.containsKey(movie)){
	    		    				 		large_sum = large_sum + (float)(weight * (movie_rating.get(movie) - perception_new_movie));
	    		    				 	} else {
	    		    				 		large_sum = large_sum + (float)(weight * (perception_new_movie * -1));
	    		    				 	}
	    			    				
	    			    				perception_new_movie = 0.0;
	    			    				continue;
	    							}
	    						}
	    					}
	    				}
	    				
	    				UserValues new_uservalue = new UserValues();
	    				new_uservalue = filereader.User_Statistics.get(users);
	    				perception_new_movie = new_uservalue.mean;
	    				
	    				combine_numerator = 0.0;
	    				perception_denom_1 = 0.0;
	    				perception_denom_2 = 0.0;
	    				perception_denominator = 0.0;
	    				
	    				Map<String, Double> movie_rating = new HashMap<String, Double>();
	    				Map<String, Double> movie_rating_two = new HashMap<String, Double>();
	    				movie_rating = filereader.User_MovieRating.get(users);
	    				movie_rating_two = filereader.User_MovieRating.get(user);
	    				for (String new_movie: movie_rating_two.keySet()){
	    					if (movie_rating.containsKey(new_movie)){
	    						try{
	    							perception_user = movie_rating_two.get(new_movie) - perception_movie;
	    							perception_new_user = movie_rating.get(new_movie) - perception_new_movie; 
	    							combine_numerator = combine_numerator + perception_user * perception_new_user;
	    							
	    							perception_true_user = perception_user * perception_user;
	    							perception_user_new = perception_new_user * perception_new_user;
	    							perception_denom_1 = perception_denom_1 + perception_true_user;
	    							perception_denom_2 = perception_denom_2 + perception_user_new;
	    							
	    						} catch (Exception ex){
	    							combine_numerator = 0.0;
	    							perception_denom_1 = 0.0;
	    							perception_denom_2 = 0.0;
	    						}
	    					}
	    				}
	    				try{
	    					perception_denominator = perception_denom_1 * perception_denom_2;  	
	    				 	perception_denominator = Math.sqrt(perception_denominator);
	    				 	
	    				 	if (perception_denominator == 0.0){
	    				 		weight = 0.0f;
	    				 	} else {
	    				 		weight = (float)(combine_numerator / perception_denominator);
	    				 	}    				 	
	    				 	
	    				 	super_weight = super_weight + weight;
	    				 	
	    				 	if (movie_rating.containsKey(movie)){
	    				 		large_sum = large_sum + (float)(weight * (movie_rating.get(movie) - perception_new_movie));
	    				 	} else {
	    				 		large_sum = large_sum + (float)(weight * (perception_new_movie * -1));
	    				 	}
	    				 	
	    				 	perception_new_movie = 0.0;
	    				 	
	    				 	if (weightbucket.isEmpty()){
	    				 		Map<String, Float> weight_relation = new HashMap<String,Float>();
		    				 	weight_relation.put(users, weight);
		    				 	weightbucket.put(user, weight_relation);	
	    				 	} else {
	    				 		if(weightbucket.containsKey(user)){
	    				 			Map<String, Float> weight_relation = new HashMap<String,Float>();
	    				 			weight_relation = weightbucket.get(user);
	    				 			weight_relation.put(users, weight);
	    				 			weightbucket.put(user,weight_relation);
	    				 		} else {
	    				 			Map<String, Float> weight_relation = new HashMap<String,Float>();
			    				 	weight_relation.put(users, weight);
			    				 	weightbucket.put(user, weight_relation);	
	    				 		}
	    				 	}
	    				 	
	    				 	
	    				} catch (Exception ey){
	    					weight = 0.0f;
	    				}
	    			}
	    		}
	    		try{
	    			if (super_weight == 0.0f){
	    				k = 0;
	    			} else {
	    				k = 1 / super_weight;
	    			}	    			
	    			
	    			actual_perception = (float)(perception_movie + k * large_sum);
	    			System.out.println("The perception for record "+increment+ " is: "+actual_perception);
	    			
	    			PerceptionValue perceptionvalue = new PerceptionValue();
	    			PerceptionKey perceptionkey = new PerceptionKey();
	    			
	    			perceptionvalue.perception = actual_perception;
	    			perceptionvalue.rating = rating;
	    			
	    			perceptionkey.movie = movie;
	    			perceptionkey.user = user;
	    			
	    			perceptmap.put(perceptionkey, perceptionvalue);
	    			
	    		} catch (Exception ez){
	    			k = 0.0f;
	    		}
	    		perception_movie = 0.0;	    		
	    	}	    	
	    	reader.close();
	    	System.out.println("");
	    	System.out.println("The total number of records executed are "+increment);
	    	System.out.println("");
	    	return (HashMap<PerceptionKey, PerceptionValue>) perceptmap;
	    } catch (Exception e){
	    	e.printStackTrace();
	    	return (HashMap<PerceptionKey, PerceptionValue>) perceptmap;
	    }
	}
}
