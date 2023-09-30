package com.maperz.invoiceService.service;

import com.maperz.invoiceService.dto.UserDTO;

public interface UserInfoService {

    UserDTO getUserInfoByOrderNumber(String orderNumber);

}
