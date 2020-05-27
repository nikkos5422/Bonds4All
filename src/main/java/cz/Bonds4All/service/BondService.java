package cz.Bonds4All.service;

import cz.Bonds4All.dto.BondDto;
import cz.Bonds4All.dto.BondHistory;
import cz.Bonds4All.dto.NewBond;
import cz.Bonds4All.exceptionHandling.CustomException;
import cz.Bonds4All.exceptionHandling.ResponseError;
import cz.Bonds4All.model.Bond;
import cz.Bonds4All.model.BondVersion;
import cz.Bonds4All.model.IpRequest;
import cz.Bonds4All.repository.BondRepository;
import cz.Bonds4All.repository.BondVersionRepository;
import cz.Bonds4All.repository.IpRequestRepository;
import cz.Bonds4All.repository.UserRepository;
import cz.Bonds4All.utils.BondConverter;
import cz.Bonds4All.utils.DateUtils;
import lombok.var;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Objects;
import java.util.Optional;

@Service
public class BondService {

    @Autowired
    private BondRepository bondRepository;

    @Autowired
    private BondVersionRepository bondVersionRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private IpRequestRepository ipRequestRepository;

    private static final int MAX_TERM = 100;
    private static final int MIN_TERM = 5;
    private static final int MAX_AMOUNT = 100000;
    private static final int MAX_SALE_PER_DAY = 5;
    private static final String TIME_EXCEPTION_TEXT = "Request amount more than 1000 after midnight.";
    private static final String IP_EXCEPTION_TEXT = "The same ip requests more than 5 times per day.";

    public BondService(BondRepository bondRepository, BondVersionRepository bondVersionRepository, UserRepository userRepository, IpRequestRepository ipRequestRepository) {
        this.bondRepository = bondRepository;
        this.bondVersionRepository = bondVersionRepository;
        this.userRepository = userRepository;
        this.ipRequestRepository = ipRequestRepository;
    }

    public BondDto createBond(final NewBond newBond, final String ipAddress) throws CustomException {

        validateInput(newBond, ipAddress);

        var bond = new Bond();

        bond.setUser(userRepository.findById(newBond.getUserId()).get());

        bond = bondRepository.save(bond);

        BondVersion version = createBondVersion(bond, newBond.getAmount(), newBond.getTerm(), BondVersion.OperationType.BOND_CREATED);

        version = bondVersionRepository.save(version);

        addNextIpAddressUsage(ipAddress);

        return convert(version);
    }

    public BondDto updateBondTerm(final Long bondId, final int newTerm) throws CustomException {

        if (newTerm < MIN_TERM || newTerm > MAX_TERM) {
            throw new CustomException(ResponseError.ErrorType.INVALID_TERM_VALUE, "Fail to update term caused by invalid term value.");
        }

        Bond bond = findBond(bondId);

        return adjustTerm(bond, newTerm);
    }

    public BondHistory getBond(final Long bondId) throws CustomException {

        Bond bond = findBond(bondId);

        return BondConverter.convertToBondHistory(bond);
    }


    public void validateInput(final NewBond bond, final String ipAddress) throws CustomException {

        var user = userRepository.findById(bond.getUserId());

        if (!user.isPresent()) {
            throw new CustomException(ResponseError.ErrorType.USER_NOT_FOUND, "user with current id does not exist.");
        }

        if (Objects.isNull(bond.getTerm()) || bond.getTerm() < MIN_TERM || bond.getTerm() > MAX_TERM) {
            throw new CustomException(ResponseError.ErrorType.INVALID_TERM_VALUE, "There is invalid value for bond term.");
        }

        if (Objects.isNull(bond.getAmount()) || bond.getAmount() <= 0 || bond.getAmount() > MAX_AMOUNT) {
            throw new CustomException(ResponseError.ErrorType.INVALID_AMOUNT_VALUE, "There is invalid value for bond amount.");
        }

        if (DateUtils.isHourInAfterMidnightInterval() && bond.getAmount() > 1000) {
            throw new CustomException(ResponseError.ErrorType.ILLEGAL_MIDNIGHT_TIME_OPERATION, TIME_EXCEPTION_TEXT);

        }

        if (checkMaxNumberSoldBondsForIP(ipAddress)) {
            throw new CustomException(ResponseError.ErrorType.EXCEEDED_MAX_SOLD_PER_DAY, IP_EXCEPTION_TEXT);
        }
    }

    private BondDto convert(BondVersion version) {
        var dto = new BondDto();
        dto.setAmount(version.getAmount());
        dto.setTerm(version.getTerm());
        dto.setId(version.getBond().getId());

        return dto;
    }


    private void addNextIpAddressUsage(final String ipAddress) {

        var ipRequest = new IpRequest();
        ipRequest.setIpAddress(ipAddress);
        ipRequest.setOperationDate(DateUtils.getSimpleDate(new Date()));

        ipRequestRepository.save(ipRequest);
    }

    private boolean checkMaxNumberSoldBondsForIP(final String ipAddress) {

        var dayStart = DateUtils.getStartOfDay();
        var dayEnd = DateUtils.getEndOfDay();
        var count = ipRequestRepository.findAllRequestsForIPForToday(ipAddress, dayStart, dayEnd);

        return count >= MAX_SALE_PER_DAY;
    }

    private BondVersion createBondVersion(final Bond bond, final int amount, final int term, final BondVersion.OperationType operation) {

        var version = new BondVersion();
        version.setBond(bond);
        version.setAmount(amount);
        version.setTerm(term);
        version.setOperationDate(DateUtils.getSimpleDate(new Date()));
        version.setOperationType(operation.name());

        return version;
    }

    private BondDto adjustTerm(final Bond bond, final int newTerm) {

        var versions = bond.getVersions();
        var actualVersion = versions.get(versions.size() - 1);
        var originalTerm = actualVersion.getTerm();

        if (originalTerm == newTerm) {
            var result = new BondDto();
            result.setId(bond.getId());
            result.setTerm(newTerm);
            result.setAmount(actualVersion.getAmount());
            return result;
        }

        if (originalTerm > newTerm) {
            var newVersion = createBondVersion(bond, actualVersion.getAmount(), newTerm, BondVersion.OperationType.TERM_REDUCED);
            return convert(bondVersionRepository.save(newVersion));
        }

        int newAmount = (int) (actualVersion.getAmount() * 0.9);

        var newVersion = createBondVersion(bond, newAmount, newTerm, BondVersion.OperationType.TERM_EXTENDED);
        return convert(bondVersionRepository.save(newVersion));
    }

    private Bond findBond(final Long bondId) throws CustomException {

        Optional<Bond> bond = bondRepository.findById(bondId);

        if (!bond.isPresent()) {
            throw new CustomException(ResponseError.ErrorType.BOND_NOT_FOUND, "bond with current id does not exist.");
        }
        return bond.get();
    }
}