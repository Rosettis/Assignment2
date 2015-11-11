package csci4100.uoit.ca.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        // get the result text label, so that Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_create) {
            Intent intent = new Intent(this,CreateProductActivity.class);
            startActivityForResult(intent, 1);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                // Implement the data as a new Product
                String prodName = data.getExtras().getString("name");
                String prodDesc = data.getExtras().getString("desc");
                String priceCAD = data.getExtras().getString("price");
                ProductDBHelper dbHelper = new ProductDBHelper(this);
                if(priceCAD != null){
                    Double dblPrice = Double.parseDouble(priceCAD);
                    dbHelper.createProduct(prodName, prodDesc, dblPrice);
                }else {
                    Double dblPrice = 0.00;
                    dbHelper.createProduct(prodName, prodDesc, dblPrice);
                }
                this.products = dbHelper.getAllProducts();
            } else if (resultCode == RESULT_CANCELED) {
                Toast.makeText(getApplicationContext(),
                        "Product addition cancelled",
                        Toast.LENGTH_SHORT).show();
            }
        }
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
        if(this.products.size() > 1) {
            dbHelper.deleteProduct(this.products.get(this.ProductIndex).getId());
            this.products = dbHelper.getAllProducts();
            if (this.ProductIndex >= this.products.size()) {
                this.ProductIndex--;
            } else {
                this.ProductIndex++;
            }
            showProduct(this.products.get(this.ProductIndex));
        }else{
            Toast.makeText(getApplicationContext(),
                    "Cannot delete, Database must have at least one value",
                    Toast.LENGTH_SHORT).show();
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
}
