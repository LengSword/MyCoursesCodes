import numpy as np
from numpy.linalg import *


def my_int_inv(mat, n=26):
    x = np.zeros(mat.shape, dtype=np.int16)
    for i in range(mat.shape[0]):
        for j in range(mat.shape[1]):
            x[i, j] = int(round(mat[i, j])) % n
    return x


def gcd(a, b):
    while a != 0:
        a, b = b % a, a
    return b


def exgcd(a, m):
    if gcd(a, m) != 1:
        return None
    u1, u2, u3 = 1, 0, a
    v1, v2, v3 = 0, 1, m
    while v3 != 0:
        q = u3 // v3
        v1, v2, v3, u1, u2, u3 = (u1 - q * v1), (u2 - q * v2), (u3 - q * v3), v1, v2, v3
    return u1 % m


a = np.mat([[3, 13, 21, 9], [15, 10, 6, 25], [10, 17, 4, 8], [1, 23, 7, 2]])
b = np.mat([[1], [21], [8], [17]])
words_list = [[15], [11], [4], [0], [18], [4], [18], [4],
              [13], [3], [12], [4], [19], [7], [4], [1],
              [14], [14], [10], [12], [24], [2], [17], [4],
              [3], [8], [19], [2], [0], [17], [3], [13],
              [14],  [8], [18], [18], [8], [23], [14], [13],
              [4], [19], [22], [14], [14], [13], [4], [19],
              [7], [17], [4], [4], [4], [8], [6], [7],
              [19], [18], [8], [23], [25], [4], [17], [14],
              [14], [13], [4], [18], [8], [23], [4], [8],
              [6], [7], [19], [5], [14], [20], [17], [13],
              [8], [13], [4], [18], [4], [21], [4], [13],
              [25], [4], [17], [14], [19], [22], [14]]


space_positions = [6, 11, 14, 18, 23, 24, 27, 34, 39, 42, 45,
                   49, 53, 57, 61, 67, 73, 77, 82, 86, 90, 96, 101, 106, 112, 117]

if len(words_list) % 2 == 1:
    words_list.append([0])

c_total = []

for i in range(0, len(words_list), 4):
    m = np.mat(words_list[i:i+4])
    c = a * m + b
    c = my_int_inv(c)
    c = np.transpose(c).ravel().tolist()
    c_total += c

c_str = ''.join([chr(ch + ord('A')) for ch in c_total])

c_list = list(c_str)
for i in range(len(space_positions)):
    c_list.insert(space_positions[i], ' ')
print("".join(c_list))


a_inv = a.I
a_det = det(a)
a_adju = my_int_inv(a_det * a_inv)
a_det_inv = exgcd(int(round((det(a) % 26))), 26)
aa_inv = my_int_inv(a_det_inv * a_adju)
m = (aa_inv * (c - b)) % 26
# print(m)
