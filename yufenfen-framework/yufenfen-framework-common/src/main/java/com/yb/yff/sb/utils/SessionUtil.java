package com.yb.yff.sb.utils;


import com.yb.yff.sb.data.dto.SessionDTO;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * Copyright (c) 2024 to 2045  YangBo.
 * All rights reserved.
 * <p>
 * This software is the confidential and proprietary information of
 * YangBo. You shall not disclose such Confidential Information and
 * shall use it only in accordance with the terms of the license
 * agreement you entered into with YangBo.
 *
 * @author : YangBo
 * @Project: SpringbootDemo
 * @Class: SessionUtil
 * @CreatedOn 2024/10/23.
 * @Email: yangboyff@gmail.com
 * @Description: session工具
 */
public class SessionUtil {
	private static long validTime = 30 * 24 * 3600;
	private static String sKey = "abc1234de56789ef";

	public static String newSessionStr(int id) {
		SessionDTO session = new SessionDTO();
		session.setMTime(LocalDateTime.now());
		session.setId(id);

		try {
			return session2Str(session);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public static String session2Str(SessionDTO session) throws Exception {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String timeStr = session.getMTime().format(formatter);
		String str = String.format("%d|%s", session.getId(), timeStr);

		String sessionStr = AesEncryptor.aesCBCEncrypt(str.getBytes(), sKey);

		return sessionStr;
	}

	/**
	 * 将 res 解码 session
	 *
	 * @param res
	 * @param target
	 * @throws Exception
	 */
	public static void str2Session(String res, SessionDTO target) throws Exception {
		if (res == null || res.isEmpty()) {
			throw new IllegalArgumentException("sessionStr is empty");
		}

		byte[] data = AesEncryptor.aesCBCDecrypt(res, sKey).getBytes();

		String[] arr = new String(data, StandardCharsets.UTF_8).split("\\|");
		if (arr.length != 2) {
			throw new IllegalArgumentException("sessionStr format error");
		}

		int id = Integer.parseInt(arr[0]);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		LocalDateTime mTime = LocalDateTime.parse(arr[1], formatter);

		target.setId(id);
		target.setMTime(mTime);
	}

	public static boolean isValid(SessionDTO session) {
		LocalDateTime now = LocalDateTime.now();

		long diff = ChronoUnit.SECONDS.between(session.getMTime(), now);

		return diff < validTime;
	}

	public static boolean isValid(String sessionStr) {
		try {
			SessionDTO session = new SessionDTO();
			str2Session(sessionStr, session);

			LocalDateTime now = LocalDateTime.now();

			long diff = ChronoUnit.SECONDS.between(session.getMTime(), now);
			return diff < validTime;

		} catch (Exception e) {
			e.printStackTrace();

			return false;
		}
	}

	public static void main(String[] args) {
		try {
			// 示例使用
			SessionDTO session = new SessionDTO();
			session.setMTime(LocalDateTime.now());
			session.setId(1);
			System.out.println("New Session: " + session.getId() + ", " + session.getMTime());

			String encodedSession = session2Str(session);
			System.out.println("encoded Session: " + encodedSession);

			SessionDTO parsedSession = new SessionDTO();
			str2Session(encodedSession, parsedSession);
			System.out.println("desCoded Session: " + parsedSession.getId() + ", " + parsedSession.getMTime());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
