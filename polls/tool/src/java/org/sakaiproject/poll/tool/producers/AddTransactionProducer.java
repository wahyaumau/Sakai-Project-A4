/**********************************************************************************
 * $URL: $
 * $Id:  $
 ***********************************************************************************
 *
 * Copyright (c) 2006, 2007, 2008, 2009 The Sakai Foundation
 *
 * Licensed under the Educational Community License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.opensource.org/licenses/ECL-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 **********************************************************************************/

package org.sakaiproject.poll.tool.producers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.extern.slf4j.Slf4j;

import org.sakaiproject.poll.logic.ExternalLogic;
import org.sakaiproject.poll.logic.PollListManager;
import org.sakaiproject.poll.model.Customer;
import org.sakaiproject.poll.model.PollCategory;
import org.sakaiproject.poll.model.Product;
import org.sakaiproject.poll.model.ProductType;
import org.sakaiproject.poll.tool.params.PollViewParameters;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

@Slf4j
public class AddTransactionProducer implements ViewComponentProducer,NavigationCaseReporter, ViewParamsReporter{
	public static final String VIEW_ID = "addTransaction";	
        
        private PollListManager pollListManager;
	private MessageLocator messageLocator;
	private LocaleGetter localeGetter;
        private TargettedMessageList targettedMessageList;
        private ExternalLogic externalLogic;
        
                

        @Override
	public String getViewID() {
		return VIEW_ID;
	}
        
        public void setPollListManager(PollListManager pollListManager) {
		this.pollListManager = pollListManager;
	}

	public void setMessageLocator(MessageLocator messageLocator) {
		this.messageLocator = messageLocator;
	}

	public void setLocaleGetter(LocaleGetter localeGetter) {
		this.localeGetter = localeGetter;
	}	
	
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.targettedMessageList = tml;
	}
	
	public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}	
	
        @Override
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {		
            
            PollViewParameters pollViewParameter = (PollViewParameters) viewparams;            
            
            String locale = localeGetter.get().toString();
            Map<String, String> langMap = new HashMap<String, String>();
            langMap.put("lang", locale);
            langMap.put("xml:lang", locale);
            UIOutput.make(tofill, "polls-html", null).decorate(new UIFreeAttributeDecorator(langMap));
            UIOutput.make(tofill, "add-transaction-title", "Add Transaction");	
            
            List<ProductType> listProductType;
                listProductType = pollListManager.getAllProductType();
                
                for(ProductType productType : listProductType){
                    UIBranchContainer row = UIBranchContainer.make(tofill, "list-product-type:");                    
                    UIInternalLink productTypeLink = UIInternalLink.make(row,"product-type", "See Product By" + productType.getProductType(),                   
						new PollViewParameters(AddTransactionProducer.VIEW_ID, productType.getId().toString()));
                    productTypeLink.decorators = new DecoratorList(new UITooltipDecorator("See Product By" + productType.getProductType()));                                                        
                }

                List<Product> listProduct;
            if (pollViewParameter.id == null) {
                listProduct = pollListManager.findAllProduct();
            }else{
                listProduct = pollListManager.findProductByProductType(Long.parseLong(pollViewParameter.id));
            }                        
                        
            List<String> listProductId = new ArrayList<>();
            List<String> listProductName = new ArrayList<>();
            
            for(Product product : listProduct){
                    listProductId.add(product.getId().toString());                                        
                    listProductName.add(product.getName());                                        
                }
            String[] stringListProductId = listProductId.toArray(new String[0]);
            String[] stringListProductName = listProductName.toArray(new String[0]);                                                    
            
            List<Customer> listCustomer = pollListManager.findAllCustomer();
            List<String> listCustomerId = new ArrayList<>();
            List<String> listCustomerName = new ArrayList<>();
            
            for(Customer customer : listCustomer){
                    listCustomerId.add(customer.getId().toString());                                        
                    listCustomerName.add(customer.getName());                                        
            }
            String[] stringListCustomerId = listCustomerId.toArray(new String[0]);
            String[] stringListCustomerName = listCustomerName.toArray(new String[0]);                                                    
            
            UIForm addTransactionForm = UIForm.make(tofill, "add-transaction-form");
            
            UIOutput.make(tofill, "transaction-customer-label", "Select Customer Name");
            UIOutput.make(tofill, "transaction-product-label", "Select Product");
            UIOutput.make(tofill, "transaction-amount-label", "Product Amount");                                                            
            UISelect productSelect = UISelect.make(addTransactionForm, "product-id", stringListProductId, stringListProductName, "#{productBean.productId}");
            UISelect customerSelect = UISelect.make(addTransactionForm, "customer-id", stringListCustomerId, stringListCustomerName, "#{productBean.customerId}");
            UIInput.make(addTransactionForm, "product-amount-input", "#{productBean.productAmount}");
            UICommand.make(addTransactionForm, "submit-new-transaction", "#{productBean.processActionAddTransaction}");
	}        
        
        @Override
	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(TransactionProducer.VIEW_ID)));
		return togo;
	}	
        
        @Override
        public ViewParameters getViewParameters() {
		return new PollViewParameters();

	}
        
}