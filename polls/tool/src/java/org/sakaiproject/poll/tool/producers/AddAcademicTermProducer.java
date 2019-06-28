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

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UICommand;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIForm;
import uk.org.ponder.rsf.components.UIInput;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

@Slf4j
public class AddAcademicTermProducer implements ViewComponentProducer,NavigationCaseReporter{
	public static final String VIEW_ID = "addAcademicTerm";	
	private MessageLocator messageLocator;
	private LocaleGetter localeGetter;
        private TargettedMessageList targettedMessageList;
        private ExternalLogic externalLogic;
                

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
            
            String userId = externalLogic.getCurrentUserId();
            Map<String, String> langMap = new HashMap<String, String>();
            langMap.put("lang", locale);
            langMap.put("xml:lang", locale);
            UIOutput.make(tofill, "polls-html", null).decorate(new UIFreeAttributeDecorator(langMap));
            
            UIOutput.make(tofill, "add-academic-term-title", messageLocator.getMessage("add_academic_term_title"));				
                                        
            UIForm newAcademicTermForm = UIForm.make(tofill, "add-academic-term-form");
            
            UIMessage.make(tofill, "academic-term-title-label", "academic_term_title_label");
            UIMessage.make(tofill, "academic-term-description-label", "academic_term_description_label");
            UIMessage.make(tofill, "academic-term-start-date-label", "academic_term_start_date_label");            
            UIMessage.make(tofill, "academic-term-end-date-label", "academic_term_end_date_label");            
            UIInput.make(newAcademicTermForm, "academic-term-created-by-input", "#{pollToolBean.academicTermCreator}", userId);
            UIInput.make(newAcademicTermForm, "academic-term-title-input", "#{pollToolBean.academicTermTitle}");
            UIInput.make(newAcademicTermForm, "academic-term-description-input", "#{pollToolBean.academicTermDescription}");
            UIInput.make(newAcademicTermForm, "academic-term-start-date-input", "#{pollToolBean.academicTermStartDateString}");
            UIInput.make(newAcademicTermForm, "academic-term-end-date-input", "#{pollToolBean.academicTermEndDateString}");
            UICommand.make(newAcademicTermForm, "submit-new-academic-term", "#{pollToolBean.processActionAddAcademicTerm}");
	}        
        
        @Override
	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(AcademicTermProducer.VIEW_ID)));
		return togo;
	}	
        
}