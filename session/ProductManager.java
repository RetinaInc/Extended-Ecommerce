package session;

import entity.Category;
import javax.ejb.Stateless;
import entity.Product;
//import java.math.BigDecimal;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.SessionContext;
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
public class ProductManager {
    @PersistenceContext(unitName = "e-ShopPU")
    private EntityManager em;
    @Resource
    private SessionContext context;
    @EJB
    //private CategoryFacade categoryFacade;
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /*
    public void addPro(String name, BigDecimal Price, String description, Category category){
        try {
            //Product product = addProduct(name,price, description,category);
            
            //return product.getId();
           //return null;
        }
        catch (Exception e) {
            context.setRollbackOnly();
            //return 0;
        }
    }
    */
    //public Product addProduct(String name, Double price, String description, Integer category_id)
    public Product addProduct(String name, Double price, String description, Integer category_id) {
        Product product = new Product();
        product.setName(name);
        //product.setCatId();
        Category category = new Category(category_id);
        product.setCategory(category);
        product.setPrice(price);
        product.setDescription(description);
        
        //em.persist(category);
        em.persist(product); 
        return product;
    }
    public Product updateProduct(Product product, String name, Double price, String description, Integer category_id)
    {
        product.setName(name);
        //product.setCatId();
        Category category = new Category(category_id);
        product.setCategory(category);
        product.setPrice(price);
        product.setDescription(description);
        //em.persist(category);
        em.merge(product);
        return product;
    }
    
    /*
    public Product deleteProduct(Integer id) {
        Product product = new Product();
        product.setId(id);
        
        em.remove(product); 
        return product;
    }
    */
    public Product deleteProduct(Product product){
    Product productToRemove = em.getReference(Product.class, product.getId());
    em.remove(productToRemove); 
    return product;
    }
    public ProductManager(){
        
        
    }
    // Add business logic below. (Right-click in editor and choose
    // "Insert Code > Add Business Method")
    
}
