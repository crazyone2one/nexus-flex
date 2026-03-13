import {NFR} from "/@/api";
import type {IPageResponse, ITableQueryParams} from "/@/api/types/common.ts";
import type {OrgProjectTableItem, ProjectListItem} from "/@/api/types/project.ts";

export const OrgProjectApi = {
    fetchProjectPage: (data: ITableQueryParams) =>
        NFR.Post<IPageResponse<OrgProjectTableItem>>('/system/project/page', data, {cacheFor: 0}),
    // 创建或更新项目
    createOrUpdateProject: (data: Partial<OrgProjectTableItem>) =>
        NFR.Post(data.id ? '/system/project/update' : '/system/project/save', data),
    fetchProjectList: (organizationId: string) => NFR.Get<Array<ProjectListItem>>(`/system/project/list/options/${organizationId}`, {cacheFor: 0}),
    // 启用或禁用项目
    enableOrDisableProject: (id: string, isEnable = true) =>
        NFR.Get(isEnable ? `/system/project/enable/${id}` : `/system/project/disable/${id}`),
    // 修改项目名称
    renameProject: (data: { id: string; name: string; organizationId: string }) =>
        NFR.Post('/system/project/rename', data),
    // 删除项目
    deleteProject: (id: string) => NFR.Get(`/system/project/remove/${id}`),
}