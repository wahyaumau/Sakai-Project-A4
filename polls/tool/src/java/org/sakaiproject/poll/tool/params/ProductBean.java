/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.tool.params;

import java.util.Date;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.model.Product;
import org.sakaiproject.poll.model.Transaction;

/**
 *
 * @author Irsa
 */
public class ProductBean {
    private PollListManager pollListManager;
    public String id;
    public String productName;
    public String productPrice;
    public String productStock;
    public String productTypeId;
    
    public String productId;
    public String customerId;
    public String productAmount;
    
    public String productStockAdd;
    
    public void setPollListManager(PollListManager pollListManager) {
        this.pollListManager = pollListManager;
    }
    
    public String processActionAddProduct(){
            if (productName != null && productPrice != null && productStock != null && productTypeId != null) {
                Product product = new Product();
                boolean status;
                
                product.setName(productName);
                product.setPrice(Float.parseFloat(productPrice));
                product.setStock(Integer.parseInt(productStock));
                product.setProductTypeId(Long.parseLong(productTypeId));
 
                status = pollListManager.saveProduct(product);
                if(status){
                    return "success";
                }
                
            }else{
                return "failure";
            }
            return "failure";
    }
    
    public String processActionUpdateProduct(){
            
                Product product = pollListManager.getProduct(Long.valueOf(id));
                boolean status;                
                product.setName(productName);
                product.setPrice(Float.parseFloat(productPrice));
                product.setStock(Integer.parseInt(productStock));
                product.setProductTypeId(Long.parseLong(productTypeId)); 
                status = pollListManager.updateProduct(product);
                if(status){
                    return "success";
                }else{
                    return "failure";
                }                            
    }
    
    public String processActionDeleteProduct(){
        Product product = pollListManager.getProduct(Long.valueOf(id));
        pollListManager.deleteProduct(product);
        return "success";
    }
    
    public String processActionAddTransaction(){
            Product product = pollListManager.getProduct(Long.parseLong(productId));                    
            if (Integer.parseInt(productAmount) <= product.getStock() ) {
                Transaction transaction = new Transaction();
                boolean status;
                
                transaction.setCustomerId(Long.parseLong(customerId));
                transaction.setProductId(Long.parseLong(productId));
                transaction.setAmount(Integer.parseInt(productAmount));
                transaction.setTransactionDate(new Date());                                
                
                status = pollListManager.saveTransaction(transaction);
                if(status){
                    int updateStock = product.getStock() - Integer.parseInt(productAmount);
                    product.setStock(updateStock);
                    status = pollListManager.updateProduct(product);
                    if (status) {
                        return "success";
                    }
                }
                
            }else{
                return "failure";
            }
            return "failure";
    }
    
    public String processActionAddStockProduct(){
            
                Product product = pollListManager.getProduct(Long.valueOf(id));
                int updatedStock = product.getStock() + Integer.parseInt(productStockAdd);
                boolean status;                                
                product.setStock(updatedStock);                
                status = pollListManager.updateProduct(product);
                if(status){
                    return "success";
                }else{
                    return "failure";
                }                            
    }
    
    
}

