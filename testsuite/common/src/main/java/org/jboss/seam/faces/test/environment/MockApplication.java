package org.jboss.seam.faces.test.environment;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.NavigationHandler;
import javax.faces.application.StateManager;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.el.MethodBinding;
import javax.faces.el.PropertyResolver;
import javax.faces.el.ReferenceSyntaxException;
import javax.faces.el.ValueBinding;
import javax.faces.el.VariableResolver;
import javax.faces.event.ActionListener;
import javax.faces.validator.Validator;

public class MockApplication extends Application {

    @Override
    public ActionListener getActionListener() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setActionListener(ActionListener listener) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Locale getDefaultLocale() {
        return Locale.US;
    }

    @Override
    public void setDefaultLocale(Locale locale) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public String getDefaultRenderKitId() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setDefaultRenderKitId(String renderKitId) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public String getMessageBundle() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setMessageBundle(String bundle) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public NavigationHandler getNavigationHandler() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setNavigationHandler(NavigationHandler handler) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public PropertyResolver getPropertyResolver() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setPropertyResolver(PropertyResolver resolver) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public VariableResolver getVariableResolver() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setVariableResolver(VariableResolver resolver) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public ViewHandler getViewHandler() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setViewHandler(ViewHandler handler) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public StateManager getStateManager() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void setStateManager(StateManager manager) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public void addComponent(String componentType, String componentClass) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public UIComponent createComponent(String componentType) throws FacesException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public UIComponent createComponent(ValueBinding componentBinding, FacesContext context, String componentType)
            throws FacesException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Iterator<String> getComponentTypes() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public void addConverter(String converterId, String converterClass) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public void addConverter(Class<?> targetClass, String converterClass) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public Converter createConverter(String converterId) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Converter createConverter(Class<?> targetClass) {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Iterator<String> getConverterIds() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Iterator<Class<?>> getConverterTypes() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public MethodBinding createMethodBinding(String ref, Class<?>[] params) throws ReferenceSyntaxException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Iterator<Locale> getSupportedLocales() {
        return Arrays.asList(Locale.US,Locale.GERMAN,Locale.FRENCH).iterator();
    }

    @Override
    public void setSupportedLocales(Collection<Locale> locales) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public void addValidator(String validatorId, String validatorClass) {
        throw new UnsupportedOperationException("Not supported");        
    }

    @Override
    public Validator createValidator(String validatorId) throws FacesException {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public Iterator<String> getValidatorIds() {
        throw new UnsupportedOperationException("Not supported");
    }

    @Override
    public ValueBinding createValueBinding(String ref) throws ReferenceSyntaxException {
        throw new UnsupportedOperationException("Not supported");
    }

}
