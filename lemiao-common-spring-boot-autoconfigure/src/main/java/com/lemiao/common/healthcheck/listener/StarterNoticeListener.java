package com.lemiao.common.healthcheck.listener;

import java.text.MessageFormat;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

import com.lemiao.common.consts.SpringBootEnvConst;
import com.lemiao.common.dingding.client.DingDingClient;
import com.lemiao.common.dingding.message.MarkdownMessage;
import com.lemiao.common.healthcheck.properties.NoticeProperties;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.health.HealthEndpoint;
import org.springframework.boot.actuate.health.Status;
import org.springframework.boot.actuate.health.SystemHealth;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.PropertySource;
import org.springframework.lang.NonNull;

/**
 * @author by catface
 * @date 2020/08/01
 */
@Slf4j
public class StarterNoticeListener implements ApplicationListener<ApplicationReadyEvent> {

    /**
     * SpringCloud 应用信息 Key
     */
    private static final String HOST_INFO_KEY = "springCloudClientHostInfo";

    /**
     * 应用 hostname Key
     */
    private static final String HOSTNAME_KEY = "spring.cloud.client.hostname";

    /**
     * 应用 ip Key
     */
    private static final String IP_ADDRESS_KEY = "spring.cloud.client.ip-address";

    /**
     * 通知上下文
     */
    private NoticeContext noticeContext = new NoticeContext();

    private NoticeProperties noticeProperties;

    private DingDingClient dingDingClient;

    private HealthEndpoint healthEndpoint;

    private Executor executor;

    public StarterNoticeListener(NoticeProperties noticeProperties,
                                 DingDingClient dingDingClient,
                                 HealthEndpoint healthEndpoint, Executor executor) {
        this.noticeProperties = noticeProperties;
        this.dingDingClient = dingDingClient;
        this.healthEndpoint = healthEndpoint;
        this.executor = executor;
    }

    @Override
    public void onApplicationEvent(@NonNull ApplicationReadyEvent event) {
        // 通知判断
        ConfigurableApplicationContext context = event.getApplicationContext();
        if (noticeContext.isNotice() || SpringBootEnvConst.LOCAL.equals(noticeProperties.getEnv())) {
            return;
        }

        // 构建通知消息
        MarkdownMessage message = buildMessage(context);

        // 执行健康检查并通知
        doHealthCheckAndNotice(message);

        // 超时检查并通知
        checkTimeOutAndNotice(message);
    }

    /**
     * 构建通知消息
     *
     * @param context 应用上下文
     * @return 通知消息
     */
    private MarkdownMessage buildMessage(ConfigurableApplicationContext context) {
        MarkdownMessage message = new MarkdownMessage();
        message.setTitle(" 流水线 ");
        message.add("## app: " + noticeProperties.getApp());
        message.add("## env: " + noticeProperties.getEnv());

        MutablePropertySources propertySources = context.getEnvironment().getPropertySources();
        PropertySource<?> hostInfo = propertySources.get(HOST_INFO_KEY);
        // hostname
        if (hostInfo != null && hostInfo.containsProperty(HOSTNAME_KEY)) {
            message.add("## hostname: " + hostInfo.getProperty(HOSTNAME_KEY));
        }
        // ip-address
        if (hostInfo != null && hostInfo.containsProperty(IP_ADDRESS_KEY)) {
            message.add("## ip-address: " + hostInfo.getProperty(IP_ADDRESS_KEY));
        }
        return message;
    }

    /**
     * 超时检查并通知
     *
     * @param message 通知消息
     */
    private void checkTimeOutAndNotice(MarkdownMessage message) {
        long start = System.currentTimeMillis();
        while (!noticeContext.isNotice() && System.currentTimeMillis() - start < noticeProperties
            .getHealthCheckTimeOutMillis()) {
            try {
                Thread.sleep(100L);
            } catch (InterruptedException e) {
                log.error(e.getMessage(), e);
            }
        }

        if (!noticeContext.isNotice()) {
            sendTimeOutNotice(message);
        }
    }

    /**
     * 发送超时通知
     *
     * @param message 通知消息
     */
    public void sendTimeOutNotice(MarkdownMessage message) {
        message.add("## result: **HEALTH_CHECK_TIMEOUT** ");
        noticeProperties.getStartDingUrls().forEach(url -> dingDingClient.sendMessage(url, message));
    }

    /**
     * 执行健康检查
     *
     * @param message 通知消息
     */
    private void doHealthCheckAndNotice(MarkdownMessage message) {
        executor.execute(new HealthCheckTask(noticeContext, healthEndpoint, dingDingClient, noticeProperties, message));
    }

    /**
     * 启动通知上下文类
     */
    public static class NoticeContext {

        private volatile AtomicBoolean notice = new AtomicBoolean(false);

        /**
         * 设置通知标识
         */
        public void noticed() {
            this.notice.set(true);
        }

        /**
         * 通知标识
         *
         * @return 是否发送通知
         */
        public boolean isNotice() {
            return notice.get();
        }

    }

    /**
     * 健康检查线程任务类
     */
    @AllArgsConstructor
    public static class HealthCheckTask implements Runnable {

        private NoticeContext context;

        private HealthEndpoint healthEndpoint;

        private DingDingClient dingDingClient;

        private NoticeProperties noticeProperties;

        private MarkdownMessage message;

        @Override
        public void run() {
            SystemHealth health = (SystemHealth)healthEndpoint.health();
            sendStartNotice(health);
            context.noticed();
        }

        /**
         * 发送通知消息
         *
         * @param health 是否健康检查通过
         */
        public void sendStartNotice(SystemHealth health) {
            boolean success = health.getStatus().equals(Status.UP);
            message.add(MessageFormat.format("## result: **{0}** ", success ? "SUCCESS" : "FAIL"));

            // 健康检查信息
            Set<String> components = new HashSet<>();
            health.getComponents().forEach(
                (k, c) -> components.add(MessageFormat.format("{0}-{1}", k, c.getStatus())));
            message.add(MessageFormat.format("## components: **{0}** ", components));
            noticeProperties.getStartDingUrls().forEach(url -> dingDingClient.sendMessage(url, message));
        }

    }

}
