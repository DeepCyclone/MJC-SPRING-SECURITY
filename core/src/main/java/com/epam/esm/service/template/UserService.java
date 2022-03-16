package com.epam.esm.service.template;

import java.util.List;

import com.epam.esm.repository.model.Tag;
import com.epam.esm.repository.model.User;

public interface UserService {
     List<User> getAll(long limit,long offset);
     User getById(long id);
     User getByName(String userName);
     Tag fetchMostUsedTagWithRichestOrders();
}
