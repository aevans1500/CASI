package analyze;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.concurrent.Future;

import com.google.cloud.texttospeech.v1.AudioConfig;
import com.google.cloud.texttospeech.v1.AudioEncoding;
import com.google.cloud.texttospeech.v1.SsmlVoiceGender;
import com.google.cloud.texttospeech.v1.SynthesisInput;
import com.google.cloud.texttospeech.v1.SynthesizeSpeechResponse;
import com.google.cloud.texttospeech.v1.TextToSpeechClient;
import com.google.cloud.texttospeech.v1.VoiceSelectionParams;
import com.google.protobuf.ByteString;
import com.microsoft.cognitiveservices.speech.CancellationDetails;
import com.microsoft.cognitiveservices.speech.CancellationReason;
import com.microsoft.cognitiveservices.speech.ResultReason;
import com.microsoft.cognitiveservices.speech.SpeechConfig;
import com.microsoft.cognitiveservices.speech.SpeechRecognitionResult;
import com.microsoft.cognitiveservices.speech.SpeechRecognizer;
import javazoom.jl.player.Player;


public class Speech {
	
	public static String return_message = "";
	
	public static final String subscriptionKey = "b9fd2069fd3a44d68fe28f5c2ae25575";
	
	public static final String uriBase = "https://eastus.tts.speech.microsoft.com/cognitiveservices/v1";
	
	public static final String region = "eastus";

	

	public static void main(String[] args) throws Exception {
		
		String text = runToText();
		System.out.println(text);
		synthesizeText(text);
		
	}
	
	
	//Text to Speech
	
	
	public static void synthesizeText(String text) throws Exception {
		  // Instantiates a client
		  try (TextToSpeechClient textToSpeechClient = TextToSpeechClient.create()) {
		    // Set the text input to be synthesized
		    SynthesisInput input = SynthesisInput.newBuilder().setText(text).build();

		    // Build the voice request
		    VoiceSelectionParams voice =
		        VoiceSelectionParams.newBuilder()
		            .setLanguageCode("en-US") // languageCode = "en_us"
		            .setSsmlGender(SsmlVoiceGender.FEMALE) // ssmlVoiceGender = SsmlVoiceGender.FEMALE
		            .build();

		    // Select the type of audio file you want returned
		    AudioConfig audioConfig =
	    		AudioConfig.newBuilder()
		            .setAudioEncoding(AudioEncoding.MP3) // MP3 audio.
		            .build();

		    // Perform the text-to-speech request
		    SynthesizeSpeechResponse response =
		        textToSpeechClient.synthesizeSpeech(input, voice, audioConfig);

		    // Get the audio contents from the response
		    ByteString audioContents = response.getAudioContent();

		    // Write the response to the output file.
		    try (OutputStream out = new FileOutputStream("output.mp3")) {
		    	out.write(audioContents.toByteArray());
		      	System.out.println("Audio content written to file \"output.mp3\"");
		    }
		  }
		  
		  try {
			  Player p = new Player(new FileInputStream("output.mp3"));
			  p.play();
		  } catch (Exception e) {}
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	//Speech to Text
	
	static class sharedVars {
		public volatile String returnedText;
		public volatile boolean toTextDone;
	}
	
	final sharedVars getVars = new sharedVars();
	
	
	
	public static String runToText() {
		
		System.out.println("runToText");
		
		Speech speech = new Speech();
		
		return speech.toText();
		
	}
	
	private String toText() {
		
		return_message = "";
		
		getVars.toTextDone = false;
		getVars.returnedText = "";
		
		System.out.println("Starting Speech to Text...");
		
		Thread speech = new Thread(new speechToText());
		speech.start();
		
		while (!getVars.toTextDone);
		
		return_message = getVars.returnedText;
		
		return return_message;
		
	}
	
	
	
	
	
	
	class speechToText implements Runnable {
		
		
		public void run() {
			
			try {
				
	            // Replace below with your own subscription key
	            String speechSubscriptionKey = subscriptionKey;
	            // Replace below with your own service region (e.g., "westus").
	            String serviceRegion = region;

	            
	            
	            int exitCode = 1;
	            SpeechConfig config = SpeechConfig.fromSubscription(speechSubscriptionKey, serviceRegion);
	            assert(config != null);

	            SpeechRecognizer reco = new SpeechRecognizer(config);
	            assert(reco != null);

	            
	            System.out.println("Say something...");
	            Speech.synthesizeText("yes?");

	            
	            Future<SpeechRecognitionResult> task = reco.recognizeOnceAsync();
	            assert(task != null);

	            SpeechRecognitionResult result = task.get();
	            assert(result != null);

	            
	            
	            if (result.getReason() == ResultReason.RecognizedSpeech) {
	            	
	            	getVars.returnedText = result.getText();
	            	
	                System.out.println("We recognized: " + getVars.returnedText);
	                exitCode = 0;
	                
	            }
	            else if (result.getReason() == ResultReason.NoMatch) {
	            	
	            	return_message = null;
	            	
	                System.out.println("NOMATCH: Speech could not be recognized.");
	                
	            }
	            else if (result.getReason() == ResultReason.Canceled) {
	            	
	            	return_message = null;
	            	
	                CancellationDetails cancellation = CancellationDetails.fromResult(result);
	                System.out.println("CANCELED: Reason=" + cancellation.getReason());

	                
	                if (cancellation.getReason() == CancellationReason.Error) {
	                	
	                    System.out.println("CANCELED: ErrorCode=" + cancellation.getErrorCode());
	                    System.out.println("CANCELED: ErrorDetails=" + cancellation.getErrorDetails());
	                    System.out.println("CANCELED: Did you update the subscription info?");
	                    
	                }
	                
	            }

	            
	            reco.close();
	            
	            getVars.toTextDone = true;
	            
	            //System.exit(exitCode);
	            
	            
	        } catch (Exception ex) {
	        	
	            System.out.println("Unexpected exception: " + ex.getMessage());

	            assert(false);

	            return_message = null;
	            
	        }
			
		}
		
		
	}
	
}


