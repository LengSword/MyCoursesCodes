#!/usr/bin/env/ python3
# -*- coding: utf-8 -*-

# References:
# https://algorithm.yuanbin.me/zh-hans/basics_data_structure/huffman_compression.html
# https://github.com/bhrigu123/huffman-coding

"""
1. 压缩

1) 统计文件中各字节出现的次数(概率)
2) 哈夫曼编码,构建哈夫曼树,并编码
3) 读取文件内容,查找各字节对应的哈夫曼编码,并将结果写入到压缩文件中(PS:要凑够8位二进制位)
4) 将原始文件中各字节及出现的次数也写入到压缩文件中

2. 解压

1) 从压缩文件中获得原始文件各字节及出现的次数,并根据此构建哈夫曼对照表
2) 将压缩文件中对应原始文件数据的部分依据哈夫曼对照表还原成原来的字节,写入到解压文件中
"""

import collections
import heapq
import pickle


def get_rate(compressed_binary, uncompressed_bits):
    return len(compressed_binary) / uncompressed_bits


class Huffman:
    class Trie:
        def __init__(self, count, char=''):
            self.char = char
            self.count = count
            self.coding = ''
            self.left = self.right = None

        def __eq__(self, other):
            return self.count == other.count

        def __lt__(self, other):
            return self.count < other.count

        def __gt__(self, other):
            return self.count > other.count

    def __init__(self):
        pass

    def make_frequency_dict(self, data):
        return {word: data.count(word) for word in set(data)}

    def build_huffman_tree(self, counter):
        heap = []
        for char, count in counter.items():
            heapq.heappush(heap, Huffman.Trie(count, char))

        while len(heap) != 1:
            left = heapq.heappop(heap)
            right = heapq.heappop(heap)
            trie = Huffman.Trie(left.count + right.count)
            trie.left, trie.right = left, right
            heapq.heappush(heap, trie)

        root = heap[0]

        return root

    def get_codes_mapping(self, root):
        codes_mapping = {}
        queue = collections.deque()
        queue.append(root)
        while queue:
            node = queue.popleft()
            if node.char:
                codes_mapping[node.char] = node.coding
                continue
            if node.left:
                node.left.coding = node.coding + '0'
                queue.append(node.left)
            if node.right:
                node.right.coding = node.coding + '1'
                queue.append(node.right)

        return codes_mapping

    def get_codes_text(self, codes_mapping, data):
        encoded_text = ''
        for word in data:
            encoded_text += codes_mapping[word]

        return encoded_text

    def pad_encoded_text(self, encoded_text: str):
        padding_bits_count = 8 - len(encoded_text) % 8
        encoded_text += '0' * (padding_bits_count)

        encoded_text = f'{padding_bits_count:08b}{encoded_text}'

        return encoded_text

    def encode_text(self, padded_encoded_text: str):
        if len(padded_encoded_text) % 8:
            return None
        result = bytearray()
        bytes_list = [padded_encoded_text[i: i + 8]
                      for i in range(0, len(padded_encoded_text), 8)]

        for byte in bytes_list:
            result.append(int(byte, 2))

        return bytes(result)

    def compress(self, input_path: str, output_path: str):
        with open(input_path, 'rb') as file, open(output_path, 'wb') as output:
            data = file.read()

            bits_length = len(data) * 8
            print('需编码的二进制位长度: {}'.format(bits_length))
            print('编码前: {}'.format(data))
            decoded_data = data.decode('utf-8')
            counter = self.make_frequency_dict(decoded_data)
            root = self.build_huffman_tree(counter)
            codes_mapping = self.get_codes_mapping(root)

            print('编码对照表: {}'.format(codes_mapping))

            encoded_text = self.get_codes_text(codes_mapping, decoded_data)
            padded_encoded_text = self.pad_encoded_text(encoded_text)
            encoded_bytes = self.encode_text(padded_encoded_text)

            print('编码后: ', encoded_bytes)

            compressed = pickle.dumps(
                (counter, encoded_bytes)
            )
            output.write(compressed)

            print('压缩率: {:.2%}'.format(get_rate(encoded_bytes, bits_length)))

            return encoded_bytes

    def encoded_bytes_to_text(self, encoded_bytes):
        encoded_text_list = [f'{byte:08b}' for byte in encoded_bytes]
        return ''.join(encoded_text_list)

    def remove_text_padding(self, padded_encoded_text):
        padded_info = padded_encoded_text[:8]
        padded_bits_count = int(padded_info, 2)

        encode_text = padded_encoded_text[8:][:-1 * padded_bits_count]
        return encode_text

    def decode_text(self, encoded_text, root):
        bits_string = ''
        node = root
        for bit in encoded_text:
            if bit == '0':
                node = node.left
            else:
                node = node.right
            if node.char:
                bits_string += node.char
                node = root

        return bits_string

    def decompress(self, input_path: str, output_path: str):
        with open(input_path, 'rb') as file, open(output_path, 'wb') as output:
            counter, encoded_bytes = pickle.loads(
                file.read(),
                encoding='utf-8'
            )
            print('编码字符次数统计: {}'.format(counter))
            print('编码后: ', encoded_bytes)

            root = self.build_huffman_tree(counter)

            padded_encoded_text = self.encoded_bytes_to_text(encoded_bytes)
            encoded_text = self.remove_text_padding(padded_encoded_text)
            decompressed = self.decode_text(encoded_text, root).encode()
            print('解码数据: {}'.format(decompressed))
            output.write(decompressed)
            print('Decompressed')
            return decompressed


if __name__ == '__main__':
    huffman = Huffman()

    huffman.compress('string.txt', 'compressed_string.txt')

    huffman.decompress('compressed_string.txt', 'decompressed_string.txt')
