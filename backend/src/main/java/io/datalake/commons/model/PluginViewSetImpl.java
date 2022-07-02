package io.datalake.commons.model;

import io.datalake.plugins.common.base.domain.Datasource;
import io.datalake.plugins.view.entity.PluginViewSet;
import lombok.Data;

@Data
public class PluginViewSetImpl extends PluginViewSet {

    private Datasource ds;
}
