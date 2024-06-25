package Managers;

import Models.Product;
import Models.ProductDAO;
import Models.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ProductManager extends Observable {
    private List<Product> products = new ArrayList<>();
    private ProductDAO productDAO = new ProductDAO();

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
        setChanged();
        notifyObservers(products);
    }

    public void addProduct(Product product) {
        productDAO.insertProduct(product);
        products.add(product);
        setChanged();
        notifyObservers(products);
    }

    public void updateProduct(Product updatedProduct) {
        productDAO.updateProduct(updatedProduct);
        for (int i = 0; i < products.size(); i++) {
            Product product = products.get(i);
            if (product.getId().equals(updatedProduct.getId())) {
                products.set(i, updatedProduct);
                break;
            }
        }
        setChanged();
        notifyObservers(products);
    }

    public void removeProduct(Product product) {
        productDAO.deleteProduct(product);
        products.remove(product);
        setChanged();
        notifyObservers(products);
    }

    public void loadProductsFromDatabase() {
        List<Product> productsFromDB = productDAO.getAllProducts();
        setProducts(productsFromDB);
    }
    
    public void loadProductsFromDatabase(User canteen) {
        List<Product> productsFromDB = productDAO.getAllProducts(canteen);
        setProducts(productsFromDB);
    }
}
