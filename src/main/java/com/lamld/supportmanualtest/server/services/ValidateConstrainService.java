package com.lamld.supportmanualtest.server.services;

import com.lamld.supportmanualtest.app.dto.validateConstrain.ValidateConstrainDto;
import com.lamld.supportmanualtest.app.response.validateConstrain.ValidateConstrainResponse;
import com.lamld.supportmanualtest.server.data.auth.AccountInfo;
import com.lamld.supportmanualtest.server.data.type.StatusEnum;
import com.lamld.supportmanualtest.server.entities.ValidateConstrain;
import com.lamld.supportmanualtest.server.exception.BadRequestException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ValidateConstrainService extends BaseService {

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
  public ValidateConstrainResponse createValidateConstrain(AccountInfo accountInfo, ValidateConstrainDto validateConstrainDto) {
    ValidateConstrain validateConstrain = validateConstrainStorage.findByConstrainName(validateConstrainDto.constrainName())
        .orElse(modelMapper.toValidateConstrain(validateConstrainDto));
    validateConstrain = validateConstrainStorage.save(validateConstrain);
    return modelMapper.toValidateConstrainResponse(validateConstrain);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
  public ValidateConstrainResponse updateValidateConstrain(AccountInfo accountInfo, Integer id, ValidateConstrainDto validateConstrainDto) {
    ValidateConstrain validateConstrain = validateConstrainStorage.findById(id)
        .orElseThrow(() -> new BadRequestException("Validate constrain not found"));
    modelMapper.mapToValidateConstrain(validateConstrain, validateConstrainDto);
    validateConstrainStorage.save(validateConstrain);
    return modelMapper.toValidateConstrainResponse(validateConstrain);
  }

  @Transactional(isolation = Isolation.SERIALIZABLE, propagation = Propagation.REQUIRES_NEW)
  public void deleteValidateConstrain(AccountInfo accountInfo, Integer id) {
    ValidateConstrain validateConstrain = validateConstrainStorage.findById(id)
        .orElseThrow(() -> new BadRequestException("Validate constrain not found"));
    validateConstrainStorage.delete(validateConstrain);
  }

  @Transactional(readOnly = true)
  public ValidateConstrainResponse findValidateConstrainById(AccountInfo accountInfo, Integer id) {
    return validateConstrainStorage.findById(id)
        .map(validateConstrain -> modelMapper.toValidateConstrainResponse(validateConstrain))
        .orElseThrow(() -> new BadRequestException("Validate constrain not found"));
  }

  @Transactional(readOnly = true)
  public Page<ValidateConstrainResponse> findValidateConstrain(AccountInfo accountInfo, String constrainName, String regexValue, StatusEnum status, Pageable pageable) {
    Page<ValidateConstrain> validateConstrainResponses = validateConstrainStorage.findPage(constrainName, regexValue, status, pageable);
    return modelMapper.toPageValidateConstrainResponse(validateConstrainResponses);
  }

  @Transactional(readOnly = true)
  public List<ValidateConstrain> findAll() {
    return validateConstrainStorage.findAll();
  }

  public List<ValidateConstrain> findByInIn(List<Integer> allValidateConstrainIds) {
    return validateConstrainStorage.findByIdIn(allValidateConstrainIds);
  }
}
