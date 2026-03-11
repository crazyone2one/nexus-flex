package cn.master.nexus.modules.log.service;

import cn.master.nexus.modules.log.dto.LogDTO;
import com.mybatisflex.core.service.IService;
import cn.master.nexus.modules.log.entity.OperationLog;

import java.util.List;

/**
 * 操作日志 服务层。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
public interface OperationLogService extends IService<OperationLog> {

    void add(LogDTO log);

    void batchAdd(List<LogDTO> logs);
}
