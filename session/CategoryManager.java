/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import javax.ejb.Stateless;
import entity.Category;
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
public class CategoryManager {
    @PersistenceContext(unitName = "e-ShopPU")
    private EntityManager em;
    @Resource
    private SessionContext context;
    @EJB
    //private CategoryFacade categoryFacade;
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    /**
    public int addCat(String name){
        try {
            Category category = addCategory(name);
            return category.getId();
        }
        catch (Exception e) {
            context.setRollbackOnly();
            return 0;
        }
    }
    * */
    public Category addCategory(String name) {
        Category category = new Category();
        category.setName(name);
        em.persist(category); 
        return category;
        
        
    }
    /*
    //public Category updateCategory(String name) {
    public Category updateCategory(int id, String name) {
        //Category category = new Category();
        Category category = em.find(Category.class,id);
        category.setName(name);
        //category.setId(category_id);
        em.merge(category); 
        return category;
        
        
    }
    */
    public Category updateCategory(Category category, String name){
        //Category category = new Category();
        //em.getTransaction().begin();
        //Category category = em.find(Category.class, id);
        category.setName(name);
        em.merge(category); 
       // em.getTransaction().commit();
        return category;
    }
    /*
    public Category updateCategory(int id, String name) {
        Category category = em.find(Category.class,id);
       
        //category.setName(name);
        //category.setId(category_id);
        category.setName(name);
        em.merge(category); 
        return category;
        
        
    }
    */
    public Category deleteCategory(Category category){
    //Category category = em.find(Category.class,id);
        Category categoryToRemove = em.getReference(Category.class, category.getId());
    em.remove(categoryToRemove); 
    return category;
    }
    /*
    public Category deleteCategory(int id){
        Category category = new Category();
        category.setId(id);
        em.remove(category); 
        return category;
    }
    */
    public CategoryManager(){
        
        
    }
    /*
    public Category updateCategory(int category_id, String name){
        Category category = em.find(Category.class, category_id);
        category.setName(name);
        em.merge(category); 
        return category;
    }
    */
    
}
