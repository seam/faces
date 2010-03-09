package org.jboss.seam.faces;

import javax.faces.context.FacesContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;


/**
 * Selector implementation for JSF environments
 * 
 * @author Shane Bryzak
 */
public class FacesManagedCookie //extends ManagedCookie
{
   private static final long serialVersionUID = 7212365784926629129L;

//   @Override
//   public void clearCookieValue()
//   {
//      Cookie cookie = getCookie();
//      if ( cookie!=null )
//      {
//         HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
//         cookie.setValue(null);
//         cookie.setPath(getCookiePath());
//         cookie.setMaxAge(0);
//         response.addCookie(cookie);
//      }
//   }
//
//   @Override
//   public Cookie getCookie()
//   {
//      FacesContext ctx = FacesContext.getCurrentInstance();
//      if (ctx != null)
//      {
//          return (Cookie) ctx.getExternalContext().getRequestCookieMap()
//            .get( getCookieName() );
//      }
//      else
//      {
//         return null;
//      }
//   }
//
//   @Override
//   public void setCookieValueIfEnabled(String value)
//   {
//      FacesContext ctx = FacesContext.getCurrentInstance();
//
//      if ( isCookieEnabled() && ctx != null)
//      {
//         HttpServletResponse response = (HttpServletResponse) ctx.getExternalContext().getResponse();
//         Cookie cookie = new Cookie( getCookieName(), value );
//         cookie.setMaxAge( getCookieMaxAge() );
//         cookie.setPath(getCookiePath());
//         response.addCookie(cookie);
//      }
//   }
}
