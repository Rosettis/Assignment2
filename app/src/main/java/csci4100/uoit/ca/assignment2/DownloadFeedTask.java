package csci4100.uoit.ca.assignment2;

import android.os.AsyncTask;
import android.util.Log;

/*import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;*/

/*import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;*/

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

/**
 * DownloadFeedTask
 *
 * @author Matthew Rosettis
 */
public class DownloadFeedTask extends AsyncTask<String, Void, String> {
    private BitcoinReturn listener = null;
    private Exception exception = null;

    public DownloadFeedTask(BitcoinReturn listener) {
        this.listener = listener;
    }

    @Override
    protected String doInBackground(String... params) {
        //Parsing just the String data
        String btcPrice = "";
        try{
            URL url = new URL(params[0]);
            BufferedReader breader = new BufferedReader(new InputStreamReader(url.openStream()));
            String doc;
            while((doc = breader.readLine()) != null){
                btcPrice = doc;
            }
            breader.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return btcPrice;
    }

    @Override
    protected void onPostExecute(String amount){
        if (exception != null) {
            exception.printStackTrace();
            return;
        }
        Log.d("InternetResourcesSample", "showing feed: " + amount);
        listener.getBitcoinPrice(amount);
    }
}

//Attempting to use the default conversion he gave us
        /*String btcPrice = "";

        try {
            // parse out the data
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
//            Log.i("parameter",params[0]);
            URL convert = new URL(params[0]);
//            URL convert = new URL("https://blockchain.info/tobtc?currency=CAD&value=599.99");
            Document document = docBuilder.parse(convert.openStream());
//            Document document = docBuilder.parse(new InputSource(new StringReader(params[0])));
            document.getDocumentElement().normalize();
            // look for <body> tags and get the data inside
            NodeList body = document.getElementsByTagName("body");
//            btcPrice = body.item(0).getTextContent();
//            Log.i("BitCoin",btcPrice);
            if ((body.getLength() > 0) && (body.item(0).getNodeType() == Node.ELEMENT_NODE)) {
                Element value = (Element) body.item(0);
                NodeList preTags = value.getElementsByTagName("pre");
                btcPrice = preTags.item(0).getTextContent();
                Log.i("the BitCoin",btcPrice);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return btcPrice;*/
//Using JSoup
        /*String linkText = "";
        try {
            Document doc = Jsoup.connect(params[0]).get();
            Elements amount = doc.getElementsByTag("body");
            linkText = amount.text();
            Log.i("the bitcoin",linkText);
        }catch(Exception e){
            e.printStackTrace();
        }
        return linkText;*/
