package hessian.expensivest;

import com.datastax.oss.driver.api.core.metadata.Node;
import com.datastax.oss.driver.api.core.metadata.NodeStateListener;

public class LastUpdatedStateListener implements NodeStateListener {
    private long lastUpdated = System.currentTimeMillis();
    private long lastChecked = -1;

    @Override
    public void onAdd(Node host) {
        this.lastUpdated = System.currentTimeMillis();
    }

    @Override
    public void onUp(Node host) {
        this.lastUpdated = System.currentTimeMillis();
    }

    @Override
    public void onDown(Node host) {
        this.lastUpdated = System.currentTimeMillis();
    }

    @Override
    public void onRemove(Node host) {
        this.lastUpdated = System.currentTimeMillis();
    }

    @Override
    public void close() {

    }

    public long getLastUpdated() {
        return this.lastUpdated;
    }

    public long getLastChecked() {
        long retval = this.lastChecked;
        this.lastChecked = System.currentTimeMillis();
        return retval;
    }
}
