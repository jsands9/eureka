package edu.emory.cci.aiw.cvrg.eureka.services.thread;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import edu.emory.cci.aiw.cvrg.eureka.services.config.ServiceProperties;

/**
 * Creates an Executor thread pool, and queues new jobs to that pool.
 *
 * @author hrathod
 */
@Singleton
public class JobExecutor {
	/**
	 * The service used to run tasks.
	 */
	private final ExecutorService service;

	/**
	 * Create a new JobExecutor with the given application properties.
	 *
	 * @param properties The application level configuration object.
	 */
	@Inject
	public JobExecutor(final ServiceProperties properties) {
		this.service = Executors
				.newFixedThreadPool(properties.getJobPoolSize());
	}

	/**
	 * Put a new job on the service queue.
	 *
	 * @param task The task to queue up.
	 */
	public void queueJob(final JobTask task) {
		this.service.execute(task);
	}
}
