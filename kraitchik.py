#kraitchick's Factoring

import math

# Python program to print all Primes Smaller
# than or equal to N using Sieve of Eratosthenes


def SieveOfEratosthenes(num):
    array = []
    for i in range(2,num):
        if i not in array:
            for k in range(i*i, i+1,i):
                array.append(k)

	return array

def isPerfectSquare(x):
 
    #if x >= 0,
    if(x >= 0):
        sr = int(math.sqrt(x))
        # sqrt function returns floating value so we have to convert it into integer
        #return boolean T/F
        return ((sr*sr) == x)
    return false

def Q(y,N):
    return y**2 - N
#N = int(input("enter a number to factorise: "))

dict = {}
j = range(1011,1500,2)

for N in j:
    y = int(math.ceil(math.sqrt(N)))
    for i in range(y,int(((N+1)/2)),1):
        x_square = Q(i,N)
        if(isPerfectSquare(x_square)):
            print(i+math.sqrt(x_square)," and ", i-math.sqrt(x_square)," are", N,"'s divisors") 

primes = SieveOfEratosthenes(20)

def small_factor(x_square):
    for k in primes:
        while(x_square != 1):
            if(x_square % primes[k]):
                if(dict[x_square[k]] == 1):
                    dict[x_square][k] -= 1
                    x_square = x_square / primes[k]
                else:
                    dict[x_square][k] += 1
                    x_square = x_square / primes[k]

def match(dict):
    dict[]

#kraitchik's          
if(math.gcd(N,i**2 - x_square) is not 1 and math.gcd(N,i**2 - x_square) is not N):
