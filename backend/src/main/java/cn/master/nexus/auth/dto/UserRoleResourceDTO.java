package cn.master.nexus.auth.dto;

import cn.master.nexus.modules.system.entity.UserRole;
import cn.master.nexus.modules.system.entity.UserRolePermission;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/3/10, 星期二
 **/
@Data
public class UserRoleResourceDTO implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    private UserRoleResource resource;
    private List<UserRolePermission> permissions;
    private String type;

    private UserRole userRole;
    private List<UserRolePermission> userRolePermissions;
}
