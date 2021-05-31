from math import gcd as bltin_gcd
def coprime2(a, b):
    return bltin_gcd(a, b) == 1

print(coprime2(2,4))
