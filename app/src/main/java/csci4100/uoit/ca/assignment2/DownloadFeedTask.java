package csci4100.uoit.ca.assignment2;

import android.os.AsyncTask;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

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
        String linkText = "";
        try {
            Document doc = Jsoup.connect(params[0]).get();
            Elements amount = doc.getElementsByTag("body");
            linkText = amount.text();
            Log.i("the bitcoin",linkText);
        }catch(Exception e){
            e.printStackTrace();
        }
        return linkText;
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
