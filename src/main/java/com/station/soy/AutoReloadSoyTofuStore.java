package com.station.soy;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;

import com.google.template.soy.tofu.SoyTofu;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AutoReloadSoyTofuStore implements SoyTofuStore {

	private Thread thread;
	private SoyTofu tofu;

	public AutoReloadSoyTofuStore(String templateDirectory) throws IOException {
		this.tofu = new StandardSoyTofuStore(templateDirectory).getTofu();

		thread = new Thread(new Runnable() {
			@Override
			public void run() {
				FileSystem fs = FileSystems.getDefault();
				while (true) {
					try (WatchService watcher = fs.newWatchService()) {
						log.debug("Watch directory: {}", templateDirectory);

						Files.walkFileTree(new File(templateDirectory).toPath(), new SimpleFileVisitor<Path>() {
							@Override
							public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs)
									throws IOException {
								dir.register(watcher,
										StandardWatchEventKinds.ENTRY_CREATE,
										StandardWatchEventKinds.ENTRY_MODIFY,
										StandardWatchEventKinds.ENTRY_DELETE,
										StandardWatchEventKinds.OVERFLOW);
								return FileVisitResult.CONTINUE;
							}
						});

						watcher.take();

						log.trace("Reload templates");
						tofu = new StandardSoyTofuStore(templateDirectory).getTofu();
						log.trace("Reload templates ... OK");
					} catch (IOException e) {
						log.error("Template autorealod thread has interrupted", e);
					} catch (InterruptedException e) {
						log.trace("Interrupted");
						break;
					}
				}
			}
		});
		thread.start();
	}

	@Override
	public SoyTofu getTofu() {
		return tofu;
	}

	@Override
	protected void finalize() throws Throwable {
		thread.interrupt();
	}
}
