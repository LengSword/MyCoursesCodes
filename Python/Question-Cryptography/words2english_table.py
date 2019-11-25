import string

words = "PLEASE SEND ME THE BOOK, MY CREDIT CARD NO IS SIX ONE TWO ONE THREE EIGHT SIX ZERO ONE SIX EIGHT FOUR NINE SEVEN ZERO TWO"
space_positions = []

for index, word in enumerate(words):
    if word in [' ', ',']:
        space_positions.append(index)

print(space_positions)
words = words.replace(" ", "")

english_list = [(ord(word) - ord('A'))
                for word in words if word in string.ascii_uppercase]

result = [english_list[i:i + 4] for i in range(0, len(english_list), 4)]


print(result)
