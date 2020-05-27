package cz.Bonds4All.repository;

import cz.Bonds4All.model.Bond;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

@Repository
@Transactional
public interface BondRepository extends JpaRepository<Bond, Long> {
}
