package com.openpayd.repository;

import com.openpayd.repository.entity.Client;
import org.springframework.data.repository.CrudRepository;

public interface ClientRepo extends CrudRepository<Client, Long> {

}
