/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.tool.params;

import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.model.ProductType;

/**
 *
 * @author Irsa
 */
public class ProductTypeBean {
    private PollListManager pollListManager;
    public String id;
    public String productType;
    public String processActionAddProductType(){
            if (productType != null ) {
                ProductType newproductType = new ProductType();
                boolean status;
                
                newproductType.setProductType(productType);
 
                status = pollListManager.saveProductType(newproductType);
                if(status){
                    return "success";
                }
                
            }else{
                return "failure";
            }
            return "failure";
        }
    public String processActionUpdateProductType(){
            if (productType != null ) {
                ProductType newproductType = pollListManager.getProductType(Long.valueOf(id));
                boolean status;
                
                newproductType.setProductType(productType);
 
                status = pollListManager.updateProductType(newproductType);
                if(status){
                    return "success";
                }
                
            }else{
                return "failure";
            }
            return "failure";
        }

    public void setPollListManager(PollListManager pollListManager) {
        this.pollListManager = pollListManager;
    }
    
    public String processActionDeleteProductType(){            
                ProductType newproductType = pollListManager.getProductType(Long.valueOf(id));
                boolean status;
 
                pollListManager.deleteProductType(newproductType);
                
                return "success";

        }

}
