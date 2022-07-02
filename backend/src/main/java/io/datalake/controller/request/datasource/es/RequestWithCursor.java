package io.datalake.controller.request.datasource.es;

import lombok.Data;

@Data
public class RequestWithCursor extends Request {
    private String cursor;
}
