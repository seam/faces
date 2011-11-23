package org.jboss.seam.faces.test.weld.view.action.binding;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Named;

@Named
@ApplicationScoped
public class OrderedController {
    
    private OrderedController mock;
    
    @HighOrderViewAction(ViewConfigEnum.Pages.ORDER_TEST)
    public void highOrder() {
        mock.highOrder();
    }
    
    @LowOrderViewAction(ViewConfigEnum.Pages.ORDER_TEST)
    public void lowOrder() {
        mock.lowOrder();
    }
    
    @MiddleOrderViewAction(ViewConfigEnum.Pages.ORDER_TEST)
    public void middleOrder() {
        mock.middleOrder();
    }
    
    @ParameterizedOrderViewAction(value=ViewConfigEnum.Pages.ORDER_TEST, order=1)
    public void order1() {
        mock.order1();
    }
    
    @ParameterizedOrderViewAction(value=ViewConfigEnum.Pages.ORDER_TEST, order=600)
    public void order600() {
        mock.order600();
    }

    public OrderedController getMock() {
        return mock;
    }

    public void setMock(OrderedController mock) {
        this.mock = mock;
    }
    
}
