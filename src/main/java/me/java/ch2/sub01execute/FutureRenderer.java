package me.java.ch2.sub01execute;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public abstract class FutureRenderer {
	private final ExecutorService executor = Executors.newCachedThreadPool();

	void renderPage(CharSequence source) {
		final List<ImageInfo> imageInfos = scanForImageInfo(source);
		Callable<List<ImageData>> task = () -> {
			List<ImageData> result = new ArrayList<>();
			for (ImageInfo imageInfo : imageInfos) {
				result.add(imageInfo.downloadImage());
			}
			return result;
		};

		Future<List<ImageData>> future = executor.submit(task);
		renderText(source);

		try {
			List<ImageData> imageData = future.get();
			for (ImageData data : imageData) {
				renderImage(data);
			}
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			// 예외가 일어난 이상 해당 작업은 필요가 없으니 취소한다.
			future.cancel(true);
		} catch (ExecutionException e) {
			e.printStackTrace();
		}
	}

	abstract void renderText(CharSequence source);

	abstract List<ImageInfo> scanForImageInfo(CharSequence source);

	abstract void renderImage(ImageData i);
}
