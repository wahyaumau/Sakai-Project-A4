/**********************************************************************************
 * $URL$
 * $Id$
 ***********************************************************************************
 *
 * Copyright (c) 2008 The Sakai Foundation
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

package org.sakaiproject.poll.dao.impl;

import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.Query;
import org.hibernate.Session;

import org.sakaiproject.genericdao.hibernate.HibernateGeneralGenericDao;
import org.sakaiproject.poll.dao.PollDao;
import org.sakaiproject.poll.model.Poll;

@Slf4j
public class PollDoaImpl extends HibernateGeneralGenericDao implements PollDao {

    public void init() {
        log.debug("init");
    }
    
    @SuppressWarnings("unchecked")
    public int getDisctinctVotersForPoll(Poll poll) {

        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT DISTINCT VOTE_SUBMISSION_ID from POLL_VOTE where VOTE_POLL_ID = " + poll.getPollId().toString();
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        if (results.size() > 0)
            return results.size();

        return 0; 
    }

    public int getDistinctNotVotersForPoll(Poll poll){
        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT FULL_NAME from poll_user_not_vote_view where POLL_ID = " + poll.getPollId().toString();
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        if (results.size() > 0)
            return results.size();

        return 0;
    }
    
    public List<String> getUsernameNotVotersForPoll(Poll poll){
        Query q = null;
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT FULL_NAME from poll_user_not_vote_view where POLL_ID = " + poll.getPollId().toString();
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        return results;
    }
    
    public List<String> getUserIdNotVotersForPoll(Poll poll){
        Query q = null;
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT USER_ID from poll_user_not_vote_view where POLL_ID = " + poll.getPollId().toString();
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        return results;
    }
    
    public List<String> getUserIdUsersNotVote(String siteId){
        Query q = null;
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT USER_ID from poll_user_not_vote_view where SITE_ID = '" + siteId + "'";
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        return results;
    }
    
    public List<String> getUsernameUsersNotVote(String siteId){
        Query q = null;
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT FULL_NAME from poll_user_not_vote_view where SITE_ID = '" + siteId + "'";
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        return results;
    }
    
    public List<String> getPollTextUsersNotVote(String siteId){
        Query q = null;
        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT POLL_TEXT from poll_user_not_vote_view where SITE_ID = '" + siteId + "'";
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        return results;
    }
    
    public int getUsersNotVoteCount(String siteId){
        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT FULL_NAME from poll_user_not_vote_view where SITE_ID = '" + siteId + "'";
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        if (results.size() > 0)
            return results.size();

        return 0;
    }

    @Override
    public int getUsersInPollSiteCount(String siteId, Poll poll) {
        Query q = null;

        Session session = getHibernateTemplate().getSessionFactory().getCurrentSession();
        String statement = "SELECT DISTINCT sakai_user.USER_ID from sakai_user, sakai_site_user, poll_poll WHERE sakai_user.USER_ID = "
                + "sakai_site_user.USER_ID AND sakai_site_user.SITE_ID = '" + siteId + "' "
                + "AND sakai_site_user.SITE_ID = poll_poll.POLL_SITE_ID AND poll_poll.POLL_SITE_ID = '" 
                + poll.getSiteId() + "'";
        q = session.createSQLQuery(statement);
        List<String> results = q.list();
        if (results.size() > 0)
            return results.size();

        return 0;
    }

}
