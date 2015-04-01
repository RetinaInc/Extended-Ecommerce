package controller;
import entity.Category;
import entity.Customer;
import entity.CustomerOrder;
import entity.Product;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.HttpConstraint;
import javax.servlet.annotation.ServletSecurity;
import javax.servlet.annotation.ServletSecurity.TransportGuarantee;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import session.CategoryFacade;
import session.ProductFacade;
import session.CategoryManager;
import session.ProductManager;
import session.CustomerFacade;
import session.CustomerOrderFacade;
import session.OrderManager;
import validate.Validator;

/**
 *
 * @author Admin
 */
@WebServlet(name = "AdminServlet", 
            loadOnStartup = 2,
            urlPatterns = {
                            "/admin/",
                            "/admin/viewOrders",
                            "/admin/viewCustomers",
                            "/admin/customerRecord",
                            //"/admin/customerOrder",
                            "/admin/orderRecord",
                            "/admin/logout",
                            "/admin/showProduct",
                            "/admin/showCategory",
                            "/admin/addCategory",
                            "/admin/addCategory.jsp",
                            "/admin/addProduct",
                            "/admin/addProduct.jsp",
                            "/admin/deleteProduct.jsp",
                            "/admin/deleteProduct"
                            
                            })
@ServletSecurity( @HttpConstraint(transportGuarantee = TransportGuarantee.CONFIDENTIAL, rolesAllowed = {"eshopAdmin"}) )
public class AdminServlet extends HttpServlet {
    @EJB
    private OrderManager orderManager;
    @EJB
    private ProductManager productManager;
    @EJB
    private CustomerFacade customerFacade;
    @EJB
    private ProductFacade productFacade;
    @EJB
    private CategoryFacade categoryFacade;
    @EJB
    private CategoryManager categoryManager;
    @EJB
    private CustomerOrderFacade customerOrderFacade;
     
    private Customer customer;
    private CustomerOrder order;
    
    private List orderList = new ArrayList();
    private List customerList = new ArrayList();
    private Category category;
    private Product product;
    private List categoryList = new ArrayList();
    private List productList = new ArrayList();
   
    
    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //HttpSession session;
        //String userPath = request.getServletPath();
        
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
        HttpSession session;
        
        String userPath = request.getServletPath();
        //customer list
        if (userPath.equals("/admin/viewCustomers")){
            customerList = customerFacade.findAll();
            request.setAttribute("customerList", customerList);
        }
        //order list
        else if (userPath.equals("/admin/viewOrders")){
            orderList = customerOrderFacade.findAll();
            request.setAttribute("orderList", orderList);
        }
        //customer records of particular customer
        else if (userPath.equals("/admin/customerRecord")){
            //customer id from user request url
            String customerId = request.getQueryString();
            //get details of customer needs casting back
            customer = customerFacade.find(Integer.parseInt(customerId));
            request.setAttribute("customerRecord", customer);
            //get customer's order details
            order = customerOrderFacade.findByCustomer(customer);
            request.setAttribute("order", order);    
        }
        
