package br.com.danielschiavo.shop.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.danielschiavo.shop.models.client.Client;

public interface ClientRepository extends JpaRepository<Client, Long>{

}
