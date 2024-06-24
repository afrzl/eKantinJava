package Managers;

import Models.CartItem;
import Models.Product;
import Utils.AlertUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class CartManager extends Observable {
    private List<CartItem> cart = new ArrayList<>();

    public void addProductToCart(Product product) {
        boolean found = false;
        for (CartItem cartItem : cart) {
            if (cartItem.getProduct().getId().equals(product.getId())) {

                if (cartItem.getProduct().getStock() < cartItem.getQty() + 1) {
                    AlertUtils.showErrorAlert("Stok kurang", "Stok produk " + cartItem.getProductName() + " tidak mencukupi");
                    return;
                }

                cartItem.setQty(cartItem.getQty() + 1);
                found = true;
                break;
            }
        }

        if (!found) {
            if (product.getStock() < 1) {
                AlertUtils.showErrorAlert("Stok kurang", "Stok produk " + product.getName() + " tidak mencukupi");
                return;
            }

            CartItem newCartItem = new CartItem(product, 1);
            cart.add(newCartItem);
        }

        setChanged();
        notifyObservers(cart);
    }

    public void removeProductFromCart(CartItem cartItem) {
        cart.remove(cartItem);
        setChanged();
        notifyObservers(cart);
    }

    public List<CartItem> getCart() {
        return cart;
    }
}
