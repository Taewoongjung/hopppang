package kr.hoppang.domain.cache.initializer.repository;

import java.util.List;
import kr.hoppang.domain.cache.CacheData;

public interface CacheInitializeRepository<V extends CacheData> {

    public Boolean setBucketOf(final String bucketKey, final List<V> values);

    public boolean refreshDataIn(final String bucketKey, final List<V> values);

    public List<V> getDataOf(final String bucketKey);
}
