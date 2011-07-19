/*
 * JBoss, Home of Professional Open Source
 * Copyright 2011, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.seam.faces.examples.shortly;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.List;

import javax.ejb.Stateful;
import javax.enterprise.context.ConversationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.jboss.seam.faces.context.conversation.Begin;
import org.jboss.seam.faces.context.conversation.End;
import org.jboss.seam.international.status.Messages;
import org.jboss.seam.international.status.builder.BundleKey;

/**
 * @author <a href="mailto:lincolnbaxter@gmail.com">Lincoln Baxter, III</a>
 */
@Named
@ConversationScoped
@Stateful
public class LinkBean implements Serializable {
    private static final long serialVersionUID = -2209547152337410725L;

    @PersistenceContext
    private EntityManager em;
    
    @Inject
    private Messages messages;

    private TinyLink link = new TinyLink();

    @Begin
    @End
    public String createLink() throws SQLException {
    	messages.info("Created link {0}",link.getName());
        System.out.println("Created link: [ " + link.getName() + " => " + link.getTarget() + " ]");
        em.persist(link);
        return "pretty:create";
    }

    @SuppressWarnings("unchecked")
    public TinyLink getByKey(final String key) {
        Query query = em.createQuery("select t from TinyLink t where t.name=:key", TinyLink.class);
        query.setParameter("key", key);
        List<TinyLink> resultList = query.getResultList();
        if (resultList.isEmpty()) {
            return new TinyLink();
        }
        return resultList.get(0);
    }

    public String format(final String link) {
        if (link != null) {
            String result = link.trim();
            if (!result.matches("(http|ftp)://.*")) {
                result = "http://" + result;
            }
            return result;
        }
        return "";
    }

    public String deleteAll() {
    	messages.info(new BundleKey("shortly", "linksDeleted"));
        em.createQuery("delete from TinyLink").executeUpdate();
        return "pretty:";
    }

    public List<TinyLink> getLinks() {
        return em.createQuery("select t from TinyLink t").getResultList();
    }

    public TinyLink getLink() {
        return link;
    }

    public void setLink(final TinyLink link) {
        this.link = link;
    }
}
