export interface CreateOrUpdateSystemProjectParams {
    id?: string;
    // 项目名称
    name: string;
    num: string;
    // 项目描述
    description: string;
    // 启用或禁用
    enable: boolean;
    // 项目成员
    userIds: string[];
    // 模块配置
    moduleIds?: string[];
    // 所属组织
    organizationId?: string;
    // 资源池
    resourcePoolIds: string[];
    // 列表里的
    allResourcePool: boolean; // 默认全部资源池
}

export interface CreateOrUpdateOrgProjectParams {
    id?: string;
    name: string;
    description?: string;
    enable?: boolean;
    userIds?: string[];
    organizationId?: string;
    resourcePoolIds?: string[];
}
export interface OrgProjectTableItem {
    id: string;
    name: string;
    description: string;
    enable: boolean;
    adminList: UserExtend[];
    organizationId: string;
    organizationName: string;
    num: string;
    updateTime: number;
    createTime: number;
    memberCount: number;
    userIds: string[];
    resourcePoolIds: string[];
    orgAdmins: Record<string, any>;
    moduleIds: string[];
    // resourcePoolList: ResourcePoolItem[];
    allResourcePool: boolean;
}
export interface ProjectListItem {
    id: string;
    num: string;
    organizationId: string;
    name: string;
    description: string;
    createTime: number;
    updateTime: number;
    updateUser: string;
    createUser: string;
    deleteTime: number;
    deleted: boolean;
    deleteUser: string;
    enable: boolean;
    moduleIds: string[];
    moduleSetting: string;
}

export interface UserExtend {
    userId:string
    userName:string
}