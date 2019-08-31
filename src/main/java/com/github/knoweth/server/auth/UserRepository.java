package com.github.knoweth.server.auth;

import org.springframework.data.repository.CrudRepository;

/**
 * CRUD repository to store users.
 */
public interface UserRepository extends CrudRepository<User, Long> {
    User findByUsername(String username);
}
