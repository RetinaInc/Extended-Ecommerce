package controller;


import cart.ShoppingCart;

import entity.Category;
import entity.Product;
import java.io.IOException;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

import javax.ejb.EJB;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import validate.Validator;
import session.CategoryFacade;
import session.OrderManager;
import session.ProductFacade;

/**
 *
 * @author Admin
 */
@WebServlet(name = "ControllerServlet",
            loadOnStartup = 1,
            urlPatterns = {
                            "/category",
                            "/product",
                            "/addToCart", 
                            "/viewCart",
                            "/updateCart",
                            "/checkout",
                            "/purchase",
                            "/chooseLanguage"
                                                        })
public class ControllerServlet extends HttpServlet {

    private String surcharge;
    @EJB
    private CategoryFacade categoryFacade;
    @EJB
    private ProductFacade productFacade;
    @EJB
    private OrderManager orderManager;
    @Override
    public void init(ServletConfig servletConfig) throws ServletException {
        super.init(servletConfig);
        // initialize servlet with configuration information
        surcharge = servletConfig.getServletContext().getInitParameter("deliverySurcharge");
        // store category list in servlet context
        getServletContext().setAttribute("categories", categoryFacade.findAll());
        getServletContext().setAttribute("products", categoryFacade.findAll());
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //processRequest(request, response);
        String userPath = request.getServletPath();
        HttpSession session;
        session = request.getSession();
        //if category page is requested
        // if category page is requested
        if (userPath.equals("/category")) {

            // get categoryId from request
            String categoryId = request.getQueryString();
                // get categoryId from request
                //if category id is not null
                if (categoryId != null){
                    try {
                        // get selected category
                        Category selectedCategory = categoryFacade.find(Integer.parseInt(categoryId));
                        // place selected category in request scope
                        //request.setAttribute("selectedCategory", selectedCategory);
                        //place selected category in session scope
                        session.setAttribute("selectedCategory", selectedCategory);
                        // get all products for selected category
                       
                        //categoryProducts = selectedCategory.getProductCollection(); old version way
                        Collection<Product> categoryProducts = selectedCategory.getProductCollection();
                        // place category products in request scope
                        //request.setAttribute("categoryProducts", categoryProducts);
                        // place category products in session scope
                        session.setAttribute("categoryProducts", categoryProducts);
                    } catch (Exception ex) {
                    ex.toString();
                        }
                    
                }   
                
                // if cart page is requested
        } 
        
        else if (userPath.equals("/viewCart")) {
                // TODO: Implement cart page request
                String clear = request.getParameter("clear");
                if ((clear != null) && clear.equals("true")){
                    //to retrieve the cart from the session
                ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
                cart.clear();
                }   
                userPath = "/cart";
        }
        else if (userPath.equals("/checkout")) {
                // TODO: Implement cart page request
                //to retrieve the cart from the session
                ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
                //calculate total
                if (surcharge != null){
                    cart.calculateTotal(surcharge);
                }
                
                // forward to checkout page and switch to a secure channel
        }
        
        else if (userPath.equals("/chooseLanguage")) {
                // get language choice
            String language = request.getParameter("language");
            // place in request scope
            request.setAttribute("language", language);

            String userView = (String) session.getAttribute("view");

            if ((userView != null) &&
                (!userView.equals("/index"))) {     // index.jsp exists outside 'view' folder
                                                    // so must be forwarded separately
                userPath = userView;
            } else {

                // if previous view is index or cannot be determined, send user to welcome page
                try {
                    request.getRequestDispatcher("/index.jsp").forward(request, response);
                } catch (ServletException | IOException ex) {
                    ex.toString();
                }
                return;
            }
        }
        // use RequestDispatcher to forward request internally
        String url = "/WEB-INF/view/" + userPath + ".jsp";
        try {
            request.getRequestDispatcher(url).forward(request, response);
        }
        catch (ServletException | IOException ex) {
            ex.toString();
        }
        
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");  
        String userPath = request.getServletPath();  
        HttpSession session = request.getSession();
        ShoppingCart cart = (ShoppingCart) session.getAttribute("cart");
        Validator validator = new Validator() {};
        
        // if addToCart action is called
        if (userPath.equals("/addToCart")) {
            
                    // TODO: Implement add product to cart action
                    
                    // if user is adding item to cart for first time
                    // create cart object and attach it to user session
                    if (cart == null){
                        cart = new ShoppingCart();
                        session.setAttribute("cart", cart);
                    }       
                    // get user input from request
                    String productId = request.getParameter("productId");
                    //if product id exist in request
                    if (!productId.isEmpty()){
                        Product product = productFacade.find(Integer.parseInt(productId));
                    cart.addItem(product);
                    }       
                userPath = "/category";
                
        }
        else if (userPath.equals("/updateCart")) {
            
                // TODO: Implement update cart action
                // get input from request
                String productId = request.getParameter("productId");
                String quantity =  request.getParameter("quantity");
                Product product = productFacade.find(Integer.parseInt(productId));
                cart.update(product, quantity);
                userPath = "/cart";
                // if purchase action is called        
        }
            
        else if (userPath.equals("/purchase")) {
            
                // TODO: Implement purchase action
                if (cart != null){
                    // extract user data from request
                    String name = request.getParameter("name");
                    String email = request.getParameter("email");
                    String phone = request.getParameter("phone");
                    String address = request.getParameter("address");
                    String cityRegion = request.getParameter("cityRegion");
                    
                   
                    boolean validationErrorFlag;
                    validationErrorFlag = validator.validateForm(name, email, phone, address, cityRegion, request);
                    // if validation error found, return user to checkout
                    if (true == validationErrorFlag) {
                    request.setAttribute("validationErrorFlag", validationErrorFlag);
                    userPath = "/checkout";
                    // otherwise, save order to database
                    } 
                    else {
                        
                        int orderId = orderManager.placeOrder(name, email, phone, address, cityRegion, cart);

                    // if order processed successfully send user to confirmation page
                        if (orderId != 0) {
                            // dissociate shopping cart from session
                        Locale locale = (Locale) session.getAttribute("javax.servlet.jsp.jstl.fmt.locale.session");
                        String language = "";
                            if (locale != null) {

                            language = (String) locale.getLanguage();
                            }
                        // dissociate shopping cart from session
                        //cart = null;

                        // end session
                        session.invalidate();
                            

                        // get order details
                        Map orderMap = orderManager.getOrderDetails(orderId);

                        // place order details in request scope
                        request.setAttribute("customer", orderMap.get("customer"));
                        request.setAttribute("products", orderMap.get("products"));
                        request.setAttribute("orderRecord", orderMap.get("orderRecord"));
                        request.setAttribute("orderedProducts", orderMap.get("orderedProducts"));

                        userPath = "/confirmation";
                        }
                        else {
                            // otherwise, send back to checkout page and display error
                            userPath = "/checkout";
                            request.setAttribute("orderFailureFlag", true);
                        }
                    }
                }
                
        
                
                
            }
    
      
            
        
    // use RequestDispatcher to forward request internally
    String url = "/WEB-INF/view" + userPath + ".jsp";
    try {            
    request.getRequestDispatcher(url).forward(request, response);
    } catch (ServletException | IOException ex) {
        ex.toString();
    }   
        
    }

    

}
