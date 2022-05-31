package com.mindhub.homebanking.repositories;

import com.mindhub.homebanking.models.Loan;
import com.mindhub.homebanking.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin
@RepositoryRestResource
public interface LoanRepository extends JpaRepository<Loan, Long>  {
}
