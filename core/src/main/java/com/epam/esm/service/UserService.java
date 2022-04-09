package com.epam.esm.service;

import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;

import java.util.List;

public interface UserService {
     List<User> getAll(int limit,int offset);
     User getById(long id);
     User getByName(String userName);
     Tag fetchMostUsedTagWithRichestOrders();
}
