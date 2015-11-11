package csci4100.uoit.ca.assignment2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

public class CreateProductActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void cancelCreate(View view){
        Intent intent = new Intent(this, BrowseProductsActivity.class);
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    public void createProduct(View view){
        Intent intent = new Intent(this, BrowseProductsActivity.class);
        EditText name = (EditText)findViewById(R.id.inputName);
        intent.putExtra("name",name.getText().toString());
        EditText desc = (EditText)findViewById(R.id.inputDescription);
        intent.putExtra("desc",desc.getText().toString());
        EditText price = (EditText)findViewById(R.id.inputPrice);
        intent.putExtra("price",price.getText().toString());
        Log.i("price value", price.getText().toString());
        setResult(RESULT_OK, intent);
        finish();
    }
}
