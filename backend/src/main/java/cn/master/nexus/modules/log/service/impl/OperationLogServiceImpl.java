package cn.master.nexus.modules.log.service.impl;

import cn.master.nexus.modules.log.dto.LogDTO;
import cn.master.nexus.modules.log.entity.OperationHistory;
import cn.master.nexus.modules.log.entity.OperationLog;
import cn.master.nexus.modules.log.mapper.OperationHistoryMapper;
import cn.master.nexus.modules.log.mapper.OperationLogMapper;
import cn.master.nexus.modules.log.service.OperationLogService;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jspecify.annotations.NonNull;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 操作日志 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-03
 */
@Service
@RequiredArgsConstructor
public class OperationLogServiceImpl extends ServiceImpl<OperationLogMapper, OperationLog> implements OperationLogService {
    private final OperationHistoryMapper operationHistoryMapper;

    @Override
    public void add(LogDTO log) {
        if (StringUtils.isBlank(log.getProjectId())) {
            log.setProjectId("none");
        }
        if (StringUtils.isBlank(log.getCreateUser())) {
            log.setCreateUser("admin");
        }
        log.setContent(subStrContent(log.getContent()));
        mapper.insert(log);
        if (log.getHistory()) {
            operationHistoryMapper.insert(getHistory(log));
        }
    }

    @Override
    public void batchAdd(List<LogDTO> logs) {
        if (CollectionUtils.isEmpty(logs)) {
            return;
        }
        logs.forEach(item -> {
            item.setContent(subStrContent(item.getContent()));
            item.setCreateTime(LocalDateTime.now());
            // 限制长度
            mapper.insert(item);
            if (item.getHistory()) {
                operationHistoryMapper.insert(getHistory(item));
            }
        });
    }

    private String subStrContent(String content) {
        if (StringUtils.isNotBlank(content) && content.length() > 500) {
            return content.substring(0, 499);
        }
        return content;
    }

    private @NonNull OperationHistory getHistory(LogDTO dto) {
        OperationHistory history = new OperationHistory();
        BeanUtils.copyProperties(dto, history);
        return history;
    }
}
