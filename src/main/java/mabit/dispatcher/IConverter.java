package mabit.dispatcher;

/**
 * Created by martin on 9/9/2016.
 */
public interface IConverter<T, U> {
    U convert(T t);
}
