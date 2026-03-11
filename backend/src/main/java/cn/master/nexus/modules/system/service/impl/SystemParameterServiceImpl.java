package cn.master.nexus.modules.system.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import cn.master.nexus.modules.system.entity.SystemParameter;
import cn.master.nexus.modules.system.mapper.SystemParameterMapper;
import cn.master.nexus.modules.system.service.SystemParameterService;
import org.springframework.stereotype.Service;

/**
 * 系统参数 服务层实现。
 *
 * @author 11's papa
 * @since 2026-03-09
 */
@Service
public class SystemParameterServiceImpl extends ServiceImpl<SystemParameterMapper, SystemParameter>  implements SystemParameterService{

}
