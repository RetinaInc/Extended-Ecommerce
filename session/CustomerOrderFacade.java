/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package session;

import entity.CustomerOrder;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Admin
 */
@Stateless
public class CustomerOrderFacade extends AbstractFacade<CustomerOrder> {
    @PersistenceContext(unitName = "e-ShopPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CustomerOrderFacade() {
        super(CustomerOrder.class);
    }
    // overridden - refresh method called to retrieve order id from database
    @Override
    public CustomerOrder find(Object id) {
        CustomerOrder order = em.find(CustomerOrder.class, id);
        em.refresh(order);
        return order;
    }
    @RolesAllowed("eshopAdmin")
    public CustomerOrder findByCustomer(Object customer) {
        return (CustomerOrder) em.createNamedQuery("CustomerOrder.findByCustomer").setParameter("customer", customer).getSingleResult();
    }
    
}
