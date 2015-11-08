package csci4100.uoit.ca.assignment2;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;

/**
 * @author Matthew Rosettis
 */
public class BrowseProductsActivity extends AppCompatActivity implements BitcoinReturn {

    private ArrayList<Product> products = null;
    private int ProductIndex = -1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // get the result text label, so that we can output status messages
        TextView resultField = (TextView)findViewById(R.id.lblResult);

        // create an instance of our helper class to do the job
        ProductDBHelper dbHelper = new ProductDBHelper(this);

        // delete any products from a previous execution
        dbHelper.deleteAllProducts();

        // create a bunch of products (testing create) String productName, String description, double price
        Product refrigerator = dbHelper.createProduct("LG T500 Refrigerator","A simple fridge for all your food maintenance needs",599.99);
        Product television = dbHelper.createProduct("LG 55\" Television","120 Hz, 60ms refresh rate",379.87);
        Product computer = dbHelper.createProduct("Alienware 17\" Laptop","Ultimate Gaming Laptop for mobile dominance",1739.99);
        Product phone = dbHelper.createProduct("OnePlus Two","The new flagship killer!",429.99);
        Product watch = dbHelper.createProduct("TAG Heuer T450","For the man who knows what he wants",5199.99);

        /*
        // update a Product in the database
        refrigerator.setDescription("555-9999");
        if (!dbHelper.updateProduct(refrigerator)) {
            resultField.setText("0001" + getResources().getText(R.string.error));
        }

        // load one of the products (testing read single)
        Product fridge2 = dbHelper.getProduct(refrigerator.getId());

        // delete a Product
        if (!dbHelper.deleteProduct(television.getId())) {
            resultField.setText("0002" + getResources().getText(R.string.error));
        }*/

        // add them to the local list (testing read all)
        this.products = dbHelper.getAllProducts();
        this.ProductIndex = -1;
        nextProduct();
    }

    public void nextProduct(View v) {
        nextProduct();
    }

    private void nextProduct() {
        this.ProductIndex++;

        if (this.ProductIndex >= this.products.size()) {
            this.ProductIndex--;
//            this.ProductIndex = 0;
        }

        showProduct(this.products.get(this.ProductIndex));
    }

    public void prevProduct(View v) {
        prevProduct();
    }

    private void prevProduct(){
        this.ProductIndex--;

        if (this.ProductIndex < 0) {
            this.ProductIndex++;
//            this.ProductIndex = this.products.size()-1;
        }

        showProduct(this.products.get(this.ProductIndex));
    }

    public void deleteProduct(View view){
        // create an instance of our helper class to do the job
        ProductDBHelper dbHelper = new ProductDBHelper(this);

        dbHelper.deleteProduct(this.products.get(this.ProductIndex).getId());
        this.products = dbHelper.getAllProducts();
        if (this.ProductIndex >= this.products.size()) {
            this.ProductIndex--;
        }else{
            this.ProductIndex++;
        }
    }

    private void showProduct(Product product) {
        TextView productNameField = (TextView)findViewById(R.id.lblProductNameValue);
        TextView descriptionField = (TextView)findViewById(R.id.lblDescriptionValue);
        TextView priceField = (TextView)findViewById(R.id.lblPriceValue);

        productNameField.setText(product.getProductName());
        descriptionField.setText(product.getDescription());
        priceField.setText(Double.toString(product.getPrice()));

        getBTC(product.getPrice());
    }

    public void getBTC(Double price){
        String url = "https://blockchain.info/tobtc?currency=CAD&value=";
        url += price;
        Log.i("the price page", url);

        DownloadFeedTask task = new DownloadFeedTask(this);
        task.execute(url);

    }

    public void getBitcoinPrice(String price) {
        TextView priceBitcoin = (TextView) findViewById(R.id.lblPriceBitcoinValue);
        priceBitcoin.setText(price);
    }

        /*

        try {
            // parse out the data
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = dbFactory.newDocumentBuilder();
            URL convert = new URL(url);

            Document document = docBuilder.parse(convert.openStream());
            document.getDocumentElement().normalize();
            // look for <body> tags and get the data inside
            NodeList body = document.getElementsByTagName("body");
            if ((body.getLength() > 0) && (body.item(0).getNodeType() == Node.ELEMENT_NODE)) {
                Element definitions = (Element) body.item(0);
                NodeList preTags = definitions.getElementsByTagName("pre");
                String bitcoin = preTags.item(0).getTextContent();
                Log.i("the bitcoin",bitcoin);
                btcPrice = Double.parseDouble(bitcoin);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        return btcPrice;*/
}
