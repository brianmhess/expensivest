package hessian.expensivest;

import com.datastax.driver.dse.DseSession;
import org.springframework.stereotype.Component;

@Component
public class ExpensivestHealthIndicator extends StateListeningHealthIndicator {
    public ExpensivestHealthIndicator(DseSession session) {
        super(session);
    }
}
