package me.java.ch2.sub01execute;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class Renderer {
	private final ExecutorService executor;

	public Renderer(ExecutorService executor) {
		this.executor = executor;
	}

	void renderPage(CharSequence source) {
		List<ImageInfo> info = scanForImageInfo(source);

		ExecutorCompletionService<ImageData> completionService =
			new ExecutorCompletionService<>(executor);

		// FutureRenderer 의 Future 와 Callable 의 task 는 기존 작업이 완료될때까지 다음 이미지 다운로드를 시도하는 것조차 허용하지 않았지만
		// ExecutorCompletionService 은 Executor 를 통해,
		for (final ImageInfo imageInfo : info) {
			completionService.submit(
				imageInfo::downloadImage
			);
		}

		renderText(source);

		try {
			for (int t = 0, n = info.size(); t < n; t++) {
				Future<ImageData> f = completionService.take();
				ImageData imageData = f.get();
				renderImage(imageData);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
		} catch (ExecutionException e) {
			e.getCause();
		}
	}

	abstract void renderText(CharSequence s);

	abstract List<ImageInfo> scanForImageInfo(CharSequence s);

	abstract void renderImage(ImageData i);

}
