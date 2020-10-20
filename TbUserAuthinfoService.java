package com.yoyo.authentication.service;

public interface TbUserAuthinfoService {

    /**
     * 验证身份信息
     * @param idCard
     * @param realName
     * @return
     */
    Object verifyIdentityInfo(String idCard, String realName);

}
