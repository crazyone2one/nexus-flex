import {NFR} from "/@/api";
import type {IPageResponse, ITableQueryParams} from "/@/api/types/common.ts";
import type {OrgProjectTableItem} from "/@/api/types/project.ts";

export const OrgProjectApi = {
    fetchProjectPage: (data: ITableQueryParams) =>
        NFR.Post<IPageResponse<OrgProjectTableItem>>('/system/project/page', data, {cacheFor: 0})
}