package com.videorental.model;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author andrey.semenov
 */
@Data
@Accessors(chain = true)
public class Order {
    private Film film;
    private Integer releaseId;
    private Integer cost;
}
