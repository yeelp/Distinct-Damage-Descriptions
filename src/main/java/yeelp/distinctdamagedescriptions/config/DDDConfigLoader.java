package yeelp.distinctdamagedescriptions.config;

import java.util.LinkedList;
import java.util.Queue;
import java.util.stream.Collectors;

import yeelp.distinctdamagedescriptions.DistinctDamageDescriptions;
import yeelp.distinctdamagedescriptions.config.readers.DDDConfigReader;
import yeelp.distinctdamagedescriptions.config.readers.exceptions.DDDConfigReaderException;

public final class DDDConfigLoader {
	private final Queue<DDDConfigReader> readers;
	private static DDDConfigLoader instance;

	private DDDConfigLoader() {
		this.readers = new LinkedList<DDDConfigReader>();
	}

	public void enqueue(DDDConfigReader reader) {
		this.readers.add(reader);
	}

	public void enqueueAll(DDDConfigReader... readers) {
		for(DDDConfigReader reader : readers) {
			this.readers.add(reader);
		}
	}
	
	public void enqueueAll(Iterable<DDDConfigReader> readers) {
		readers.forEach(this.readers::add);
	}

	public static DDDConfigLoader getInstance() {
		return instance == null ? instance = new DDDConfigLoader() : instance;
	}

	public static void readConfig() {
		Iterable<Thread> threads = getInstance().readers.stream().map(DDDConfigReader::toThread).collect(Collectors.toList());
		threads.forEach(Thread::start);
		for(Thread thread : threads) {
			try {
				thread.join();
			}
			catch(InterruptedException e) {
				e.printStackTrace();
			}
		}
		getInstance().readers.stream().filter(DDDConfigReader::doesSelfReportErrors).map(DDDConfigReader::getSelfReportedErrors).reduce((c1, c2) -> {
			c1.addAll(c2);
			return c1;
		}).filter((c) -> !c.isEmpty()).ifPresent((errors) -> {
			DistinctDamageDescriptions.fatal("There were some problems reading from the config! Check the following:");
			errors.forEach(DDDConfigReaderException::log);
		});
	}
	
	public static Iterable<String> getErrorMessages() {
		return getInstance().readers.stream().filter(DDDConfigReader::doesSelfReportErrors).flatMap((r) -> r.getSelfReportedErrors().stream().map(DDDConfigReaderException::getLocalizedMessage)).collect(Collectors.toList());
	}
}
