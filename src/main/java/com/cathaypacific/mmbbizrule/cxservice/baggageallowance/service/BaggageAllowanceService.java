package com.cathaypacific.mmbbizrule.cxservice.baggageallowance.service;

import com.cathaypacific.mbcommon.exception.BusinessBaseException;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.btu.BgAlBtuRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.request.od.BgAlOdRequestDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.btu.BgAlBtuResponseDTO;
import com.cathaypacific.mmbbizrule.cxservice.baggageallowance.model.response.od.BgAlOdResponseDTO;

public interface BaggageAllowanceService {

	BgAlBtuResponseDTO getBaggageAllowanceByBtu(BgAlBtuRequestDTO requestDTO) throws BusinessBaseException;

	BgAlOdResponseDTO getBaggageAllowanceByOd(BgAlOdRequestDTO requestDTO) throws BusinessBaseException;

}
