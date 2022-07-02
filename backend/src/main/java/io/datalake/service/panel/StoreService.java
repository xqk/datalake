package io.datalake.service.panel;

import io.datalake.commons.utils.AuthUtils;
import io.datalake.controller.sys.base.BaseGridRequest;
import io.datalake.controller.sys.base.ConditionEntity;
import io.datalake.dto.panel.PanelStoreDto;
import io.datalake.ext.ExtPanelStoreMapper;
import io.datalake.ext.query.GridExample;
import io.datalake.plugins.common.base.domain.PanelStore;
import io.datalake.plugins.common.base.domain.PanelStoreExample;
import io.datalake.plugins.common.base.mapper.PanelStoreMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class StoreService {

    @Resource
    private PanelStoreMapper panelStoreMapper;

    @Resource
    private ExtPanelStoreMapper extPanelStoreMapper;

    public void save(String panelGroupId) {
        Long userId = AuthUtils.getUser().getUserId();
        PanelStore panelStore = new PanelStore();
        panelStore.setCreateTime(System.currentTimeMillis());
        panelStore.setPanelGroupId(panelGroupId);
        panelStore.setUserId(userId);
        panelStoreMapper.insert(panelStore);
    }

    public void removeByPanelId(String panelId) {
        Long userId = AuthUtils.getUser().getUserId();
        PanelStoreExample panelStoreExample = new PanelStoreExample();
        panelStoreExample.createCriteria().andPanelGroupIdEqualTo(panelId).andUserIdEqualTo(userId);
        panelStoreMapper.deleteByExample(panelStoreExample);
    }

    public List<PanelStoreDto> query(BaseGridRequest request) {
        Long userId = AuthUtils.getUser().getUserId();
        ConditionEntity condition = new ConditionEntity();
        condition.setField("s.user_id");
        condition.setOperator("eq");
        condition.setValue(userId);
        request.setConditions(new ArrayList<ConditionEntity>() {{
            add(condition);
        }});
        GridExample example = request.convertExample();
        return extPanelStoreMapper.query(example);
    }

    public Long count(String panelId) {
        PanelStoreExample example = new PanelStoreExample();
        example.createCriteria().andUserIdEqualTo(AuthUtils.getUser().getUserId()).andPanelGroupIdEqualTo(panelId);
        return panelStoreMapper.countByExample(example);
    }


}
