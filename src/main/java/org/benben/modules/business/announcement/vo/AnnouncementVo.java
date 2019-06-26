package org.benben.modules.business.announcement.vo;

import lombok.Data;

import java.util.Date;

@Data
public class AnnouncementVo {
    private String AnnouncementId;
    private String title;
    private Date sendTime;
    private String imgUrl;
}
