package hr.fer.zemris.java.fractals.cmplxcache;

public class ComplexCache {
	
	private static final IComplexCacheProducer PRODUCER = new IComplexCacheProducer() {
		@Override
		public IComplexCache getCache() {
			IThreadBoundComplexCache customThread = (IThreadBoundComplexCache)Thread.currentThread();
			return customThread.getComplexCache();
		}
	};

	private ComplexCache() {
	}
	
	public static IComplexCache getCache() {
		return PRODUCER.getCache();
	}
	
}
