/**
 * Copyright (c) 2013-2016, The SeedStack authors <http://seedstack.org>
 *
 * This Source Code Form is subject to the terms of the Mozilla Public
 * License, v. 2.0. If a copy of the MPL was not distributed with this
 * file, You can obtain one at http://mozilla.org/MPL/2.0/.
 */
package org.seedstack.netflix.eureka;

import com.netflix.appinfo.EurekaAccept;
import com.netflix.discovery.EurekaClientConfig;
import com.netflix.discovery.shared.transport.EurekaTransportConfig;
import org.seedstack.coffig.Config;
import org.seedstack.coffig.SingleValue;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Config("eureka")
public class EurekaConfig {

    private ClientConfig client = new ClientConfig();


    public ClientConfig client() {
        return client;
    }



    @Config("client")
    public static class ClientConfig implements EurekaClientConfig {
        private int registryFetchIntervalSeconds = 30;
        private int instanceInfoReplicationIntervalSeconds = 30;
        private int initialInstanceInfoReplicationIntervalSeconds = 40;
        private int eurekaServiceUrlPollIntervalSeconds = 5 * 60 * 1000;
        private String proxyHost;
        private String proxyPort;
        private String proxyUserName;
        private String proxyPassword;
        private boolean shouldGZipContent = true;
        private int eurekaServerReadTimeoutSeconds = 8;
        private int eurekaServerConnectTimeoutSeconds = 5;
        private String backupRegistryImpl;
        private int eurekaServerTotalConnections = 200;
        private int eurekaServerTotalConnectionsPerHost = 50;
        private String eurekaServerURLContext;
        private String eurekaServerPort;
        private String eurekaServerDNSName;
        private boolean shouldUseDnsForFetchingServiceUrls = false;
        private boolean shouldRegisterWithEureka = true;
        private boolean shouldPreferSameZoneEureka = true;
        private boolean allowRedirects = false;
        private boolean shouldLogDeltaDiff = false;
        private boolean shouldDisableDelta = false;
        private String fetchRegistryForRemoteRegions;
        private String region;
        private Map<String, List<String>> availabilityZonesByRegion = new HashMap<>();
        @SingleValue
        private List<String> eurekaServerServiceUrls = new ArrayList<>();
        private Map<String, List<String>> eurekaServerServiceUrlsByZone = new HashMap<>();
        private boolean shouldFilterOnlyUpInstances = true;
        private int eurekaConnectionIdleTimeoutSeconds = 30;
        private boolean shouldFetchRegistry = true;
        private String registryRefreshSingleVipAddress;
        private int heartbeatExecutorThreadPoolSize = 5;
        private int heartbeatExecutorExponentialBackOffBound = 10;
        private int cacheRefreshExecutorThreadPoolSize = 5;
        private int cacheRefreshExecutorExponentialBackOffBound = 10;
        private String dollarReplacement = "_-";
        private String escapeCharReplacement = "__";
        private boolean shouldOnDemandUpdateStatusChange = true;
        private String encoderName;
        private String decoderName;
        private String clientDataAccept = EurekaAccept.full.name();
        private Map<String, String> experimental = new HashMap<>();
        private TransportConfig transport = new TransportConfig();

        @Override
        public int getRegistryFetchIntervalSeconds() {
            return registryFetchIntervalSeconds;
        }

        @Override
        public int getInstanceInfoReplicationIntervalSeconds() {
            return instanceInfoReplicationIntervalSeconds;
        }

        @Override
        public int getInitialInstanceInfoReplicationIntervalSeconds() {
            return initialInstanceInfoReplicationIntervalSeconds;
        }

        @Override
        public int getEurekaServiceUrlPollIntervalSeconds() {
            return eurekaServiceUrlPollIntervalSeconds;
        }

        @Override
        public String getProxyHost() {
            return proxyHost;
        }

        @Override
        public String getProxyPort() {
            return proxyPort;
        }

        @Override
        public String getProxyUserName() {
            return proxyUserName;
        }

        @Override
        public String getProxyPassword() {
            return proxyPassword;
        }

        @Override
        public boolean shouldGZipContent() {
            return shouldGZipContent;
        }

        @Override
        public int getEurekaServerReadTimeoutSeconds() {
            return eurekaServerReadTimeoutSeconds;
        }

        @Override
        public int getEurekaServerConnectTimeoutSeconds() {
            return eurekaServerConnectTimeoutSeconds;
        }

        @Override
        public String getBackupRegistryImpl() {
            return backupRegistryImpl;
        }

        @Override
        public int getEurekaServerTotalConnections() {
            return eurekaServerTotalConnections;
        }

        @Override
        public int getEurekaServerTotalConnectionsPerHost() {
            return eurekaServerTotalConnectionsPerHost;
        }

        @Override
        public String getEurekaServerURLContext() {
            return eurekaServerURLContext;
        }

        @Override
        public String getEurekaServerPort() {
            return eurekaServerPort;
        }

        @Override
        public String getEurekaServerDNSName() {
            return eurekaServerDNSName;
        }

        @Override
        public boolean shouldUseDnsForFetchingServiceUrls() {
            return shouldUseDnsForFetchingServiceUrls;
        }

