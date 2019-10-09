package com.demo.architect.domain;



import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

/**
 * Created by uyminhduc on 4/5/17.
 */

public abstract class BaseUseCase<T> {
    //check rxJava sequence and parallel
    protected UseCaseCallback useCaseCallback;
    protected RequestValues requestValues;

    private final CompositeDisposable disposables = new CompositeDisposable();

    protected BaseUseCase() {
    }

    /**
     * Builds an {@link Observable} which will be used when executing the current UseCase.
     */
    protected abstract Observable<T> buildUseCaseObservable();

    protected abstract DisposableObserver<T> buildUseCaseSubscriber();

    /**
     * Executes the current use case.
     *
     * @param useCaseSubscriber The guy who will be listen to the observable build
     *                          with {@link #buildUseCaseObservable()}.
     */
    @SuppressWarnings("unchecked")


    public void executeIO(RequestValues requestValues, UseCaseCallback useCaseCallback) {
        this.useCaseCallback = useCaseCallback;
        this.requestValues = requestValues;
        final Observable<T> observable = this.buildUseCaseObservable()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
        addDisposable(observable.subscribeWith(this.buildUseCaseSubscriber()));

    }


    /**
     * Unsubscribes from current
     */


    /**
     * Data passed to a request.
     */
    public interface RequestValues {
    }

    /**
     * Data received from a request.
     */
    public interface ResponseValues {
    }

    public interface ErrorValues {
    }

    public interface UseCaseCallback<R, E> {
        void onSuccess(R successResponse);

        void onError(E errorResponse);
    }

    /**
     * Dispose from current {@link CompositeDisposable}.
     */
    private void addDisposable(Disposable disposable) {
        disposables.add(disposable);
    }

    public void dispose() {
        if (!disposables.isDisposed()) {
            disposables.dispose();
        }
    }
}

