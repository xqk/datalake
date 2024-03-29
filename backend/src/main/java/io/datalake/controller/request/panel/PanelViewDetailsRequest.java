package io.datalake.controller.request.panel;

import lombok.Data;

import java.util.List;

/**
 * Author: xqk
 * Date: 2022/4/8
 * Description:
 */
@Data
public class PanelViewDetailsRequest {

    private String viewName;

    private String[] header;

    private List<String[]> details;

    private String snapshot;

    private int snapshotWidth;

    private int snapshotHeight;


}
