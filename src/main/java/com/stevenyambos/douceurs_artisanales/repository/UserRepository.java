package com.stevenyambos.douceurs_artisanales.repository;

import com.stevenyambos.douceurs_artisanales.model.UserModel;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<UserModel, String> {
    Boolean existsByEmail(String email);
    UserModel findByEmail(String email);
    Boolean deleteUserById(String id);
}
