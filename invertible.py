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
    print(det,det%modulus)

    det_inv = egcd(det, modulus)[1] % modulus  # Step 2)
    matrix_modulus_inv = (
        det_inv * np.round(det * np.linalg.inv(matrix)).astype(int) % modulus
    )
    print("Matrix inverse\n",matrix_modulus_inv)

    return matrix_modulus_inv
