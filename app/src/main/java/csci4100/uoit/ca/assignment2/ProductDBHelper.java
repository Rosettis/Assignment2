package csci4100.uoit.ca.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.lang.Long;
import java.lang.Override;
import java.lang.String;
import java.util.ArrayList;

/**
 * @author Matthew Rosettis
 */
public class ProductDBHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_FILENAME = "product.db";
    public static final String TABLE_NAME = "product";

    // don't forget to use the column name '_id' for your primary key
    public static final String CREATE_STATEMENT = "CREATE TABLE " + TABLE_NAME + "(" +
            "  _id integer primary key autoincrement, " +
            "  productName text not null," +
            "  Description text not null," +
            "  price real not null" +
            ")";
    public static final String DROP_STATEMENT = "DROP TABLE " + TABLE_NAME;

    public ProductDBHelper(Context context) {
        super(context, DATABASE_FILENAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        database.execSQL(CREATE_STATEMENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        // the implementation below is adequate for the first version
        // however, if we change our table at all, we'd need to execute code to move the data
        // to the new table structure, then delete the old tables (renaming the new ones)

        // the current version destroys all existing data
        database.execSQL(DROP_STATEMENT);
        database.execSQL(CREATE_STATEMENT);
    }

    public Product createProduct(String productName, String Description, Double price) {
        // create the object
        Product product = new Product(productName, Description, price);

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // insert the data into the database
        ContentValues values = new ContentValues();
        values.put("productName", product.getProductName());
        values.put("Description", product.getDescription());
        values.put("price", product.getPrice());
        long id = database.insert(TABLE_NAME, null, values);

        // assign the Id of the new database row as the Id of the object
        product.setId(id);

        return product;
    }

    public Product getProduct(long id) {
        Product product = null;

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the product from the database
        String[] columns = new String[] { "_id", "productName", "Description", "price" };
        Cursor cursor = database.query(TABLE_NAME, columns, "_id = ?", new String[] { "" + id }, "", "", "");
        if (cursor.getCount() >= 1) {
            cursor.moveToFirst();
            String productName = cursor.getString(0);
            String Description = cursor.getString(1);
            Double price = cursor.getDouble(2);
            product = new Product(productName, Description, price);
            product.setId(id);
        }

        Log.i("DatabaseAccess", "getProduct(" + id + "):  product: " + product);
        cursor.close();
        return product;
    }

    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();

        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // retrieve the product from the database
        String[] columns = new String[] { "_id", "productName", "Description", "price" };
        Cursor cursor = database.query(TABLE_NAME, columns, "", new String[]{}, "", "", "");
        cursor.moveToFirst();
        do {
            // collect the product data, and place it into a product object
            long id = Long.parseLong(cursor.getString(0));
            String productName = cursor.getString(1);
            String Description = cursor.getString(2);
            Double price = cursor.getDouble(3);
            Product product = new Product(productName, Description, price);
            product.setId(id);

            // add the current product to the list
            products.add(product);

            // advance to the next row in the results
            cursor.moveToNext();
        } while (!cursor.isAfterLast());

        Log.i("DatabaseAccess", "getAllProducts():  num: " + products.size());
        cursor.close();
        return products;
    }
    public boolean updateProduct(Product product) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // update the data in the database
        ContentValues values = new ContentValues();
        values.put("productName", product.getProductName());
        values.put("Description", product.getDescription());
        values.put("price", product.getPrice());
        int numRowsAffected = database.update(TABLE_NAME, values, "_id = ?", new String[] { "" + product.getId() });

        Log.i("DatabaseAccess", "updateProduct(" + product + "):  numRowsAffected: " + numRowsAffected);

        // verify that the product was updated successfully
        return (numRowsAffected == 1);
    }

    public boolean deleteProduct(long id) {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the product
        int numRowsAffected = database.delete(TABLE_NAME, "_id = ?", new String[] { "" + id });

        Log.i("DatabaseAccess", "deleteProduct(" + id + "):  numRowsAffected: " + numRowsAffected);

        // verify that the product was deleted successfully
        return (numRowsAffected == 1);
    }

    public void deleteAllProducts() {
        // obtain a database connection
        SQLiteDatabase database = this.getWritableDatabase();

        // delete the products
        int numRowsAffected = database.delete(TABLE_NAME, "", new String[] {});

        Log.i("DatabaseAccess", "deleteAllProducts():  numRowsAffected: " + numRowsAffected);
    }
}

