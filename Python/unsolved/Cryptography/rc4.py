#!/usr/bin/env/ python3
# -*- coding: utf-8 -*-
##
# @project-name rc4
# @description rc4
# @author LengSword
# @website https://github.com/LengSword
# @email 1030588973@qq.com
# @version 0.1
# @created 2019-11-02
# @last-modified 2019-11-11
# @license MIT


import base64


def init_s_box(key):
    key_length = len(key)
    s_box = list(range(256))

    new_index = 0
    for i in range(256):
        new_index = (new_index + s_box[i] + ord(key[i % key_length])) % 256
        s_box[i], s_box[new_index] = s_box[new_index], s_box[i]

    return s_box


def crypt(plaintext, key, mode='encrypt', is_base64=True):
    result = []
    s_box = init_s_box(key)

    if mode == 'decrypt' and is_base64:
        plaintext = bytes.decode(base64.b64decode(plaintext), 'utf-8')

    i = j = 0
    for byte in plaintext:
        i = (i + 1) % 256
        j = (j + s_box[i]) % 256
        s_box[i], s_box[j] = s_box[j], s_box[i]
        key_stream = s_box[(s_box[i] + s_box[j]) % 256]
        result.append(chr(ord(byte) ^ key_stream))

    ciphertext = ''.join(result)

    if mode == 'encrypt' and is_base64:
        return str(base64.b64encode(ciphertext.encode('utf-8')), 'utf-8')
    return ciphertext


if __name__ == '__main__':
    plaintext = 'asdxasdgczczv'
    key = '12345678'

    cipher = crypt(plaintext, key, 'encrypt', False)
    print('====ordinary====')
    print('plaintext: ' + plaintext)
    print('key: ' + key)
    print('ciphertext: ' + cipher)
    print('decrypt(ciphertext) -> ' + crypt(cipher, key, 'decrypt', False))

    cipher = crypt(plaintext, key, 'encrypt')
    print('====base64====')
    print('plaintext: ' + plaintext)
    print('key: ' + key)
    print('ciphertext: ' + cipher)
    print('decrypt(ciphertext) -> ' + crypt(cipher, key, 'decrypt'))
