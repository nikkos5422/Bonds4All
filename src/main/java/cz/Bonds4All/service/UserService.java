package cz.Bonds4All.service;

import cz.Bonds4All.dto.BondHistory;
import cz.Bonds4All.dto.UserHistory;
import cz.Bonds4All.dto.newUser;
import cz.Bonds4All.exceptionHandling.CustomException;
import cz.Bonds4All.exceptionHandling.ResponseError;
import cz.Bonds4All.model.Bond;
import cz.Bonds4All.model.User;
import cz.Bonds4All.repository.UserRepository;
import cz.Bonds4All.utils.BondConverter;
import lombok.var;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.LinkedList;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long createUser(newUser newUser) throws CustomException {

        validate(newUser);

        var user = new User();

        user.setUsername(newUser.getUsername());
        user.setEmail(newUser.getEmail());

        return userRepository.save(user).getId();
    }

    public UserHistory getUserHistory(Long userId) throws CustomException {

        var user = userRepository.findById(userId);

        if (!user.isPresent()) {
            throw new CustomException(ResponseError.ErrorType.USER_NOT_FOUND, "User with current id does not exist.");
        }

        if (CollectionUtils.isEmpty(user.get().getBonds())) {
            return new UserHistory();
        }

        var bondsHistory = new LinkedList<BondHistory>();
        var bonds = user.get().getBonds();
        var userHistory = new UserHistory();

        for (Bond b : bonds) {
            bondsHistory.add(BondConverter.convertToBondHistory(b));
        }

        userHistory.setBonds(bondsHistory);
        userHistory.setUserId(userId);

        return userHistory;
    }

    private void validate(newUser user) throws CustomException {

        if (StringUtils.isEmpty(user.getEmail())) {
            throw new CustomException(ResponseError.ErrorType.MISSING_MANDATORY_FIELD, "missing mandatory parameter email.");
        }

        if (StringUtils.isEmpty(user.getUsername())) {
            throw new CustomException(ResponseError.ErrorType.MISSING_MANDATORY_FIELD, "missing mandatory parameter username.");
        }
    }
}
