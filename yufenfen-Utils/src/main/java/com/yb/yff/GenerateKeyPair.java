package com.yb.yff;

import java.io.FileOutputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.Base64;

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
 * @Class: GenerateKeyPair
 * @CreatedOn 2024/10/6.
 * @Email: yangboyff@gmail.com
 * @Description: 公私要工具
 */

public class GenerateKeyPair {
    public static void main(String[] args) {
        try {
            // 创建 KeyPairGenerator 对象
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048); // 设置密钥大小

            // 生成公私钥对
            KeyPair keyPair = keyGen.generateKeyPair();
            PublicKey publicKey = keyPair.getPublic();
            PrivateKey privateKey = keyPair.getPrivate();

            // 保存公钥到文件
            try (FileOutputStream fos = new FileOutputStream("public.key")) {
                fos.write(publicKey.getEncoded());
            }

            // 保存私钥到文件
            try (FileOutputStream fos = new FileOutputStream("private.key")) {
                fos.write(privateKey.getEncoded());
            }

            // 输出公钥和私钥的 Base64 编码
            System.out.println("Public Key:");
            System.out.println(Base64.getEncoder().encodeToString(publicKey.getEncoded()));
            System.out.println("Private Key:");
            System.out.println(Base64.getEncoder().encodeToString(privateKey.getEncoded()));

        } catch (NoSuchAlgorithmException | IOException e) {
            e.printStackTrace();
        }
    }
}

