/**
 *  SIWECOS-Host-Validator - A Webservice for the Siwecos Infrastructure to validate user provided hosts
 *
 *  Copyright 2019 Ruhr University Bochum / Hackmanit GmbH
 *
 *  Licensed under Apache License 2.0
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 */
package de.rub.nds.siwecos.validator.ws;

import java.security.Security;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.apache.logging.log4j.LogManager;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;

/**
 *
 * @author Robert Merget <robert.merget@rub.de>
 */
public class PoolManager {

    protected static final org.apache.logging.log4j.Logger LOGGER = LogManager.getLogger(PoolManager.class.getName());

    private ThreadPoolExecutor service;

    private int parallelProbeThreads = 64;

    private int probeThreads = 9;

    private PoolManager() {
        LOGGER.info("Initializing PoolManager...");
        LOGGER.info("Adding BC as a Security Provider");
        Security.addProvider(new BouncyCastleProvider());
        LOGGER.info("Starting thread pool");

        String redisHost = System.getenv("REDIS_HOST");
        String redisDb = System.getenv("REDIS_DB");
        BlockingQueue<Runnable> blockingQueue;

        if (redisHost == null || redisDb == null) {
            LOGGER.error("Could not find REDIS server, falling back to local queue");
            blockingQueue = new LinkedBlockingDeque<>();
        } else {
            LOGGER.info("Initializing connection to redis:" + redisHost + "/" + redisDb);
            Config config = new Config();
            config.useClusterServers().addNodeAddress(redisHost);
            try {
                RedissonClient redisson = Redisson.create();
                blockingQueue = redisson.getBlockingDeque(redisDb);
                System.out.println("Established connection to redis :)");
            } catch (Exception E) {
                LOGGER.error("Connection to redis failed", E);
                LOGGER.error("Falling back to normal queue");
                blockingQueue = new LinkedBlockingDeque<>();
            }
        }
        service = new ThreadPoolExecutor(10, 10, 10, TimeUnit.MINUTES, blockingQueue);
        LOGGER.info("PoolManager Inialized successfully");
    }

    public static PoolManager getInstance() {
        return PoolManagerHolder.INSTANCE;
    }

    private static class PoolManagerHolder {

        private static final PoolManager INSTANCE = new PoolManager();
    }

    public ThreadPoolExecutor getService() {
        return service;
    }

    public void setPoolSize(int poolsize) {
        boolean increasing = poolsize > service.getPoolSize();
        service.setCorePoolSize(poolsize);
        service.setMaximumPoolSize(poolsize);
        if (!increasing) {
            LOGGER.warn("You decreased the Threadpool Size! Changes take effect once all Tasks are completed or you restart the service!");
        }
    }

    public int getParallelProbeThreads() {
        return parallelProbeThreads;
    }

    public void setParallelProbeThreads(int parallelProbeThreads) {
        this.parallelProbeThreads = parallelProbeThreads;
    }

    public int getProbeThreads() {
        return probeThreads;
    }

    public void setProbeThreads(int probeThreads) {
        this.probeThreads = probeThreads;
    }
}
