package com.lemiao.common.dingding.client;

import com.lemiao.common.dingding.message.Message;

/**
 * @author by catface
 * @date 2020/08/01
 */
public interface DingDingClient {

    /**
     * 发送消息
     *
     * @param url     钉钉url
     * @param message 消息
     */
    void sendMessage(String url, Message message);
}
