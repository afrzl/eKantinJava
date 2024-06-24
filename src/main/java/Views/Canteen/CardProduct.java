package Views.Canteen;

import Managers.CartManager;
import Managers.ProductManager;
import Models.Product;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class CardProduct implements Initializable {
    @FXML
    private ImageView imageView;

    @FXML
    private Label nameLabel;

    @FXML
    private Label priceLabel;

    private Product product;
    private CartManager cartManager;

    public CardProduct(Product product, CartManager cartManager) {
        this.product = product;
        this.cartManager = cartManager;
    }

    @FXML
    void handleClick(MouseEvent event) {
        cartManager.addProductToCart(product);
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        nameLabel.setText(product.getName());
        priceLabel.setText(String.valueOf(product.getPriceFormatted()));

        String imagePath = product.getImage();
        if (imagePath != null && !imagePath.isEmpty()) {
            File imageFile = new File(imagePath);
            if (imageFile.exists()) {
                Image image = new Image(imageFile.toURI().toString());
                imageView.setImage(image);
            } else {
                imageView.setImage(null);
            }
        } else {
            imageView.setImage(null);
        }
    }
}
