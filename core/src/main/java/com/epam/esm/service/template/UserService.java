package com.epam.esm.service.template;

import java.util.List;

import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;

public interface UserService {
     List<User> getAll(int limit,int offset);
     User getById(long id);
     User getByName(String userName);
     Tag fetchMostUsedTagWithRichestOrders();
}
