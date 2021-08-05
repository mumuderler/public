import random
import math
import time
import psutil

import numpy as np

def getRandomPrimeInteger(bounds):

    for i in range(bounds.__len__()-1):
        if bounds[i + 1] > bounds[i]:
            x = bounds[i] + np.random.randint(bounds[i+1]-bounds[i])
            if isPrime(x):
                return x

        else:
            if isPrime(bounds[i]):
                return bounds[i]

        if isPrime(bounds[i + 1]):
            return bounds[i + 1]

    newBounds = [0 for i in range(2*bounds.__len__() - 1)]
    newBounds[0] = bounds[0]
    for i in range(1, bounds.__len__()):
        newBounds[2*i-1] = int((bounds[i-1] + bounds[i])/2)
        newBounds[2*i] = bounds[i]

    return getRandomPrimeInteger(newBounds)

def isPrime(x):
    count = 0
    for i in range(int(x/2)):
        if x % (i+1) == 0:
            count = count+1
    return count == 1



#ex: get 50 random prime integers between 100 and 10000:
bounds = [10**7, 10**8]
x = []
first = time.perf_counter()
for i in range(2):
    x.append(getRandomPrimeInteger(bounds))
second = time.perf_counter()
interval = second - first
print("CPU USAGE: ", psutil.cpu_percent(2))
print(f"Process time is {interval:0.4f} seconds ",x)

'''
first = time.perf_counter()
print(rand_prime())
second = time.perf_counter()
interval = second - first

print(f"Process time is {interval:0.4f} seconds")'''
