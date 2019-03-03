package gui;

import analyze.Response;
import analyze.Speech;

public class RequestHandler implements Runnable {


	@Override
	public void run() {

		try {
    		
			System.out.println("New Request");
			
	    	String response = Response.getResponse(Speech.runToText());
	    	
	    	gui.FXMLDocumentController.talking = true;
	    	Speech.synthesizeText(response);
	    	gui.FXMLDocumentController.talking = false;
	    	
    	}
    	catch (Exception e) {
    		
    		e.printStackTrace();
    		
    	}
		
	}

	
}
