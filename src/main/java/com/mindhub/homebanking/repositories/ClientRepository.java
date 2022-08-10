package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
//    @Query("select client from Client client where (?1 is null or client.enabled = ?1) and client.email = ?2")
    Client findByEmail(String email);


//    @Query("select client from Client client where client.enabled = ?1")
//    List<Client> findAll(boolean enabled);

}
