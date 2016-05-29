package com.station.soy;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;

import com.google.template.soy.SoyFileSet;
import com.google.template.soy.tofu.SoyTofu;

public class StandardSoyTofuStore implements SoyTofuStore {
	
	private SoyTofu tofu;

	public StandardSoyTofuStore(String templateDirectory) throws IOException {
		this.tofu = createSoyTofu(templateDirectory);
	}

	private SoyTofu createSoyTofu(String templateDirectory) throws IOException {
		SoyFileSet.Builder fileSet = SoyFileSet.builder();

		FileSystem fileSystem = FileSystems.getDefault();
		Files.walk(fileSystem.getPath(templateDirectory), FileVisitOption.FOLLOW_LINKS).forEach(path -> {
			File file = path.toFile();
			if (file.isFile()) {
				fileSet.add(file);
			}
		});

		return fileSet.build().compileToTofu();
	}
	
	@Override
	public SoyTofu getTofu() {
		return tofu;
	}
}
