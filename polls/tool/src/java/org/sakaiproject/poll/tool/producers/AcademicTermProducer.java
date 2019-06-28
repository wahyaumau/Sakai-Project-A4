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
import org.sakaiproject.poll.logic.PollVoteManager;
import org.sakaiproject.poll.model.AcademicSession;
import org.sakaiproject.poll.tool.params.VoteBean;

import uk.org.ponder.localeutil.LocaleGetter;
import uk.org.ponder.messageutil.MessageLocator;
import uk.org.ponder.messageutil.TargettedMessageList;
import uk.org.ponder.rsf.components.UIBranchContainer;
import uk.org.ponder.rsf.components.UIContainer;
import uk.org.ponder.rsf.components.UIInternalLink;
import uk.org.ponder.rsf.components.UIMessage;
import uk.org.ponder.rsf.components.UIOutput;
import uk.org.ponder.rsf.components.decorators.UIFreeAttributeDecorator;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCase;
import uk.org.ponder.rsf.flow.jsfnav.NavigationCaseReporter;
import uk.org.ponder.rsf.view.ComponentChecker;
import uk.org.ponder.rsf.view.DefaultView;
import uk.org.ponder.rsf.view.ViewComponentProducer;
import uk.org.ponder.rsf.viewstate.SimpleViewParameters;
import uk.org.ponder.rsf.viewstate.ViewParameters;

@Slf4j
public class AcademicTermProducer implements ViewComponentProducer,
DefaultView,NavigationCaseReporter {
	public static final String VIEW_ID = "academicTerm";
        private static final String NAVIGATE_ADD_ACADEMIC_TERM = "actions-add-academic-term";
	
	private PollListManager pollListManager;	
	private MessageLocator messageLocator;
	private LocaleGetter localegetter;       
        private ExternalLogic externalLogic;    
        private TargettedMessageList targettedMessageList;
        

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
                Map<String, String> langMap = new HashMap<String, String>();
                langMap.put("lang", locale);
                langMap.put("xml:lang", locale);

		UIOutput.make(tofill, "polls-html", null).decorate(new UIFreeAttributeDecorator(langMap));
                
                UIBranchContainer actions = UIBranchContainer.make(tofill,"actions:",Integer.toString(0));
                UIInternalLink.make(actions, NAVIGATE_ADD_ACADEMIC_TERM, UIMessage.make("action_add_academic_term"),new SimpleViewParameters(AddAcademicTermProducer.VIEW_ID));
			
		UIOutput.make(tofill, "academic-term-title", messageLocator.getMessage("academic_term_title"));	
		List<AcademicSession> listAS;
                listAS = pollListManager.findAllAcademicSession();
                
                for(AcademicSession item : listAS){                    
                    UIBranchContainer row = UIBranchContainer.make(tofill, "list-academic-term:");
                    UIOutput.make(row, "academic-term-id", String.valueOf(item.getAcademicSessionId()) );
                    UIOutput.make(row, "academic-term-title", item.getTitle());
                    UIOutput.make(row, "academic-term-description", item.getDescription());
                    if (item.getStartDate() != null && item.getEndDate() != null) {
                        UIOutput.make(row, "academic-term-start-date", item.getStartDate().toString());
                        UIOutput.make(row, "academic-term-end-date", item.getEndDate().toString());                        
                    }else{
                        UIOutput.make(row, "academic-term-start-date", "");
                        UIOutput.make(row, "academic-term-end-date", "");                        
                    }
                    
                    UIOutput.make(row, "academic-term-created-by", item.getCreatedBy());
                }  

	}


	

        @Override
	public List<NavigationCase> reportNavigationCases() {
		List<NavigationCase> togo = new ArrayList<NavigationCase>(); // Always navigate back to this view.
		togo.add(new NavigationCase(null, new SimpleViewParameters(VIEW_ID)));
		return togo;
	}
	
}
