/*
 * JBoss, Home of Professional Open Source
 * Copyright 2010, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */ 
package org.jboss.seam.faces.test.environment;

import java.util.Iterator;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.application.FacesMessage.Severity;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseStream;
import javax.faces.context.ResponseWriter;
import javax.faces.event.PhaseId;
import javax.faces.render.RenderKit;

/**
 * @author Dan Allen
 */
public class MockFacesContext extends FacesContext
{
   private PhaseId currentPhaseId;
   private ExternalContext externalContext = new MockExternalContext();

   public FacesContext set()
   {
      setCurrentInstance(this);
      return this;
   }

   @Override
   public void release()
   {
      setCurrentInstance(null);
   }

   @Override
   public PhaseId getCurrentPhaseId()
   {
      return currentPhaseId;
   }

   @Override
   public void setCurrentPhaseId(PhaseId currentPhaseId)
   {
      this.currentPhaseId = currentPhaseId;
   }

   @Override
   public Application getApplication()
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public Iterator<String> getClientIdsWithMessages()
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public ExternalContext getExternalContext()
   {
      return externalContext;
   }

   @Override
   public Severity getMaximumSeverity()
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public Iterator<FacesMessage> getMessages()
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public Iterator<FacesMessage> getMessages(String clientId)
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public RenderKit getRenderKit()
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public boolean getRenderResponse()
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public boolean getResponseComplete()
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public ResponseStream getResponseStream()
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public void setResponseStream(ResponseStream stream)
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public ResponseWriter getResponseWriter()
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public void setResponseWriter(ResponseWriter writer)
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public UIViewRoot getViewRoot()
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public void setViewRoot(UIViewRoot uivr)
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public void addMessage(String clientId, FacesMessage message)
   {
      throw new UnsupportedOperationException("Not supported");
   }

   @Override
   public void renderResponse()
   {
      throw new UnsupportedOperationException("Not supported.");
   }

   @Override
   public void responseComplete()
   {
      throw new UnsupportedOperationException("Not supported.");
   }
}
