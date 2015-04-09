
package session;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.math.BigDecimal;

import cart.ShoppingCart;
import cart.ShoppingCartItem;

import entity.Customer;
import entity.CustomerOrder;
import entity.OrderedProduct;
import entity.OrderedProductPK;
import entity.Product;


import javax.annotation.Resource;

import javax.ejb.EJB;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Admin
 */
@Stateless
@TransactionManagement(TransactionManagementType.CONTAINER)
public class OrderManager {
    @PersistenceContext(unitName = "e-ShopPU")
    private EntityManager em;
    @Resource
    private SessionContext context;
    @EJB
    private ProductFacade productFacade;
    @EJB
    private CustomerOrderFacade customerOrderFacade;
    
    @EJB
    private OrderedProductFacade orderedProductFacade;
    
    
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int placeOrder(String name, String email, String phone, String address, String cityRegion, ShoppingCart cart) {
        try {
            Customer customer = addCustomer(name, email, phone, address, cityRegion);
            CustomerOrder order = addOrder(customer, cart);
            addOrderedItems(order, cart);
        
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        return order.getId();
        }
        catch (Exception e) {
            context.setRollbackOnly();
            return 0;
        }
        
    }

    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")

    private Customer addCustomer(String name, String email, String phone, String address, String cityRegion) {
        Customer customer = new Customer();
        customer.setName(name);
        customer.setEmail(email);
        customer.setPhone(phone);
        customer.setAddress(address);
        customer.setCityRegion(cityRegion); 
        
        em.persist(customer);    
        return customer;
           
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private CustomerOrder addOrder(Customer customer, ShoppingCart cart) {
        // set up customer order
        CustomerOrder order = new CustomerOrder();
        order.setCustomer(customer); 
        order.setAmount(BigDecimal.valueOf(cart.getTotal())); 
        // create confirmation number 
        Random random = new Random();
        int i = random.nextInt(999999999);
        order.setConfirmationNumber(i);
        em.persist(order);
        return order;
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    private void addOrderedItems(CustomerOrder order, ShoppingCart cart) {
        em.flush();
        List<ShoppingCartItem> items = cart.getItems();
        // iterate through shopping cart and create OrderedProducts
        for (ShoppingCartItem scItem : items){
            int productId = scItem.getProduct().getId();
            // set up primary key object   
            OrderedProductPK orderedProductPK = new OrderedProductPK();
            orderedProductPK.setCustomerOrderId(order.getId());
            orderedProductPK.setProductId(productId);  
            // create ordered item using PK object 
            OrderedProduct orderedItem = new OrderedProduct(orderedProductPK); 
            // set quantity
            orderedItem.setQuantity(scItem.getQuantity());
            em.persist(orderedItem);
        }
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    public Map getOrderDetails(int orderId){
        Map orderMap = new HashMap();
        // get order
        CustomerOrder order = customerOrderFacade.find(orderId);
        // get customer
        Customer customer = order.getCustomer();
        // get all ordered products
        List<OrderedProduct> orderedProducts = orderedProductFacade.findByOrderId(orderId);

        List<Product> products;
        products = new ArrayList<>();
        for (OrderedProduct op : orderedProducts) {

            Product p = (Product) productFacade.find(op.getOrderedProductPK().getProductId());
            products.add(p);
        }

        // add each item to orderMap
        orderMap.put("orderRecord", order);
        orderMap.put("customer", customer);
        orderMap.put("orderedProducts", orderedProducts);
        orderMap.put("products", products);

        return orderMap;
    }
}
