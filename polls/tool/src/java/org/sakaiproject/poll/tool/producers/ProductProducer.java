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
import org.sakaiproject.poll.model.Glossary;
import org.sakaiproject.poll.model.Product;
import org.sakaiproject.poll.model.ProductType;
import org.sakaiproject.poll.model.Transaction;
import org.sakaiproject.poll.tool.params.PollViewParameters;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.DecoratorList;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.components.decorators.UITooltipDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

@Slf4j
public class ProductProducer implements ViewComponentProducer,
DefaultView,NavigationCaseReporter {
    
	public static final String VIEW_ID = "productList";	
	private PollListManager pollListManager;	
	private MessageLocator messageLocator;
	private LocaleGetter localegetter;       
        private ExternalLogic externalLogic;    
        private TargettedMessageList targettedMessageList;
        
        private static final String NAVIGATE_ADD_PRODUCT = "actions-add-product";
        
        @Override
	public String getViewID() {
		return VIEW_ID;
	}

        
        public void setExternalLogic(ExternalLogic externalLogic) {
		this.externalLogic = externalLogic;
	}
	
	public void setMessageLocator(MessageLocator messageLocator) {

		this.messageLocator = messageLocator;
	}

	
	public void setPollListManager(PollListManager pollListManager) {
		this.pollListManager = pollListManager;
	}

	public void setLocaleGetter(LocaleGetter localegetter) {
		this.localegetter = localegetter;
	}	

	
	public void setTargettedMessageList(TargettedMessageList tml) {
		this.targettedMessageList = tml;
	}

        @Override
	public void fillComponents(UIContainer tofill, ViewParameters viewparams,
			ComponentChecker checker) {
            
		String locale = localegetter.get().toString();
                Map<String, String> langMap = new HashMap<>();
                langMap.put("lang", locale);
                langMap.put("xml:lang", locale);

		UIOutput.make(tofill, "polls-html", null).decorate(new UIFreeAttributeDecorator(langMap));
                
                UIBranchContainer actions = UIBranchContainer.make(tofill,"actions:",Integer.toString(0));
                UIInternalLink.make(actions, NAVIGATE_ADD_PRODUCT, "Add Product",new SimpleViewParameters(AddProductProducer.VIEW_ID));
//                
		UIOutput.make(tofill, "glossary-title", "Product List");	
		List<Product> listProduct;
                listProduct = pollListManager.findAllProduct();
//                
                for(Product product : listProduct){                    
                    UIBranchContainer row = UIBranchContainer.make(tofill, "list-glossary:");                    
                    UIOutput.make(row, "glossary-term", product.getName());
                    UIOutput.make(row, "glossary-description", String.valueOf(product.getPrice()));
                    UIOutput.make(row, "glossary-category", String.valueOf(product.getStock()));                    
                    ProductType productType = pollListManager.getProductType(product.getProductTypeId());
                    if (productType != null) {
                        UIOutput.make(row, "glossary-id", String.valueOf(productType.getProductType()));                                            
                    }else{
                        UIOutput.make(row, "glossary-id", "Tidak ada tipe");                    
                    }                    
                    
                    List<Transaction> listTransaction = pollListManager.findTransactionByProductId(product.getId());
                    int amountSelled = 0;                    
                    for(Transaction transaction : listTransaction){
                        amountSelled += transaction.getAmount();
                    }
                    UIOutput.make(row, "amount-selled", String.valueOf(amountSelled));                    
                    
                    UIInternalLink editLink = UIInternalLink.make(row,"product-edit",messageLocator.getMessage("action_edit_product"),
						new PollViewParameters(EditProductProducer.VIEW_ID, product.getId().toString()));
                    editLink.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("action_edit_product")+ ":" + product.getName()));                    
                    UIInternalLink deleteLink = UIInternalLink.make(row,"product-delete",messageLocator.getMessage("action_delete_product"),
						new PollViewParameters(DeleteProductProducer.VIEW_ID, product.getId().toString()));
                    deleteLink.decorators = new DecoratorList(new UITooltipDecorator(messageLocator.getMessage("action_delete_product")+ ":" + product.getName()));
                    
                    UIInternalLink updateStock = UIInternalLink.make(row,"product-add-stock", "Add Stock",
						new PollViewParameters(AddProductStockProducer.VIEW_ID, product.getId().toString()));
                    updateStock.decorators = new DecoratorList(new UITooltipDecorator("Add Stock For " + product.getName()));                    
                }  
	}


	

	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		return togo;
	}
	
}
