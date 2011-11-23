package org.jboss.seam.faces.view.action.el;

import javax.el.MethodExpression;
import javax.inject.Inject;

import org.jboss.seam.faces.view.action.SimpleViewActionHandlerProvider;
import org.jboss.solder.el.Expressions;

public class ElViewActionHandlerProvider extends SimpleViewActionHandlerProvider<ElViewAction> {

    @Inject
    private Expressions expressions;
    
    private MethodExpression methodExpression;

    @Override
    protected void doInitialize(ElViewAction annotation) {
        methodExpression = expressions.getExpressionFactory().createMethodExpression(expressions.getELContext(),
                annotation.value(), null, new Class[] {});
    }

    @Override
    public Object execute() {
        return methodExpression.invoke(expressions.getELContext(), new Object[] {});
    }
}
