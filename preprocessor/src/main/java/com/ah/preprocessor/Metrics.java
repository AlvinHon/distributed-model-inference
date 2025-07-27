package com.ah.preprocessor;

import io.prometheus.metrics.core.metrics.Counter;
import io.prometheus.metrics.exporter.pushgateway.PushGateway;

public class Metrics {
    public static PushGateway pushGateway = PushGateway.builder()
            .address(Config.Prometheus.PUSHGATEWAY_HOST)
            .job(Config.Prometheus.JOB_NAME)
            .build();
    private static Counter requestCountTotal = Counter.builder()
            .name(Config.Prometheus.METRIC_REQUEST_COUNT)
            .help(Config.Prometheus.METRIC_REQUEST_COUNT_HELP_STRING)
            .register();

    public static void incrementRequestCount() {
        try {
            requestCountTotal.inc();
            // TODO: consider pushing on scheduled task for better performance.
            pushGateway.push();
        } catch (Exception e) {
            System.err.println("Failed to push metrics to Prometheus Pushgateway: " + e.getMessage());
        }
    }
}
