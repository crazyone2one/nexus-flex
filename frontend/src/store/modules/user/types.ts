import type {UserRole, UserRolePermissions, UserRoleRelation} from "/@/api/types/auth.ts";

export interface UserState {
    id?: string;
    name?: string;
    email?: string;
    phone?: string;
    enable?: boolean;
    lastOrganizationId?: string;
    lastProjectId?: string;
    avatar?: string;
    userGroup?: string[];
    userRolePermissions?: UserRolePermissions[];
    userRoles?: UserRole[];
    userRoleRelations?: UserRoleRelation[];
}