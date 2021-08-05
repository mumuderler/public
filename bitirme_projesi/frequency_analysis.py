frequency_analysis = { "a" : 0,  "b" : 0,  "c" : 0,  "d" : 0,  "e" : 0,                      "f" : 0,  "g" : 0,
    "h" : 0,  "i" : 0,  "j" : 0,  "k" : 0,  "l" : 0,  "m" : 0,  "n" : 0,  "o" :   0,
    "p" : 0,  "q" : 0,  "r" : 0,  "s" : 0,  "t" : 0,  "u" : 0,  "v" : 0,  "w" : 0,
    "x" : 0,  "y" : 0,  "z" : 0 }

listing = []

letters = 'eatniroshlcdguwpbfynkvxzjq'
alphabet = 'abcdefghijklmnopqrstuvwxyz'

text = input("Please Enter text to decipher ").lower()

for letter in text:

    if letter.isalpha():
      frequency_analysis[letter] += 1

def get_num (frequency_analysis):
    return frequency_analysis[1]


unsorted_items = frequency_analysis.items()
sorted_items = sorted(unsorted_items, key = get_num)

descending = reversed(sorted_items)
descending = list(descending)

inorder = list()
for char in descending:
    inorder.append(char)


for key in inorder:

    if key[1] > 0:

        print (key)

percent = {}

for key in inorder:
    percent[key[0]] = float(key[1]/len(text))*100

print(percent)
for key in percent:
    print(key," %",percent[key])
