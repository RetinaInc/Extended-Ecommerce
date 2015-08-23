package cart;

import entity.Product;

/**
 *
 * @author Admin
 */
public class ShoppingCartItem {
    Product product;
    int quantity;
    
    public ShoppingCartItem(Product product){
        this.product = product;
        quantity = 1;
    }
    public Product getProduct(){
        return product;
    }
    public int getQuantity(){
        return quantity;
    }
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void incrementQuantity(){
        quantity++;
    }
    public void decrementQuantity(){
        quantity--;
    }
    public double getTotal(){
        //double amount = 0;
        double amount;
        
        //amount = (this.getQuantity() * product.getPrice().doubleValue());
        //double incase it is not not float
        amount = (this.getQuantity() * product.getPrice());
        return amount;
    }
}
