package com.yb.yff.game.controller;

import com.yb.yff.game.service.IAccountService;
import com.yb.yff.sb.constant.ResponseCode;
import com.yb.yff.sb.data.dto.ResponseDTO;
import com.yb.yff.sb.data.dto.account.LoginDTO;
import com.yb.yff.sb.data.dto.account.RegisterDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.yb.yff.sb.constant.NetResponseCodeConstants.SUCCESS;

/**
 * <p>
 * 用户信息表 前端控制器
 * </p>
 *
 * @author yangbo
 * @since 2024-10-07
 */
@RestController
@RequestMapping("/account")
@Slf4j
public class AccountController {
	@Autowired
	IAccountService accountService;

	@PermitAll
	@GetMapping("/publickey")
	public ResponseDTO<String> getPublicKey(){
		String publicKey = "";
		return new ResponseDTO(SUCCESS, publicKey);
	}

	@PermitAll
	@GetMapping("/check/token")
	public ResponseDTO validateToken(String token){
		ResponseCode responseCode = accountService.doValidateToken(token);
		return new ResponseDTO(responseCode, null);
	}

	@PermitAll
	@GetMapping("/login")
	public ResponseDTO Login(@ModelAttribute @Valid LoginDTO loginDTO) {
		ResponseCode responseCode = accountService.doLogin(loginDTO);
		log.info("====== Login responseCode:{}", responseCode);
		return new ResponseDTO(responseCode, null);
	}

//	@PermitAll
	@GetMapping("/register")
	public ResponseDTO Register(@ModelAttribute @Valid RegisterDTO registerDTO) {
		ResponseCode responseCode = accountService.doRegiter(registerDTO);
		log.info("====== Register responseCode:{}", responseCode);
		return new ResponseDTO(responseCode, null);
	}


}
