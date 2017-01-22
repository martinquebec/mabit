package mabit.oms.position;

import mabit.oms.order.Exec;
import mabit.oms.order.Order;

/**
 * Created by martin on 4/10/2016.
 */
public interface IPositionManager {
    void addExec(Exec exec);
    void newOrder(Order order);
    void cancelOrder(Order order);
}
