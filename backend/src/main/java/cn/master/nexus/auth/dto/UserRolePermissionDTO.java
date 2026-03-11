package cn.master.nexus.auth.dto;

import cn.master.nexus.modules.system.entity.UserRole;
import cn.master.nexus.modules.system.entity.UserRoleRelation;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author : 11's papa
 * @since : 2026/3/10, 星期二
 **/
@Data
public class UserRolePermissionDTO {
    List<UserRoleResourceDTO> list = new ArrayList<>();
    List<UserRole> userRoles = new ArrayList<>();
    List<UserRoleRelation> userRoleRelations = new ArrayList<>();
}
