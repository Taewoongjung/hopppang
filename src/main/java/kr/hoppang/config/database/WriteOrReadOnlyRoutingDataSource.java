package kr.hoppang.config.database;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import javax.sql.DataSource;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class WriteOrReadOnlyRoutingDataSource extends AbstractRoutingDataSource {

    private final Map<Object, Object> targetDataSources;


    public WriteOrReadOnlyRoutingDataSource(DataSource writeDataSource, DataSource readOnlyDataSource) {
        Objects.requireNonNull(writeDataSource, "writeDataSource cannot be null");
        Objects.requireNonNull(readOnlyDataSource, "readOnlyDataSource cannot be null");

        this.targetDataSources = new HashMap<>();
        this.targetDataSources.put(RoutingType.WRITE, writeDataSource);
        this.targetDataSources.put(RoutingType.READ_ONLY, readOnlyDataSource);
        super.setTargetDataSources(this.targetDataSources);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        RoutingType routingType = TransactionSynchronizationManager.isCurrentTransactionReadOnly() ?
            RoutingType.READ_ONLY : RoutingType.WRITE;
        return routingType;
    }
}
