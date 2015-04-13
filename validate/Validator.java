
package validate;

import javax.servlet.http.HttpServletRequest;

    
/**
 *
 * @author Admin
 */
public class Validator {
    public boolean validateQuantity (String productId, String quantity){
        boolean errorFlag = false;
        
        if (!productId.isEmpty() && !quantity.isEmpty()){
            int i = -1;
            try{
                i = Integer.parseInt(quantity);
            }catch (NumberFormatException nfe){
                System.out.println("User did not enter a number in the quantity field");
            }
            if (i < 0 || i > 99){
                errorFlag = true;
            }
        }
        return errorFlag;
    }
    public boolean validateForm(String name, HttpServletRequest request){
        boolean errorFlag = false;
        boolean nameError;
        if (name==null || name.equals("") || name.length() < 3 || name.length() > 30){
            errorFlag = true;
            nameError = true;
            request.setAttribute("nameError", nameError);
        }
        return errorFlag;
    }
    public boolean validateForm(
                                String name,
                                String category,
                                String price,
                                String description,
                                HttpServletRequest request
                                ){
        boolean errorFlag = false;
        boolean nameError;
        boolean categoryError;
        boolean priceError;
        boolean descriptionError;
        if (name == null
                || name.equals("")
                || name.length() > 45) {
            errorFlag = true;
            nameError = true;
        request.setAttribute("nameError", nameError);
        }
        if (category == null || category.equals("")){
            errorFlag = true;
            categoryError = true;
            request.setAttribute("categoryError", categoryError);
        }
        if (price == null || price.equals("")){
            errorFlag = true;
            priceError = true;
            request.setAttribute("priceError", priceError);
        }
        if (description == null || description.equals("")){
            errorFlag = true;
            descriptionError = true;
            request.setAttribute("descriptionError", descriptionError);
        }
        return errorFlag;
    }
    
    public boolean validateForm(
                                String name,
                                String email,
                                String phone,
                                String address,
                                String cityRegion,
                                HttpServletRequest request
                            ){
        boolean errorFlag = false;
        boolean nameError;
        boolean emailError;
        boolean phoneError;
        boolean addressError;
        boolean cityRegionError;
        
        if (name == null
                || name.equals("")
                || name.length() > 45) {
            errorFlag = true;
            nameError = true;
            request.setAttribute("nameError", nameError);
        }
        if (email == null
                || email.equals("")
                || !email.contains("@")) {
            errorFlag = true;
            emailError = true;
            request.setAttribute("emailError", emailError);
        }
        if (phone == null
                || phone.equals("")
                || phone.length() < 9) {
            errorFlag = true;
            phoneError = true;
            request.setAttribute("phoneError", phoneError);
        }
        if (address == null
                || address.equals("")
                || address.length() > 45) {
            errorFlag = true;
            addressError = true;
            request.setAttribute("addressError", addressError);
        }
        if (cityRegion == null
                || cityRegion.equals("")
                || cityRegion.length() < 3) {
            errorFlag = true;
            cityRegionError = true;
            request.setAttribute("cityRegionError", cityRegionError);
        }
        
        return errorFlag;
    }
}
