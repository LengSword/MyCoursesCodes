#!/usr/bin/env/ python3
# -*- coding: utf-8 -*-

"""
实验目的：理解LZ78编码算法。
实验内容：写出程序，利用LZ78编码实现对某字符序列（只做英文字母的序列）的二元压缩（二元压缩，即编成二进制序列），并能解压。
实验步骤：
1、压缩
（1） 为字符序列中可能出现的字符进行二进制编码
（2） 根据LZ78编码算法为字符序列分段；
（3） 为各段分配二进制段号，并建立字典；
（4） 为字符序列的各段进行二进制编码。
2、解压
（1） 一边建立字典表，一边译码
"""


def compress(message):
    result = []
    char_segments = {}
    message_len = len(message)
    i = 0
    while i < message_len:
        # 处理不在字典中的字符
        if message[i] not in char_segments:
            bin_char = bin(ord(message[i]))[2:]
            result.append((0, bin_char))
            char_segments[message[i]] = len(char_segments) + 1
            i += 1
        # 处理最后一个字符
        elif i == message_len - 1:
            result.append((char_segments.get(message[i]), ''))
            i += 1
        else:
            for j in range(i + 1, message_len):
                # 处理最长匹配子串
                if message[i: j + 1] not in char_segments:
                    bin_char = bin(ord(message[j]))[2:]
                    result.append((char_segments.get(message[i:j]), bin_char))
                    char_segments[message[i: j + 1]] = len(char_segments) + 1
                    i = j + 1
                    break
                # 处理最后一个字符
                elif j == message_len - 1:
                    result.append((char_segments.get(message[i: j + 1]), ''))
                    i = j + 1

    return result


def uncompress(packed):
    unpacked = ''
    char_segments = {}
    print('译码顺序: ', end='')
    for index, bin_ch in packed:
        ch = chr(int('0b' + bin_ch, 2)) if bin_ch else ''
        print(ch, end=' ')
        if index == 0:
            unpacked += ch
            char_segments[len(char_segments) + 1] = ch
        else:
            term = char_segments.get(index) + ch
            unpacked += term
            char_segments[len(char_segments) + 1] = term
    return unpacked


if __name__ == '__main__':
    messages = ['ABBCBCABABCAABCAAB', 'BABAABRRRA', 'AAAAAAAAA']
    for message in messages:
        pack = compress(message)
        print('字符序列: {0}\n长度: {1}'.format(message, len(pack)))
        print('段号    二进制编码')
        for index, ch in pack:
            print('{:0>3s}     {:0>8s}'.format(bin(index)[2:], ch))
        unpack = uncompress(pack)
        print('\n译码结果: {}'.format(unpack))
        print('====================')