        @Override
        public boolean shouldRegisterWithEureka() {
            return shouldRegisterWithEureka;
        }

        @Override
        public boolean shouldPreferSameZoneEureka() {
            return shouldPreferSameZoneEureka;
        }

        @Override
        public boolean allowRedirects() {
            return allowRedirects;
        }

        @Override
        public boolean shouldLogDeltaDiff() {
            return shouldLogDeltaDiff;
        }

        @Override
        public boolean shouldDisableDelta() {
            return shouldDisableDelta;
        }

        @Nullable
        @Override
        public String fetchRegistryForRemoteRegions() {
            return fetchRegistryForRemoteRegions;
        }

        @Override
        public String getRegion() {
            return region;
        }

        @Override
        public String[] getAvailabilityZones(String region) {
            return (availabilityZonesByRegion.getOrDefault(region, new ArrayList<>())).toArray(new String[availabilityZonesByRegion.size()]);
        }

        @Override
        public List<String> getEurekaServerServiceUrls(String myZone) {
            if (myZone == null || "default".equals(myZone)) {
                return eurekaServerServiceUrls;
            } else {
                return eurekaServerServiceUrlsByZone.getOrDefault(myZone, new ArrayList<>());
            }
        }

        @Override
        public boolean shouldFilterOnlyUpInstances() {
            return shouldFilterOnlyUpInstances;
        }

        @Override
        public int getEurekaConnectionIdleTimeoutSeconds() {
            return eurekaConnectionIdleTimeoutSeconds;
        }

        @Override
        public boolean shouldFetchRegistry() {
            return shouldFetchRegistry;
        }

        @Nullable
        @Override
        public String getRegistryRefreshSingleVipAddress() {
            return registryRefreshSingleVipAddress;
        }

        @Override
        public int getHeartbeatExecutorThreadPoolSize() {
            return heartbeatExecutorThreadPoolSize;
        }

        @Override
        public int getHeartbeatExecutorExponentialBackOffBound() {
            return heartbeatExecutorExponentialBackOffBound;
        }

        @Override
        public int getCacheRefreshExecutorThreadPoolSize() {
            return cacheRefreshExecutorThreadPoolSize;
        }

        @Override
        public int getCacheRefreshExecutorExponentialBackOffBound() {
            return cacheRefreshExecutorExponentialBackOffBound;
        }

        @Override
        public String getDollarReplacement() {
            return dollarReplacement;
        }

        @Override
        public String getEscapeCharReplacement() {
            return escapeCharReplacement;
        }

        @Override
        public boolean shouldOnDemandUpdateStatusChange() {
            return shouldOnDemandUpdateStatusChange;
        }

        @Override
        public String getEncoderName() {
            return encoderName;
        }

        @Override
        public String getDecoderName() {
            return decoderName;
        }

        @Override
        public String getClientDataAccept() {
            return clientDataAccept;
        }

        @Override
        public String getExperimental(String name) {
            return experimental.get(name);
        }

        @Override
        public EurekaTransportConfig getTransportConfig() {
            return transport;
        }

        @Config("transport")
        public static class TransportConfig implements EurekaTransportConfig {
            private int sessionedClientReconnectIntervalSeconds = 1200;
            private double retryableClientQuarantineRefreshPercentage = 0.66;
            private int applicationsResolverDataStalenessThresholdSeconds = 300;
            private boolean applicationsResolverUseIp = false;
            private int asyncResolverRefreshIntervalMs = 300_000;
            private int asyncResolverWarmUpTimeoutMs = 5000;
            private int asyncExecutorThreadPoolSize = 5;
            private String writeClusterVip;
            private String readClusterVip;
            private String bootstrapResolverStrategy;
            private boolean useBootstrapResolverForQuery = true;

            @Override
            public int getSessionedClientReconnectIntervalSeconds() {
                return sessionedClientReconnectIntervalSeconds;
            }

            @Override
            public double getRetryableClientQuarantineRefreshPercentage() {
                return retryableClientQuarantineRefreshPercentage;
            }

            @Override
            public int getApplicationsResolverDataStalenessThresholdSeconds() {
                return applicationsResolverDataStalenessThresholdSeconds;
            }

            @Override
            public boolean applicationsResolverUseIp() {
                return applicationsResolverUseIp;
            }

            @Override
            public int getAsyncResolverRefreshIntervalMs() {
                return asyncResolverRefreshIntervalMs;
            }

            @Override
            public int getAsyncResolverWarmUpTimeoutMs() {
                return asyncResolverWarmUpTimeoutMs;
            }

            @Override
            public int getAsyncExecutorThreadPoolSize() {
                return asyncExecutorThreadPoolSize;
            }

            @Override
            public String getWriteClusterVip() {
                return writeClusterVip;
            }

            @Override
            public String getReadClusterVip() {
                return readClusterVip;
            }

            @Override
            public String getBootstrapResolverStrategy() {
                return bootstrapResolverStrategy;
            }

            @Override
            public boolean useBootstrapResolverForQuery() {
                return useBootstrapResolverForQuery;
            }
        }
    }
}
