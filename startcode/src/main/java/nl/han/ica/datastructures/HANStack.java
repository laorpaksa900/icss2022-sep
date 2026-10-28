package nl.han.ica.datastructures;

import java.util.ArrayList;

public class HANStack<T> implements IHANStack<T> {

    private final ArrayList<T> elements = new ArrayList<>();

    @Override
    public void push(T value) {
        elements.add(value);
    }

    @Override
    public T pop() {
        return elements.remove(elements.size() - 1);
    }

    @Override
    public T peek() {
        return elements.get(elements.size() - 1);
    }

}
