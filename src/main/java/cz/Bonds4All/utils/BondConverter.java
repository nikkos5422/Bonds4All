package cz.Bonds4All.utils;

import cz.Bonds4All.dto.BondHistory;
import cz.Bonds4All.dto.BondVersion;
import cz.Bonds4All.model.Bond;
import lombok.var;

import java.util.List;
import java.util.stream.Collectors;

public class BondConverter {

    public static BondHistory convertToBondHistory(Bond bond) {

        var bondHistory = new BondHistory();

        bondHistory.setId(bond.getId());
        bondHistory.setUser(bond.getUser().getId());
        bondHistory.setVersions(convertVersions(bond.getVersions()));

        return bondHistory;
    }

    private static List<BondVersion> convertVersions(List<cz.Bonds4All.model.BondVersion> versions) {

        return versions.stream()
                .map(BondConverter::convertVersion
                ).collect(Collectors.toList());
    }

    private static cz.Bonds4All.dto.BondVersion convertVersion(cz.Bonds4All.model.BondVersion version) {
        var bondVersion = new cz.Bonds4All.dto.BondVersion();

        bondVersion.setOperationDate(version.getOperationDate());
        bondVersion.setOperationType(version.getOperationType());
        bondVersion.setAmount(version.getAmount());
        bondVersion.setTerm(version.getTerm());

        return bondVersion;
    }
}
