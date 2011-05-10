package org.jboss.seam.faces.component;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import javax.el.ELContext;
import javax.el.ELException;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.MethodInfo;
import javax.el.ValueExpression;
import javax.faces.component.StateHolder;
import javax.faces.context.FacesContext;
import javax.faces.el.EvaluationException;
import javax.faces.el.MethodBinding;
import javax.faces.el.MethodNotFoundException;

/**
 * <p>
 * Wrap a MethodExpression instance and expose it as a MethodBinding
 * </p>
 */
class MethodBindingMethodExpressionAdapter extends MethodBinding implements StateHolder, Serializable {

    private static final long serialVersionUID = 7334926223014401689L;
    private MethodExpression methodExpression = null;
    private boolean tranzient;

    public MethodBindingMethodExpressionAdapter() {
    } // for StateHolder

    MethodBindingMethodExpressionAdapter(final MethodExpression methodExpression) {
        this.methodExpression = methodExpression;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Object invoke(final FacesContext context, final Object params[]) throws EvaluationException, MethodNotFoundException {
        assert (null != methodExpression);
        if (context == null) {
            throw new NullPointerException("FacesConext -> null");
        }

        Object result = null;
        try {
            result = methodExpression.invoke(context.getELContext(), params);
        } catch (javax.el.MethodNotFoundException e) {
            throw new javax.faces.el.MethodNotFoundException(e);
        } catch (javax.el.PropertyNotFoundException e) {
            throw new EvaluationException(e);
        } catch (ELException e) {
            Throwable cause = e.getCause();
            if (cause == null) {
                cause = e;
            }
            throw new EvaluationException(cause);
        } catch (NullPointerException e) {
            throw new MethodNotFoundException(e);
        }
        return result;
    }

    @Override
    @SuppressWarnings("deprecation")
    public Class<?> getType(final FacesContext context) throws MethodNotFoundException {
        assert (null != methodExpression);
        if (context == null) {
            throw new NullPointerException("FacesConext -> null");
        }
        Class<?> result = null;
        if (context == null) {
            throw new NullPointerException();
        }

        try {
            MethodInfo mi = methodExpression.getMethodInfo(context.getELContext());
            result = mi.getReturnType();
        } catch (javax.el.PropertyNotFoundException e) {
            throw new MethodNotFoundException(e);
        } catch (javax.el.MethodNotFoundException e) {
            throw new MethodNotFoundException(e);
        } catch (ELException e) {
            throw new MethodNotFoundException(e);
        }
        return result;
    }

    @Override
    public String getExpressionString() {
        assert (null != methodExpression);
        return methodExpression.getExpressionString();
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (other instanceof MethodBindingMethodExpressionAdapter) {
            return methodExpression.equals(((MethodBindingMethodExpressionAdapter) other).getWrapped());
        } else if (other instanceof MethodBinding) {
            MethodBinding binding = (MethodBinding) other;

            // We'll need to do a little leg work to determine
            // if the MethodBinding is equivalent to the
            // wrapped MethodExpression
            String expr = binding.getExpressionString();
            int idx = expr.indexOf('.');
            String target = expr.substring(0, idx).substring(2);
            String t = expr.substring(idx + 1);
            String method = t.substring(0, (t.length() - 1));

            FacesContext context = FacesContext.getCurrentInstance();
            ELContext elContext = context.getELContext();
            MethodInfo controlInfo = methodExpression.getMethodInfo(elContext);

            // ensure the method names are the same
            if (!controlInfo.getName().equals(method)) {
                return false;
            }

            // Using the target, create an expression and evaluate
            // it.
            ExpressionFactory factory = context.getApplication().getExpressionFactory();
            ValueExpression ve = factory.createValueExpression(elContext, "#{" + target + '}', Object.class);
            if (ve == null) {
                return false;
            }

            Object result = ve.getValue(elContext);

            if (result == null) {
                return false;
            }

            // Get all of the methods with the matching name and try
            // to find a match based on controlInfo's return and parameter
            // types
            Class<?> type = binding.getType(context);
            Method[] methods = result.getClass().getMethods();
            for (Method meth : methods) {
                if (meth.getName().equals(method) && type.equals(controlInfo.getReturnType())
                        && Arrays.equals(meth.getParameterTypes(), controlInfo.getParamTypes())) {
                    return true;
                }
            }
        }

        return false;

    }

    @Override
    public int hashCode() {
        assert (null != methodExpression);

        return methodExpression.hashCode();
    }

    public boolean isTransient() {
        return this.tranzient;
    }

    public void setTransient(final boolean tranzient) {
        this.tranzient = tranzient;
    }

    public Object saveState(final FacesContext context) {
        if (context == null) {
            throw new NullPointerException();
        }
        Object result = null;
        if (!tranzient) {
            if (methodExpression instanceof StateHolder) {
                Object[] stateStruct = new Object[2];

                // save the actual state of our wrapped methodExpression
                stateStruct[0] = ((StateHolder) methodExpression).saveState(context);
                // save the class name of the methodExpression impl
                stateStruct[1] = methodExpression.getClass().getName();

                result = stateStruct;
            } else {
                result = methodExpression;
            }
        }

        return result;

    }

    public void restoreState(final FacesContext context, final Object state) {
        if (context == null) {
            throw new NullPointerException();
        }
        // if we have state
        if (null == state) {
            return;
        }

        if (!(state instanceof MethodExpression)) {
            Object[] stateStruct = (Object[]) state;
            Object savedState = stateStruct[0];
            String className = stateStruct[1].toString();
            MethodExpression result = null;

            Class<?> toRestoreClass = null;
            if (null != className) {
                try {
                    toRestoreClass = loadClass(className, this);
                } catch (ClassNotFoundException e) {
                    throw new IllegalStateException(e.getMessage());
                }

                if (null != toRestoreClass) {
                    try {
                        result = (MethodExpression) toRestoreClass.newInstance();
                    } catch (InstantiationException e) {
                        throw new IllegalStateException(e.getMessage());
                    } catch (IllegalAccessException a) {
                        throw new IllegalStateException(a.getMessage());
                    }
                }

                if ((null != result) && (null != savedState)) {
                    // don't need to check transient, since that was
                    // done on the saving side.
                    ((StateHolder) result).restoreState(context, savedState);
                }
                methodExpression = result;
            }
        } else {
            methodExpression = (MethodExpression) state;
        }
    }

    public MethodExpression getWrapped() {
        return methodExpression;
    }

    //
    // Helper methods for StateHolder
    //
    private static Class<?> loadClass(final String name, final Object fallbackClass) throws ClassNotFoundException {
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        if (loader == null) {
            loader = fallbackClass.getClass().getClassLoader();
        }
        return Class.forName(name, true, loader);
    }
}
