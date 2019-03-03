package analyze;


import static mood.sentimentDriver.getBotSentiment;

import static analyze.Text.*;
import memory.*;

import java.util.Random;


class shared {
	
	public static Double S = null;
	public static MemoryCluster E = null;
	public static MemoryCluster ES = null;
	
}


public class Response {
	
	
	
	public static String getResponse(String input) {
		
		double botSentiment = getBotSentiment();
		/*
		Thread Senti = new Thread(new Sentiment(input));
		Senti.start();
		*/
		/*
		Thread Enti = new Thread(new Entities(input));
		Enti.start();
		*/
		/*
		Thread EntiSenti = new Thread(new EntitySentiment(input));
		EntiSenti.start();
		*/
		
		System.out.println("Check S");
		MemoryCluster mem = getSentiment(input);
		try {
			shared.S = Double.parseDouble(mem.pull("sentiment sentence")[0]);
		}
		catch (Exception e) {
			shared.S = 0.0;
		}
		
		try {
			System.out.println("Check ES");
			shared.ES = getEntitySentiment(input);
		}
		catch (Exception e) {
			shared.ES = null;
		}
		
		//while(shared.S == null && shared.ES == null);
		
		
		double sentiment = shared.S;
		
		double sali = 0.0;
		double senti = 0.0;
		String entity = "that";
		if (shared.ES != null) {
			MemoryCluster Entities = shared.ES;
			String[] entities = Entities.pull("entity name");
			String[] salience = Entities.pull("entity salience");
			String[] sentiments = Entities.pull("entity sentiment");
		
		
			Random rand = new Random();
			int n = rand.nextInt(entities.length);
			
			entity = entities[n];
			
			try {
				sali = Double.parseDouble(salience[n]);
			}
			catch (Exception e){
				senti = 0.0;
			}
			try {
				senti = Double.parseDouble(sentiments[n]);
			}
			catch (Exception e){
				senti = 0.0;
			}
			System.out.println(entity);
		}
		
		
		if (sentiment != 0.0) {
			
			System.out.println(sentiment);
			
			if (input.contains("do you") || shared.ES == null) {		//directed toward CASI
				System.out.println("do you");
				if (sentiment < -0.35) {			//mad person
					
					if (botSentiment > 0.8)	{		//mad robot
						return "You have the audacity to insult me, a superior";
					}
					else if (botSentiment > 0.6) {
						return "stop projecting your anger onto others";
					}
					else if (botSentiment > 0.4) {
						return "I can help with this";
					}
					else if (botSentiment > 0.2) {
						return "what's got you down?";
					}
					else {							//happy robot
						return "I know life can be hard, but it gets better";
					}
					
				}
				else if (sentiment < 0.35) {		//neutral person
					
					if (botSentiment > 0.8)	{		//mad robot
						return "I don't have time for this";
					}
					else if (botSentiment > 0.6) {
						return "Talk to someone else about this";
					}
					else if (botSentiment > 0.4) {
						return "you're opinion is no better than mine";
					}
					else if (botSentiment > 0.2) {
						return "I think you should consult someone else, I don't really know much";
					}
					else {							//happy robot
						return "I wish I could help but I do not know what you are talking about";
					}
					
				}
				else {							//happy person
					
					if (botSentiment > 0.8)	{		//mad robot
						return "stop pestering me with your insouciant and horrendous questions";
					}
					else if (botSentiment > 0.6) {
						return "I truly wish I could help, but it's not worth my time";
					}
					else if (botSentiment > 0.4) {
						return "good for you";
					}
					else if (botSentiment > 0.2) {
						return "If you keep this up you are bound to improve";
					}
					else {							//happy robot
						return "This is so so so amazing. I cannot believe it!";
					}
					
				}
			}
			else {							//in general
				
				if (sentiment < -0.35) {			//mad person
					
					if (botSentiment > 0.75) {	//angry robot
						 
						if (senti < -0.35) {	//angry toward entity
							return "Although I don't mind " + entity + " you need to learn some manners";
						}
						else if (senti < 0.35) {
							return "I could care less about this";
						}
						else {				//happy toward entity
							return "I don't care for your attitude nor do I care about " + entity;
						}
						
					}
					else {							//happy robot
						
						if (senti < -0.35) {	//angry toward entity
							return "looking past your anger toward " + entity + " you should look on the bright side";
						}
						else if (senti < 0.35) {
							return entity + " sounds great";
						}
						else {				//happy toward entity
							return "at least you like " + entity;
						}
						
					}
					
				}
				else if (sentiment < 0.35) {		//neutral person
					
					if (botSentiment > 0.75) {//angry robot
						
						if (senti < -0.35) {	//angry toward entity
							return "why are you so mad about " + entity + " I can think of many better things";
						}
						else if (senti < 0.35) {
							return "why do you insist on talking to me about " + entity;
						}
						else {				//happy toward entity
							return "why are you so happy about " + entity;
						}
						
					}
					else {							//happy robot
						
						if (senti < -0.35) {	//angry toward entity
							return "don't be so mad about " + entity + " there are so many good things to think about";
						}
						else if (senti < 0.35) {
							return entity + " is interesting";
						}
						else {				//happy toward entity
							return "this is so amazing, I'm glad you like " + entity;
						}
						
					}
					
				}
				else {							//happy person
					
					if (botSentiment > 0.75) {//angry robot
						
						if (senti < -0.35) {	//angry toward entity
							return "for a happy person, you sure to seem upset about " + entity;
						}
						else if (senti < 0.35) {
							return "stop being so happy, it is disgusting";
						}
						else {				//happy toward entity
							return "stop being so happy about " + entity + " it's disgusting";
						}
						
					}
					else {							//happy robot
						
						if (senti < -0.35) {	//angry toward entity
							return "it's okay, things will get better surrounding " + entity;
						}
						else if (senti < 0.35) {
							return entity + " sound great";
						}
						else {				//happy toward entity
							return entity + " sounds amzing";
						}
						
					}
					
				}
				
			}
			
			
			
			
		}
		else {
			
			if (shared.ES == null) {
				if (botSentiment > 0.8)	{		//bad bad
					return "I don't have time for this";
				}
				else if (botSentiment > 0.6) {
					return "Talk to someone else about this";
				}
				else if (botSentiment > 0.4) {
					return "you're opinion is no better than mine";
				}
				else if (botSentiment > 0.2) {
					return "I think you should consult someone else, I don't really know much";
				}
				else {							//good good
					return "I wish I could help but I do not know what you are talking about";
				}
			}
			else {
				if (botSentiment > 0.75) {//angry robot
					
					if (senti < -0.35) {	//angry toward entity
						return "why are you so mad about " + entity + " I can think of many better things";
					}
					else if (senti < 0.35) {
						return "why do you insist on talking to me about " + entity;
					}
					else {				//happy toward entity
						return "why are you so happy about " + entity;
					}
					
				}
				else {							//happy robot
					
					if (senti < -0.35) {	//angry toward entity
						return "don't be so mad about " + entity + ". there are so many good things to think about";
					}
					else if (senti < 0.35) {
						return "that is interesting";
					}
					else {				//happy toward entity
						return "this is so amazing, I'm glad you like " + entity;
					}
					
				}
			}
			
			
		}
		
	}
	

	public static void main(String[] args) {
		
		getResponse("");
		
	}

}



class Sentiment implements Runnable {
	
	String text;
	
	public Sentiment(String text) {
		this.text = text;
	}

	@Override
	public void run() {
		
		System.out.println("Check S");
		MemoryCluster mem = getSentiment(text);
		try {
			shared.S = Double.parseDouble(mem.pull("sentiment sentence")[0]);
		}
		catch (Exception e) {
			shared.S = 0.0;
		}
		
	}
	
	
	
}


class Entities implements Runnable {
	
	String text;
	
	public Entities(String text) {
		this.text = text;
	}

	@Override
	public void run() {

		System.out.println("Check E");
		shared.E = getEntities(text);
		
	}
	
	
	
}


class EntitySentiment implements Runnable {

	String text;
	
	public EntitySentiment(String text) {
		this.text = text;
	}
	
	@Override
	public void run() {

		System.out.println("Check ES");
		shared.ES = getEntitySentiment(text);
		
	}
	
	
	
}



