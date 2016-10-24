package com.ag.nanshi.redis.transcoder.closeable;

import java.io.Closeable;

public class CloseableCommon {

	public static void close(Closeable closeable) {
		if (closeable != null) {
			try {
				closeable.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

}
