package com.epam.esm.hateoas;

public interface LinksBuilder<T> {
    void buildLinks(T response);
}
