package com.example.demo.util;

import org.apache.commons.lang3.StringUtils;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Component;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 密碼工具類
 */
@Slf4j
@Component
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PasswordUtil {

	/**
	 * 加密密碼
	 * 
	 * @param target 密碼
	 * @return 加密後的密文
	 */
	public static String encode(String target) {
		if (StringUtils.isNotBlank(target)) {
			return BCrypt.hashpw(target, BCrypt.gensalt());
		}
		log.warn("轉換失敗，回傳原值");
		return "";
	}

	/**
	 * 解密密碼
	 * 
	 * @param target          密碼
	 * @param encodedPassword 加密後的密碼
	 * @return boolean 兩者是否一致
	 */
	public static boolean checkPassword(String target, String encodedPassword) {
		return BCrypt.checkpw(target, encodedPassword);
	}

}
