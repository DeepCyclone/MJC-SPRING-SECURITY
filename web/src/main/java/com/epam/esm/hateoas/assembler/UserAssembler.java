package com.epam.esm.hateoas.assembler;

import com.epam.esm.controller.OrderController;
import com.epam.esm.controller.UserController;
import com.epam.esm.hateoas.model.UserModel;
import com.epam.esm.repository.model.User;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import java.util.LinkedList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class UserAssembler extends RepresentationModelAssemblerSupport<User,UserModel>{

    public UserAssembler() {
        super(UserController.class,UserModel.class);
    }

    @Override
    public UserModel toModel(User entity) {
        UserModel model = createModelWithId(entity.getId(), entity);
        populateFields(entity,model);
        generateLinks(entity,model);
        return model;
    }

    @Override
    public CollectionModel<UserModel> toCollectionModel(Iterable<? extends User> entities) {
        List<UserModel> models = new LinkedList<>();
        entities.forEach(entity->{
            UserModel model = toModel(entity);
            models.add(model);
        });
        return CollectionModel.of(models);
    }

    private void populateFields(User source,UserModel destination){
        destination.setId(source.getId());
        destination.setName(source.getName());
    }

    private void generateLinks(User source,UserModel destination){
        source.getOrders().forEach(order->destination.add(linkTo(methodOn(OrderController.class).getById(order.getId())).withRel("orders")));
        //TODO how to add notes with available operations;only text without refs due to same links; affordance? or another HAL features
    }
    
}
