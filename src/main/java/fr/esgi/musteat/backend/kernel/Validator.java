package fr.esgi.musteat.backend.kernel;

public interface Validator<T> {
    void validate(T object);
}
