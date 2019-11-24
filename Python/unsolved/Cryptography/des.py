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
# @last-modified 2019-11-11
# @license MIT

import base64

import utils

from constants.des_constants import *


def generate_subkey(key):
    subkey = []
    bin_key = utils.padding_zeros(str2bin(key), 64)
    bin_pc_1_key = utils.reindex_message(bin_key, PC_1_TABLE)
    left_block = bin_pc_1_key[0:28]
    right_block = bin_pc_1_key[28:56]

    for i in range(0, 16):
        left_block = utils.left_shift_message(
            left_block, ROUND_SHIFT_BITS_COUNT[i])
        right_block = utils.left_shift_message(
            right_block, ROUND_SHIFT_BITS_COUNT[i])
        subkey_block = left_block + right_block
        subkey.append(utils.reindex_message(subkey_block, PC_2_TABLE))

    return subkey


def encrypt_block(plaintext, key):
    bin_plaintext = utils.padding_zeros(str2bin(plaintext), 64)
    bin_ip_plaintext = utils.reindex_message(bin_plaintext, IP_TABLE)
    print(bin_ip_plaintext)
    left_block = bin_ip_plaintext[0:32]
    right_block = bin_ip_plaintext[32:64]


if __name__ == '__main__':
    test_str = '01234567'
    test_key = '12345678'
    test_bin = utils.str2bin(test_str)
    print(test_bin)
    print(generate_subkey(test_key))
    # encrypt_block(test_str, test_key)
