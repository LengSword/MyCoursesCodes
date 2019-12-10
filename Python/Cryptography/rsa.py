#!/usr/bin/env/ python3
# -*- coding: utf-8 -*-
##
# @project-name RSA
# @description RSA
# @author LengSword
# @website https://github.com/LengSword
# @email 1030588973@qq.com
# @version 0.1
# @created 2019-11-02
# @last-modified 2019-12-05
# @license MIT

from montgomery import montgomery
from utils import extended_gcd


def generate_key(p, q, e):
    n = p * q
    prime_count_of_n = (p - 1) * (q - 1)
    # Get d
    x, y, r = extended_gcd(e, prime_count_of_n)
    if x >= 0:
        d = x
    else:
        # x为负数则 取x为模 prime_count_of_n 后的补数
        d = prime_count_of_n + x

    return (n, e), (n, d)


def encrypt(plain, public_key):
    n, e = public_key

    c = montgomery(plain, e, n)
    return c


def decrypt(cipher, private_key):
    n, d = private_key

    m = montgomery(cipher, d, n)
    return m


if __name__ == '__main__':
    # # 例1
    # p = 7
    # q = 17
    # e = 5
    # public_key, private_key = generate_key(p, q, e)
    # print('p =', p)
    # print('q =', q)
    # print('e =', e)
    # print('公钥 =', public_key)
    # print('私钥 =', private_key)

    # m = 19
    # print('明文m =', m)

    # c = encrypt(m, public_key)
    # print('密文c =', c)

    # d = decrypt(c, private_key)
    # print('解密(明文m) =', d)

    # 例2
    p = 71593
    q = 77041
    e = 1757316971
    public_key, private_key = generate_key(p, q, e)
    print('p =', p)
    print('q =', q)
    print('e =', e)
    print('公钥 =', public_key)
    print('私钥 =', private_key)

    m1 = int('1612050119')
    m2 = int('0500230109')
    m3 = int('2000061518')
    m4 = int('0013050000')
    print(
        '明文m1, m2, m3, m4 = ({:0>10d}, {:0>10d}, {:0>10d}, {:0>10d})'.format(
            m1, m2, m3, m4
        )
    )

    c1 = encrypt(m1, public_key)
    c2 = encrypt(m2, public_key)
    c3 = encrypt(m3, public_key)
    c4 = encrypt(m4, public_key)
    print(
        '密文c1, c2, c3, c4 = ({:0>10d}, {:0>10d}, {:0>10d}, {:0>10d})'.format(
            c1, c2, c3, c4
        )
    )

    d1 = decrypt(c1, private_key)
    d2 = decrypt(c2, private_key)
    d3 = decrypt(c3, private_key)
    d4 = decrypt(c4, private_key)
    print(
        '解密(密文c1, c2, c3, c4) = ({:0>10d}, {:0>10d}, {:0>10d}, {:0>10d})'.format(
            d1, d2, d3, d4
        )
    )
