package org.semanticwb.process.model;

import java.util.Iterator;
import org.semanticwb.model.User;


public class CancelationEndEvent extends org.semanticwb.process.model.base.CancelationEndEventBase 
{
    public CancelationEndEvent(org.semanticwb.platform.SemanticObject base)
    {
        super(base);
    }

    @Override
    public void execute(FlowNodeInstance instance, User user)
    {
        instance.close(user);
        Instance parent=instance.getParentInstance();
        if(parent instanceof SubProcessInstance)
        {
            //list atached events
            Iterator<GraphicalElement> it=((SubProcessInstance)parent).getFlowNodeType().listChilds();
            while (it.hasNext())
            {
                GraphicalElement graphicalElement = it.next();
                if(graphicalElement instanceof CancelationIntermediateCatchEvent)
                {
                    CancelationIntermediateCatchEvent event=(CancelationIntermediateCatchEvent)graphicalElement;
                    
                    if(event instanceof ActionCodeable && instance.getFlowNodeType() instanceof ActionCodeable)
                    {
                        String c1=((ActionCodeable)event).getActionCode();
                        String c2=((ActionCodeable)instance.getFlowNodeType()).getActionCode();
                        if((c1!=null && c1.equals(c2)) || c1==null && c2==null)
                        {
                            FlowNodeInstance source=(FlowNodeInstance)parent;
                            source.close(user, Instance.STATUS_ABORTED, Instance.ACTION_EVENT, false);

                            FlowNodeInstance fn=((FlowNodeInstance)parent).getRelatedFlowNodeInstance(event);
                            fn.setSourceInstance(instance);
                            event.notifyEvent(fn, instance);
                        }
                    }
                }
            }
        }else
        {
            instance.getParentInstance().close(user);
        }
    }
}
