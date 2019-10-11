package hessian.expensivest;

import com.datastax.oss.driver.api.core.metadata.schema.*;
import com.datastax.oss.driver.api.core.type.UserDefinedType;
import edu.umd.cs.findbugs.annotations.NonNull;

public class LastUpdatedSchemaListener implements SchemaChangeListener {
    private long lastUpdated = System.currentTimeMillis();
    private long lastChecked = -1;

    public long getLastUpdated() {
        return this.lastUpdated;
    }

    public long getLastChecked() {
        long retval = this.lastChecked;
        this.lastChecked = System.currentTimeMillis();
        return retval;
    }


    @Override
    public void onKeyspaceCreated(@NonNull KeyspaceMetadata keyspaceMetadata) {
        lastUpdated = System.currentTimeMillis();
    }

    @Override
    public void onKeyspaceDropped(@NonNull KeyspaceMetadata keyspaceMetadata) {
        lastUpdated = System.currentTimeMillis();
    }

    @Override
    public void onKeyspaceUpdated(@NonNull KeyspaceMetadata keyspaceMetadata, @NonNull KeyspaceMetadata keyspaceMetadata1) {
        lastUpdated = System.currentTimeMillis();
    }

    @Override
    public void onTableCreated(@NonNull TableMetadata tableMetadata) {

    }

    @Override
    public void onTableDropped(@NonNull TableMetadata tableMetadata) {

    }

    @Override
    public void onTableUpdated(@NonNull TableMetadata tableMetadata, @NonNull TableMetadata tableMetadata1) {

    }

    @Override
    public void onUserDefinedTypeCreated(@NonNull UserDefinedType userDefinedType) {

    }

    @Override
    public void onUserDefinedTypeDropped(@NonNull UserDefinedType userDefinedType) {

    }

    @Override
    public void onUserDefinedTypeUpdated(@NonNull UserDefinedType userDefinedType, @NonNull UserDefinedType userDefinedType1) {

    }

    @Override
    public void onFunctionCreated(@NonNull FunctionMetadata functionMetadata) {

    }

    @Override
    public void onFunctionDropped(@NonNull FunctionMetadata functionMetadata) {

    }

    @Override
    public void onFunctionUpdated(@NonNull FunctionMetadata functionMetadata, @NonNull FunctionMetadata functionMetadata1) {

    }

    @Override
    public void onAggregateCreated(@NonNull AggregateMetadata aggregateMetadata) {

    }

    @Override
    public void onAggregateDropped(@NonNull AggregateMetadata aggregateMetadata) {

    }

    @Override
    public void onAggregateUpdated(@NonNull AggregateMetadata aggregateMetadata, @NonNull AggregateMetadata aggregateMetadata1) {

    }

    @Override
    public void onViewCreated(@NonNull ViewMetadata viewMetadata) {

    }

    @Override
    public void onViewDropped(@NonNull ViewMetadata viewMetadata) {

    }

    @Override
    public void onViewUpdated(@NonNull ViewMetadata viewMetadata, @NonNull ViewMetadata viewMetadata1) {

    }

    @Override
    public void close() throws Exception {

    }
}
