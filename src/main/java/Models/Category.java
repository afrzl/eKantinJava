package Models;

import java.util.ArrayList;

public class Category {
    private Integer id;
    private String name;
    private ArrayList<Product> products;

    public Category(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Category(Integer id, String name, ArrayList<Product> products) {
        this.id = id;
        this.name = name;
        this.products = products;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Product> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Product> products) {
        this.products = products;
    }

    @Override
    public String toString() {
        return name;
    }
}
