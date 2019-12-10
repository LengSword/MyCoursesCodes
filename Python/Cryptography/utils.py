#!/usr/bin/env/ python3
# -*- coding: utf-8 -*-
##
# @project-name cryptography utils
# @description cryptography utils
# @author LengSword
# @website https://github.com/LengSword
# @email 1030588973@qq.com
# @version 0.1
# @created 2019-11-03
# @last-modified 2019-12-05
# @license MIT


def gcd(a, b):
    if b == 0:
        return a
    else:
        return gcd(b, a % b)


def extended_gcd(a, b):
    if b == 0:
        x = 1
        y = 0
        remainder = a
        return x, y, remainder
    else:
        x1, y1, remainder = extended_gcd(b, a % b)
        x = y1
        y = x1 - (a // b) * y1
        return x, y, remainder


def reindex_message(bin_msg, reindexing_table):
    result = []

    for i in reindexing_table:
        result.append(bin_msg[i - 1])

    return result


def xor_message(msg1, msg2):
    result = [int(byte1) ^ int(byte2) for byte1, byte2 in zip(msg1, msg2)]
    return result


def left_shift_message(bin_msg, bits):
    return bin_msg[bits:] + bin_msg[0:bits]


def bin2str(msg):
    result = ''
    bin_list = [msg[i : i + 8] for i in range(0, len(msg), 8)]

    for bin_str in bin_list:
        result += chr(int(bin_str, 2))

    return result


def str2bin(msg):
    result = ''

    for char in msg:
        # ignore '0b' prefix
        bin_temp = bin(ord(char))[2:]
        # fill to 8 bytes
        bin_temp = padding_zeros(bin_temp, 8)
        result += bin_temp

    return result


def bin2hex(msg):
    result = ''
    bin_list = [msg[i : i + 8] for i in range(0, len(msg), 8)]

    for bin_str in bin_list:
        hex_temp = hex(int(bin_str, 2))[2:]
        result += padding_zeros(hex_temp, 2)

    return result


def hex2bin(msg):
    result = ''
    hex_list = [msg[i : i + 2] for i in range(0, len(msg), 2)]

    for hex_str in hex_list:
        bin_temp = bin(int(hex_str, 16))[2:]
        result += padding_zeros(bin_temp, 8)

    return result


def padding_zeros(msg, target_len):
    diff_len = target_len - len(msg)
    if diff_len <= 0:
        return msg

    return '0' * (diff_len) + msg
