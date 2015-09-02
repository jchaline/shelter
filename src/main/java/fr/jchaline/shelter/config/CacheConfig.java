package fr.jchaline.shelter.config;

import java.util.concurrent.ConcurrentHashMap;

import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCache;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableCaching
public class CacheConfig {

	/**
	 * Configuration du cache manager, ici avec une simple Map
	 * Pas de configuration de la taille ou du délai de conservation
	 * @return Le cache manager
	 */
	@Bean
	public CacheManager cacheManager() {
		ConcurrentMapCacheManager cacheManager = new ConcurrentMapCacheManager() {
        	@Override
            protected Cache createConcurrentMapCache(final String name) {
        		return new ConcurrentMapCache(name,
        			new ConcurrentHashMap<Object, Object>(), false);
        	}
        };
    	return cacheManager;
	}
}
