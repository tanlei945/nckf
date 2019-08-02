package org.benben.modules.business.config.vo;

import lombok.Data;

@Data
public class VersionVo {
    private String isNew;
    private String appver;
    private String versionUrl;
    private Integer versionNumber;
    private String updateTip;
}
