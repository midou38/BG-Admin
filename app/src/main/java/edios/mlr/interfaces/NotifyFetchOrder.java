package edios.mlr.interfaces;

import edios.mlr.model.ResultOutput;

public interface NotifyFetchOrder {
    void orderFetched(ResultOutput orders, boolean isSuccess);
}
