package com.epam.esm.repository;

import java.util.Optional;

import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;

public interface UserCustomRepository extends GenericRepository<User> {
    Optional<Tag> fetchMostUsedTagWithRichestOrders();
    boolean deleteById(long id);
    User save(User object);
}
