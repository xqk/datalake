package io.datalake.dto.authModel;

import io.datalake.plugins.common.base.domain.VAuthModelWithBLOBs;
import io.datalake.plugins.common.model.ITreeBase;
import lombok.Data;

import java.util.List;

/**
 * Author: xqk
 * Date: 2021/11/5
 * Description:
 */
@Data
public class VAuthModelDTO extends VAuthModelWithBLOBs implements ITreeBase<VAuthModelDTO> {

    private String privileges;

    private List<VAuthModelDTO> children;

    private long allLeafs = 0l;

    private String innerId;

    public String toString(){
        return this.getName();
    }

}
