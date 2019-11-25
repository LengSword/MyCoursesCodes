#!/usr/bin/env/ python3
# -*- coding: utf-8 -*-
##
# @project-name affine_cipher
# @description affine_cipher
# @author LengSword
# @website https://github.com/LengSword
# @email 1030588973@qq.com
# @version 0.1
# @created 2019-11-03
# @last-modified 2019-11-11
# @license MIT

import sys
import string

from utils import gcd, extended_gcd


def encrypt(plaintext, key_a, key_b):
    result = []

    for byte in plaintext:
        if byte in string.ascii_lowercase:
            value = ord(byte) - ord('a')
            value = (key_a * value + key_b) % 26
            result.append(chr(value + ord('a')))
        elif byte in string.ascii_uppercase:
            value = ord(byte) - ord('A')
            value = (key_a * value + key_b) % 26
            result.append(chr(value + ord('A')))
    return ''.join(result)


def decrypt(ciphertext, key_a, key_b):
    result = []
    inverse_key_a = extended_gcd(key_a, 26)[0]

    for byte in ciphertext:
        if byte in string.ascii_lowercase:
            value = ord(byte) - ord('a')
            value = (inverse_key_a * (value - key_b)) % 26
            result.append(chr(value + ord('a')))
        elif byte in string.ascii_uppercase:
            value = ord(byte) - ord('A')
            value = (inverse_key_a * (value - key_b)) % 26
            result.append(chr(value + ord('A')))
    return ''.join(result)


if __name__ == '__main__':
    plaintext = input('请输入明文(字母): ')
    a, b = input('请输入a, b: ').split()
    a = int(a)
    b = int(b)
    if gcd(a, 26) != 1:
        print('Error: a与26不互质!')
        sys.exit(1)
    if (a not in range(25)) or (b not in range(25)):
        print('Error: 0<=a,b<=25')
        sys.exit(1)

    ciphertext = encrypt(plaintext, a, b)
    print(ciphertext)

    print(decrypt(ciphertext, a, b))
