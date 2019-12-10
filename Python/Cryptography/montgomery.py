#!/usr/bin/env/ python3
# -*- coding: utf-8 -*-
##
# @project-name 大整数幂取模算法 - montgomery
# @description 大整数幂取模算法 - montgomery
# @author LengSword
# @website https://github.com/LengSword
# @email 1030588973@qq.com
# @version 0.1
# @created 2019-11-26
# @last-modified 2019-11-30
# @license MIT


def montgomery(base, exponent, modulo):
    '''
    (base ^ exponent) mod modulo
    '''
    exp_bin_array = bin(exponent)[2:][::-1]

    base_array = []
    base_array.append(base)
    for _ in range(len(exp_bin_array) - 1):
        next_base = (base * base) % modulo
        base_array.append(next_base)
        base = next_base

    result = _base_exp_multi(base_array, exp_bin_array, modulo)
    return result % modulo


def _base_exp_multi(base_array, exp_bin_array, modulo):
    result = 1
    for i in range(len(base_array)):
        if int(exp_bin_array[i]):
            result = (result * base_array[i]) % modulo

    return result
