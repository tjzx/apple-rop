/**
 * Copyright： 版权所有 违者必究 2013
 */
package com.appleframework.rop.session;

import com.appleframework.rop.AbstractInterceptor;
import com.appleframework.rop.CommonConstant;
import com.appleframework.rop.RopRequestContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 将{@link Session}绑定到{@link RopSessionHolder}中，默认注册。
 *
 * @author : chenxh(quickselect@163.com)
 * @date: 13-10-16
 */
public class SessionBindInterceptor extends AbstractInterceptor {

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void beforeService(RopRequestContext ropRequestContext) {
        Session session = ropRequestContext.getSession();
        if (session != null) {
            RopSessionHolder.put(session);
            if (logger.isDebugEnabled()) {
                logger.debug("会话绑定到{}中", RopSessionHolder.class.getCanonicalName());
            }
        }
    }

    @Override
    public void beforeResponse(RopRequestContext ropRequestContext) {
        Session session = ropRequestContext.getSession();
        if (session != null && session.isChanged()) {
            session.removeAttribute(CommonConstant.SESSION_CHANGED);
            SessionManager sessionManager = ropRequestContext.getRopContext().getSessionManager();
            sessionManager.addSession(ropRequestContext.getSessionId(), session);
            if (logger.isDebugEnabled()) {
                logger.debug("会话内容发生更改，将其同步到外部缓存管理器中。");
            }
        }
    }
}
