/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.sakaiproject.poll.tool.params;

import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.model.Customer;

/**
 *
 * @author Irsa
 */
public class CustomerBean {
    
    private PollListManager pollListManager;
    public String id;
    public String customerName;
    public String customerAddress;
    public String customerPhone;
    
    public void setPollListManager(PollListManager pollListManager) {
        this.pollListManager = pollListManager;
    }
    
    public String processActionAddCustomer(){
            if (customerName != null && customerAddress != null && customerPhone != null) {
                Customer customer = new Customer();
                boolean status;
                
                customer.setName(customerName);
                customer.setAddress(customerAddress);
                customer.setPhone(customerPhone);
 
                status = pollListManager.saveCustomer(customer);
                if(status){
                    return "success";
                }
            }else{
                return "failure";
            }
            return "failure";
    }
    
    public String processActionUpdateCustomer(){
        
        Customer customer = pollListManager.getCustomer(Long.valueOf(id));
        boolean status;  
        
        customer.setName(customerName);
        customer.setAddress(customerAddress);
        customer.setPhone(customerPhone);
        
        status = pollListManager.updateCustomer(customer);
        
        if(status){
            return "success";
        }else{
            return "failure";
        }                            
    }
    
    public String processActionDeleteCustomer(){
        
        Customer customer = pollListManager.getCustomer(Long.valueOf(id));                
        pollListManager.deleteCustomer(customer);
        
        return "success";
    }
}

