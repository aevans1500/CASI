package analyze;

import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.Document.Type;
import com.google.cloud.language.v1.EncodingType;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Map;

import org.json.JSONObject;

import com.google.cloud.language.v1.AnalyzeEntitiesRequest;
import com.google.cloud.language.v1.AnalyzeEntitiesResponse;
import com.google.cloud.language.v1.AnalyzeEntitySentimentRequest;
import com.google.cloud.language.v1.AnalyzeEntitySentimentResponse;
import com.google.cloud.language.v1.Entity;
import com.google.cloud.language.v1.EntityMention;

import memory.*;

public class Text {
	
	
	public static MemoryCluster getEntities(String text) {
		
		MemoryCluster mem = new MemoryCluster();
		
		try (LanguageServiceClient language = LanguageServiceClient.create()) {
			  Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
			  AnalyzeEntitiesRequest request = AnalyzeEntitiesRequest.newBuilder().setDocument(doc).setEncodingType(EncodingType.UTF16).build();

			  AnalyzeEntitiesResponse response = language.analyzeEntities(request);

			  
			  //adds response data to MemoryCluster
			  for (Entity entity : response.getEntitiesList()) {
			    System.out.printf("Entity: %s\n", entity.getName());
			    System.out.printf("Salience: %.3f\n", entity.getSalience());
			    
			    String[] key_words0 = {"entity", "name"};
			    mem.add(
			    		key_words0,
			    		entity.getName()
		    		);
			    
			    String[] key_words1 = {"entity", entity.getName(), "salience"};
			    mem.add(
			    		key_words1,
			    		entity.getSalience() + ""
		    		);
			    
			 }
		}
		catch (Exception e) {}
		
		return mem;
		
	}
	
	
	
	public static MemoryCluster getSentiment(String text) {
		
		MemoryCluster mem = new MemoryCluster();
		
		double senti = 0;
		
		try {
			
			LanguageServiceClient language = LanguageServiceClient.create();
			
			// The text to analyze
			Document doc = Document.newBuilder().setContent(text).setType(Type.PLAIN_TEXT).build();
		
			// Detects the sentiment of the text
			Sentiment sentiment = language.analyzeSentiment(doc).getDocumentSentiment();
		
			senti = sentiment.getScore();
			
			String[] key_words = {"sentiment", "sentence"};
			mem.add(
					key_words, 
					senti + ""
				);
			
		}
		catch (Exception e) {
			e.printStackTrace();
		}

		return mem;
		
	}
	
	
	
	public static MemoryCluster getEntitySentiment(String text) {
		
		MemoryCluster mem = new MemoryCluster();
		
		try (LanguageServiceClient language = LanguageServiceClient.create()) {
			  Document doc = Document.newBuilder()
			      .setContent(text).setType(Type.PLAIN_TEXT).build();
			  AnalyzeEntitySentimentRequest request = AnalyzeEntitySentimentRequest.newBuilder()
			      .setDocument(doc)
			      .setEncodingType(EncodingType.UTF16).build();
			  // detect entity sentiments in the given string
			  AnalyzeEntitySentimentResponse response = language.analyzeEntitySentiment(request);

			  
			  for (Entity entity : response.getEntitiesList()) {
				  
				String[] key_words0 = {"entity", "name"};
				mem.add(
						key_words0,
						entity.getName()
					);
				
				String[] key_words1 = {"entity", entity.getName(), "salience"};
				mem.add(
						key_words1,
						entity.getSalience() + ""
					);
				
				String[] key_words2 = {"entity", entity.getName(), "sentiment"};
				mem.add(
						key_words2,
						entity.getSentiment() + ""
					);
				  
				/*
			    System.out.printf("Entity: %s\n", entity.getName());
			    System.out.printf("Salience: %.3f\n", entity.getSalience());
			    System.out.printf("Sentiment : %s\n", entity.getSentiment());
			    for (EntityMention mention : entity.getMentionsList()) {
			      System.out.printf("Begin offset: %d\n", mention.getText().getBeginOffset());
			      System.out.printf("Content: %s\n", mention.getText().getContent());
			      System.out.printf("Magnitude: %.3f\n", mention.getSentiment().getMagnitude());
			      System.out.printf("Sentiment score : %.3f\n", mention.getSentiment().getScore());
			      System.out.printf("Type: %s\n\n", mention.getType());
		    	}
		    	*/
				
			  }
		}
		catch (Exception e) {}
		
		return mem;
		
	}
	
	
	
	public static void main(String[] args) throws Exception {
		
		getEntities("the president gave a speech at the White House");
		
	}

}
