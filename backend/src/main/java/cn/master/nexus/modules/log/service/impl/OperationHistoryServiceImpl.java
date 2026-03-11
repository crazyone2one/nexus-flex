package cn.master.nexus.modules.log.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.log.entity.OperationHistory;
import cn.master.nexus.modules.log.mapper.OperationHistoryMapper;
import cn.master.nexus.modules.log.service.OperationHistoryService;
import org.springframework.stereotype.Service;

/**
 * 变更记录 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-05
 */
@Service
public class OperationHistoryServiceImpl extends ServiceImpl<OperationHistoryMapper, OperationHistory>  implements OperationHistoryService{

}
