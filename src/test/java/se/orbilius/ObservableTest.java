package se.orbilius;

import org.junit.Test;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Subscriber;
import rx.schedulers.Schedulers;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertTrue;

public class ObservableTest {

	@Test
	public void mapTarEnFunktionSomReturnerarVärden() {
		Observable.just(1, 2, 3, 4).map(integer -> integer + 1)
				.subscribe(integer -> System.out.println(integer));
	}

	@Test
	public void flatMapTarEnFunktionSomReturnerarEnNyObservable() {
		Observable
				.just(1, 2, 3, 4)
				.flatMap(
						integer -> timesTenAsync(integer)
								.doOnNext(
										integer2 -> System.out
												.println("Det här är alltså också en observable")))
				.subscribe(integer -> System.out.println(integer));
	}

	private Observable<Integer> timesTenAsync(Integer integer) {
		return Observable.just(integer * 10);
	}


	@Test
	public void omManInteAngerEnTrådpoolKörsAlltSynkront() {
		System.out.println(getThreadName());

		Observable.just(1).subscribe(
				(integer) -> System.out.println(getThreadName()));
	}

	@Test
	public void manKanKöraSakerAsynkrontMedEnSchedulerare() throws Exception {
		System.out.println("Nu börjar jag på " + getThreadName());

		Observable
				.just(1)
				.subscribeOn(Schedulers.io())
				.subscribe(
						(integer) -> printDelayed(3000, "Kör onNext på "
								+ getThreadName()));

		System.out.println("Nu är jag klar på " + getThreadName());
		Thread.sleep(4000);
	}

	@Test
	public void ettFelAvbryterObservablen() throws Exception {

		Observable<String> errorObservable = Observable
				.create(new OnSubscribe<String>() {

					@Override
					public void call(Subscriber<? super String> t1) {
						t1.onNext("1");
						t1.onNext("2");
						t1.onError(new RuntimeException(
								"Det här gick inte så bra"));
						t1.onNext("3");
					}
				});

		errorObservable.subscribe(
				string -> System.out.println(string),
				throwable -> System.out.println("Kastade: "
						+ throwable.getMessage()));
	}

	@Test
	public void manKanFångaFelSomKastasMenObservablenKommerÄndåStanna() throws Exception {

		Observable<String> errorObservable = Observable
				.create(new OnSubscribe<String>() {

					@Override
					public void call(Subscriber<? super String> t1) {
						t1.onNext("1");
						t1.onNext("2");
						t1.onError(new RuntimeException(
								"Det här gick inte så bra"));
						t1.onNext("3");
					}
				});

		errorObservable
		.onErrorReturn(throwable -> {System.out.println("Fick ett exception, no problem"); return "defaultvärde";})
		.subscribe(
				string -> System.out.println(string),
				throwable -> System.out.println("Kastade: "
						+ throwable.getMessage()));
	}

	@Test
	public void manKanFångaIgnoreraFelMenObservablenStoppas() throws Exception {

		Observable<String> errorObservable = Observable
				.create(new OnSubscribe<String>() {

					@Override
					public void call(Subscriber<? super String> t1) {
						t1.onNext("1");
						t1.onNext("2");
						t1.onError(new RuntimeException(
								"Det här gick inte så bra"));
						t1.onNext("3");
					}
				});

		errorObservable
				.onErrorResumeNext(Observable.<String>empty())
				.subscribe(
						string -> System.out.println(string),
						throwable -> System.out.println("Kastade: "
								+ throwable.getMessage()));
	}

	@Test
	public void medMergeWithDelayKanManIgnorerarFel() throws Exception {

		Observable<String> noErrorObservable = Observable.create(new OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {
				subscriber.onNext("1");
			}
		});

		Observable<String> errorObservable = Observable.create(new OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {
				subscriber.onError(new RuntimeException("Det blev fel"));
			}
		});

		Observable<String> noErrorObservable2 = Observable.create(new OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {
				subscriber.onNext("2");
			}
		});
		Observable.mergeDelayError(noErrorObservable, errorObservable, noErrorObservable2)
				.subscribe(
						string -> System.out.println("Merged: " + string),
						throwable -> System.out.println("Fick ett fel: " + throwable.getMessage())
				);
	}
/*
	@Test
	public void tvåSubscribesPåverkarInteVarandra() throws InterruptedException {
		List<Integer> firstEntries = new ArrayList<>();
		List<Integer> secondEntries = new ArrayList<>();

		Observable<Integer> intObservable = Observable.create(new OnSubscribe<Integer>() {
			private int counter = 0;

			@Override
			public void call(Subscriber<? super Integer> subscriber) {
				subscriber.onNext(counter++);
			}
		}).subscribeOn(Schedulers.io());

		intObservable.subscribe(new Subscriber<Integer>() {
			@Override
			public void onCompleted() {
				
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(Integer integer) {
				firstEntries.add(integer);
			}
		})
		);

		intObservable.subscribe(
				intg -> secondEntries.add(intg)
		);

		Thread.sleep(1000);
		assertTrue(firstEntries.get(0) == 0 && firstEntries.size() == 1);
		assertTrue(secondEntries.get(0) == 1 && secondEntries.size() == 1);
	}
*/
	@Test
	public void omManCacharFårTvåSubscribesSammaItemsMenDerasEventhanterareÄrSeparata() throws InterruptedException {
		List<Integer> firstEntries = new ArrayList<>();
		List<Integer> secondEntries = new ArrayList<>();

		Observable<Integer> intObservable =
				Observable
						.create(new OnSubscribe<Integer>() {
							private int counter = 0;

							@Override
							public void call(Subscriber<? super Integer> subscriber) {
								subscriber.onNext(counter++);
							}
						})
						.cache()
						.subscribeOn(Schedulers.io());

		intObservable.subscribe(
				intg -> firstEntries.add(intg)
		);

		intObservable.subscribe(
				intg -> secondEntries.add(intg)
		);

		Thread.sleep(2000);
		assertTrue(firstEntries.get(0) == 0 && firstEntries.size() == 1);
		assertTrue(secondEntries.get(0) == 0 && secondEntries.size() == 1);
	}
	
	
	@Test
	public void subscribearToBlocking() {

		Observable<String> just = Observable.just("1");
		just.subscribe(new Subscriber<String>() {
			@Override
			public void onCompleted() {
				System.out.println("DONE!");
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(String s) {

			}
		});

		Observable<String> just2 = Observable.just("2");
		just2
				.doOnCompleted(() -> System.out.println("DONE 2"))
				//.doOn
				.toBlocking().single();

	}

	@Test
	public void hurFunkarDoOnError(){

		Observable<String> obs = Observable.create(new OnSubscribe<String>() {
			@Override
			public void call(Subscriber<? super String> subscriber) {
				subscriber.onNext("Sträng");
				subscriber.onCompleted();


			}
		});

		obs.subscribe(new Subscriber<String>() {
			@Override
			public void onCompleted() {
				
			}

			@Override
			public void onError(Throwable e) {

			}

			@Override
			public void onNext(String s) {

				System.out.println("JAG FIXK " + s);
			}
		});

	}

	private void printDelayed(int millis, String message) {

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
