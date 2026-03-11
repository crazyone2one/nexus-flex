package cn.master.nexus.modules.log.service;

import cn.master.nexus.common.constants.HttpMethodConstants;
import cn.master.nexus.common.constants.OperationLogConstants;
import cn.master.nexus.common.constants.OperationLogModule;
import cn.master.nexus.common.constants.OperationLogType;
import cn.master.nexus.common.util.JSON;
import cn.master.nexus.modules.log.dto.LogDTO;
import cn.master.nexus.modules.system.dto.AddProjectRequest;
import cn.master.nexus.modules.system.dto.UpdateProjectRequest;
import cn.master.nexus.modules.system.entity.Project;
import cn.master.nexus.modules.system.mapper.ProjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author : 11's papa
 * @since : 2026/3/5, 星期四
 **/
@Service
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class SystemProjectLogService {
    private final ProjectMapper projectMapper;

    public LogDTO addLog(AddProjectRequest project) {
        LogDTO dto = new LogDTO(
                OperationLogConstants.SYSTEM,
                OperationLogConstants.SYSTEM,
                null,
                null,
                OperationLogType.ADD.name(),
                OperationLogModule.SETTING_SYSTEM_ORGANIZATION,
                project.getName());

        dto.setOriginalValue(JSON.toJSONBytes(project));
        return dto;
    }
    public LogDTO updateLog(UpdateProjectRequest request) {
        Project project = projectMapper.selectOneById(request.getId());
        if (project != null) {
            LogDTO dto = new LogDTO(
                    OperationLogConstants.SYSTEM,
                    OperationLogConstants.SYSTEM,
                    project.getId(),
                    null,
                    OperationLogType.UPDATE.name(),
                    OperationLogModule.SETTING_SYSTEM_ORGANIZATION,
                    request.getName());

            dto.setOriginalValue(JSON.toJSONBytes(project));
            return dto;
        }
        return null;
    }
    public LogDTO updateLog(String id) {
        Project project = projectMapper.selectOneById(id);
        if (project != null) {
            LogDTO dto = new LogDTO(
                    OperationLogConstants.SYSTEM,
                    OperationLogConstants.SYSTEM,
                    project.getId(),
                    null,
                    OperationLogType.UPDATE.name(),
                    OperationLogModule.SETTING_SYSTEM_ORGANIZATION,
                    project.getName());
            dto.setMethod(HttpMethodConstants.GET.name());

            dto.setOriginalValue(JSON.toJSONBytes(project));
            return dto;
        }
        return null;
    }
    public LogDTO deleteLog(String id) {
        Project project = projectMapper.selectOneById(id);
        if (project != null) {
            LogDTO dto = new LogDTO(
                    OperationLogConstants.SYSTEM,
                    OperationLogConstants.SYSTEM,
                    id,
                    null,
                    OperationLogType.DELETE.name(),
                    OperationLogModule.SETTING_SYSTEM_ORGANIZATION,
                    project.getName());

            dto.setOriginalValue(JSON.toJSONBytes(project));
            return dto;
        }
        return null;
    }
}
