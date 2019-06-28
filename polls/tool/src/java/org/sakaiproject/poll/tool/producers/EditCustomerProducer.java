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
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIELBinding;
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
import uk.org.ponder.rsf.viewstate.ViewParamsReporter;

@Slf4j
public class EditCustomerProducer implements ViewComponentProducer,NavigationCaseReporter, ViewParamsReporter{
	public static final String VIEW_ID = "editCustomer";	
        
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
            
            Customer customer = pollListManager.getCustomer(Long.parseLong(pollViewParameter.id));
            UIForm newProductTypeForm = UIForm.make(tofill, "add-customer-form");
            UIInput.make(newProductTypeForm, "customer-id-input", "#{customerBean.id}", customer.getId().toString());
            
            UIMessage.make(tofill, "customer-name-label", "customer_name_label");
            UIInput.make(newProductTypeForm, "customer-name-input", "#{customerBean.customerName}", customer.getName());
            UIMessage.make(tofill, "customer-address-label", "customer_address_label");
            UIInput.make(newProductTypeForm, "customer-address-input", "#{customerBean.customerAddress}", customer.getAddress());
            UIMessage.make(tofill, "customer-phone-label", "customer_phone_label");
            UIInput.make(newProductTypeForm, "customer-phone-input", "#{customerBean.customerPhone}", customer.getPhone());
            UICommand.make(newProductTypeForm, "submit-new-customer", "#{customerBean.processActionUpdateCustomer}");
	}        
        
        @Override
	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(CustomerProducer.VIEW_ID)));
		return togo;
	}	
        
        @Override
        public ViewParameters getViewParameters() {
		return new PollViewParameters();

	}
        
}