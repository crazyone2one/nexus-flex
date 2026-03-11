package cn.master.nexus.common.constants;

/**
 * @author : 11's papa
 * @since : 2026/3/4, 星期三
 **/
public enum OperationLogType {
    ADD,
    DELETE,
    UPDATE,
    DEBUG,
    REVIEW,
    COPY,
    EXECUTE,
    SHARE,
    RESTORE,
    IMPORT,
    EXPORT,
    LOGIN,
    SELECT,
    RECOVER,
    LOGOUT,
    DISASSOCIATE,
    ASSOCIATE,
    QRCODE,
    ARCHIVED,
    STOP,
    RERUN;

    public boolean contains(OperationLogType keyword) {
        return this.name().contains(keyword.name());
    }
}
