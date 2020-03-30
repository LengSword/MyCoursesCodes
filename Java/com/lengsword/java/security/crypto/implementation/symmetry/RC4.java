package com.lengsword.java.security.crypto.implementation.symmetry;

import com.lengsword.java.security.crypto.util.StringUtils;

public class RC4 {
    public static void main(String[] args) {
        RC4 rc4 = new RC4();
        String plaintext = "helloRC4";
        String key = "其实RC4算法很简单！";
        System.out.println("Plaintext: " + plaintext);
        System.out.println("Key: " + key);
        String cipher = rc4.crypt(plaintext, key);
        System.out.println("Cipher: " + cipher);
        System.out.println("Hex(Cipher): " + StringUtils.plainToHex(cipher));
        System.out.println("crypt(Cipher): " + rc4.crypt(cipher, key));
    }

    public String crypt(String plaintext, String key) {
        return crypt(plaintext.toCharArray(), key.toCharArray());
    }

    private String crypt(char[] plaintext, char[] key) {
        // S盒 | 初始: 0,1,2,...,255
        char[] sBox = new char[256];

        ksa(sBox, key);
        char[] keyStream = prga(sBox, plaintext.length);
        StringBuilder ciphertext = new StringBuilder();
        for (int i = 0; i < plaintext.length; i++) {
            int result = plaintext[i] ^ keyStream[i];
            ciphertext.append((char) result);
        }
        return ciphertext.toString();
    }

    /**
     * Key-scheduling algorithm(KSA)
     * 利用Key生成S盒
     *
     * @param s S盒
     */
    private void ksa(char[] s, char[] key) {
        // 临时向量T | 用于临时存储key的轮转数据
        char[] t = new char[256];
        // 初始化S盒和临时向量T
        for (int i = 0; i < 256; i++) {
            s[i] = (char) i;
            t[i] = key[i % key.length];
        }
        // 打乱S盒
        int newIndex = 0;
        for (int i = 0; i < 256; i++) {
            newIndex = (newIndex + s[i] + t[i]) % 256;
            swap(s, i, newIndex);
        }
    }

    /**
     * Pseudo-random generation algorithm(PRGA)
     * 生成密钥流
     *
     * @param s               S盒
     * @param plaintextLength 明文长度
     *
     * @return 密钥流
     */
    private char[] prga(char[] s, int plaintextLength) {
        int i = 0;
        int j = 0;
        char[] keyStream = new char[plaintextLength];
        for (int k = 0; k < plaintextLength; k++) {
            i = (i + 1) % 256;
            j = (j + s[i]) % 256;
            swap(s, i, j);
            keyStream[k] = s[(s[i] + s[j]) % 256];
        }
        return keyStream;
    }

    private void swap(char[] s, int a, int b) {
        char temp = s[a];
        s[a] = s[b];
        s[b] = temp;
    }
}
