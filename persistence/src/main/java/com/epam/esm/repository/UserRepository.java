package com.epam.esm.repository;

import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;

import java.util.List;
import java.util.Optional;

/*
 * A specification how to interact with datasource which contains users
 * @author Flexus
 * */
public interface UserRepository extends GenericRepository<User>,Identifiable<User>,Nameable<User>{
    List<Order> fetchAssociatedOrders(long userId);
    Optional<Tag> fetchMostUsedTagWithRichestOrders();
}
