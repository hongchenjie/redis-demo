package org.study.redis.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author lipo
 * @version v1.0
 * @date 2019-11-13 11:32
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private Integer id;
    private String name;
}
