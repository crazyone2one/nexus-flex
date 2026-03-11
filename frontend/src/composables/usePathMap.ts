import {MENU_LEVEL, pathMap, type PathMapItem, type PathMapRoute} from "/@/config/pathMap.ts";
import {findNodeByKey, mapTree, type TreeNode} from "/@/utils";
import router from "/@/router";

export default function usePathMap() {
    const getPathMapByLevel = <T>(
        level: (typeof MENU_LEVEL)[number],
        customNodeFn: (node: TreeNode<T>) => TreeNode<T> | null = (node) => node
    ) => {
        return mapTree(pathMap, (e) => {
            let isValid = true; // 默认是系统级别
            if (level === MENU_LEVEL[1]) {
                // 组织级别只展示组织、项目
                isValid = e.level !== MENU_LEVEL[0];
            } else if (level === MENU_LEVEL[2]) {
                // 项目级别只展示项目
                isValid = e.level !== MENU_LEVEL[0] && e.level !== MENU_LEVEL[1];
            }
            if (isValid && !e.hideInModule) {
                return typeof customNodeFn === 'function' ? customNodeFn(e) : e;
            }
            return null;
        });
    };
    const jumpRouteByMapKey = (key: PathMapRoute, routeQuery?: Record<string, any>, openNewPage = false) => {
        const pathNode = findNodeByKey<PathMapItem>(pathMap, key as unknown as string);
        if (pathNode && (pathNode.route || key.includes('PROJECT_MANAGEMENT_TASK_CENTER'))) {
            if (openNewPage) {
                window.open(
                    `${window.location.origin}#${router.resolve({name: pathNode.route}).fullPath}?${new URLSearchParams({
                        ...routeQuery,
                        ...pathNode.routeQuery,
                    }).toString()}`
                );
            } else {
                router.push({
                    name: pathNode.route,
                    query: {
                        ...routeQuery,
                        ...pathNode.routeQuery,
                    },
                });
            }
        }
    };
    const getRouteLevelByKey = (name: PathMapRoute) => {
        const pathNode = findNodeByKey<PathMapItem>(pathMap, name, 'route');
        if (pathNode) {
            return pathNode.level;
        }
        return null;
    };
    const findLocalePath = (targetKey: string) => {
        function getLocalePathArr(trees: PathMapItem[], path: string[] = []): string[] {
            for (let i = 0; i < trees.length; i++) {
                const node = trees[i];
                if (node) {
                    const newPathArr = [...path, node.locale];

                    if (node.key === targetKey) {
                        return newPathArr;
                    }

                    if (targetKey.startsWith(node.key) && node.children) {
                        const result = getLocalePathArr(node.children, newPathArr);
                        if (result) {
                            return result;
                        }
                    }
                }
            }

            return [];
        }

        return getLocalePathArr(pathMap);
    };
    return {
        getPathMapByLevel,
        jumpRouteByMapKey,
        getRouteLevelByKey,
        findLocalePath,
    };
}