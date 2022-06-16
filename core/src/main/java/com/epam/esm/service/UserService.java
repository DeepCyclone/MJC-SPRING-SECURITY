package com.epam.esm.service;


import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;

import org.springframework.data.domain.Page;


public interface UserService {
     Page<User> getAll(int limit,int offset);
     User getById(long id);
     User getByName(String userName);
     User save(User user);
     Tag fetchMostUsedTagWithRichestOrders();
}
