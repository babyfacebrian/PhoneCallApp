
import static spark.Spark.get;
import static spark.Spark.notFound;
import static spark.Spark.post;
import java.net.URI;

import com.twilio.http.TwilioRestClient;
import com.twilio.rest.api.v2010.account.Call;
import com.twilio.rest.api.v2010.account.CallCreator;
import com.twilio.twiml.Play;
import com.twilio.twiml.Say;
import com.twilio.twiml.VoiceResponse;
import com.twilio.type.PhoneNumber;

public class phoneCalls {

    public static final String ACCOUNT_SID = "Axxxxxxxxxxxxxxxxxxxxxxxxxxxxxx"; // Twilio account ID

    public static final String AUTH_TOKEN = "xxxxxxxxxxxxxxxxxxxxxxxxxxxxxx";   // Twilio token

    public static final String TWILIO_NUMBER = "+1xxxxxxxxxx";                  // Twilio phone number

    public static final String NGROK_URL = "http://xxxxxxx.ngrok.io";           // ngrok url



    public static void main(String[] args) {

        TwilioRestClient client = new TwilioRestClient.Builder(ACCOUNT_SID, AUTH_TOKEN).build();
        get("/", (request,response) -> "Spark is running!");


        post("/twiml", (request, response) -> {
            Say sayHello = new Say.Builder("Hello Brian this is a phone app with the twilio api. Love Past Brian ").build();

            Play playSong = new Play.Builder("https://api.twilio.com/cowbell.mp3").build();

            VoiceResponse voiceResponse = new VoiceResponse.Builder().say(sayHello).play(playSong).build();

            return voiceResponse.toXml();
        });

        get("/dial-phone/:number", (request, response) -> {
            String phoneNumber = request.params(":number");

            if(!phoneNumber.isEmpty()){
                PhoneNumber to = new PhoneNumber(phoneNumber);
                PhoneNumber from = new PhoneNumber(TWILIO_NUMBER);

                URI uri = URI.create(NGROK_URL + "/twiml");

                Call call = new CallCreator(to, from, uri).create(client);
                return "Dialing " + phoneNumber + " from your Twilio phone number..";

            }else{
                return "Not a valid phone number in the URL!";
            }
        });

    }
}
