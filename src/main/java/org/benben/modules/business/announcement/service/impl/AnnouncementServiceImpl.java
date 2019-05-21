package org.benben.modules.business.announcement.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.benben.modules.business.announcement.entity.Announcement;
import org.benben.modules.business.announcement.mapper.AnnouncementMapper;
import org.benben.modules.business.announcement.service.IAnnouncementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import java.util.List;

/**
 * @Description: 用户通告
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class AnnouncementServiceImpl extends ServiceImpl<AnnouncementMapper, Announcement> implements IAnnouncementService {

	@Autowired
	private AnnouncementMapper announcementMapper;

	/**
	 * 查询通告列表
	 * @return
	 */
	@Override
	public List<Announcement> queryAnnouncement() {
		QueryWrapper<Announcement> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("send_status","1");
		queryWrapper.orderByDesc("create_time");
		return announcementMapper.selectList(queryWrapper);
	}
}
