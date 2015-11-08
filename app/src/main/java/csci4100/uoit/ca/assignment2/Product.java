package csci4100.uoit.ca.assignment2;

import java.lang.String;

/**
 * @author Matthew Rosettis
 */
public class Product {
    private long id;
    private String productName;
    private String description;
    private double price;

    public Product(String productName, String description, double price) {
        this.id = -1;
        this.productName = productName;
        this.description = description;
        this.price = price;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }
}
