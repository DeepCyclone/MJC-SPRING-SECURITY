package com.epam.esm.repository.template;

import java.util.List;
import java.util.Optional;


import com.epam.esm.repository.model.Order;
import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;

public interface UserRepository extends GenericRepository<User>,Identifiable<User>,Nameable<User>{
    List<Order> fetchAssociatedOrders(long userId);
    Optional<Tag> fetchMostUsedTagWithRichestOrders();
    boolean linkAssociatedOrder(long orderId,long userId);
}
