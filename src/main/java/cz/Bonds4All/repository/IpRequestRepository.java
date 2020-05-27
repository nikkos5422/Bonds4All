package cz.Bonds4All.repository;

import cz.Bonds4All.model.IpRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.List;

@Transactional
@Repository
public interface IpRequestRepository extends JpaRepository<IpRequest, Long> {

    @Query("select count(a) from IpRequest a where a.ipAddress = :ipAddress and a.operationDate between :dateFrom and :dateTo")
    int findAllRequestsForIPForToday(
            @Param("ipAddress") String ipAddress,
            @Param("dateFrom") String dateFrom,
            @Param("dateTo") String dateTo
    );
}
