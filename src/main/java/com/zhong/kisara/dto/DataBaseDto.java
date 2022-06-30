package com.zhong.kisara.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author zhonghanbo
 * @date 2022年06月30日 7:17
 */


@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataBaseDto {
    private String url;
    private String username;
    private String password;

    private final static DataBaseDto DATA_BASE_DTO = new DataBaseDto();

    public static DataBaseDto getInstance() {
        return DATA_BASE_DTO;
    }
}
