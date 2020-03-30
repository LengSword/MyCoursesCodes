package com.lengsword.java.security.crypto.implementation.symmetry;

import com.lengsword.java.security.crypto.constant.CryptoConstants;
import com.lengsword.java.security.crypto.constant.DESConstants;
import com.lengsword.java.security.crypto.util.StringUtils;

/**
 * DES
 *
 * @author LengSword
 */
public class DES {
    private String workingMode;
    private boolean isDebug;

    public DES() {
        this("ECB", true);
    }

    public DES(String workingMode) {
        this.workingMode = workingMode;
        this.isDebug = true;
    }

    public DES(String workingMode, boolean isDebug) {
        this.workingMode = workingMode;
        this.isDebug = isDebug;
    }

    public static void main(String[] args) {
        DES des = new DES();
        String cipher = des.encryptBlock("01234567", "12345678");
        System.out.println(cipher);
        System.out.println(des.decryptBlock(cipher, "12345678"));
    }

    private String cryptBlock(String message, String key, String type) {
        // 1. 将明文信息转换为二进制串
        String binaryPlaintext = StringUtils.plainBlockToBinary(message);
        // 2. 通过初始置换表IP置换明文二进制串
        String substitutedPlaintext = StringUtils.reindexBinary(binaryPlaintext, DESConstants.IP_TABLE).toString();
        if (isDebug) {
            StringUtils.printFormattedBinaryData("binaryPlaintext", binaryPlaintext);
            StringUtils.printFormattedBinaryData("substitutedPlaintext", substitutedPlaintext);
        }
        // 3. 将置换后的二进制串分成左右两块leftBlock和rightBlock
        StringBuilder leftBlock = new StringBuilder();
        StringBuilder rightBlock = new StringBuilder();
        leftBlock.append(substitutedPlaintext, 0, 32);
        rightBlock.append(substitutedPlaintext, 32, 64);
        // 4. 生成子密钥
        StringBuilder[] subKeys = generateSubKeys(key);
        // 加密/解密处理
        if (CryptoConstants.DECRYPT_TYPE.equals(type)) {
            subKeys = inverseSubKeys(subKeys);
        }
        // 5. 进行16轮迭代
        int roundsCount = 16;
        for (int i = 0; i < roundsCount; i++) {
            // 5.1. 核心加密函数f, 用以得到下一轮的右块
            StringBuilder nextRightBlock = coreEncrypt(rightBlock, subKeys[i]);
            // 5.2. 置换后的下一轮的右块与最开始的左块进行异或
            StringBuilder xorResult = StringUtils.xorBinary(leftBlock, nextRightBlock, 32);
            // 5.3. 根据L_{i+1}=R_{i}, 由当前轮的右块得到下一轮左块
            leftBlock.replace(0, 32, rightBlock.toString());
            // 5.4. 异或结果为下一轮的右块
            rightBlock = xorResult;
            if (isDebug) {
                StringUtils.printFormattedBinaryDataWithIndex(i + 1, "leftBlock", leftBlock);
                StringUtils.printFormattedBinaryDataWithIndex(i + 1, "rightBlock", rightBlock);
            }
        }
        // 6. 合并leftBlock和rightBlock (注意: 要反过来连接)
        String ciphertextBlock = rightBlock.toString() + leftBlock.toString();
        // 7. 通过逆初始置换表IPR置换cipherBlock
        StringBuilder tempCiphertextBlock = StringUtils.reindexBinary(ciphertextBlock, DESConstants.INVERSE_IP_TABLE);
        if (isDebug) {
            StringUtils.printFormattedBinaryData("ciphertextBlock", tempCiphertextBlock);
        }
        // 8. 将二进制串转为字符串
        ciphertextBlock = StringUtils.binToHexCipher(tempCiphertextBlock, 8);
        if (CryptoConstants.DECRYPT_TYPE.equals(type)) {
            ciphertextBlock = StringUtils.hexToPlainCipher(ciphertextBlock, 8);
        }
        if (isDebug) {
            StringUtils.printFormattedHexData("ciphertextBlockHexString", ciphertextBlock);
        }
        return ciphertextBlock;
    }

