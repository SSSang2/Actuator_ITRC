public class main {
	public static void main(String[] args) throws InterruptedException {
		
	        ThreadPool.getInstance().runWorker("TCP",  new Link());
	}
}