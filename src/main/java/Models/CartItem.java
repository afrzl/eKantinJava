package Models;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.text.NumberFormat;
import java.util.Locale;

public class CartItem {
    private Product product;
    private SimpleIntegerProperty qty;
    private SimpleIntegerProperty totalPrice;
    private StringProperty formattedTotalPrice;

    public CartItem(Product product, int qty) {
        this.product = product;
        this.qty = new SimpleIntegerProperty(qty);
        this.totalPrice = new SimpleIntegerProperty(product.getPrice() * qty);;
        this.formattedTotalPrice = new SimpleStringProperty(formatPrice(this.totalPrice.get()));

        this.qty.addListener((observable, oldValue, newValue) -> {
            int newTotalPrice = product.getPrice() * newValue.intValue();
            this.totalPrice.set(newTotalPrice);
            this.getFormattedTotalPrice().set(formatPrice(newTotalPrice));
        });
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty.get();
    }

    public void setQty(int qty) {
        this.qty.set(qty);
        this.totalPrice.set(product.getPrice() * qty);
    }

    public Integer getTotalPrice() {
        return totalPrice.get();
    }

    public void setTotalPrice(Integer totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public String getProductName() {
        return product.getName();
    }

    public SimpleIntegerProperty qtyProperty() {
        return qty;
    }

    public SimpleIntegerProperty totalPriceProperty() {
        return totalPrice;
    }

    private String formatPrice(int price) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));
        return formatter.format(price);
    }

    public StringProperty getFormattedTotalPrice() {
        return formattedTotalPrice;
    }

    public void setFormattedTotalPrice(StringProperty formattedTotalPrice) {
        this.formattedTotalPrice = formattedTotalPrice;
    }
}
