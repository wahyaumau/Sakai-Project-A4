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
import org.sakaiproject.poll.model.ProductType;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.UISelect;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

@Slf4j
public class AddProductProducer implements ViewComponentProducer,NavigationCaseReporter{
	public static final String VIEW_ID = "addProduct";	
        
        private PollListManager pollListManager;	
	private MessageLocator messageLocator;
	private LocaleGetter localeGetter;
        private TargettedMessageList targettedMessageList;
        private ExternalLogic externalLogic;

    public void setPollListManager(PollListManager pollListManager) {
        this.pollListManager = pollListManager;
    }
                

        @Override
	public String getViewID() {
		return VIEW_ID;
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
            String locale = localeGetter.get().toString();
            Map<String, String> langMap = new HashMap<String, String>();
            langMap.put("lang", locale);
            langMap.put("xml:lang", locale);
            UIOutput.make(tofill, "polls-html", null).decorate(new UIFreeAttributeDecorator(langMap));
            UIOutput.make(tofill, "add-glossary-title", "Add Product");				
            
            List<ProductType> listProductType = pollListManager.getAllProductType();
            List<String> listProductTypeId = new ArrayList<>();
            List<String> listProductTypeName = new ArrayList<>();
            
            for(ProductType productType : listProductType){
                    listProductTypeId.add(productType.getId().toString());                                        
                    listProductTypeName.add(productType.getProductType());                                        
                }
            String[] stringListProductTypeId = listProductTypeId.toArray(new String[0]);
            String[] stringListProductTypeName = listProductTypeName.toArray(new String[0]);
            

                                        
            UIForm newProductForm = UIForm.make(tofill, "add-product-form");
            
            UIMessage.make(tofill, "product-name-label", "product_name_label");
            UIMessage.make(tofill, "product-price-label", "product_price_label");
            UIMessage.make(tofill, "product-stock-label", "product_stock_label");            
            UIInput.make(newProductForm, "product-name-input", "#{productBean.productName}");
            UIInput.make(newProductForm, "product-price-input", "#{productBean.productPrice}");
            UIInput.make(newProductForm, "product-stock-input", "#{productBean.productStock}");
            UISelect productTypeId = UISelect.make(newProductForm, "product-type-id", stringListProductTypeId, stringListProductTypeName, "#{productBean.productTypeId}");                                
            UICommand.make(newProductForm, "submit-new-product", "#{productBean.processActionAddProduct}");
	}        
        
        @Override
	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(ProductProducer.VIEW_ID)));
		return togo;
	}	
        
}