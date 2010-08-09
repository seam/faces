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
package org.jboss.seam.faces.viewdata;

import java.lang.annotation.Annotation;
import java.util.List;
/**
 * stores data specific to a given view is a heiracial fashion
 * @author Stuart Douglas
 *
 */
public interface ViewDataStore
{

   /**
    * Adds data to the store
    * 
    * @param viewId The view id to associate the data with. A * at the end of
    *           the view id is considered a wildcard
    * @param annotation the data to store
    */
   public abstract void addData(String viewId, Annotation annotation);

   /**
    * gets the most specific data for a given viewId
    * 
    */
   public abstract <T extends Annotation> T getData(String viewId, Class<T> type);

   /**
    * gets the most specific data for the current viewId
    */
   public abstract <T extends Annotation> T getData(Class<T> type);

   /**
    * returns all data for a given viewId, with the most specific data at the
    * start of the list
    */
   public abstract <T extends Annotation> List<T> getAllData(String viewId, Class<T> type);

   /**
    * returns all data for the current viewId, with the most specific data at
    * the start of the list
    */
   public abstract <T extends Annotation> List<T> getAllData(Class<T> type);

}