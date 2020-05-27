package cz.Bonds4All.controller;

import cz.Bonds4All.dto.BondDto;
import cz.Bonds4All.dto.BondHistory;
import cz.Bonds4All.dto.NewBond;
import cz.Bonds4All.exceptionHandling.CustomException;
import cz.Bonds4All.service.BondService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/bond")
public class BondController {

    @Autowired
    private BondService bondService;

    @PostMapping
    public BondDto create(@RequestBody NewBond newBond,
                          HttpServletRequest request) throws CustomException {
        return bondService.createBond(newBond, request.getRemoteAddr());
    }

    @PutMapping
    public BondDto changeTerm(@RequestParam(value = "bondId") Long bondId,
                              @RequestParam(value = "newTerm") int newTerm
    ) throws CustomException {
        return bondService.updateBondTerm(bondId, newTerm);
    }

    @GetMapping
    public BondHistory findBond(@RequestParam(value = "bondId") Long bondId) throws CustomException {
        return bondService.getBond(bondId);
    }
}

