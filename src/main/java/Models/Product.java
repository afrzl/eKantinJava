package Models;

import javafx.beans.binding.BooleanExpression;

import java.text.NumberFormat;
import java.util.Locale;

public class Product {
    private Integer id;
    private String name;
    private String description;
    private String image;
    private Integer price;
    private Integer stock;
    private Category category;
    private User canteen;

    public Product(Integer id, String name, String description, String image, Integer price, Integer stock, Category category, User canteen) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.image = image;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.canteen = canteen;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPrice() {
        return price;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public Integer getStock() {
        return stock;
    }

    public void setStock(Integer stock) {
        this.stock = stock;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public User getCanteen() {
        return canteen;
    }

    public void setCanteen(User canteen) {
        this.canteen = canteen;
    }

    public String getPriceFormatted() {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(getPrice());
    }
}