        else if (userPath.equals("/admin/orderRecord")){
            //get customer id from order request
            String orderId = request.getQueryString();
            //get order details by mapping orders and customer
            Map orderMap = orderManager.getOrderDetails(Integer.parseInt(orderId));
            //place order details into app's scope by mapping
            request.setAttribute("customer", orderMap.get("customer"));
            request.setAttribute("products", orderMap.get("products"));
            request.setAttribute("orderRecord", orderMap.get("orderRecord"));
            request.setAttribute("orderedProducts", orderMap.get("orderedProducts"));
            
        }
        else if (userPath.equals("/admin/uploads.jsp")){
            
        }
        else if (userPath.equals("/admin/showProduct")){
            productList = productFacade.findAll();
            request.setAttribute("productList", productList);
            
        }
        else if (userPath.equals("/admin/deleteProduct")){
            String action= request.getParameter("action");
            // get categoryId from request
            Integer id = Integer.parseInt(request.getQueryString());
            //Integer id = request.getParameter("id");
            //Integer id = Integer.parseInt(request.getParameter("id"));
            if (action.equalsIgnoreCase("Delete")){
                //request.setAttribute("id" , id);
                    //userPath = "/admin/showCategory";
                    productManager.deleteProduct(id);
            }
        }
        else if (userPath.equals("/admin/showCategory")){
            categoryList = categoryFacade.findAll();
            request.setAttribute("categoryList", categoryList);
            
        }
        /*
       else if (userPath.contains("/admin/updateCategory.jsp")){
            // get categoryId from request
            //String categoryId = request.getQueryString();
            String categoryId = request.getParameter("id");
            if (categoryId != null){
                try {
                    category = categoryFacade.find(Integer.parseInt(categoryId));
                    request.setAttribute("category", category);
                }
                catch (Exception e){
                    e.toString();
                }
            }
        }
        */
        else if (userPath.equals("/admin/logout")){
            try {
                session = request.getSession();
                session.invalidate();
            
                response.sendRedirect("/e-Shop/admin/");
                return;
            }
            catch (Exception e){
                e.toString();
            }
            
        }
        //forward request internally
        userPath = "/admin/index.jsp";
        try {
            request.getRequestDispatcher(userPath).forward(request, response);
        }
        catch (ServletException | IOException ex){
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
        //processRequest(request, response);
        request.setCharacterEncoding("UTF-8");  
        String userPath = request.getServletPath();  
        HttpSession session = request.getSession();
        
        Validator validator = new Validator() {};
        if (userPath.equals("/admin/addCategory")){
            
                String name = request.getParameter("name");
                boolean validationErrorFlag;
                validationErrorFlag = validator.validateForm(name, request);
                // if validation error found, return  to same
                if (validationErrorFlag == true) {
                    request.setAttribute("validationErrorFlag", validationErrorFlag);
                    
                }
                else {
                    try{
                    request.setAttribute("name" , name);
                    //userPath = "/admin/showCategory";
                    categoryManager.addCategory(name);
                    //RequestDispatcher rd = 
                           // request.getRequestDispatcher("/admin/showCategory").forward(request, response);
                   getServletContext().getRequestDispatcher("/admin/showCategory").forward(
				request, response);

                        ///response.sendRedirect("/admin/showCategory");
                    }
                    catch(Exception ex){
                        ex.toString();
                    }
             
                    //Category category = categoryManager.addCategory(name);
                    
                }
                
        
        }
        else if (userPath.equals("/admin/updateCategory")){
            Integer id = Integer.parseInt(request.getParameter("id"));
            //category = categoryFacade.find(id);
            String name = request.getParameter("name");
            boolean validationErrorFlag;
                validationErrorFlag = validator.validateForm(name, request);
                // if validation error found, return  to same
                if (validationErrorFlag == true) {
                    request.setAttribute("validationErrorFlag", validationErrorFlag);
                    
                }
                else {
                    try {
                        request.setAttribute("name" , name);
                        request.setAttribute("id" , id);
                    }
                    catch(Exception ex){
                        ex.toString();
                    }
                }
        }
        /*
        else if (userPath.equals("/admin/updateCategory")){
             //Integer category_id = Integer.parseInt(request.getParameter("category_id")) ;
           Integer id = Integer.parseInt(request.getParameter("id"));
             //category = categoryFacade.find(id);
            //String id = request.getParameter("id");
             String name = request.getParameter("name");
             boolean validationErrorFlag;
                validationErrorFlag = validator.validateForm(name, request);
                // if validation error found, return  to same
                if (validationErrorFlag == true) {
                    request.setAttribute("validationErrorFlag", validationErrorFlag);
                    
                }
                else {
                    try{
                        
                        request.setAttribute("name" , name);
                        request.setAttribute("id" , id);
                    //userPath = "/admin/showCategory";
                    //categoryManager.updateCategory(name);
                    categoryManager.updateCategory(id, name);
                    }
                    catch(Exception ex){
                        ex.toString();
                    }
                }
             /*
             if (categoryId != null){
                try {
                    category = categoryFacade.find(Integer.parseInt(categoryId));
                    request.setAttribute("category", category);
                }
                catch (Exception e){
                    e.toString();
                }
            }
                     *//*
        }
        */
        else if (userPath.equals("/admin/addProduct")){
            
            
            String name = request.getParameter("name");
            Double price = Double.parseDouble(request.getParameter("price")) ;
            
            String description = request.getParameter("description");
            Integer category_id = Integer.parseInt(request.getParameter("category_id")) ;
                      
            //Category category = new Category(request.getParameter("category"););
            //String category = category(request.getParameter("category"));
                boolean validationErrorFlag;
                validationErrorFlag = validator.validateForm(name, request);
                // if validation error found, return  to same
                if (validationErrorFlag == true) {
                    request.setAttribute("validationErrorFlag", validationErrorFlag);
                    
                }
                else {
                    try{
                    request.setAttribute("name" , name);
                    request.setAttribute("price" , price);
                    request.setAttribute("description" , description);
                    // get selected category
                    //Category category = categoryFacade.find(Integer.parseInt(categoryId));
                    //request.setAttribute("category" , category);
                    request.setAttribute("category_id" , category_id);
                    productManager.addProduct(name, price, description, category_id);
                    //response.sendRedirect("/admin/showProduct");
                    }
                    catch(Exception ex){
                        ex.toString();
                    }
                }    
                    
        }
        
        
        
        // use RequestDispatcher to forward request internally
        String url = userPath + ".jsp";
        try {
            request.getRequestDispatcher(url).forward(request, response);
        }
        catch (ServletException | IOException ex){
            ex.toString();
        }
        
    }


}
