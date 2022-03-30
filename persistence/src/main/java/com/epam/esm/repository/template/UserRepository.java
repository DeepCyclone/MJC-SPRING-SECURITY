package com.epam.esm.repository.template;

import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;

import java.util.List;
import java.util.Optional;


public interface UserRepository extends GenericRepository<User>,Identifiable<User>,Nameable<User>{
    List<Order> fetchAssociatedOrders(long userId);
    Optional<Tag> fetchMostUsedTagWithRichestOrders();
}
