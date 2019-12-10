#!/usr/bin/env/ python3
# -*- coding: utf-8 -*-
##
# @project-name DES
# @description My DES implementation
# @author LengSword
# @website https://github.com/LengSword
# @email 1030588973@qq.com
# @version 0.1
# @created 2019-11-02
# @last-modified 2019-12-05
# @license MIT


from utils import (
    padding_zeros,
    bin2str,
    str2bin,
    bin2hex,
    hex2bin,
    reindex_message,
    left_shift_message,
    xor_message,
)

from constants import des_constants


def generate_subkeys(key):
    subkeys = []
    bin_key = padding_zeros(str2bin(key), 64)
    bin_pc_1_key = reindex_message(bin_key, des_constants.PC_1_TABLE)
    left_block = bin_pc_1_key[0:28]
    right_block = bin_pc_1_key[28:56]

    for i in range(0, 16):
        left_block = left_shift_message(
            left_block, des_constants.ROUND_SHIFT_BITS_COUNT[i]
        )
        right_block = left_shift_message(
            right_block, des_constants.ROUND_SHIFT_BITS_COUNT[i]
        )
        subkey_block = left_block + right_block
        subkeys.append(reindex_message(subkey_block, des_constants.PC_2_TABLE))

    return subkeys


def encrypt_block(plaintext, key):
    return crypt_block(plaintext, key)


def decrypt_block(ciphertext, key):
    return crypt_block(ciphertext, key, False)


def crypt_block(message, key, is_encrypt=True):
    '''
    crypt_block

    Args:
        message (str): plaintext/ciphertext(hex)
        key (str): key
        is_encrypt (Boolean): is encrypt or decrypt

    Returns:
        str: binary ciphertext/plaintext
    '''
    if is_encrypt:
        bin_plaintext = padding_zeros(str2bin(message), 64)
    else:
        bin_plaintext = padding_zeros(hex2bin(message), 64)
    bin_ip_plaintext = reindex_message(bin_plaintext, des_constants.IP_TABLE)
    # print('bin_ip_plaintext: {}'.format(bin_ip_plaintext))
    left_block = bin_ip_plaintext[0:32]
    right_block = bin_ip_plaintext[32:64]

    subkeys = generate_subkeys(key)
    if not is_encrypt:
        subkeys = subkeys[::-1]

    for i in range(0, 16):
        next_right_block = core_encrypt(right_block, subkeys[i])
        left_block, right_block = right_block, xor_message(next_right_block, left_block)

    ciphertext_block = right_block + left_block
    ciphertext_block = reindex_message(ciphertext_block, des_constants.INVERSE_IP_TABLE)
    # print(ciphertext_block)
    return ''.join(map(str, ciphertext_block))


def core_encrypt(right_block, subkey):
    last_right_block = reindex_message(right_block, des_constants.E_TABLE)
    xor_right_block = xor_message(last_right_block, subkey)
    s_right_block = s_box(xor_right_block)
    s_right_block = [j for i in s_right_block for j in i]
    # print(s_right_block)
    return reindex_message(s_right_block, des_constants.P_TABLE)


def s_box(xor_right_block):
    result = []
    for i in range(0, len(des_constants.S_BOX)):
        s_block = list(map(str, xor_right_block[i * 6 : (i + 1) * 6]))
        row_index = int(s_block[0] + s_block[5], 2)
        column_index = int(''.join(s_block[1:5]), 2)
        # print(row_index, column_index)
        current_num = padding_zeros(
            bin(des_constants.S_BOX[i][row_index * 16 + column_index])[2:], 4
        )
        result.append(list(current_num))
    return result


if __name__ == '__main__':
    test_str = '01234567'
    test_key = '12345678'
    test_bin = str2bin(test_str)

    cipher_bin = encrypt_block(test_str, test_key)
    ciphertext = bin2hex(cipher_bin)
    print('明文: '+test_str)
    print('密钥: '+test_key)
    print('二进制明文串: '+test_bin)
    print('二进制密文串: '+cipher_bin)
    print('十六进制密文串: '+ciphertext)
    plaintext_bin = decrypt_block(ciphertext, test_key)
    print('解密(十六进制密文串) -> 二进制明文串: '+plaintext_bin)
    print('二进制明文串 -> 明文: '+bin2str(plaintext_bin))
