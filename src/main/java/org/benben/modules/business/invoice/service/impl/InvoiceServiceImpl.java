package org.benben.modules.business.invoice.service.impl;

import org.benben.modules.business.invoice.entity.Invoice;
import org.benben.modules.business.invoice.mapper.InvoiceMapper;
import org.benben.modules.business.invoice.service.IInvoiceService;
import org.benben.modules.business.order.entity.Order;
import org.benben.modules.business.order.mapper.OrderMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

/**
 * @Description: 用户发票
 * @author： jeecg-boot
 * @date：   2019-04-23
 * @version： V1.0
 */
@Service
public class InvoiceServiceImpl extends ServiceImpl<InvoiceMapper, Invoice> implements IInvoiceService {
}
