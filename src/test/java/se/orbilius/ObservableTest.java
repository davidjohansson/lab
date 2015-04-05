package se.orbilius;

import org.junit.Test;

import rx.Observable;
import rx.schedulers.Schedulers;


public class ObservableTest {

	@Test
	public void mapTarEnFunktionSomReturnerarVärden(){
		Observable.just(1,2,3,4)
		.map(integer -> plusOne(integer))
		.subscribe(integer -> System.out.println(integer));
	}
	
	@Test
	public void flatMapTarEnFunktionSomReturnerarEnNyObservable(){
		Observable.just(1,2,3,4)
		.flatMap(integer -> timesTenAsync(integer)
				.doOnNext(integer2 -> System.out.println("Det här är alltså också en observable")))
		.subscribe(integer -> System.out.println(integer));
	}
	
	@Test
	public void omManInteAngerEnTrådpoolKörsAlltSynkront(){
		System.out.println(getThreadName());

		Observable.just(1)
		.subscribe((integer)-> System.out.println(getThreadName()));
	}
	
	@Test
	public void manKanKöraSakerAsynkrontMedEnSchedulerare() throws Exception{
		System.out.println("Nu börjar jag på " + getThreadName());

		Observable.just(1)
		.subscribeOn(Schedulers.io())
		.subscribe((integer)-> printDelayed(3000, "Kör onNext på " + getThreadName()));

		System.out.println("Nu är jag klar på " + getThreadName());
		Thread.sleep(4000);
	}
	
	private Integer plusOne(Integer integer) {
		return integer + 1;
	}

	private Observable<Integer> timesTenAsync(Integer integer) {
		return Observable.just(integer * 10);
	}
	
	private void printDelayed(int millis, String message){
		
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(message);
	}
	
	private String getThreadName() {
		return "Tråd: " + Thread.currentThread().getName();
	}
}
