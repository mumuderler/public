import numpy as np
from egcd import egcd
import random
from math import gcd as bltin_gcd
import time
import os,glob

alphabet = "abcdefghijklmnopqrstuvwxyz"

letter_to_index = dict(zip(alphabet, range(len(alphabet))))
index_to_letter = dict(zip(range(len(alphabet)), alphabet))

folder_path = '/home/mumu/Desktop/texts'

def coprime2(a, b):
    return bltin_gcd(a, b) == 1

def generate_matrix(size,modulus):
    
    matrix = np.zeros((size,size))
    for i in range(size):
        for j in range(size):
            matrix[i][j] = random.randint(0, modulus-1)
    
    det = int(np.round(np.linalg.det(matrix)))  # Step 1)

    if(det%modulus != 0) and (coprime2(modulus,det) == True):
        return matrix
    else:
        return generate_matrix(size,modulus)


def matrix_mod_inv(matrix,size,modulus):

    det = int(np.round(np.linalg.det(matrix)))  # Step 1)
    #print(det,det%modulus)

    det_inv = egcd(det, modulus)[1] % modulus  # Step 2)
    matrix_modulus_inv = (
        det_inv * np.round(det * np.linalg.inv(matrix)).astype(int) % modulus
    )
    #print("Matrix inverse\n",matrix_modulus_inv)

    return matrix_modulus_inv


def encrypt(message, K):
    encrypted = ""

    message_in_numbers = []
    counter = 0
    for letter in message:
        letter = letter.lower()      
        if letter not in alphabet:
            continue
        else:
            message_in_numbers.append(letter_to_index[letter])

    x = len(message_in_numbers) % K.shape[0]
    if x != 0:
        for i in range(K.shape[0]-x):
            message_in_numbers.append(0)

    #print("Message in numbers: "+str(message_in_numbers)+"\n")
    split_P = [
        message_in_numbers[i : i + int(K.shape[0])]
        for i in range(0, len(message_in_numbers), int(K.shape[0]))
    ]
    for P in split_P:
        P = np.transpose(np.asarray(P))[:, np.newaxis]
        while P.shape[0] != K.shape[0]:
            P = np.append(P, letter_to_index[" "])[:, np.newaxis]
        
        numbers = (np.dot(K, P)) % len(alphabet)

        #print("Key Matrix: "+str(K)+"\n")
        
        #print("Encrypted data: "+str(numbers)+"\n")
        n = numbers.shape[0]  # length of encrypted message (in numbers)

        # Map back to get encrypted text
        for idx in range(n):
            number = int(numbers[idx, 0])
            encrypted += index_to_letter[number]
        counter += 1

    return encrypted


def decrypt(cipher, matrix):
    decrypted = ""
    cipher_in_numbers = []
    counter = 0

    for letter in cipher:
        cipher_in_numbers.append(letter_to_index[letter])

    split_C = [
        cipher_in_numbers[i : i + int(matrix.shape[0])]
        for i in range(0, len(cipher_in_numbers), int(matrix.shape[0]))
    ]
    
    #inverse = matrix_mod_inv(Kinv*seed2[counter], len(alphabet))
    for C in split_C:
        
        C = np.transpose(np.asarray(C))[:, np.newaxis]
        
        #print(counter+1,".","block of Cipher-text",C)
        Kinv = matrix_mod_inv(matrix, matrix.shape[0], len(alphabet))
        numbers = np.dot(Kinv, C) % len(alphabet)

        n = numbers.shape[0]    
        #print("Deciphered block",numbers)
        for idx in range(n):
            number = int(numbers[idx, 0])
            decrypted += index_to_letter[number]
        counter += 1

    return decrypted


def main():
    #message = input("""enter message: """)

	for filename in glob.glob(os.path.join(folder_path, '*.txt')): 
		with open(filename, 'r') as file:
			input_lines = [line.strip() for line in file]
				
		message = str(input_lines)

		generation = time.perf_counter()
		#matrix = generate_matrix(int(input("Enter block size: ")),len(alphabet))
		matrix = generate_matrix(3,len(alphabet))
		print("after\n",matrix)


		first_time = time.perf_counter()
		encrypted_message = encrypt(message, matrix)
		second_time = time.perf_counter()
		decrypted_message = decrypt(encrypted_message, matrix)
		third_time = time.perf_counter()

		encryption_time = second_time - first_time
		decryption_time = third_time - second_time
		overall_time = third_time - first_time
		generation_time = third_time-generation

		#print("Original message: " + message + "\n")
		#print("Encrypted message: " + encrypted_message + "\n")
		#print("Decrypted message: " + decrypted_message.upper() + "\n")
		
		with open('sifreleme.odt','a') as f:
			f.write(filename)
			f.write("\n")
			f.write("Encryption time ")
			f.write(str(encryption_time))
			f.write("\n")
			f.write("Decryption time ")
			f.write(str(decryption_time))
			f.write("\n")
			f.write("Overall time ",)
			f.write(str(overall_time))
			f.write("\n")
			f.write("Generation time ")
			f.write(str(generation_time))
			f.write("\n")
			f.write("\n")

    #print("Encryption time ",encryption_time,"\n","Decryption time ",decryption_time,"\n","Overall time ",overall_time,"\n","Generation time ",generation_time,"\n")
        

main()
