package org.benben.modules.business.account.service.impl;

import org.benben.modules.business.account.entity.Account;
import org.benben.modules.business.account.mapper.AccountMapper;
import org.benben.modules.business.account.service.IAccountService;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 钱包表
 * @author： jeecg-boot
 * @date：   2019-04-24
 * @version： V1.0
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements IAccountService {

}