    private StringBuilder coreEncrypt(StringBuilder rightBlock, StringBuilder subKeys) {
        // 1. 通过扩展置换表E扩展置换当前轮的右块
        StringBuilder lastRightBlock = StringUtils.reindexBinary(rightBlock.toString(), DESConstants.E_TABLE);
        // 2. 扩展后的当前轮的右块与子密钥进行异或
        lastRightBlock = StringUtils.xorBinary(lastRightBlock, subKeys, 48);
        // 3. 通过S-盒置换表S进行S盒压缩置换(48bits -> 32bits)
        StringBuilder output = new StringBuilder();
        int sBoxGroupCount = DESConstants.S_BOX.length;
        for (int boxIndex = 0; boxIndex < sBoxGroupCount; boxIndex++) {
            // 3.1. 每组6位二进制串
            String sBinaryBlock = lastRightBlock.substring(boxIndex * 6, (boxIndex + 1) * 6);
            // 3.2. 取第1位和第6位作为S盒行坐标(x=[0...3])
            int rowIndex = StringUtils.binaryToDecimal(Character.toString(sBinaryBlock.charAt(0)) + sBinaryBlock.charAt(5));
            // 3.3. 取第2位到第5位作为S盒列坐标(y=[0...15])
            int columnIndex = StringUtils.binaryToDecimal(sBinaryBlock.substring(1, 5));
            int currentNumber = DESConstants.S_BOX[boxIndex][rowIndex][columnIndex];
            // 3.4. 将currentNumber转换为二进制串并填充, 并将结果加到当前轮的右块后面
            output.append(StringUtils.getWholeBinary(currentNumber, 4));
        }
        // 4. 通过P-盒置换表P置换当前轮的右块
        return StringUtils.reindexBinary(output.toString(), DESConstants.P_TABLE);
    }

    public String encryptBlock(String plaintext, String key) {
        return cryptBlock(plaintext, key, CryptoConstants.ENCRYPT_TYPE);
    }

    public String decryptBlock(String ciphertext, String key) {
        String decodedCipher = StringUtils.hexToPlainCipher(ciphertext, 8);
        return cryptBlock(decodedCipher, key, CryptoConstants.DECRYPT_TYPE);
    }

    /**
     * Generate 16 groups of 48-bit subKeys
     *
     * @param key the DES key
     *
     * @return a subKeys array with a length of 16
     */
    private StringBuilder[] generateSubKeys(String key) {
        StringBuilder[] subKeys = new StringBuilder[16];
        // 1. 将密钥转换为二进制串
        String binaryKey = StringUtils.plainBlockToBinary(key);
        // 2. 通过压缩选择置换表PC-1置换密钥的二进制串
        String substitutedKey = StringUtils.reindexBinary(binaryKey, DESConstants.PC_1_TABLE).toString();
        if (isDebug) {
            StringUtils.printFormattedBinaryData("binaryKey", binaryKey);
            StringUtils.printFormattedBinaryData("substitutedKey", substitutedKey);
        }
        // 3. 将置换后的二进制串分成左右两块leftBlock和rightBlock
        StringBuilder leftBlock = new StringBuilder();
        StringBuilder rightBlock = new StringBuilder();
        leftBlock.append(substitutedKey, 0, 28);
        rightBlock.append(substitutedKey, 28, 56);
        // 4. 循环16轮生成子密钥
        for (int i = 0; i < 16; i++) {
            // 4.1. 根据轮循环左移位数表R进行循环左移
            leftBlock = StringUtils.leftShiftBinary(leftBlock, DESConstants.ROUND_SHIFT_BITS_COUNT[i]);
            rightBlock = StringUtils.leftShiftBinary(rightBlock, DESConstants.ROUND_SHIFT_BITS_COUNT[i]);
            // 4.2. 合并leftBlock和rightBlock
            String subKeyBlock = leftBlock.toString() + rightBlock.toString();
            // 4.3. 通过压缩选择置换表PC-2置换subKeyBlock, 得到子密钥
            StringBuilder subKey = StringUtils.reindexBinary(subKeyBlock, DESConstants.PC_2_TABLE);
            subKeys[i] = subKey;
        }
        return subKeys;
    }

    /**
     * Inverse the subKeys to offer the decode function
     *
     * @param subKeys original subKeys array
     *
     * @return inversed subKeys
     */
    private StringBuilder[] inverseSubKeys(StringBuilder[] subKeys) {
        int subKeysLength = subKeys.length;
        StringBuilder[] inverseKeys = new StringBuilder[subKeysLength];
        for (int i = 0; i < subKeysLength; i++) {
            inverseKeys[i] = subKeys[subKeysLength - i - 1];
        }
        return inverseKeys;
    }
}
