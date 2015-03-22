import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


public class FileReader {
	
	Map<String, Map<String,Double>> Movie_User = new HashMap<String, Map<String,Double>>();
	Map<String, Map<String,Double>> User_MovieRating = new HashMap<String, Map<String,Double>>();
	Map<String, UserValues> User_Statistics = new HashMap<String, UserValues>();
    
	public static FileReader filereader(){		
		
		int increment = 0;
		
		String filepath = new File("").getAbsolutePath();
		filepath = filepath + "\\" + "TrainingRatings.txt";
		
		String line;
		String set = null;
		String movie = null;
		String user = null;
		
		double rating = 0.0;
		Boolean movie_flag = false;
		Boolean user_flag = false;
		Boolean rating_flag = false;
		Boolean bool;
		
	    BufferedReader reader = null;
	    FileInputStream fis = null;  
	    	    
	    Map<String, Map<String,Double>> Movie_User = new HashMap<String, Map<String,Double>>();
		Map<String, Map<String,Double>> User_MovieRating = new HashMap<String, Map<String,Double>>();
		Map<String, UserValues> User_Statistics = new HashMap<String, UserValues>();
	    
	    try{
	    	fis = new FileInputStream(filepath);
	    	reader = new BufferedReader(new InputStreamReader(fis));
	    	StringTokenizer stringtokenizer = null;
	    	while ((line = reader.readLine()) != null && increment <= 1000000){
	    		increment++;
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
	    		
	    		if(User_MovieRating.isEmpty()){
	    			Map<String, Double> movie_rating = new HashMap<String, Double>();
	    			UserValues uservalues = new UserValues();
	    			uservalues.aggregate = uservalues.aggregate + rating;
	    			uservalues.count = uservalues.count + 1;
	    			uservalues.mean = (double)(uservalues.aggregate / uservalues.count); 
	    			movie_rating.put(movie, rating);
	    			User_MovieRating.put(user, movie_rating);
	    			User_Statistics.put(user, uservalues);
	    		} else {
	    			bool = User_MovieRating.containsKey(user);
	    			if (bool == true){
	    				Map<String, Double> movie_rating = new HashMap<String, Double>();
	    				movie_rating = User_MovieRating.get(user);
	    				if ((bool = movie_rating.containsKey(movie)) == false){
	    					movie_rating.put(movie, rating);
	    					User_MovieRating.put(user, movie_rating);
	    					UserValues uservalues = new UserValues();
	    					uservalues = User_Statistics.get(user);
	    					uservalues.aggregate = uservalues.aggregate + rating;
	    	    			uservalues.count = uservalues.count + 1;
	    	    			uservalues.mean = (double)(uservalues.aggregate / uservalues.count);
	    	    			User_Statistics.put(user, uservalues);
	    				}
	    				
	    			} else {
	    				Map<String, Double> movie_rating = new HashMap<String, Double>();
	    				movie_rating.put(movie, rating);
	    				User_MovieRating.put(user, movie_rating);
	    				UserValues uservalues = new UserValues();
		    			uservalues.aggregate = uservalues.aggregate + rating;
		    			uservalues.count = uservalues.count + 1;
		    			uservalues.mean = (double)(uservalues.aggregate / uservalues.count);
		    			User_Statistics.put(user, uservalues);
	    			}
	    		}
	    		
	    		if(Movie_User.isEmpty()){
	    			Map<String, Double> movie_rating = new HashMap<String, Double>();
	    			movie_rating.put(user, (double)(Math.random()*4));
	    			Movie_User.put(movie, movie_rating);
	    		} else {
	    			bool = Movie_User.containsKey(movie);
	    			if (bool == true){
	    				Map<String, Double> movie_rating = new HashMap<String, Double>();
	    				movie_rating = Movie_User.get(movie);
	    				if ((bool = movie_rating.containsKey(user)) == false){
	    					movie_rating.put(user, (double)(Math.random()*4));
	    					Movie_User.put(movie, movie_rating);	    					
	    				}
	    			} else {
	    				Map<String, Double> movie_rating = new HashMap<String, Double>();
		    			movie_rating.put(user, (double)(Math.random()*4));
		    			Movie_User.put(movie, movie_rating);
	    			}
	    		}
	    	}
	    	reader.close();
	    } catch (Exception e){	
	    	e.printStackTrace();
	    }
	    FileReader file_reader = new FileReader();
	    file_reader.Movie_User = Movie_User;
	    file_reader.User_MovieRating = User_MovieRating;
	    file_reader.User_Statistics = User_Statistics;
	    return file_reader;
	}
}
